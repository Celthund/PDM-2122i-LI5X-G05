package pt.isel.pdm.chess4android.games

abstract class Piece {

    abstract fun getPossibleMoves(position: Position, board: Game) : Array<Position>
}