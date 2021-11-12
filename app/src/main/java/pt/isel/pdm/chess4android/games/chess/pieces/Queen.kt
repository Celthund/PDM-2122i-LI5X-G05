package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Queen(player: Player, position: Position) : Piece(player, position) {
    override fun getPossibleMoves(board: Game): HashSet<Position> {
        // TODO missing validation of possible check to the King if move is made.
        val positions: HashSet<Position> = HashSet()
        positions.addAll(getPositionsToLeft(board))
        positions.addAll(getPositionsToRight(board))
        positions.addAll(getPositionsToTop(board))
        positions.addAll(getPositionsToBottom(board))

        // Get diagonals
        positions.addAll(getPositionsToTopLeft(board))
        positions.addAll(getPositionsToTopRight(board))
        positions.addAll(getPositionsToBottomLeft(board))
        positions.addAll(getPositionsToBottomRight(board))
        return positions
    }
}