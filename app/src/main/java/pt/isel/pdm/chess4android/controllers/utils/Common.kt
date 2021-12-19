package pt.isel.pdm.chess4android.controllers.utils

import com.google.gson.Gson
import pt.isel.pdm.chess4android.models.PuzzleInfo

class Common {
    companion object {
        fun createPuzzleFromJSON(json: String) : PuzzleInfo {
            // GSON doesn't fill timestamp with default value.
            val puzzleInfo = Gson().fromJson(json, PuzzleInfo::class.java)
            return PuzzleInfo(puzzleInfo.game, puzzleInfo.puzzle)
        }
    }
}