package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.*
import pt.isel.pdm.chess4android.games.chess.Piece
import kotlin.math.abs

class Pawn(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_pawn else R.drawable.ic_black_pawn


    private val originalRow: Int by lazy {
        when (player) {
            Player.Top -> 1
            Player.Bottom -> 6
        }
    }


    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        if (player == Player.Top) {
            return calculateUpOrDown(position, board, 1, 2)
        }
        if (player == Player.Bottom) {
            return calculateUpOrDown(position, board, -1, -2)
        }
        return arrayListOf()
    }

    private fun calculateUpOrDown(position:Position, board: Game, oneMove: Int, twoMoves: Int): ArrayList<Position> {
        // TODO missing validation of possible check to the King if move is made.

        val positions: ArrayList<Position> = arrayListOf()

        val pos1 = Position(position.x, position.y + oneMove) // Down 1
        if (board.getPiece(pos1) == null) {
            positions.add(pos1)
        }

        val pos2 = Position(position.x, position.y + twoMoves) // Down 2
        if (position.y == originalRow && board.getPiece(pos1) == null && board.getPiece(pos2) == null) {
            positions.add(pos2)
        }

        val lastMovement: Movement? = board.getLastMovement()

        var captureLeft: Position? = null
        if (position.x > 0) {
            captureLeft = Position(position.x - 1, position.y + oneMove) // Left Down needs capture
        }
        var captureRight: Position? = null
        if (position.x < board.MAX_WIDTH - 1) {
            captureRight =
                Position(position.x + 1, position.y + oneMove) // Right Down needs capture
        }


        if (lastMovement == null) {
            if (captureLeft != null) {
                val pieceAtLeft: Piece? = board.getPiece(captureLeft);
                if (pieceAtLeft != null && pieceAtLeft.player != player) {
                    positions.add(captureLeft)
                }
            }
            if (captureRight != null) {
                val pieceAtRight: Piece? = board.getPiece(captureRight);
                if (pieceAtRight != null && pieceAtRight.player != player) {
                    positions.add(captureRight)
                }
            }
        } else if ( // Check En Passant by checking that the last piece that move was a Pawn and it move more than 2 spaces (first move)
            lastMovement.pieceAtOrigin is Pawn &&
            abs(lastMovement.origin.y - lastMovement.destination.y) == 2 //
        ) {
            if (captureLeft != null) {
                val pieceAtLeft: Piece? =
                    board.getPiece(Position(captureLeft.x, captureLeft.y - (oneMove)));
                if (pieceAtLeft is Pawn && pieceAtLeft.player != player && captureLeft.x == lastMovement.destination.x) {
                    positions.add(captureLeft)
                }
            }

            if (captureRight != null) {
                val pieceAtRight: Piece? =
                    board.getPiece(Position(captureRight.x, captureRight.y - 1));
                if (pieceAtRight is Pawn && pieceAtRight.player != player && captureRight.x == lastMovement.destination.x) {
                    positions.add(captureRight)
                }
            }
        }

        return positions;
    }
}