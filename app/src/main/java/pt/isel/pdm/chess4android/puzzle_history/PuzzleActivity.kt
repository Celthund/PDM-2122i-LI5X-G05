package pt.isel.pdm.chess4android.puzzle_history

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.activities.*
import pt.isel.pdm.chess4android.common.ChessController
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.models.games.Position
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import pt.isel.pdm.chess4android.models.games.chess.pieces.*

class PuzzleActivity : AppCompatActivity() {
    companion object {
        fun buildIntent(origin: Activity, puzzle: PuzzleInfo) : Intent {
            val intent = Intent(origin, PuzzleActivity::class.java)
            intent.putExtra(MAIN_ACTIVITY_VIEW_STATE, puzzle)
            return intent
        }

    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : MainActivityViewModel by viewModels()

    private val chessController by lazy {
        ChessController(binding, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.boardView.initBoard(8,8, chessController::makeMove)

        val boardModel = viewModel.boardModel.value
        val puzzleInfo = viewModel.lichessPuzzle.value

        if(puzzleInfo == null) {
            viewModel.getLichessPuzzle()
        }


        viewModel.lichessPuzzle.observe(this) {
            if(boardModel == null)
                viewModel.playLichessPuzzle(it)
        }

        viewModel.isInPromote.observe(this) {
            chessController.promoteIfPossible(it)
        }

        viewModel.boardModel.observe(this) {
            binding.boardView.setBoard(it, chessController.isInPromote, chessController::getPieceDrawableId)
        }

        setContentView(binding.root)
    }
}