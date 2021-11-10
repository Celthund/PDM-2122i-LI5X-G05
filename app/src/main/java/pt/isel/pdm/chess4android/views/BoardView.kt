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

    // Last selected Tile so it can reset the possible moves if it was clicked again
    private var lastSelectedTile: Tile? = null
    private var lastClickedPosition: ArrayList<Position> = ArrayList()

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
                if (currTile.piece != null) {
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


        if (currTile.piece == R.drawable.ic_empty_squares_possible_move || !isCurrPlayerPiece(currPos)) {
            return
        }

        if(lastSelectedTile == currTile) {
            resetPossiblePositions(possibleMovements)
            return
        }

        if(lastSelectedTile != null) {
            resetPossiblePositions(lastClickedPosition)
        }

        lastClickedPosition = possibleMovements



        lastSelectedTile = currTile

        for (coordinate in possibleMovements.indices) {
            // Checks the possible move the piece has by adding the piece movement to the current position
            val possibleMoveCol = possibleMovements[coordinate].x
            val possibleMoveRow = possibleMovements[coordinate].y

            val newPosition = Position(possibleMoveCol, possibleMoveRow)
            val currPossibleTile = boardTile[possibleMoveRow][possibleMoveCol]


            currPossibleTile.inPreview = true

            currPossibleTile.setOnClickListener {
                movePiece(currPos, newPosition)
            }
            currPossibleTile.update()


        }
    }

    private fun isCurrPlayerPiece(position: Position): Boolean {
        return boardModel.currentPlayer == boardModel.getPiece(position)?.player
    }

    private fun resetPossiblePositions(possibleMovements: ArrayList<Position>) {
        for (possibleMovement in possibleMovements) {
            lastSelectedTile = null
            val currTile = boardTile[possibleMovement.y][possibleMovement.x]
            currTile.inPreview = false

            // The current tile is empty so the setOnClickListener can be reseted freely
            if (currTile.piece == null) {
                currTile.setOnClickListener {}
            } else {
                currTile.setOnClickListener {
                    showValid(possibleMovement, boardModel.getPossibleMoves(possibleMovement))
                }
            }
            currTile.update()
        }
    }

    // Move the piece to the the clicked position
    private fun movePiece(
        currPosition: Position,
        newPosition: Position
    ) {
        resetPossiblePositions(boardModel.getPossibleMoves(currPosition))
        val currTile = boardTile[currPosition.y][currPosition.x]
        val moveTile = boardTile[newPosition.y][newPosition.x]

        if (currTile.piece == R.drawable.ic_empty_squares_possible_move) {
            return
        }

        moveTile.piece = currTile.piece
        currTile.piece = null

        boardModel.movePieceAtPosition(currPosition, newPosition)

        moveTile.setOnClickListener {
            showValid(newPosition, boardModel.getPossibleMoves(newPosition))
        }

        moveTile.update()
        currTile.update()
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }

}