package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.ChessPiece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Knight(player: Player, position: Position) : ChessPiece(player, position) {
    override fun internalGetPositionsInView(board: Game): HashSet<Position> {
        return getMoves(board, true)
    }

    override fun internalGetPossibleMoves(board: Game): HashSet<Position> {
        return getMoves(board, false)
    }

    private fun getMoves(board: Game, addFirstPieceFound: Boolean): HashSet<Position> {
        val allMoves: Array<Position> = arrayOf(
            Position(position.x - 2, position.y - 1),
            Position(position.x - 1, position.y - 2),
            Position(position.x + 1, position.y - 2),
            Position(position.x + 2, position.y - 1),
            Position(position.x + 2, position.y + 1),
            Position(position.x + 1, position.y + 2),
            Position(position.x - 1, position.y + 2),
            Position(position.x - 2, position.y + 1),
        )

        val possibleMoves: HashSet<Position> = HashSet()
        var piece: ChessPiece?
        for (pos in allMoves) {
            if (board.isPositionValid(pos)) {
                piece = board.getPiece(pos) as ChessPiece?
                if (piece == null || piece.player != player || (addFirstPieceFound && piece.player == player)) {
                    possibleMoves.add(pos)
                }
            }
        }

        return possibleMoves
    }
}