package pt.isel.pdm.chess4android.activities

import android.app.Application
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.DailyPuzzleService
import pt.isel.pdm.chess4android.models.PuzzleInfoParser
import pt.isel.pdm.chess4android.PuzzleInfo
import pt.isel.pdm.chess4android.URL
import pt.isel.pdm.chess4android.models.games.Player
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val MAIN_ACTIVITY_VIEW_STATE = "MainActivity.ViewState"

val dailyPuzzleService: DailyPuzzleService = Retrofit.Builder()
    .baseUrl(URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(DailyPuzzleService::class.java)

class MainActivityViewModel(
    application: Application,
    private val state: SavedStateHandle
) : AndroidViewModel(application) {

    private val _boardModel: MutableLiveData<Chess> = MutableLiveData()
    val boardModel: LiveData<Chess> get() = _boardModel

    private val _isInPromote: MutableLiveData<PromoteCandidate> = MutableLiveData()
    val isInPromote: LiveData<PromoteCandidate> get() = _isInPromote

    private var _whitePlayer = Player.Bottom
    val whitePlayer get() = _whitePlayer

    val lichessPuzzle: LiveData<PuzzleInfo> = state.getLiveData(MAIN_ACTIVITY_VIEW_STATE)

    fun GetLichessPuzzle() {
        val call = dailyPuzzleService.getPuzzle()
        call.enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                val resp = response.body()
                if (resp != null && response.isSuccessful)
                    state.set(MAIN_ACTIVITY_VIEW_STATE, resp)
                state.remove<PuzzleInfo>(MAIN_ACTIVITY_VIEW_STATE)
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                state.set(MAIN_ACTIVITY_VIEW_STATE, null)
                state.remove<PuzzleInfo>(MAIN_ACTIVITY_VIEW_STATE)
            }
        })
    }

    fun setBoardModel(boardModel: Chess) {
        this._boardModel.value = boardModel
    }

    fun savePromoteStatus(isInPromote: PromoteCandidate) {
        this._isInPromote.value = isInPromote
    }

    fun PlayLichessPuzzle(dailyGame: PuzzleInfo) {
        _whitePlayer = if (dailyGame.puzzle.initialPly % 2 == 0) {
            Player.Top
        } else {
            Player.Bottom
        }
        this._boardModel.value = PuzzleInfoParser(dailyGame).parsePuzzlePNG()
    }
}