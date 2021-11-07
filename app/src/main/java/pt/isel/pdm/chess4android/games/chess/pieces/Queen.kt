package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Queen(player: Player) : Piece(player) {
    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        TODO("Not yet implemented")
    }
}