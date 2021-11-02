package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    private var boardTile : Array<Array<Tile>>

    init {
        rowCount = side
        columnCount = side
        val board = arrayOf(
            arrayOf(
                R.drawable.ic_white_rook,
                R.drawable.ic_white_knight,
                R.drawable.ic_white_bishop,
                R.drawable.ic_white_queen,
                R.drawable.ic_white_king,
                R.drawable.ic_white_bishop,
                R.drawable.ic_white_knight,
                R.drawable.ic_white_rook
            ),
            arrayOf(
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_pawn
            ),

            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),

            arrayOf(
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn,
                R.drawable.ic_black_pawn
            ),
            arrayOf(
                R.drawable.ic_black_rook,
                R.drawable.ic_black_knight,
                R.drawable.ic_black_bishop,
                R.drawable.ic_black_queen,
                R.drawable.ic_black_king,
                R.drawable.ic_black_bishop,
                R.drawable.ic_black_knight,
                R.drawable.ic_black_rook
            )
        )
        boardTile = initBoard(board)
    }

    private fun initBoard(board: Array<Array<Int>>) : Array<Array<Tile>> {

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
                columnsArray[column].setOnClickListener {movePiece(column,row,column,row + 1, boardTile) }
               addView(columnsArray[column])
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
        boardTile: Array<Array<Tile>>
    ) {
        Thread.sleep(2000)
        val currTile = boardTile[currentRow][currentCol]
        val moveTile = boardTile[moveRow][moveCol]

        moveTile.piece = currTile.piece
        currTile.piece = 0

        updateViewLayout(moveTile, moveTile.layoutParams)
        updateViewLayout(currTile, currTile.layoutParams)

    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }
}