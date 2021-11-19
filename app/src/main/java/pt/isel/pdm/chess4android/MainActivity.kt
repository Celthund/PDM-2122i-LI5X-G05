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
import pt.isel.pdm.chess4android.games.PromoteCandidate
import pt.isel.pdm.chess4android.games.chess.Chess
import pt.isel.pdm.chess4android.games.chess.pieces.Bishop
import pt.isel.pdm.chess4android.games.chess.pieces.Knight
import pt.isel.pdm.chess4android.games.chess.pieces.Queen
import pt.isel.pdm.chess4android.games.chess.pieces.Rook

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun showPromoteOptions(candidate: PromoteCandidate) {
        val boardModel = candidate.boardModel!!

        binding.bishopBtn.visibility = View.VISIBLE
        binding.bishopBtn.setOnClickListener {
            boardModel.putPieceAtPosition(Bishop(candidate.player, candidate.position))
            hidePromoteOptions()
            binding.boardView.setBoard(boardModel)
        }
        binding.bishopBtn.invalidate()

        binding.knightBtn.visibility = View.VISIBLE
        binding.knightBtn.setOnClickListener {
            boardModel.putPieceAtPosition(Knight(candidate.player, candidate.position))
            hidePromoteOptions()
            binding.boardView.setBoard(boardModel)
        }
        binding.knightBtn.invalidate()

        binding.queenBtn.visibility = View.VISIBLE

        binding.queenBtn.setOnClickListener {
            boardModel.putPieceAtPosition(Queen(candidate.player, candidate.position))
            hidePromoteOptions()
            binding.boardView.setBoard(boardModel)
        }
        binding.queenBtn.invalidate()

        binding.rookBtn.visibility = View.VISIBLE
        binding.rookBtn.setOnClickListener {
            boardModel.putPieceAtPosition(Rook(candidate.player, candidate.position))
            hidePromoteOptions()
            binding.boardView.setBoard(boardModel)
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
    }

    private val viewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.boardView.viewModel = viewModel
        val whitePlayer = Player.Bottom

        binding.boardView.initBoard(Chess(whitePlayer, 8,8), whitePlayer)

        viewModel.boardModel.observe(this) {
            binding.boardView.setBoard(it)
        }

        viewModel.promote.observe(this) {
            if(it != null) {
                showPromoteOptions(it)
            } else {
                hidePromoteOptions()
            }
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
}