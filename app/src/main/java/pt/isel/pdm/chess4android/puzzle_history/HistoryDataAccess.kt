package pt.isel.pdm.chess4android.puzzle_history
import androidx.room.*
import pt.isel.pdm.chess4android.models.PuzzleInfo
import java.time.Instant
import java.time.temporal.ChronoUnit


@Entity(tableName = "history_puzzle")
data class PuzzleEntity(
    @PrimaryKey val puzzleId: String,
    val puzzleInfo: PuzzleInfo,
) {
    fun isTodayQuote(): Boolean =
        puzzleInfo.timestamp.toInstant().compareTo(Instant.now().truncatedTo(ChronoUnit.DAYS)) == 0
}

@Dao
interface HistoryPuzzleDao {
    @Insert
    fun insert(puzzle: PuzzleEntity)

    @Delete
    fun delete(puzzle: PuzzleEntity)

    @Query("SELECT * FROM history_puzzle ORDER BY puzzleId DESC LIMIT 100")
    fun getAllPuzzles(): List<PuzzleEntity>

    @Query("SELECT * FROM history_puzzle ORDER BY puzzleId DESC LIMIT :count")
    fun getLastPuzzles(count: Int): List<PuzzleEntity>

    @Query("SELECT * FROM history_puzzle WHERE puzzleId = :puzzleId")
    fun getPuzzle(puzzleId: String): List<PuzzleEntity>
}

@Database(entities = [PuzzleEntity::class], version = 1)
@TypeConverters(DBConverter::class)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun getHistoryPuzzleDao(): HistoryPuzzleDao
}