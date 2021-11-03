package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.GridLayout
import pt.isel.pdm.chess4android.R
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
    private var lastPossibleMoves : Array<Array<Int>>
    private var lastSelectedTile : Tile

    private var movementOfPiece : HashMap<Int, Array<Array<Int>>>

    init {
        rowCount = side
        columnCount = side
        val boardModel = TestForView.boardModel
        boardTile = initBoard(boardModel)
        lastPossibleMoves = arrayOf()
        lastSelectedTile = Tile(ctx,Type.WHITE, 0, -1)
        movementOfPiece = HashMap()

        movementOfPiece = MovementView.getMovementForEachPiece()
    }

    private fun initBoard(board: Array<Array<Int>>): Array<Array<Tile>> {

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
                    showValid(arrayOf(row, column), board[row][column])
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

        if(currTile.piece == 0) {
            return
        }
        moveTile.piece = currTile.piece
        currTile.piece = 0

        moveTile.update()
        currTile.update()
        return

    }

    fun showValid (currentPosition : Array<Int>, piece : Int ) {
        val currTile = boardTile[currentPosition[0]][currentPosition[1]]

        if(currTile.piece == 0 || isSameTile(currTile)) {
            return
        }
        resetPossiblePositions()

        if (movementOfPiece.containsKey(piece)) {
            for(coordinate in movementOfPiece[piece]?.indices!!) {
                var array : Array<Int> = arrayOf()
                array  += currentPosition[0] + movementOfPiece[piece]!![coordinate][0]
                array += currentPosition[1] + movementOfPiece[piece]!![coordinate][1]
                lastPossibleMoves += array
            }

            lastSelectedTile = currTile
            for (coordinate in lastPossibleMoves.indices) {
                val moveRow = lastPossibleMoves[coordinate][0]
                val moveCol = lastPossibleMoves[coordinate][1]
                val possibleMovesTile = boardTile[moveRow][moveCol]
                possibleMovesTile.piece = 1
                possibleMovesTile.setOnClickListener{
                    movePiece(currentPosition[1], currentPosition[0], moveCol, moveRow )
                }
                possibleMovesTile.update()
            }
        }
    }

    private fun resetPossiblePositions() {
        lastSelectedTile = Tile(ctx, Type.WHITE, 0, -1)
        for (coordinate in lastPossibleMoves.indices) {
            val currTile =
                boardTile[lastPossibleMoves[coordinate][0]][lastPossibleMoves[coordinate][1]]
            currTile.piece = 0
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