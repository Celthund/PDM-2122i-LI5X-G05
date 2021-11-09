package pt.isel.pdm.chess4android.games

import pt.isel.pdm.chess4android.games.chess.Piece

data class Movement(
    val origin: Position,
    val destination: Position,
    val pieceAtOrigin: Piece,
    val pieceAtDestination: Piece
    ) {
}