package pt.isel.pdm.chess4android.common

import android.view.View
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.activities.MainActivityViewModel
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.models.games.Position
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import pt.isel.pdm.chess4android.models.games.chess.pieces.*

class ChessController(private val binding: ActivityMainBinding, private val viewModel: MainActivityViewModel) {

    private lateinit var pieceViewMapper: HashMap<Any, Array<Int>>
    var isInPromote: Boolean = false

    init {
        mapViewToPiece()
    }

    private fun showPromoteOptions(boardModel: Chess, position: Position) {
        binding.bishopBtn.visibility = View.VISIBLE
        binding.bishopBtn.setOnClickListener {
            promotePiece(Bishop::class, boardModel, position)
        }
        binding.bishopBtn.invalidate()

        binding.knightBtn.visibility = View.VISIBLE
        binding.knightBtn.setOnClickListener {
            promotePiece(Knight::class, boardModel, position)
        }
        binding.knightBtn.invalidate()

        binding.queenBtn.visibility = View.VISIBLE

        binding.queenBtn.setOnClickListener {
            promotePiece(Queen::class, boardModel, position)
        }
        binding.queenBtn.invalidate()

        binding.rookBtn.visibility = View.VISIBLE
        binding.rookBtn.setOnClickListener {
            promotePiece(Rook::class, boardModel, position)

        }
        binding.rookBtn.invalidate()
    }

    private fun hidePromoteOptions() {
        binding.bishopBtn.visibility = View.INVISIBLE
        binding.bishopBtn.setOnClickListener {}
        binding.bishopBtn.invalidate()

        binding.knightBtn.visibility = View.INVISIBLE
        binding.knightBtn.setOnClickListener {}
        binding.knightBtn.invalidate()

        binding.queenBtn.visibility = View.INVISIBLE
        binding.queenBtn.setOnClickListener {}
        binding.queenBtn.invalidate()

        binding.rookBtn.visibility = View.INVISIBLE
        binding.rookBtn.setOnClickListener {}
        binding.rookBtn.invalidate()
        viewModel.savePromoteStatus(PromoteCandidate(isInPromote = false))
    }

    private fun promotePiece(piece: Any, boardModel: Chess, position: Position){
        boardModel.promotePawn(position, piece)
        hidePromoteOptions()
        binding.boardView.setBoard(boardModel, isInPromote, this::getPieceDrawableId)
    }

    private fun mapViewToPiece() {
        pieceViewMapper = HashMap()
        pieceViewMapper[Rook::class] = arrayOf(R.drawable.ic_white_rook, R.drawable.ic_black_rook)
        pieceViewMapper[Knight::class] =
            arrayOf(R.drawable.ic_white_knight, R.drawable.ic_black_knight)
        pieceViewMapper[Bishop::class] =
            arrayOf(R.drawable.ic_white_bishop, R.drawable.ic_black_bishop)
        pieceViewMapper[Queen::class] =
            arrayOf(R.drawable.ic_white_queen, R.drawable.ic_black_queen)
        pieceViewMapper[King::class] = arrayOf(R.drawable.ic_white_king, R.drawable.ic_black_king)
        pieceViewMapper[Pawn::class] = arrayOf(R.drawable.ic_white_pawn, R.drawable.ic_black_pawn)
    }

    fun makeMove(currPosition: Position, newPosition: Position, boardModel: Chess) {
        boardModel.movePieceAtPosition(currPosition, newPosition)
        val piece = boardModel.getPiece(newPosition)

        if(piece != null && boardModel.isReadyForPromotion(newPosition)) {
            viewModel.savePromoteStatus(PromoteCandidate(newPosition, true, boardModel))
            showPromoteOptions(boardModel, newPosition)
        }

        viewModel.setBoardModel(boardModel)
    }

    fun getPieceDrawableId(position: Position, boardModel: Chess): Int? {
        val piece = boardModel.getPiece(position)

        if (piece != null) {
            return if (piece.player == viewModel.whitePlayer)
                pieceViewMapper[piece::class]!![0]
            else
                pieceViewMapper[piece::class]!![1]
        }
        return null
    }

    fun promoteIfPossible(promoteCandidate: PromoteCandidate) {
        isInPromote = promoteCandidate.isInPromote
        if(isInPromote) {
            showPromoteOptions(promoteCandidate.boardModel!!, promoteCandidate.position!!)
        }
    }

}