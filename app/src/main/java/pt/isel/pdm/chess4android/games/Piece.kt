package pt.isel.pdm.chess4android.games

import java.util.*

interface Piece {
    fun getPossibleMoves(position: Position) : Array<Position>;
}