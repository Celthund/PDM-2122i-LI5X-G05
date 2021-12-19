package pt.isel.pdm.chess4android.controllers.puzzle_activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.controllers.application.PuzzleApplication
import pt.isel.pdm.chess4android.controllers.main_activity.MAIN_ACTIVITY_VIEW_STATE
import pt.isel.pdm.chess4android.controllers.puzzle_history_activity.PuzzleEntity
import pt.isel.pdm.chess4android.controllers.utils.ChessViewModel
import pt.isel.pdm.chess4android.controllers.utils.callbackAfterAsync
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.models.PuzzleInfo

class PuzzleActivity : AppCompatActivity() {
    companion object {
        fun buildIntent(origin: Activity, puzzle: PuzzleInfo): Intent {
            val intent = Intent(origin, PuzzleActivity::class.java)
            intent.putExtra(MAIN_ACTIVITY_VIEW_STATE, puzzle)
            return intent
        }

    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: PuzzleViewModel by viewModels()

    private val chessController by lazy {
        ChessViewModel(binding, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.boardView.initBoard(
            8, 8,
            chessController::makeMove, chessController::getPossibleMoves
        )

        val boardModel = viewModel.boardModel.value

        if (viewModel.lichessPuzzle.value == null) {
            viewModel.getLichessPuzzle()
        }

        viewModel.lichessPuzzle.observe(this) {
            if (boardModel == null)
                viewModel.playLichessPuzzle(it)
        }

        viewModel.isInPromote.observe(this) {
            chessController.promoteIfPossible(it)
        }

        viewModel.boardModel.observe(this) {
            binding.boardView.setBoard(
                it,
                chessController.isInPromote,
                chessController::getPieceDrawableId
            )
        }

        viewModel.isGameOver.observe(this) {
            callbackAfterAsync({}) {
                val puzzleInfo = viewModel.lichessPuzzle.value
                if (puzzleInfo != null) {
                    puzzleInfo.solved = true
                    (application as PuzzleApplication).historyDB.getHistoryPuzzleDao().update(
                        PuzzleEntity(
                            puzzleInfo.puzzle.id,
                            puzzleInfo,
                            puzzleInfo.timestamp
                        )
                    )
                }
            }
            Toast.makeText(this, "JOGO TERMINOU", Toast.LENGTH_LONG).show()
        }

        setContentView(binding.root)
    }
}