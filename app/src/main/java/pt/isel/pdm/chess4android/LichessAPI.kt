package pt.isel.pdm.chess4android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

@Parcelize
data class PuzzleInfo(val game: @RawValue DailyGame, val puzzle: @RawValue DailyPuzzle) : Parcelable

data class DailyGame(val id: String, val pgn: String)
data class DailyPuzzle(val id: String)

interface DailyPuzzleService {
    @GET("puzzle/daily")
    fun getPuzzle(): Call<PuzzleInfo>
}