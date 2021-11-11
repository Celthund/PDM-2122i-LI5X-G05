package pt.isel.pdm.chess4android.games.chess

import pt.isel.pdm.chess4android.games.Game
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position

abstract class Piece(val player: Player) {
    abstract val viewId: Int
    abstract fun getPossibleMoves(position: Position, board: Game): ArrayList<Position>

    protected fun getPositionsToRight(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in position.x + 1 until board.MAX_WIDTH) {
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

    protected fun getPositionsToLeft(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in position.x - 1 downTo 0) {
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

    protected fun getPositionsToBottom(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in position.y + 1 until board.MAX_HEIGHT) {
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

    protected fun getPositionsToTop(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos: Position
        var piece: Piece?
        for (i in position.y - 1 downTo 0) {
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

    protected fun getPositionsToTopLeft(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos = Position(position.x - 1, position.y - 1)
        var piece: Piece?
        // Top right
        while (board.isPositionValid(pos)) {
            piece = board.getPiece(pos)
            if (piece == null) {
                positions.add(pos)
            } else if (piece.player != player) {
                positions.add(pos)
                break
            } else if (piece.player == player) {
                break
            }
            pos = Position(pos.x - 1, pos.y - 1)
        }
        return positions
    }

    protected fun getPositionsToBottomRight(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos = Position(position.x + 1, position.y + 1)
        var piece: Piece?
        // Top right
        while (board.isPositionValid(pos)) {
            piece = board.getPiece(pos)
            if (piece == null) {
                positions.add(pos)
            } else if (piece.player != player) {
                positions.add(pos)
                break
            } else if (piece.player == player) {
                break
            }
            pos = Position(pos.x + 1, pos.y + 1)
        }
        return positions
    }

    protected fun getPositionsToTopRight(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos = Position(position.x + 1, position.y - 1)
        var piece: Piece?
        // Top right
        while (board.isPositionValid(pos)) {
            piece = board.getPiece(pos)
            if (piece == null) {
                positions.add(pos)
            } else if (piece.player != player) {
                positions.add(pos)
                break
            } else if (piece.player == player) {
                break
            }
            pos = Position(pos.x + 1, pos.y - 1)
        }
        return positions
    }

    protected fun getPositionsToBottomLeft(position: Position, board: Game): ArrayList<Position> {
        val positions: ArrayList<Position> = arrayListOf()
        var pos = Position(position.x - 1, position.y + 1)
        var piece: Piece?
        // Top right
        while (board.isPositionValid(pos)) {
            piece = board.getPiece(pos)
            if (piece == null) {
                positions.add(pos)
            } else if (piece.player != player) {
                positions.add(pos)
                break
            } else if (piece.player == player) {
                break
            }
            pos = Position(pos.x - 1, pos.y + 1)
        }
        return positions
    }
}