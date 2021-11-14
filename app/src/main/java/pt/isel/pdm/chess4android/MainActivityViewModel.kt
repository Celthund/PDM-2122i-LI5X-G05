package pt.isel.pdm.chess4android

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
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
}