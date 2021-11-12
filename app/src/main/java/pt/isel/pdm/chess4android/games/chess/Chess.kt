package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.pieces.*


class Chess(firstPlayer:Player, MAX_HEIGHT: Int, MAX_WIDTH: Int) : Game(firstPlayer, MAX_HEIGHT, MAX_WIDTH) {

    init {
        for (i in 0..7) {
            board[i][1] = Pawn(Player.Top, Position(i, 1))
            board[i][MAX_HEIGHT - 2] = Pawn(Player.Bottom, Position(i, MAX_HEIGHT - 2))
        }

        board[0][0] = Rook(Player.Top, Position(0, 0))
        board[1][0] = Knight(Player.Top, Position(1, 0))
        board[2][0] = Bishop(Player.Top, Position(2, 0))
        board[3][0] = Queen(Player.Top, Position(3, 0))
        board[4][0] = King(Player.Top, Position(4, 0))
        board[5][0] = Bishop(Player.Top, Position(5, 0))
        board[6][0] = Knight(Player.Top, Position(6, 0))
        board[7][0] = Rook(Player.Top, Position(7, 0))

        board[0][MAX_HEIGHT - 1] = Rook(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[1][MAX_HEIGHT - 1] = Knight(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[2][MAX_HEIGHT - 1] = Bishop(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[3][MAX_HEIGHT - 1] = Queen(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[4][MAX_HEIGHT - 1] = King(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[5][MAX_HEIGHT - 1] = Bishop(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[6][MAX_HEIGHT - 1] = Knight(Player.Bottom, Position(0, MAX_HEIGHT - 1))
        board[7][MAX_HEIGHT - 1] = Rook(Player.Bottom, Position(0, MAX_HEIGHT - 1))
    }

}

