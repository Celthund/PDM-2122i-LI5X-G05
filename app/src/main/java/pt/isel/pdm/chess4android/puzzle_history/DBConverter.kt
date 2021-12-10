package pt.isel.pdm.chess4android.puzzle_history

import androidx.room.TypeConverter
import com.google.gson.Gson
import pt.isel.pdm.chess4android.models.PuzzleInfo

class DBConverter {
    @TypeConverter
    fun toPuzzleInfoJson(puzzleInfo: PuzzleInfo): String = Gson().toJson(puzzleInfo)

    @TypeConverter
    fun toPuzzleInfo(json: String): PuzzleInfo = Gson().fromJson(json, PuzzleInfo::class.java)
}