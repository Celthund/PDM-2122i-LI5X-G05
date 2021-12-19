package pt.isel.pdm.chess4android.controllers.main_activity

import android.app.Application
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.controllers.application.PuzzleApplication
import pt.isel.pdm.chess4android.controllers.utils.PuzzleRepository
import pt.isel.pdm.chess4android.models.PuzzleInfoParser
import pt.isel.pdm.chess4android.models.PuzzleInfo
import pt.isel.pdm.chess4android.models.games.Player
import pt.isel.pdm.chess4android.models.games.PromoteCandidate
import pt.isel.pdm.chess4android.models.games.chess.Chess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val MAIN_ACTIVITY_VIEW_STATE = "MainActivity.ViewState"

open class MainActivityViewModel(
    application: Application,
    private val state: SavedStateHandle
) : AndroidViewModel(application) {

    protected var _whitePlayer = Player.Bottom
    val whitePlayer get() = _whitePlayer

    protected val _boardModel: MutableLiveData<Chess> = MutableLiveData()
    val boardModel: LiveData<Chess> get() = _boardModel

    private val _isInPromote: MutableLiveData<PromoteCandidate> = MutableLiveData()
    val isInPromote: LiveData<PromoteCandidate> get() = _isInPromote

    private val _isGameOver: MutableLiveData<Boolean> = MutableLiveData()
    val isGameOver: LiveData<Boolean> get() = _isGameOver

    fun setBoardModel(boardModel: Chess) {
        this._boardModel.value = boardModel
    }

    fun savePromoteStatus(isInPromote: PromoteCandidate) {
        this._isInPromote.value = isInPromote
    }

    fun endGame() {
        _isGameOver.value = true
    }


}