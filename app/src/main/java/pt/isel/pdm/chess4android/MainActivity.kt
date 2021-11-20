package pt.isel.pdm.chess4android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.Position
import pt.isel.pdm.chess4android.games.PromoteCandidate
import pt.isel.pdm.chess4android.games.chess.Chess
import pt.isel.pdm.chess4android.games.chess.pieces.*

class MainActivity : AppCompatActivity() {
    private val pieceViewMapper: HashMap<Any, Array<Int>> = HashMap()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : MainActivityViewModel by viewModels()
    var isInPromote: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapViewToPiece()

        val boardModel = Chess(viewModel.whitePlayer, 8,8)
        binding.boardView.initBoard(boardModel, this, this::makeMove)

        if(viewModel.boardModel.value == null) {
            viewModel.setBoardModel(boardModel)
        }

        viewModel.isInPromote.observe(this) {
            isInPromote = it.isInPromote
            if(isInPromote) {
                showPromoteOptions(it.boardModel!!, it.position!!)
            }
        }
        
        viewModel.boardModel.observe(this) {
            binding.boardView.setBoard(it)
        }



        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuGetPuzzle -> {
                viewModel.GetLichessPuzzle()
                viewModel.lichessPuzzle.observe(this) {
                    if(it==null) {
                        Toast.makeText (this, "Puzzle recover error", Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.PlayLichessPuzzle(it)
                        setContentView(binding.root)
                    }
                }
                true
            }
            R.id.menuSolvePuzzle -> {
                startActivity(Intent(this, SolveActivity::class.java))
                true
            }
            R.id.menuAbout -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        binding.boardView.setBoard(boardModel)
    }

    private fun mapViewToPiece() {
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

    private fun makeMove(currPosition: Position, newPosition: Position, boardModel: Chess) {
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
}