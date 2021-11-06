package pt.isel.pdm.chess4android.views

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Position

class MovementView {
    companion object {
        fun getInitialMovementForEachPiece() : HashMap<Int, Array<Position>> {
            val pieceToMovement : HashMap<Int, Array<Position>> = HashMap()

            pieceToMovement[R.drawable.ic_white_rook] = arrayOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_bishop] = arrayOf(Position(2,2), Position(2, -2), Position(3, 3), Position(4,4), Position(5, 5))
            pieceToMovement[R.drawable.ic_white_knight] = arrayOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_king] = arrayOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_queen] = arrayOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_pawn] = arrayOf(Position(0,1), Position(0, 2))

            return pieceToMovement

        }
    }



}