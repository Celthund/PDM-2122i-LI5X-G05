package pt.isel.pdm.chess4android.views

import pt.isel.pdm.chess4android.R

open class TestForView {
    companion object {
        val boardModel = arrayOf(
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

    }

}