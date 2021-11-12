package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Queen(player: Player) : Piece(player) {
    override fun getPossibleMoves(position: Position, board: Game): HashSet<Position> {
        // TODO missing validation of possible check to the King if move is made.
        val positions: HashSet<Position> = HashSet()
        positions.addAll(getPositionsToLeft(position, board))
        positions.addAll(getPositionsToRight(position, board))
        positions.addAll(getPositionsToTop(position, board))
        positions.addAll(getPositionsToBottom(position, board))

        // Get diagonals
        positions.addAll(getPositionsToTopLeft(position, board))
        positions.addAll(getPositionsToTopRight(position, board))
        positions.addAll(getPositionsToBottomLeft(position, board))
        positions.addAll(getPositionsToBottomRight(position, board))
        return positions
    }
}