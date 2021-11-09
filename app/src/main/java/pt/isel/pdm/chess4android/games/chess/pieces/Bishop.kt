package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Bishop(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_bishop else R.drawable.ic_black_bishop

    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        TODO("Not yet implemented")
    }
}