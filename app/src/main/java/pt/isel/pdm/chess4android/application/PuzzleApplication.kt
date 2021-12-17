package pt.isel.pdm.chess4android.application

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
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

fun createPuzzleFromJSON(json: String) : PuzzleInfo {
    // GSON doesn't fill timestamp with default value.
    val puzzleInfo = Gson().fromJson(json, PuzzleInfo::class.java)
    return PuzzleInfo(puzzleInfo.game, puzzleInfo.puzzle)
}

class PuzzleApplication : Application() {
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
        val array = arrayOf(
            "{\"game\":{\"id\":\"RRLJbyi2\",\"perf\":{\"icon\":\"\uE017\",\"name\":\"Rapid\"},\"rated\":true,\"players\":[{\"userId\":\"nusret49\",\"name\":\"Nusret49 (2014)\",\"color\":\"white\"},{\"userId\":\"theuntoldgospel\",\"name\":\"TheUntoldGospel (2018)\",\"color\":\"black\"}],\"pgn\":\"e4 c5 c4 Nc6 Nc3 d6 Nf3 Nf6 d3 Bg4 Be2 g6 O-O Bg7 a3 Bxf3 Bxf3 Nd4 Be3 Nd7 Bxd4 Bxd4 Rb1 O-O Nb5 Bg7 Qd2 Ne5 Be2 a6 Nc3 f5 exf5 Rxf5 f4 Nc6 Bf3 Nd4 Bxb7 Ra7 Be4 Rf8 b4 cxb4 axb4 e5 fxe5 Bxe5 Nd5 Rxf1+ Rxf1 Qh4 g3 Qd8 Kh1 Nb3 Qd1 Nd4 Qa4 Ne2 b5 Bxg3 hxg3 Nxg3+ Kg2 Nxf1 Kxf1 Rf7+ Ke2 Qh4 Kd2 Rf1 bxa6 Qe1+ Kc2 Rf2+ Kb3\",\"clock\":\"15+0\"},\"puzzle\":{\"id\":\"62Jvl\",\"rating\":1960,\"plays\":84412,\"initialPly\":76,\"solution\":[\"e1b1\",\"b3c3\",\"b1b2\"],\"themes\":[\"endgame\",\"short\",\"mateIn2\"]}}",
            "{\"game\":{\"id\":\"0AzN6JCP\",\"perf\":{\"icon\":\"\",\"name\":\"Rapid\"},\"rated\":true,\"players\":[{\"userId\":\"mferrer\",\"name\":\"mferrer (1867)\",\"color\":\"white\"},{\"userId\":\"theitch\",\"name\":\"TheItch (1909)\",\"color\":\"black\"}],\"pgn\":\"d4 Nf6 e3 g6 f4 Bg7 Bd3 O-O f5 d5 fxg6 fxg6 Nf3 Ne4 Nbd2 Bf5 O-O c5 c3 Nd7 Bxe4 dxe4 Nh4 cxd4 Nxf5 gxf5 exd4 Nb6 Qh5 Qd5 Nb3 Rf7 Bh6 Raf8 Rf4 e5 dxe5 Qxe5 Nd4 Bxh6 Qxh6 Rf6 Qh4 e3 Re1 Nd5 Rf3 f4 Rh3 h6 Nf3 Qe4 Qg4+ Rg6 Qh5 Qc2 Qxd5+ Rf7 Ne5\",\"clock\":\"20+4\"},\"puzzle\":{\"id\":\"mFVkT\",\"rating\":1686,\"plays\":10973,\"initialPly\":58,\"solution\":[\"c2f2\",\"g1h1\",\"f2e1\"],\"themes\":[\"mateIn2\",\"middlegame\",\"short\",\"fork\"]}} "
        )
        var puzzle: PuzzleInfo
        array.forEach { json ->
            puzzle = createPuzzleFromJSON(json)
            callbackAfterAsync({}) {
                historyDB.getHistoryPuzzleDao().insert(
                    PuzzleEntity(
                        puzzle.game.id,
                        puzzle
                    )
                )
            }
        }

        val call = dailyPuzzleService.getPuzzle()
        call.enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                val resp = response.body()
                if (resp != null && response.isSuccessful) {
                    callbackAfterAsync({}) {
                        val puzzleInfo = PuzzleInfo(resp.game, resp.puzzle)
                        historyDB.getHistoryPuzzleDao().insert(
                            PuzzleEntity(
                                puzzleInfo.game.id,
                                puzzleInfo
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