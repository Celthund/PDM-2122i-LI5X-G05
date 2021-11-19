package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.chess.ChessPiece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Queen(player: Player, position: Position) : ChessPiece(player, position) {
    override fun internalGetPositionsInView(board: Game): HashSet<Position> {
        return getMoves(board, true)
    }

    override fun internalGetPossibleMoves(board: Game): HashSet<Position> {
        return getMoves(board, false)
    }

    private fun getMoves(board: Game, addFirstPieceFound: Boolean): HashSet<Position> {
        val possibleMoves: HashSet<Position> = HashSet()

        possibleMoves.addAll(getPositionsToLeft(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToRight(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToTop(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToBottom(board, addFirstPieceFound))

        // Get diagonals
        possibleMoves.addAll(getPositionsToTopLeft(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToTopRight(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToBottomLeft(board, addFirstPieceFound))
        possibleMoves.addAll(getPositionsToBottomRight(board, addFirstPieceFound))
        return possibleMoves
    }
}