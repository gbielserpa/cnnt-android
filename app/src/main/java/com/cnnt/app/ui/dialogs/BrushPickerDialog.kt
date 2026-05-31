package com.cnnt.app.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnnt.app.R
import com.cnnt.app.data.model.BrushPreset

class BrushPickerDialog(
    context: Context,
    private val onBrushSelected: (BrushPreset) -> Unit
) : Dialog(context, R.style.Theme_CNNT_Dialog) {

    private val brushes = BrushPreset.defaultBrushes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_brush_picker)

        val recyclerView = findViewById<RecyclerView>(R.id.brushGrid)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = BrushAdapter(brushes) { brush ->
            onBrushSelected(brush)
            dismiss()
        }
    }
}

class BrushAdapter(
    private val brushes: List<BrushPreset>,
    private val onClick: (BrushPreset) -> Unit
) : RecyclerView.Adapter<BrushAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.brushName)
        val categoryText: TextView = view.findViewById(R.id.brushCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_brush, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brush = brushes[position]
        holder.nameText.text = brush.name
        holder.categoryText.text = brush.category.name.lowercase()
        holder.itemView.setOnClickListener { onClick(brush) }
    }

    override fun getItemCount() = brushes.size
}
