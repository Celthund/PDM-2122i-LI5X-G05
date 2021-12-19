package pt.isel.pdm.chess4android.controllers.utils

import android.util.Log
import pt.isel.pdm.chess4android.controllers.application.APP_TAG
import pt.isel.pdm.chess4android.controllers.puzzle_history_activity.HistoryPuzzleDao
import pt.isel.pdm.chess4android.controllers.puzzle_history_activity.PuzzleEntity
import pt.isel.pdm.chess4android.models.DailyPuzzleService
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.models.ServiceUnavailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PuzzleRepository(
    private val dailyPuzzleService: DailyPuzzleService,
    private val historyDB: HistoryPuzzleDao
) {
    private fun asyncMaybeGetTodayPuzzleFromDB(callback: (Result<PuzzleEntity?>) -> Unit) {
        callbackAfterAsync(callback) {
            val puzzle = historyDB.getLastPuzzles(1).firstOrNull()
            if (puzzle?.isTodayPuzzle() == true) {
                puzzle
            } else
                null
        }
    }

    /**
     * Asynchronously gets the daily puzzle from the remote API.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncGetTodayPuzzleFromAPI(callback: (Result<PuzzleInfo>) -> Unit) {
        val call = dailyPuzzleService.getPuzzle()
        call.enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                val resp = response.body()
                val result =
                    if (resp != null && response.isSuccessful)
                        Result.success(PuzzleInfo(resp.game, resp.puzzle))
                    else
                        Result.failure(ServiceUnavailable())
                callback(result)
            }

            override fun onFailure(call: Call<PuzzleInfo>, error: Throwable) {
                callback(Result.failure(ServiceUnavailable(cause = error)))
            }
        })
    }

    /**
     * Asynchronously saves the daily puzzle to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncSaveToDB(dto: PuzzleInfo, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Getting history from local DB")
            historyDB.insert(
                PuzzleEntity(
                    dto.puzzle.id,
                    dto,
                    dto.timestamp
                )
            )
        }
    }

    /**
     * Asynchronously gets the puzzle, either from the local DB, if available, or from
     * the remote API.
     *
     * @param mustSaveToDB  indicates if the operation is only considered successful if all its
     * steps, including saving to the local DB, succeed. If false, the operation is considered
     * successful regardless of the success of saving the quote in the local DB (the last step).
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD
     *
     * Using a boolean to distinguish between both options is a questionable design decision.
     */
    fun fetchPuzzleOfDay(mustSaveToDB: Boolean = false, callback: (Result<PuzzleInfo>) -> Unit) {
        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Fetching Puzzle")
        asyncMaybeGetTodayPuzzleFromDB { entity ->
            val puzzleEntity = entity.getOrNull()
            if (puzzleEntity != null) {
                callback(Result.success(puzzleEntity.puzzleInfo))
            } else {
                asyncGetTodayPuzzleFromAPI { apiResult ->
                    apiResult.onSuccess { puzzleInfo ->
                        asyncSaveToDB(puzzleInfo) { saveToDBResult ->
                            saveToDBResult
                                .onSuccess { callback(Result.success(puzzleInfo)) }
                                .onFailure {
                                    callback(
                                        if (mustSaveToDB)
                                            Result.failure(it) else Result.success(puzzleInfo)
                                    )
                                }
                        }
                    }
                    callback(apiResult)
                }
            }
        }
    }
}