package pt.isel.pdm.chess4android.activities
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.ChessController
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.models.games.Position
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import pt.isel.pdm.chess4android.models.games.chess.pieces.*
import pt.isel.pdm.chess4android.puzzle_history.HistoryActivity
import pt.isel.pdm.chess4android.puzzle_history.PuzzleActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : MainActivityViewModel by viewModels()

    private val chessController by lazy {
        ChessController(binding, viewModel)
    }

    var isInPromote: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.boardView.initBoard(8,8, chessController::makeMove)

        if(viewModel.boardModel.value == null) {
            viewModel.setBoardModel(Chess(viewModel.whitePlayer,8,8 ))
        }

        viewModel.isInPromote.observe(this) {
            chessController.promoteIfPossible(it)
        }
        
        viewModel.boardModel.observe(this) {
            binding.boardView.setBoard(it, isInPromote, chessController::getPieceDrawableId)
        }

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuGetPuzzle -> {
                startActivity(Intent(this, PuzzleActivity::class.java))
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
            R.id.history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}