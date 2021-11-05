package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.R

/**
 * Custom view that implements a chess board tile.
 * Tiles are either black or white and can they can be empty or occupied by a chess piece.
 *
 * Implementation note: This view is not to be used with the designer tool.
 * You need to adapt this view to suit your needs. ;)
 *
 * @property type           The tile's type (i.e. black or white)
 * @property tilesPerSide   The number of tiles in each side of the chess board
 */
@SuppressLint("ViewConstructor")
class Tile(
    private val ctx: Context,
    private val type: Type,
    private val tilesPerSide: Int,
    var piece : Int
) : View(ctx) {

    enum class Type { WHITE, BLACK }

    private val brush = Paint().apply {
        color = ctx.resources.getColor(
            if (type == Type.WHITE) R.color.chess_board_white else R.color.chess_board_black,
            null
        )
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val side = Integer.min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(side / tilesPerSide, side / tilesPerSide)
    }


    override fun onDraw(canvas: Canvas) {
        val padding = 8
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), brush)
        if (piece in setOf(
                R.drawable.ic_white_pawn,
                R.drawable.ic_white_bishop,
                R.drawable.ic_white_king,
                R.drawable.ic_white_knight,
                R.drawable.ic_white_queen,
                R.drawable.ic_white_rook,

                R.drawable.ic_black_pawn,
                R.drawable.ic_black_bishop,
                R.drawable.ic_black_king,
                R.drawable.ic_black_knight,
                R.drawable.ic_black_queen,
                R.drawable.ic_black_rook,
            )) {
            val drawPiece = VectorDrawableCompat
                .create(ctx.resources, piece, null)
            drawPiece?.setBounds(padding, padding , width-padding, height-padding)
            drawPiece?.draw(canvas)
        }

        when(piece) {
            1 -> {
                val drawPossibleMoves = VectorDrawableCompat
                    .create(ctx.resources, R.drawable.ic_launcher_foreground, null)
                drawPossibleMoves?.setBounds(padding, padding , width-padding, height-padding)
                drawPossibleMoves?.draw(canvas)
            }
            else -> {
                val drawPiece = VectorDrawableCompat
                .create(ctx.resources, R.drawable.ic_white_pawn, null)
                drawPiece?.setBounds(padding, padding , 0, 0)
                drawPiece?.draw(canvas)
            }

        }
    }

    fun update () {
        this.invalidate()
    }

}