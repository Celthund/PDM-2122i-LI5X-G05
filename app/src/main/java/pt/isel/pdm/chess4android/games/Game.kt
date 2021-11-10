package pt.isel.pdm.chess4android.games

import pt.isel.pdm.chess4android.games.chess.Piece

abstract class Game(val MAX_HEIGHT: Int, val MAX_WIDTH: Int) {
    internal var _currentPlayer: Player = Player.Top

    internal val board: Array<Array<Piece?>> = Array(MAX_WIDTH) { Array(MAX_HEIGHT) { null } };
    internal var moveHistory: MutableList<Movement> = mutableListOf()


    fun getPiece(position: Position): Piece? {
        if (position.x >= MAX_WIDTH || position.x < 0)
            throw Error("Invalid x position.")
        if (position.y >= MAX_HEIGHT || position.y < 0)
            throw Error("Invalid y position.")
        return board[position.x][position.y]
    }

    fun getPossibleMoves(position: Position): ArrayList<Position> {
        val piece: Piece = getPiece(position) ?: return arrayListOf()
        return piece.getPossibleMoves(position, this)
    }

    fun getLastMovement(): Movement? {
        return if (moveHistory.isNotEmpty()) {
            moveHistory.last()
        } else {
            null;
        }
    }

    fun movePieceAtPosition(oldPosition: Position, newPosition: Position) {
        if (board[oldPosition.x][oldPosition.y] != null) throw Error("No piece in that position.")
        moveHistory.add(Movement(
            oldPosition,
            newPosition,
            board[oldPosition.x][oldPosition.y],
            board[newPosition.x][newPosition.y]
        ))
        board[newPosition.x][newPosition.y] = board[oldPosition.x][oldPosition.y]
        board[oldPosition.x][oldPosition.y] = null

        _currentPlayer = if (_currentPlayer == Player.Top) Player.Bottom else Player.Top
    }
}