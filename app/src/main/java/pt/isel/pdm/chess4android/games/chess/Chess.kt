package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.pieces.*


class Chess(MAX_HEIGHT: Int, MAX_WIDTH: Int) : Game(MAX_HEIGHT, MAX_WIDTH) {

    init {
        for (i in 0..7) {
            board[i][1] = Pawn(Player.Top)
            board[i][MAX_HEIGHT - 2] = Pawn(Player.Bottom)
        }

        board[0][0] = Rook(Player.Top)
        board[1][0] = Knight(Player.Top)
        board[2][0] = Bishop(Player.Top)
        board[3][0] = Queen(Player.Top)
        board[4][0] = King(Player.Top)
        board[5][0] = Bishop(Player.Top)
        board[6][0] = Knight(Player.Top)
        board[7][0] = Rook(Player.Top)

        board[0][MAX_HEIGHT - 1] = Rook(Player.Bottom)
        board[1][MAX_HEIGHT - 1] = Knight(Player.Bottom)
        board[2][MAX_HEIGHT - 1] = Bishop(Player.Bottom)
        board[3][MAX_HEIGHT - 1] = Queen(Player.Bottom)
        board[4][MAX_HEIGHT - 1] = King(Player.Bottom)
        board[5][MAX_HEIGHT - 1] = Bishop(Player.Bottom)
        board[6][MAX_HEIGHT - 1] = Knight(Player.Bottom)
        board[7][MAX_HEIGHT - 1] = Rook(Player.Bottom)
    }

}

