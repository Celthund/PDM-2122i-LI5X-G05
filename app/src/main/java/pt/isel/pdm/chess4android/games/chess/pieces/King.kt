package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class King(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_king else R.drawable.ic_black_king

    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf();

        val positionsToValidate: Array<Position> = arrayOf(
            Position(position.x - 1, position.y - 1), // Up Left
            Position(position.x, position.y - 1), // Up
            Position(position.x + 1, position.y - 1), // Up Right
            Position(position.x + 1, position.y), // Right
            Position(position.x + 1, position.y + 1), // Down Right
            Position(position.x, position.y + 1), // Down
            Position(position.x - 1, position.y + 1), // Down Left
            Position(position.x - 1, position.y) // Left
        )
        for (pos in positionsToValidate) {
            val piece : Piece? = board.getPiece(pos)
            if (piece == null || piece.player != player) {
                positions.add(pos);
            }
        }
        // TODO missing validation of possible check to the King if move is made.
        return positions
    }
}