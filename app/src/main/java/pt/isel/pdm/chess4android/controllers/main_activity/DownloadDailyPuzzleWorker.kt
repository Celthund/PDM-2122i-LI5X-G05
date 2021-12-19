package pt.isel.pdm.chess4android.controllers.main_activity

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import pt.isel.pdm.chess4android.controllers.application.APP_TAG
import pt.isel.pdm.chess4android.controllers.application.PuzzleApplication
import pt.isel.pdm.chess4android.controllers.utils.PuzzleRepository

class DownloadDailyPuzzleWorker (appContext: Context, workerParams: WorkerParameters)
    : ListenableWorker(appContext, workerParams) {

    override fun startWork(): ListenableFuture<Result> {
        val app : PuzzleApplication = applicationContext as PuzzleApplication
        val repo = PuzzleRepository(
            PuzzleApplication.dailyPuzzleService, app.historyDB.getHistoryPuzzleDao())

        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Starting DownloadDailyPuzzleWorker")

        return CallbackToFutureAdapter.getFuture { completer ->
            repo.fetchPuzzleOfDay(mustSaveToDB = true) { result ->
                result
                    .onSuccess {
                        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: DownloadDailyPuzzleWorker succeeded")
                        completer.set(Result.success())
                    }
                    .onFailure {
                        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: DownloadDailyPuzzleWorker failed")
                        completer.setException(it)
                    }
            }
        }
    }
}