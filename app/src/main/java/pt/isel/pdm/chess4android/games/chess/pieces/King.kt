package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class King(player: Player) : Piece(player) {
    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        // TODO missing validation of possible check to the King if move is made.
        val positions: ArrayList<Position> = arrayListOf();

        val allMoves: Array<Position> = arrayOf(
            Position(position.x - 1, position.y - 1), // Up Left
            Position(position.x, position.y - 1), // Up
            Position(position.x + 1, position.y - 1), // Up Right
            Position(position.x + 1, position.y), // Right
            Position(position.x + 1, position.y + 1), // Down Right
            Position(position.x, position.y + 1), // Down
            Position(position.x - 1, position.y + 1), // Down Left
            Position(position.x - 1, position.y) // Left
        )
        for (pos in allMoves) {
            if (board.isPositionValid(pos)){
                val piece : Piece? = board.getPiece(pos)
                if (piece == null || piece.player != player) {
                    positions.add(pos);
                }
            }
        }
        return positions
    }
}