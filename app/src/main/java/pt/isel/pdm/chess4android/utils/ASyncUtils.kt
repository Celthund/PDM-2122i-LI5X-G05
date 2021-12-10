package pt.isel.pdm.chess4android.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception
import java.util.concurrent.Executors

private val ioThreadPool = Executors.newSingleThreadExecutor()

private fun <T> executeAndCollectResult(asyncAction: () -> T) : Result<T> {
    return try {
        Result.success(asyncAction())
    } catch (err: Exception) {
        Result.failure(err)
    }
}

fun <T> callbackAfterAsync(callback: (Result<T>) -> Unit, asyncAction:() -> T) {
    val mainHandler = Handler(Looper.getMainLooper())
    ioThreadPool.submit {
        val result = executeAndCollectResult(asyncAction)
        mainHandler.post {
            callback(result)
        }
    }
}