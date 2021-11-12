package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.chess.pieces.*


class Chess(firstPlayer:Player, MAX_HEIGHT: Int, MAX_WIDTH: Int) : Game(firstPlayer, MAX_HEIGHT, MAX_WIDTH) {

    init {

        for (i in 0..7) {
            addPieceToBoard(Pawn(Player.Top, Position(i, 1)))
            addPieceToBoard(Pawn(Player.Bottom, Position(i, MAX_HEIGHT - 2)))
        }
        addPieceToBoard(Rook(Player.Top, Position(0, 0)))

        addPieceToBoard(Knight(Player.Top, Position(1, 0)))
        addPieceToBoard(Bishop(Player.Top, Position(2, 0)))
        addPieceToBoard(Queen(Player.Top, Position(3, 0)))
        addPieceToBoard(King(Player.Top, Position(4, 0)))
        addPieceToBoard(Bishop(Player.Top, Position(5, 0)))
        addPieceToBoard(Knight(Player.Top, Position(6, 0)))
        addPieceToBoard(Rook(Player.Top, Position(7, 0)))

        addPieceToBoard(Rook(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Knight(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Bishop(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Queen(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(King(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Bishop(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Knight(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
        addPieceToBoard(Rook(Player.Bottom, Position(0, MAX_HEIGHT - 1)))
    }

}

