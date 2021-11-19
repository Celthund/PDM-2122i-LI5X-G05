package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.pieces.*
import java.util.*
import kotlin.math.abs


class Chess(firstPlayer: Player, MAX_HEIGHT: Int, MAX_WIDTH: Int) :
    Game(firstPlayer, MAX_HEIGHT, MAX_WIDTH) {

    val playersKing: EnumMap<Player, King> = EnumMap(Player::class.java)

    init {
        for (i in 0..7) {
            addPieceToBoard(Pawn(Player.Top, Position(i, 1)))
            addPieceToBoard(Pawn(Player.Bottom, Position(i, MAX_HEIGHT - 2)))
        }

        // Set Top Player Back Row
        addPieceToBoard(Rook(Player.Top, Position(0, 0)))
        addPieceToBoard(Knight(Player.Top, Position(1, 0)))
        addPieceToBoard(Bishop(Player.Top, Position(2, 0)))
        addPieceToBoard(Queen(Player.Top, Position(3, 0)))
        addPieceToBoard(King(Player.Top, Position(4, 0)))
        addPieceToBoard(Bishop(Player.Top, Position(5, 0)))
        addPieceToBoard(Knight(Player.Top, Position(6, 0)))
        addPieceToBoard(Rook(Player.Top, Position(7, 0)))

        // Set Bottom Player Back Row
        addPieceToBoard(Rook(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Knight(Player.Bottom, Position(1, MAX_HEIGHT - 1)))
        addPieceToBoard(Bishop(Player.Bottom, Position(2, MAX_HEIGHT - 1)))
        addPieceToBoard(Queen(Player.Bottom, Position(3, MAX_HEIGHT - 1)))
        addPieceToBoard(King(Player.Bottom, Position(4, MAX_HEIGHT - 1)))
        addPieceToBoard(Bishop(Player.Bottom, Position(5, MAX_HEIGHT - 1)))
        addPieceToBoard(Knight(Player.Bottom, Position(6, MAX_HEIGHT - 1)))
        addPieceToBoard(Rook(Player.Bottom, Position(7, MAX_HEIGHT - 1)))
    }

    override fun isGameOver(): Boolean {
        playersPieces[currentPlayer]?.forEach {
            if (it.getPossibleMoves(this).isNotEmpty()) return false
        }
        return true
    }

    override fun whichPlayerWon(): Player? {
        if (isGameOver()) {
            Player.values().forEach { player ->
                if (player != currentPlayer) {
                    playersPieces[player]?.forEach { piece ->
                        if (piece.getPossibleMoves(this).contains(
                                playersKing[currentPlayer]?.position
                            )
                        ) return player
                    }
                }
            }
        }
        return null
    }

    override fun movePieceAtPosition(oldPosition: Position, newPosition: Position): Boolean {
        val playerThatPlayed = _currentPlayer
        val res = super.movePieceAtPosition(oldPosition, newPosition)
        if (res) {
            // TODO Missing promotion of pawn
            playersKing[playerThatPlayed]?.resetPossibleMoves()
            when (board[newPosition.x][newPosition.y]) {
                // En passant
                is Pawn -> {
                    if (abs(moveHistory.last().origin.y - moveHistory.last().destination.y) == 2 && oldPosition.x != newPosition.x) {
                        board[newPosition.x][oldPosition.y] = null
                    }
                }
                // Castling
                is King -> {
                    if (abs(oldPosition.x - newPosition.x) > 1) {
                        val pos: Position
                        if (newPosition.x == 2) {
                            pos = Position(newPosition.x + 1, newPosition.y)
                            board[pos.x][pos.y] = board[newPosition.x - 2][newPosition.y]
                            board[newPosition.x - 2][newPosition.y] = null
                            board[pos.x][pos.y]?.position = pos
                        } else if (newPosition.x == 6) {
                            pos = Position(newPosition.x - 1, newPosition.y)
                            board[pos.x][pos.y] = board[newPosition.x + 1][newPosition.y]
                            board[newPosition.x + 1][newPosition.y] = null
                            board[pos.x][pos.y]?.position = pos
                        }
                    }
                }
            }
        }
        return res
    }

    override fun addPieceToBoard(piece: Piece): Boolean {
        if (piece is King) {
            if (board[piece.position.x][piece.position.y] != null) throw Error("Position already has a piece.")
            board[piece.position.x][piece.position.y] = piece
            playersKing[piece.player] = piece
            return true
        } else {
            return super.addPieceToBoard(piece)
        }
    }

}

