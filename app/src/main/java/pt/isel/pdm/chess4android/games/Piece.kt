package pt.isel.pdm.chess4android.games

abstract class Piece (val player: Player){
    abstract fun getPossibleMoves(position: Position, board: Game) : ArrayList<Position>
}