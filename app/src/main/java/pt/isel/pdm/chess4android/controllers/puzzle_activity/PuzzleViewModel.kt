package pt.isel.pdm.chess4android.controllers.puzzle_activity

import android.app.Application
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.controllers.application.PuzzleApplication
import pt.isel.pdm.chess4android.controllers.main_activity.MainActivityViewModel
import pt.isel.pdm.chess4android.controllers.utils.PuzzleRepository
import pt.isel.pdm.chess4android.models.PuzzleInfoParser
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.models.games.Player
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val PUZZLE_ACTIVITY_VIEW_STATE = "PuzzleActivity.ViewState"

class PuzzleViewModel(
    application: Application,
    private val state: SavedStateHandle
) : MainActivityViewModel(application, state) {

    private val puzzleRepository by lazy {
        PuzzleRepository(
            PuzzleApplication.dailyPuzzleService,
            getApplication<PuzzleApplication>().historyDB.getHistoryPuzzleDao()
        )
    }

    val lichessPuzzle: LiveData<PuzzleInfo> = state.getLiveData(PUZZLE_ACTIVITY_VIEW_STATE)

    fun getLichessPuzzle() {
        puzzleRepository.fetchPuzzleOfDay(true) { result ->
            result
                .onSuccess { puzzleInfo ->
                    state.set(PUZZLE_ACTIVITY_VIEW_STATE, puzzleInfo)
                }
                .onFailure {
                    state.set(PUZZLE_ACTIVITY_VIEW_STATE, null)
                }
        }
    }

    fun playLichessPuzzle(dailyGame: PuzzleInfo) {
        _whitePlayer = if (dailyGame.puzzle.initialPly % 2 == 0) {
            Player.Top
        } else {
            Player.Bottom
        }
        this._boardModel.value = PuzzleInfoParser(dailyGame).parsePuzzlePNG()
    }
}