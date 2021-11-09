package pt.isel.pdm.chess4android.views

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Position

class MovementView {
    companion object {
        fun getInitialMovementForEachPiece() : HashMap<Int, ArrayList<Position>> {
            val pieceToMovement : HashMap<Int, ArrayList<Position>> = HashMap()

            pieceToMovement[R.drawable.ic_white_rook] = arrayListOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_bishop] = arrayListOf(Position(2,2), Position(2, -2), Position(3, 3), Position(4,4), Position(5, 5))
            pieceToMovement[R.drawable.ic_white_knight] = arrayListOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_king] = arrayListOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_queen] = arrayListOf(Position(0,1))
            pieceToMovement[R.drawable.ic_white_pawn] = arrayListOf(Position(0,1), Position(0, 2))

            return pieceToMovement

        }
    }



}