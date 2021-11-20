package pt.isel.pdm.chess4android

import pt.isel.pdm.chess4android.games.Movement
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.Chess
import pt.isel.pdm.chess4android.games.chess.Puzzle
import pt.isel.pdm.chess4android.games.chess.pieces.*

class LoadPGN(dailyGame: PuzzleInfo) {
    private var initialPlayer: Player
    var chess : Chess

    init {
        initialPlayer = if (dailyGame.puzzle.initialPly % 2 == 0) Player.Top else Player.Bottom

        chess = Puzzle(initialPlayer, 8,8)
        val moves = dailyGame.game.pgn.split(" ").toTypedArray()
        //val moves = "e3 e6 Qf3 f6 Qxb7 Na6 Qe4 c6 b4 d6 b5 f5 b6 c5 b7 c4 b8=B c3 dxc3".split(" ").toTypedArray()

        moves.forEach { pgnMove ->
            parsePGN(pgnMove, chess)
        }

        val solution: ArrayList<Movement> = arrayListOf()
        dailyGame.puzzle.solution.forEach {
            solution.add(Movement(convertPGNPosition(it[0], it[1]), convertPGNPosition(it[2], it[3]), null, null))
        }
        (chess as Puzzle).solutionMoves = solution
    }

    fun convertPGNPosition(x: Char?, y: Char?) : Position {
        var x: Int = (x?.code ?: 'a'.code) - 'a'.code
        var y: Int = (y?.digitToInt() ?: '1'.digitToInt()) - 1

        if(initialPlayer == Player.Bottom) y = 7 - y
        else x = 7 - x

        return Position(x, y)
    }

    fun parseNewPosition(pgnMove: String) : Position {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1
        if(pgnMove[pgnLen-1] in "BNQR" && pgnMove[pgnLen-2] == '=') pgnLen = pgnLen-2

        return convertPGNPosition(pgnMove[pgnLen-2], pgnMove[pgnLen-1])
    }

    fun parseCurrPosition(pgnMove: String, piece: Piece) : Boolean {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] == '+') pgnLen = pgnLen-1
        if(pgnMove[pgnLen-1] in "BNQR" && pgnMove[pgnLen-2] == '=') pgnLen = pgnLen-2

        if(pgnLen <= 3) return true

        if(pgnMove[pgnLen-3] == 'x') pgnLen = pgnLen-3
        else pgnLen = pgnLen-2

        if(pgnMove[pgnLen-1].code > 'A'.code && pgnMove[pgnLen-1].code < 'Z'.code) return true

        if(pgnMove[pgnLen-1].code > 'a'.code && pgnMove[pgnLen-1].code < 'z'.code) {
            if (piece.position.x == convertPGNPosition(pgnMove[pgnLen-1], null).x) return true
        }

        if(pgnMove[pgnLen-1].code > '0'.code && pgnMove[pgnLen-1].code < '9'.code) {
            if(piece.position.y == convertPGNPosition(null, pgnMove[pgnLen-1]).y) return true
        }

        return false
    }

    fun samePieceType(pgnMove: String, piece: Piece) : Boolean {
        if(pgnMove[0] in ("BNQR")) {
            if (pgnMove[0] == 'B' && piece is Bishop) return true
            if (pgnMove[0] == 'N' && piece is Knight) return true
            if (pgnMove[0] == 'Q' && piece is Queen) return true
            if (pgnMove[0] == 'R' && piece is Rook) return true
        } else
            if (piece is Pawn) return true

        return false
    }

    fun verifyPromote(pgnMove: String) : Any? {
        var pgnLen: Int = pgnMove.length
        if(pgnMove[pgnLen-1] !in "BNQR" || pgnMove[pgnLen-2] != '=') return null

        if (pgnMove[pgnLen-1] == 'B') return Bishop::class
        if (pgnMove[pgnLen-1] == 'N') return Knight::class
        if (pgnMove[pgnLen-1] == 'Q') return Queen::class
        if (pgnMove[pgnLen-1] == 'R') return Rook::class

        return null
    }

    fun parsePGN(pgnMove: String, chess: Chess) {
        var newPosition: Position

        if(pgnMove[0] in "KO") {
            val piece = chess.playersKing[chess.currentPlayer]!!

            if(piece.getPossibleMoves(chess).size > 0) {
                if(pgnMove[0] == 'K')
                    newPosition = parseNewPosition(pgnMove)
                else {
                    if( (initialPlayer == Player.Bottom && pgnMove.length > 3) ||
                        (initialPlayer == Player.Top && pgnMove.length <= 3))
                            newPosition = Position(piece.position.x - 2, piece.position.y)
                    else
                            newPosition = Position(piece.position.x + 2, piece.position.y)
                }

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
                            val promotedClass = verifyPromote(pgnMove)
                            if(promotedClass != null)
                                chess.promotePawn(newPosition, promotedClass)
                            return
                        }
                    }
                }
            }
        }
    }
}