package pt.isel.pdm.chess4android.games.chess


import pt.isel.pdm.chess4android.games.*

class Puzzle(private val solutionMoves: ArrayList<Movement>, whitePlayerPosition: Player, MAX_HEIGHT: Int, MAX_WIDTH: Int) :
    Chess(whitePlayerPosition, MAX_HEIGHT, MAX_WIDTH) {

    override fun movePieceAtPosition(oldPosition: Position, newPosition: Position): Boolean {
        if (solutionMoves.isEmpty()) return false
        val nextMovement = solutionMoves.first()
        if (nextMovement.origin == oldPosition && nextMovement.destination == newPosition) {
            val res = super.movePieceAtPosition(oldPosition, newPosition)
            if (res) {
                solutionMoves.remove(nextMovement)
                makeMoveForTopPlayer()
                return true
            }
        }
        return false
    }

    private fun makeMoveForTopPlayer(){
        if (solutionMoves.isEmpty()) return
        val nextMovement = solutionMoves.first()
        val res = super.movePieceAtPosition(nextMovement.origin, nextMovement.destination)
        if (res)
            solutionMoves.remove(nextMovement)
    }
}

