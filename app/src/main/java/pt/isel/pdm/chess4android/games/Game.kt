package pt.isel.pdm.chess4android.games

abstract class Game(val MAX_HEIGHT: Int, val MAX_WIDTH: Int) {
    internal var _currentPlayer: Player = Player.Bottom

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

}