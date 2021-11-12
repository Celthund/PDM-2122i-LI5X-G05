package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Knight(player: Player, position: Position) : Piece(player, position) {
    override fun getPossibleMoves(board: Game): HashSet<Position> {
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
        val positions: HashSet<Position> = HashSet()
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