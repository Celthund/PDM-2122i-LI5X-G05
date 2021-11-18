package pt.isel.pdm.chess4android

import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.Chess

class LoadPGN(dailyGamePGN: String) {
    val chess = Chess(Player.Top, 8,8)

    init {
        val moves = dailyGamePGN.split(" ").toTypedArray()

        moves.forEach { pgnMove ->
            parsePGN(pgnMove, chess)
        }
    }

    fun parsePieceName(pgnMove: String) : String {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1

        var pieceName: String = "Pawn"

        if(pgnLen > 2) {
            if (pgnMove[0] == 'B') pieceName = "Bishop"
            if (pgnMove[0] == 'K') pieceName = "King"
            if (pgnMove[0] == 'N') pieceName = "Knight"
            if (pgnMove[0] == 'Q') pieceName = "Queen"
            if (pgnMove[0] == 'R') pieceName = "Rook"
        }
        return pieceName
    }

    fun parseNewPosition(pgnMove: String) : Position {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1

        val x: Int = pgnMove[pgnLen-2].code - 'a'.code
        val y: Int = pgnMove[pgnLen-1].digitToInt() - 1

        return Position(x, y)
    }

    fun parseCurrPosition(pgnMove: String, piece: Piece) : Boolean {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1

        if(pgnLen <= 3) return true

        if(pgnMove[pgnLen-3] == 'x') pgnLen = pgnLen-3
        else pgnLen = pgnLen-2

        if(pgnMove[pgnLen-1].code > 'A'.code && pgnMove[pgnLen-1].code < 'Z'.code) return true

        if(pgnMove[pgnLen-1].code > 'a'.code && pgnMove[pgnLen-1].code < 'z'.code) {
            if (piece.position.x == (pgnMove[pgnLen - 1].code - 'a'.code)) return true
        }

        if(pgnMove[pgnLen-1].code > '0'.code && pgnMove[pgnLen-1].code < '9'.code) {
            if(piece.position.y == pgnMove[pgnLen-1].digitToInt() - 1) return true
        }

        return false
    }

    fun parsePGN(pgnMove: String, chess: Chess) {
        if(pgnMove[0] == 'O') {
            null
        } else {
            val currPieceName: String = parsePieceName(pgnMove)
            val newPosition = parseNewPosition(pgnMove)

            chess.playersPieces[chess.currentPlayer]?.forEach { piece ->
                if (piece.getPieceName().equals(currPieceName) && piece.getPossibleMoves(chess).size > 0) {
                    piece.getPossibleMoves(chess).forEach { position ->
                        if (position.equals(newPosition) && parseCurrPosition(pgnMove, piece)) {
                            chess.movePieceAtPosition(piece.position, newPosition)
                            return
                        }
                    }
                }
            }
        }
    }
}