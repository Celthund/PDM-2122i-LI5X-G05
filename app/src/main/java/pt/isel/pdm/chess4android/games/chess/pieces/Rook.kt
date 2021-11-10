package pt.isel.pdm.chess4android.games.chess.pieces

import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.chess.Piece
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

class Rook(player: Player) : Piece(player) {
    override val viewId: Int
        get() = if (player.name == Player.Top.name) R.drawable.ic_white_rook else R.drawable.ic_black_rook

    override fun getPossibleMoves(position: Position, board: Game): ArrayList<Position> {
        // TODO missing validation of possible check to the King if move is made.
        val positions: ArrayList<Position> = arrayListOf()
        positions.addAll(getPositionsLeftOrRight(position, board, position.x + 1, board.MAX_WIDTH))
        positions.addAll(getPositionsLeftOrRight(position, board, position.x - 1, 0))
        positions.addAll(getPositionsUpOrDown(position, board, position.y + 1, board.MAX_HEIGHT))
        positions.addAll(getPositionsUpOrDown(position, board, position.y - 1, 0))
        return positions
    }

    private fun getPositionsLeftOrRight(
        position: Position,
        board: Game,
        start: Int,
        end: Int
    ): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in start until end) {
            pos = Position(i, position.y)
            if (board.isPositionValid(pos)) {
                piece = board.getPiece(pos)
                if (piece == null) {
                    positions.add(pos)
                } else if (piece.player != player) {
                    positions.add(pos)
                    break
                } else if (piece.player == player) {
                    break
                }
            }
        }
        for (i in start downTo end) {
            pos = Position(i, position.y)
            if (board.isPositionValid(pos)) {
                piece = board.getPiece(pos)
                if (piece == null) {
                    positions.add(pos)
                } else if (piece.player != player) {
                    positions.add(pos)
                    break
                } else if (piece.player == player) {
                    break
                }
            }
        }
        return positions
    }

    private fun getPositionsUpOrDown(
        position: Position,
        board: Game,
        start: Int,
        end: Int
    ): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in start until end) {
            pos = Position(position.x, i)
            if (board.isPositionValid(pos)) {
                piece = board.getPiece(pos)
                if (piece == null) {
                    positions.add(pos)
                } else if (piece.player != player) {
                    positions.add(pos)
                    break
                } else if (piece.player == player) {
                    break
                }
            }
        }
        for (i in start downTo end) {
            pos = Position(position.x, i)
            if (board.isPositionValid(pos)) {
                piece = board.getPiece(pos)
                if (piece == null) {
                    positions.add(pos)
                } else if (piece.player != player) {
                    positions.add(pos)
                    break
                } else if (piece.player == player) {
                    break
                }
            }
        }
        return positions
    }

}