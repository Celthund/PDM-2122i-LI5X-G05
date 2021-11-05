package pt.isel.pdm.chess4android.games

abstract class Game(val MAX_HEIGHT: Int, val MAX_WIDTH: Int) {
    private var _currentPlayer: Player = Player.Bottom
    val currentPlayer: Player get() = _currentPlayer
    private val board: Array<Array<Piece?>> = arrayOf(arrayOf());
    private var moveHistory: MutableList<Movement> = mutableListOf<Movement>()


    fun getPiece(position: Position): Piece? {
        if (position.x >= MAX_WIDTH || position.x < 0)
            throw Error("Invalid x position.")
        if (position.y >= MAX_HEIGHT || position.y < 0)
            throw Error("Invalid y position.")
        return board[position.x][position.y]
    }

    fun getPossibleMoves(position: Position): Array<Position> {
        val piece: Piece = getPiece(position) ?: return arrayOf()
        return piece.getPossibleMoves(position, this)
    }

}