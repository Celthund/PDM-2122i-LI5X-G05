package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Piece
import pt.isel.pdm.chess4android.games.chess.ChessPiece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class King(player: Player, position: Position) : ChessPiece(player, position) {
    override fun internalGetPositionsInView(board: Game): HashSet<Position> {
        return getMoves(board, true)
    }

    override fun internalGetPossibleMoves(board: Game): HashSet<Position> {
        return getMoves(board, false)
    }

    private fun getMoves(board: Game, addFirstPieceFound: Boolean): HashSet<Position> {
        val possibleMoves: HashSet<Position> = HashSet()

        val allMoves: Array<Position> = arrayOf(
            Position(position.x - 1, position.y - 1), // Up Left
            Position(position.x, position.y - 1), // Up
            Position(position.x + 1, position.y - 1), // Up Right
            Position(position.x + 1, position.y), // Right
            Position(position.x + 1, position.y + 1), // Down Right
            Position(position.x, position.y + 1), // Down
            Position(position.x - 1, position.y + 1), // Down Left
            Position(position.x - 1, position.y) // Left
        )

        var otherPlayerPieces: HashSet<Piece>?
        val otherPlayersMoves: HashSet<Position> = HashSet()
        for (otherPlayer in Player.values()) {
            if (otherPlayer != player) {
                otherPlayerPieces = board.playersPieces[otherPlayer]
                if (otherPlayerPieces != null) {
                    for (piece in otherPlayerPieces){
                        if (piece is Pawn) {
                            val yValToAdd = when(piece.player) {
                                Player.Bottom -> -1
                                Player.Top -> 1
                            }
                            otherPlayersMoves.add(Position(piece.position.x - 1, piece.position.y + yValToAdd))
                            otherPlayersMoves.add(Position(piece.position.x + 1, piece.position.y + yValToAdd))
                        } else if (piece !is King)
                            otherPlayersMoves.addAll(piece.getPositionsInView(board))
                    }
                }
            }
        }
        //Castling
        if (!_wasFirstMovedMade && !addFirstPieceFound) {
            if (!otherPlayersMoves.contains(position)){
                val pos = Position(position.x - 1, position.y)
                var checkLeft = true
                // Check left
                while(board.isPositionValid(pos) && pos.x > 1) {
                    if (board.getPiece(pos) != null || otherPlayersMoves.contains(pos)) {
                        checkLeft = false
                        break
                    }
                    pos.x -= 1
                }
                if (checkLeft) {
                    possibleMoves.add(Position(2, position.y))
                }

                pos.x = position.x + 1
                var checkRight = true
                // Check right
                while(board.isPositionValid(pos) && pos.x < 7) {
                    if (board.getPiece(pos) != null || otherPlayersMoves.contains(pos)) {
                        checkRight = false
                        break
                    }
                    pos.x += 1
                }
                if (checkRight) {
                    possibleMoves.add(Position(6, position.y))
                }
            }
        }

        for (pos in allMoves) {
            if (board.isPositionValid(pos)) {
                val piece: ChessPiece? = board.getPiece(pos) as ChessPiece?
                if (!otherPlayersMoves.contains(pos) && (piece == null || piece.player != player || (addFirstPieceFound && piece.player == player))) {
                    possibleMoves.add(pos)
                }
            }
        }

        return possibleMoves
    }
}