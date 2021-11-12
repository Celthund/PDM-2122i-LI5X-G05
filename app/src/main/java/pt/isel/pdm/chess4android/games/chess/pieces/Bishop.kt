package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Bishop(player: Player, position: Position) : Piece(player, position) {
    override fun getPossibleMoves(board: Game): HashSet<Position> {
        val positions: HashSet<Position> = HashSet()

        // Get diagonals
        positions.addAll(getPositionsToTopLeft(board))
        positions.addAll(getPositionsToTopRight(board))
        positions.addAll(getPositionsToBottomLeft(board))
        positions.addAll(getPositionsToBottomRight(board))
        return positions
    }
}