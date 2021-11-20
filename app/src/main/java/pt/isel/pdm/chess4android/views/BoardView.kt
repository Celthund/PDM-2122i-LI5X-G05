package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import pt.isel.pdm.chess4android.activities.MainActivity
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.models.games.Position
import pt.isel.pdm.chess4android.models.games.chess.Chess
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

    private lateinit var makeMove : (currPos: Position, newPosition: Position, boardModel: Chess) -> Unit
    private lateinit var activity: MainActivity

    // Last selected Tile so it can reset the possible moves if it was clicked again
    private var lastSelectedTile: Tile? = null
    private var lastClickedPosition: HashSet<Position> = HashSet()

    // The states of the View Board at the present
    private lateinit var boardModel: Chess
    lateinit var boardTile: Array<Array<Tile>>

    init {
        rowCount = side
        columnCount = side
    }

    // It will draw the board and put all thee beginning valid movement of each piece
    fun initBoard(boardModel: Chess, activity: MainActivity, function: (currPos: Position, newPosition: Position, boardModel: Chess) -> Unit) {
        makeMove = function
        this.activity = activity
        this.boardModel = boardModel

        var tileArray = arrayOf<Array<Tile>>()

        for (row in 0 until side) {
            var columnsArray = arrayOf<Tile>()
            for (column in 0 until side) {
                val position = Position(column, row)
                val piece = activity.getPieceDrawableId(position, boardModel)

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
                val piece = activity.getPieceDrawableId(position, boardModel)

                currTile.piece = piece

                if (!activity.isInPromote && currTile.piece != null) {
                    setTileClickListener(currTile, position)
                } else {
                    currTile.setOnClickListener {}
                }
                currTile.invalidate()
            }
        }
    }

    // Shows the valid position of a piece that was clicked
    private fun showValidMoves(currPos: Position, possibleMovements: HashSet<Position>) {
        if(activity.isInPromote) {
            return
        }
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
                    makeMove(currPos, newPosition, boardModel)
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

    fun setBoard(boardModel: Chess) {
        this.boardModel = boardModel
        invalidateBoard()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }


}