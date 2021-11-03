package pt.isel.pdm.chess4android.views

import pt.isel.pdm.chess4android.R

class MovementView {


    companion object {
        fun getMovementForEachPiece() : HashMap<Int, Array<Array<Int>>> {
            val pieceToMovement : HashMap<Int, Array<Array<Int>>> = HashMap()

            pieceToMovement[R.drawable.ic_white_rook] = arrayOf(arrayOf(1,0))
            pieceToMovement[R.drawable.ic_white_bishop] = arrayOf(arrayOf(2,2), arrayOf(2, -2), arrayOf(3, 3), arrayOf(4,4), arrayOf(5, 5))
            pieceToMovement[R.drawable.ic_white_knight] = arrayOf(arrayOf(1,0))
            pieceToMovement[R.drawable.ic_white_king] = arrayOf(arrayOf(1,0))
            pieceToMovement[R.drawable.ic_white_queen] = arrayOf(arrayOf(1,0))
            pieceToMovement[R.drawable.ic_white_pawn] = arrayOf(arrayOf(1,0), arrayOf(2, 0))

            return pieceToMovement

        }
    }



}