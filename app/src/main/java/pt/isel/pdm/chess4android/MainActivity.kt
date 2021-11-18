package pt.isel.pdm.chess4android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.games.Player
import pt.isel.pdm.chess4android.games.chess.Chess

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val whitePlayer = Player.Bottom
        binding.boardView.initBoard(Chess(whitePlayer, 8,8), whitePlayer)
        binding.boardView.viewModel = viewModel
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
                        Toast.makeText (this, "Unrecovered Puzzle", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText (this, "Successfully Retrieved Puzzle", Toast.LENGTH_LONG).show()
                        binding.boardView.initBoard(LoadPGN(it.game.pgn).chess, Player.Top)
                        binding.boardView.invalidate()
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