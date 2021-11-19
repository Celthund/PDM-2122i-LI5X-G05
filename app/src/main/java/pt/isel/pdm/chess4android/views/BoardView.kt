package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import pt.isel.pdm.chess4android.MainActivityViewModel
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.PromoteCandidate
import pt.isel.pdm.chess4android.games.chess.Chess
import pt.isel.pdm.chess4android.games.chess.pieces.*
import pt.isel.pdm.chess4android.views.Tile.Type

/**
 * Custom view that implements a chess board.
 */
@SuppressLint("ClickableViewAccessibility")
class BoardView(private val ctx: Context, attrs: AttributeSet?) : GridLayout(ctx, attrs) {
    private val side = 8
    private val brush = Paint().apply {
        ctx.resources.getColor(R.color.chess_board_black, null)
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }

    lateinit var viewModel: MainActivityViewModel

    private val pieceViewMapper: HashMap<Any, Array<Int>> = HashMap()

    // Last selected Tile so it can reset the possible moves if it was clicked again
    private var lastSelectedTile: Tile? = null
    private var lastClickedPosition: HashSet<Position> = HashSet()

    // The states of the View Board at the present
    private lateinit var boardModel: Chess
    lateinit var boardTile: Array<Array<Tile>>

    private lateinit var whitePlayer: Player

    init {
        rowCount = side
        columnCount = side
    }

    // It will draw the board and put all thee beginning valid movement of each piece
    fun initBoard(boardModel: Chess, whitePlayer: Player) {
        this.whitePlayer = whitePlayer
        this.boardModel = boardModel

        mapViewToPiece()
        var tileArray = arrayOf<Array<Tile>>()

        for (row in 0 until side) {
            var columnsArray = arrayOf<Tile>()
            for (column in 0 until side) {
                val position = Position(column, row)
                val piece = getPieceDrawableId(position)

                columnsArray += Tile(
                    ctx,
                    if ((row + column) % 2 == 0) Type.WHITE else Type.BLACK,
                    side,
                    piece
                )
                val currTile = columnsArray[column]
                if (currTile.piece != null) {
                    setTileClickListener(currTile, position)
                }
                addView(currTile)
            }
            tileArray += columnsArray
        }
        boardTile = tileArray
    }

    private fun invalidateBoard() {

        for (row in 0 until side) {
            for (column in 0 until side) {
                val position = Position(column, row)
                val currTile = boardTile[row][column]
                val piece = getPieceDrawableId(position)

                currTile.piece = piece

                if (currTile.piece != null) {
                    setTileClickListener(currTile, position)
                } else {
                    currTile.setOnClickListener {}
                }
                currTile.invalidate()
            }
        }
    }

    private fun getPieceDrawableId(position: Position): Int? {
        val piece = boardModel.getPiece(position)

        if (piece != null) {
            return if (piece.player == whitePlayer)
                pieceViewMapper[piece::class]!![0]
            else
                pieceViewMapper[piece::class]!![1]
        }
        return null
    }

    // Shows the valid position of a piece that was clicked
    private fun showValidMoves(currPos: Position, possibleMovements: HashSet<Position>) {
        viewModel.positionToPromote()
        val currTile = boardTile[currPos.y][currPos.x]

        if (currTile.piece == R.drawable.ic_empty_squares_possible_move
            || clickedOnSamePiece(currTile, possibleMovements)
        ) {
            return
        }

        if (lastSelectedTile != null) {
            resetPossiblePositions(lastClickedPosition)
        }

        lastClickedPosition = possibleMovements
        lastSelectedTile = currTile
        possibleMovements.forEach {
            val possibleMoveCol = it.x
            val possibleMoveRow = it.y

            val newPosition = Position(possibleMoveCol, possibleMoveRow)
            val currPossibleTile = boardTile[possibleMoveRow][possibleMoveCol]

            currPossibleTile.inPreview = true

            if (isCurrPlayerPiece(currPos)) {
                currPossibleTile.setOnClickListener {
                    resetPossiblePositions(boardModel.getPossibleMoves(currPos))
                    boardModel.movePieceAtPosition(currPos, newPosition)

                    viewModel.setBoardModel(boardModel)

                    viewModel.positionToPromote(
                        PromoteCandidate(
                            newPosition,
                            boardModel.getPiece(newPosition)?.player!!,
                            boardModel
                        )
                    )

                }
            }
            currPossibleTile.invalidate()
        }
    }

    private fun resetPossiblePositions(possibleMovements: HashSet<Position>) {
        for (possibleMovement in possibleMovements) {
            lastSelectedTile = null
            val currTile = boardTile[possibleMovement.y][possibleMovement.x]
            currTile.inPreview = false

            // The current tile is empty so the setOnClickListener can be reseted freely
            if (currTile.piece == null) {
                currTile.setOnClickListener {}
            } else {
                setTileClickListener(currTile, possibleMovement)
            }
            currTile.invalidate()
        }
    }

    private fun setTileClickListener(tile: Tile, position: Position) {
        tile.setOnClickListener {
            showValidMoves(position, boardModel.getPossibleMoves(position))
        }
    }

    private fun isCurrPlayerPiece(position: Position): Boolean {
        return boardModel.currentPlayer == boardModel.getPiece(position)?.player
    }

    private fun clickedOnSamePiece(tile: Tile, possibleMovements: HashSet<Position>): Boolean {
        if (lastSelectedTile == tile) {
            resetPossiblePositions(possibleMovements)
            return true
        }
        return false
    }

    private fun mapViewToPiece() {
        pieceViewMapper[Rook::class] = arrayOf(R.drawable.ic_white_rook, R.drawable.ic_black_rook)
        pieceViewMapper[Knight::class] =
            arrayOf(R.drawable.ic_white_knight, R.drawable.ic_black_knight)
        pieceViewMapper[Bishop::class] =
            arrayOf(R.drawable.ic_white_bishop, R.drawable.ic_black_bishop)
        pieceViewMapper[Queen::class] =
            arrayOf(R.drawable.ic_white_queen, R.drawable.ic_black_queen)
        pieceViewMapper[King::class] = arrayOf(R.drawable.ic_white_king, R.drawable.ic_black_king)
        pieceViewMapper[Pawn::class] = arrayOf(R.drawable.ic_white_pawn, R.drawable.ic_black_pawn)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }

    fun setBoard(boardModel: Chess) {
        this.boardModel = boardModel
        invalidateBoard()
    }

}