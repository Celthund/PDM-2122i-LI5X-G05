package pt.isel.pdm.chess4android.puzzle_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.models.DailyGame
import pt.isel.pdm.chess4android.models.DailyPuzzle
import pt.isel.pdm.chess4android.models.PuzzleInfo

class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val puzzleId: TextView = itemView.findViewById(R.id.puzzleId)
    private val gameId: TextView = itemView.findViewById(R.id.gameId)
    private lateinit var game: DailyGame
    private lateinit var puzzle: DailyPuzzle

    fun bindTo(chessPuzzle: PuzzleInfo, onItemClick: () -> Unit) {
        puzzleId.text = chessPuzzle.puzzle.id
        gameId.text = chessPuzzle.game.id
        game = chessPuzzle.game
        puzzle = chessPuzzle.puzzle

        itemView.setOnClickListener {
            onItemClick()
        }
    }

}

class HistoryAdapter(private val dataSource: List<PuzzleInfo>, private val onItemClick: (PuzzleInfo) -> Unit) : RecyclerView.Adapter<HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history_puzzle_view, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val curr = dataSource[position]
        holder.bindTo(curr) {
            onItemClick(curr)
        }
    }

    override fun getItemCount() = dataSource.size
}