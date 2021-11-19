package pt.isel.pdm.chess4android

import android.app.Application
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.games.PromoteCandidate
import pt.isel.pdm.chess4android.games.chess.Chess
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
    var isPromoting: Boolean = false

    private val _boardModel: MutableLiveData<Chess> = MutableLiveData()
    val boardModel: LiveData<Chess> = _boardModel
    val lichessPuzzle: LiveData<PuzzleInfo> = state.getLiveData(MAIN_ACTIVITY_VIEW_STATE)

    private val _promote: MutableLiveData<PromoteCandidate?> = MutableLiveData()
    val promote: LiveData<PromoteCandidate?> = _promote

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

    fun PlayLichessPuzzle(puzzle: PuzzleInfo) {
        this._boardModel.value = LoadPGN(puzzle.game.pgn).chess
    }

    fun positionToPromote(promoteCandidate: PromoteCandidate? = null) {
        if(isPromoting && promoteCandidate == null) {
           isPromoting = false
           _promote.value = null
        } else if(!isPromoting && promoteCandidate != null) {
            isPromoting = true
            _promote.value = promoteCandidate
        }
    }
}