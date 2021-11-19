package pt.isel.pdm.chess4android

import android.util.Log
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.Chess
import pt.isel.pdm.chess4android.games.chess.pieces.*
import kotlin.reflect.typeOf

class LoadPGN(dailyGamePGN: String) {
    val chess = Chess(Player.Bottom, 8,8)

    init {
        val moves = dailyGamePGN.split(" ").toTypedArray()

        moves.forEach { pgnMove ->
                parsePGN(pgnMove, chess)
        }
    }

    fun parseNewPosition(pgnMove: String) : Position {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1

        val x: Int = pgnMove[pgnLen-2].code - 'a'.code
        val y: Int = 8 - pgnMove[pgnLen-1].digitToInt()

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
            if(piece.position.y == 8 - pgnMove[pgnLen-1].digitToInt()) return true
        }

        return false
    }

    fun samePieceType(pgnMove: String, piece: Piece) : Boolean {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1

        if(pgnMove[0] in ("BKNQR")) {
            if (pgnMove[0] == 'B' && piece is Bishop) return true
            if (pgnMove[0] == 'K' && piece is King) return true
            if (pgnMove[0] == 'N' && piece is Knight) return true
            if (pgnMove[0] == 'Q' && piece is Queen) return true
            if (pgnMove[0] == 'R' && piece is Rook) return true
        } else
            if (piece is Pawn) return true

        return false
    }

    fun parsePGN(pgnMove: String, chess: Chess) {
        var newPosition: Position

        if(pgnMove[0] == 'O') {
            val piece = chess.playersKing[chess.currentPlayer]!!

            if(piece.getPossibleMoves(chess).size > 0) {
                if(pgnMove.length > 3)
                    newPosition = Position(piece.position.x - 2, piece.position.y)
                else
                    newPosition = Position(piece.position.x + 2, piece.position.y)

                piece.getPossibleMoves(chess).forEach { position ->
                    if (position.equals(newPosition)) {
                        chess.movePieceAtPosition(piece.position, newPosition)
                        return
                    }
                }
            }
        } else {
            newPosition = parseNewPosition(pgnMove)

            chess.playersPieces[chess.currentPlayer]?.forEach { piece ->
                if (samePieceType(pgnMove, piece) && piece.getPossibleMoves(chess).size > 0) {
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