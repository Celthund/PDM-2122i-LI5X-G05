package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Rook(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_rook else R.drawable.ic_black_rook

    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        // TODO missing validation of possible check to the King if move is made.
        val positions: ArrayList<Position> = arrayListOf()
        positions.addAll(getPositionsToLeft(position, board))
        positions.addAll(getPositionsToRight(position, board))
        positions.addAll(getPositionsToTop(position, board))
        positions.addAll(getPositionsToBottom(position, board))
        return positions
    }

}