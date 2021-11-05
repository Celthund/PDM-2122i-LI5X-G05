package pt.isel.pdm.chess4android.games

data class Movement(
    val origin: Position,
    val destination: Position,
    val pieceAtOrigin: Piece,
    val pieceAtDestination: Piece
    ) {
}