package pt.isel.pdm.chess4android.application

import android.app.Application
import androidx.room.Room
import pt.isel.pdm.chess4android.models.DailyPuzzleService
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.models.URL
import pt.isel.pdm.chess4android.puzzle_history.HistoryDataBase
import pt.isel.pdm.chess4android.puzzle_history.PuzzleEntity
import pt.isel.pdm.chess4android.utils.callbackAfterAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PuzzleApplication: Application() {
    companion object {
        val dailyPuzzleService: DailyPuzzleService by lazy {
            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DailyPuzzleService::class.java)
        }


    }

    /** In Memory DB for now */
    val historyDB: HistoryDataBase by lazy {
        Room
            .inMemoryDatabaseBuilder(this, HistoryDataBase::class.java)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val call = dailyPuzzleService.getPuzzle()
        call.enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                val resp = response.body()
                if (resp != null && response.isSuccessful) {
                    callbackAfterAsync({}) {
                        historyDB.getHistoryPuzzleDao().insert(
                            PuzzleEntity(resp.game.id,
                                resp.puzzle.id,
                                resp
                            )
                        )

                    }

                }
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                // TODO
            }
        })

    }
}