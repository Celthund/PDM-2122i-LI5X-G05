package pt.isel.pdm.chess4android.games

import pt.isel.pdm.chess4android.games.chess.Piece

abstract class Game(firstPlayer: Player, val MAX_HEIGHT: Int, val MAX_WIDTH: Int) {
    private var _currentPlayer: Player
    val currentPlayer get() = _currentPlayer

    init {
        _currentPlayer = firstPlayer
    }

    protected val board: Array<Array<Piece?>> = Array(MAX_WIDTH) { Array(MAX_HEIGHT) { null } };
    private var moveHistory: MutableList<Movement> = mutableListOf()

    fun getPiece(position: Position): Piece? {
        if (position.x >= MAX_WIDTH || position.x < 0)
            throw Error("Invalid x position.")
        if (position.y >= MAX_HEIGHT || position.y < 0)
            throw Error("Invalid y position.")
        return board[position.x][position.y]
    }

    fun getPossibleMoves(position: Position): HashSet<Position> {
        val piece: Piece = getPiece(position) ?: return HashSet()
        return piece.getPossibleMoves(this)
    }

    fun getLastMovement(): Movement? {
        return if (moveHistory.isNotEmpty()) { moveHistory.last() } else { null }
    }

    fun movePieceAtPosition(oldPosition: Position, newPosition: Position) {
        if (board[oldPosition.x][oldPosition.y] == null) throw Error("No piece in that position.")
        if (newPosition !in getPossibleMoves(oldPosition)) throw Error("Not a valid move.")
        moveHistory.add(
            Movement(
                oldPosition,
                newPosition,
                board[oldPosition.x][oldPosition.y],
                board[newPosition.x][newPosition.y]
            )
        )
        board[newPosition.x][newPosition.y] = board[oldPosition.x][oldPosition.y]
        board[oldPosition.x][oldPosition.y]?.position = newPosition
        board[oldPosition.x][oldPosition.y] = null

        _currentPlayer = if (_currentPlayer == Player.Top) Player.Bottom else Player.Top
    }

    fun isPositionValid(positionToCheck: Position): Boolean {
        return positionToCheck.x in 0 until MAX_WIDTH && positionToCheck.y in 0 until MAX_HEIGHT
    }
}