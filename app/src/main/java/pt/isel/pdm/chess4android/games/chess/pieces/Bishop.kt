package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Bishop(player: Player) : Piece(player) {
    override fun getPossibleMoves(position: Position, board: Game): HashSet<Position> {
        val positions: HashSet<Position> = HashSet()

        // Get diagonals
        positions.addAll(getPositionsToTopLeft(position, board))
        positions.addAll(getPositionsToTopRight(position, board))
        positions.addAll(getPositionsToBottomLeft(position, board))
        positions.addAll(getPositionsToBottomRight(position, board))
        return positions
    }
}