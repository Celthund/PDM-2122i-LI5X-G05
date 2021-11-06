package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Position
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
    private var boardTile: Array<Array<Tile>>
    // Future coordinates array to know which positions are currently being shown (if any)
    //for now its a matrix with a X and Y
    private var lastPossibleMoves : Array<Position>
    private var lastSelectedTile : Tile

    private var movementOfPiece : HashMap<Int, Array<Position>>

    init {
        rowCount = side
        columnCount = side
        val boardModel = TestForView.boardModel
        boardTile = initBoard(boardModel)
        lastPossibleMoves = arrayOf()
        lastSelectedTile = Tile(ctx,Type.WHITE, 0, null)
        movementOfPiece = HashMap()

        movementOfPiece = MovementView.getMovementForEachPiece()
    }

    private fun initBoard(board: Array<Array<Int?>>): Array<Array<Tile>> {

        var tileArray = arrayOf<Array<Tile>>()

        for (row in board.indices) {
            var columnsArray = arrayOf<Tile>()
            for (column in board[row].indices) {
                columnsArray += Tile(
                    ctx,
                    if ((row + column) % 2 == 0) Type.WHITE else Type.BLACK,
                    side,
                    board[row][column]
                )
                val currTile = columnsArray[column]

                currTile.setOnClickListener {
                    showValid(Position(column, row), board[row][column])
                }

                addView(currTile)
            }
            tileArray += columnsArray
        }
        return tileArray
    }


    private fun movePiece(
        currentCol: Int,
        currentRow: Int,
        moveCol: Int,
        moveRow: Int,
    ) {
        resetPossiblePositions()
        val currTile = boardTile[currentRow][currentCol]
        val moveTile = boardTile[moveRow][moveCol]

        if(currTile.piece == R.drawable.ic_empty_squares_possible_move) {
            return
        }
        moveTile.piece = currTile.piece
        currTile.piece = R.drawable.ic_empty_squares_possible_move

        moveTile.update()
        currTile.update()
        return

    }

    private fun showValid (currPos : Position, piece : Int? ) {
        val currTile = boardTile[currPos.y][currPos.x]

        if(currTile.piece == R.drawable.ic_empty_squares_possible_move || isSameTile(currTile)) {
            return
        }
        resetPossiblePositions()

        if (movementOfPiece.containsKey(piece)) {
            for(coordinate in movementOfPiece[piece]?.indices!!) {
                // Checks the possible move the piece has by adding the piece movement to the current position
                val possibleMoveCol = currPos.x + movementOfPiece[piece]!![coordinate].x
                val possibleMoveRow = currPos.y + movementOfPiece[piece]!![coordinate].y

                val newPossiblePos = Position(possibleMoveCol, possibleMoveRow)
                lastPossibleMoves += newPossiblePos
            }

            lastSelectedTile = currTile
            for (coordinate in lastPossibleMoves.indices) {
                val moveRow = lastPossibleMoves[coordinate].y
                val moveCol = lastPossibleMoves[coordinate].x
                val possibleMovesTile = boardTile[moveRow][moveCol]
                possibleMovesTile.inPreview = true

                possibleMovesTile.setOnClickListener{
                    movePiece(currPos.x, currPos.y, moveCol, moveRow )
                }
                possibleMovesTile.update()
            }
        }
    }

    private fun resetPossiblePositions() {
        lastSelectedTile = Tile(ctx, Type.WHITE, 0, null)
        for (coordinate in lastPossibleMoves.indices) {
            val currTile =
                boardTile[lastPossibleMoves[coordinate].y][lastPossibleMoves[coordinate].x]
            currTile.inPreview = false
            currTile.setOnClickListener{}
            currTile.update()
        }
        lastPossibleMoves = arrayOf()
    }

    private fun isSameTile(currTile : Tile) : Boolean {
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