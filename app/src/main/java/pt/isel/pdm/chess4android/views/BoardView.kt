package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.Chess
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

    // Stores the possible places the piece can move
    private val possibleTilesToMove: HashMap<Tile, Position> = HashMap()

    // Stores the pieces movement so it can reset if the "attack" was reset
    private val storePieceMovements: HashMap<Tile, ArrayList<Position>> = HashMap()

    // Last selected Tile so it can reset the possible moves if it was clicked again
    private var lastSelectedTile: Tile = Tile(ctx, Type.WHITE, 0, null)

    // The states of the View Board at the present
    private val boardModel = Chess(8, 8)
    private val boardTile: Array<Array<Tile>> = initBoard()

    init {
        rowCount = side
        columnCount = side
    }

    // It will draw the board and put all thee beginning valid movement of each piece
    private fun initBoard(): Array<Array<Tile>> {
        var tileArray = arrayOf<Array<Tile>>()

        for (row in 0 until side) {
            var columnsArray = arrayOf<Tile>()
            for (column in 0 until side) {
                val position = Position(column, row)
                columnsArray += Tile(
                    ctx,
                    if ((row + column) % 2 == 0) Type.WHITE else Type.BLACK,
                    side,
                    boardModel.getPiece(position)?.viewId
                )
                val currTile = columnsArray[column]
                if(currTile.piece == R.drawable.ic_white_pawn || currTile.piece == R.drawable.ic_black_pawn ) {
                    currTile.setOnClickListener {
                        showValid(position, boardModel.getPossibleMoves(position))
                    }
                }


                addView(currTile)
            }
            tileArray += columnsArray
        }
        return tileArray
    }

    // Shows the valid position of a piece that was clicked
    private fun showValid(currPos: Position, possibleMovements: ArrayList<Position>) {
        val currTile = boardTile[currPos.y][currPos.x]

        if (currTile.piece == R.drawable.ic_empty_squares_possible_move || isSameTile(currTile) || !isCurrPlayerPiece(currPos)) {
            return
        }
        storePieceMovements[currTile] = possibleMovements
        resetPossiblePositions()

        lastSelectedTile = currTile

        for (coordinate in possibleMovements.indices) {
            // Checks the possible move the piece has by adding the piece movement to the current position
            val possibleMoveCol = possibleMovements[coordinate].x
            val possibleMoveRow = possibleMovements[coordinate].y

            val newPosition = Position(possibleMoveCol, possibleMoveRow)
            val currPossibleTile = boardTile[possibleMoveRow][possibleMoveCol]

            possibleTilesToMove[currPossibleTile] = newPosition

            currPossibleTile.inPreview = true

            currPossibleTile.setOnClickListener {
                movePiece(currPos, newPosition)
            }
            currPossibleTile.update()


        }
    }

    private fun isCurrPlayerPiece(position : Position) : Boolean {
        return boardModel._currentPlayer == boardModel.getPiece(position)?.player
    }

    // Move the piece to the the clicked position
    private fun movePiece(
        currPosition: Position,
        newPosition: Position
    ) {
        resetPossiblePositions()
        val currTile = boardTile[currPosition.y][currPosition.x]
        val moveTile = boardTile[newPosition.y][newPosition.x]

        if (currTile.piece == R.drawable.ic_empty_squares_possible_move) {
            return
        }

        moveTile.piece = currTile.piece
        currTile.piece = R.drawable.ic_empty_squares_possible_move

        boardModel.movePieceAtPosition(currPosition, newPosition)

        moveTile.setOnClickListener {
            showValid(newPosition, boardModel.getPossibleMoves(newPosition))
        }

        moveTile.update()
        currTile.update()
    }

    // Reset to the board to the state before the a piece was clicked
    private fun resetPossiblePositions() {
        lastSelectedTile = Tile(ctx, Type.WHITE, 0, null)
        var tileToRemove: Array<Tile> = arrayOf()
        for (currTile in possibleTilesToMove.keys) {
            currTile.inPreview = false

            // The current tile is empty so the setOnClickListener can be reseted freely
            if (currTile.piece == null) {
                currTile.setOnClickListener {}
            } else {
                val currM = possibleTilesToMove[currTile]!!
                val possibleM = storePieceMovements[currTile]!!
                currTile.setOnClickListener {
                    showValid(currM, possibleM)
                }
            }
            currTile.update()
            tileToRemove += currTile
        }

        for (tile in tileToRemove) {
            possibleTilesToMove.remove(tile)
        }
    }


    // Check if a piece was clicked 2 times in a row so in the second time can hide the possible moves
    private fun isSameTile(currTile: Tile): Boolean {
        if (lastSelectedTile == currTile) {
            resetPossiblePositions()
            return true
        }
        return false
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }

}