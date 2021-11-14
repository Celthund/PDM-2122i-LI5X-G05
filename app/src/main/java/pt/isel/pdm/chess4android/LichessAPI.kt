package pt.isel.pdm.chess4android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

@Parcelize
data class PuzzleInfo(val game: @RawValue Game, val puzzle: @RawValue Puzzle) : Parcelable

data class Game(val id: String, val pgn: String)
data class Puzzle(val id: String)

interface DailyPuzzleService {
    @GET("puzzle/daily")
    fun getPuzzle(): Call<PuzzleInfo>
}