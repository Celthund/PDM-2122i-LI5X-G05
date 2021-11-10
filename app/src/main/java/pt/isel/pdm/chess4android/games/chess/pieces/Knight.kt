package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Knight(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_knight else R.drawable.ic_black_knight

    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        val allMoves : Array<Position> = arrayOf(
            Position(position.x - 2, position.y - 1),
            Position(position.x - 1, position.y - 2),
            Position(position.x + 1, position.y - 2),
            Position(position.x + 2, position.y - 1),
            Position(position.x + 2, position.y + 1),
            Position(position.x + 1, position.y + 2),
            Position(position.x - 1, position.y + 2),
            Position(position.x - 2, position.y + 1),
        )

        // TODO missing validation of possible check to the King if move is made.
        val positions: ArrayList<Position> = arrayListOf()
        var piece: Piece? = null
        for (pos in allMoves){
            if (board.isPositionValid(pos)){
                piece = board.getPiece(pos)
                if (piece == null || piece.player != player){
                    positions.add(pos)
                }
            }
        }
        return positions
    }
}