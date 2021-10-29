package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Position

class King : Piece() {

    override fun getPossibleMoves(position: Position, board: Game): Array<Position> {
        return arrayOf(
            Position(position.x - 1, position.y - 1), // Up Left
            Position(position.x, position.y - 1), // Up
            Position(position.x + 1, position.y - 1), // Up Right
            Position(position.x + 1, position.y), // Right
            Position(position.x + 1, position.y + 1), // Down Right
            Position(position.x, position.y + 1), // Down
            Position(position.x - 1, position.y + 1), // Down Left
            Position(position.x - 1, position.y) // Left
        )
    }
}