package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.games.*
import kotlin.math.abs

class Pawn(player: Player) : Piece(player) {


    private val originalRow: Int by lazy {
        when(player) {
            Player.Top -> 1
            Player.Bottom -> 6
        }
    }


    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()

        if (player == Player.Top) {

            val pos1 = Position(position.x, position.y + 1) // Down 1
            if (board.getPiece(pos1) == null){
                positions.add(pos1)
            }

            val pos2 = Position(position.x, position.y + 2) // Down 2
            if (position.y == originalRow && board.getPiece(pos1) == null && board.getPiece(pos2) == null){
                positions.add(pos2)
            }

            val lastMovement: Movement? = board.getLastMovement()

            val captureLeft = Position(position.x - 1, position.y + 1) // Left Down needs capture

            val captureRight = Position(position.x + 1, position.y + 1) // Right Down needs capture

            if (lastMovement == null) {
                val pieceAtLeft: Piece? = board.getPiece(captureLeft);
                if (pieceAtLeft != null && pieceAtLeft.player != player){
                    positions.add(captureLeft)
                }

                val pieceAtRight: Piece? = board.getPiece(captureRight);
                if (pieceAtRight != null && pieceAtRight.player != player){
                    positions.add(captureRight)
                }
            } else if ( // Check En Passant by checking that the last piece that move was a Pawn and it move more than 2 spaces (first move)
                lastMovement.pieceAtOrigin is Pawn &&
                abs(lastMovement.origin.y - lastMovement.destination.y) == 2 //
            ) {
                val pieceAtLeft: Piece? = board.getPiece(Position(captureLeft.x, captureLeft.y - 1));
                if (pieceAtLeft is Pawn && pieceAtLeft.player != player){
                    positions.add(captureLeft)
                }

                val pieceAtRight: Piece? = board.getPiece(Position(captureRight.x, captureRight.y - 1));
                if (pieceAtRight is Pawn && pieceAtRight.player != player){
                    positions.add(captureRight)
                }
            }


        } else if (player == Player.Bottom) {

            val pos1 = Position(position.x, position.y - 1) // Up 1
            if (board.getPiece(pos1) == null){
                positions.add(pos1)
            }

            val pos2 = Position(position.x, position.y - 2) // Up 2
            if (position.y == originalRow && board.getPiece(pos1) == null && board.getPiece(pos2) == null){
                positions.add(pos2)
            }

            val lastMovement: Movement? = board.getLastMovement()

            val captureLeft = Position(position.x - 1, position.y - 1) // Left Up needs capture

            val captureRight = Position(position.x + 1, position.y - 1) // Right Up needs capture

            if (lastMovement == null) {
                val pieceAtLeft: Piece? = board.getPiece(captureLeft);
                if (pieceAtLeft != null && pieceAtLeft.player != player){
                    positions.add(captureLeft)
                }

                val pieceAtRight: Piece? = board.getPiece(captureRight);
                if (pieceAtRight != null && pieceAtRight.player != player){
                    positions.add(captureRight)
                }
            } else if ( // Check En Passant by checking that the last piece that move was a Pawn and it move more than 2 spaces (first move)
                lastMovement.pieceAtOrigin is Pawn &&
                abs(lastMovement.origin.y - lastMovement.destination.y) == 2 //
            ) {
                val pieceAtLeft: Piece? = board.getPiece(Position(captureLeft.x, captureLeft.y + 1));
                if (pieceAtLeft is Pawn && pieceAtLeft.player != player){
                    positions.add(captureLeft)
                }

                val pieceAtRight: Piece? = board.getPiece(Position(captureRight.x, captureRight.y + 1));
                if (pieceAtRight is Pawn && pieceAtRight.player != player){
                    positions.add(captureRight)
                }
            }

        }

        // TODO missing validation of possible check to the King if move is made.
        return positions;
    }
}