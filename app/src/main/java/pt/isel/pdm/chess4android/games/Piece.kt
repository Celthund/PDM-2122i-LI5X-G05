package pt.isel.pdm.chess4android.games


abstract class Piece(val player: Player, var position: Position) {

    protected var _wasFirstMovedMade = false
    val wasFirstMovedMade get() = _wasFirstMovedMade
    protected var possibleMoves: HashSet<Position>? = null
    protected var positionsInView: HashSet<Position>? = null

    fun resetPossibleMoves() {
        possibleMoves = null
        positionsInView = null
    }

    fun setFirstMoveMadeFlag(){
        _wasFirstMovedMade = true
    }

    open fun getPositionsInView(board: Game): HashSet<Position> {
        if (possibleMoves == null){
            possibleMoves = internalGetPositionsInView(board)
        }
        return possibleMoves!!
    }

    abstract fun internalGetPositionsInView(board: Game): HashSet<Position>


    open fun getPossibleMoves(board: Game): HashSet<Position> {
        return internalGetPossibleMoves(board)
        //if (possibleMoves == null){
        //    possibleMoves = internalGetPossibleMoves(board)
        //}
        //return possibleMoves!!
    }

    protected abstract fun internalGetPossibleMoves(board: Game): HashSet<Position>

}