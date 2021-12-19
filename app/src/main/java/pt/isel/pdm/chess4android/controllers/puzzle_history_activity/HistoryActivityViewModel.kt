package pt.isel.pdm.chess4android.controllers.puzzle_history_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.chess4android.controllers.application.PuzzleApplication
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.controllers.utils.callbackAfterAsync

class HistoryActivityViewModel(application: Application) : AndroidViewModel(application) {

    var history: LiveData<List<PuzzleInfo>>? = null
        private set

    private val puzzleDao: HistoryPuzzleDao by lazy {
        getApplication<PuzzleApplication>().historyDB.getHistoryPuzzleDao()
    }

    fun loadPuzzleHistory(): LiveData<List<PuzzleInfo>> {
        val publish = MutableLiveData<List<PuzzleInfo>>()
        history = publish
        callbackAfterAsync(
            asyncAction = {
                puzzleDao.getAllPuzzles().map {
                    it.puzzleInfo
                }
            },
            callback = { result ->
                result.onSuccess { publish.value = it }
                result.onFailure { publish.value = emptyList() }
            }
        )

        return publish
    }
}