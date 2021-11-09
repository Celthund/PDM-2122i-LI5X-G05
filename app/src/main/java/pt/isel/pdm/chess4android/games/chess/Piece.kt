package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

abstract class Piece (val player: Player){
    abstract val viewId : Int
    abstract fun getPossibleMoves(position: Position, board: Game) : ArrayList<Position>
}