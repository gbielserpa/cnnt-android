package com.cnnt.app.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnnt.app.R
import com.cnnt.app.data.model.BrushPreset
import com.cnnt.app.data.model.Stroke
import com.cnnt.app.data.model.StrokePoint
import com.cnnt.app.ink.InkEngine

class BrushPickerDialog(
    context: Context,
    private val onBrushSelected: (BrushPreset) -> Unit
) : Dialog(context, R.style.Theme_CNNT_Dialog) {

    private val brushes = BrushPreset.defaultBrushes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_brush_picker)

        // Make dialog wider
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

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

    private val inkEngine = InkEngine()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.brushName)
        val categoryText: TextView = view.findViewById(R.id.brushCategory)
        val previewView: View = view.findViewById(R.id.brushPreview)
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

        // Render stroke preview
        holder.previewView.post {
            val w = holder.previewView.width
            val h = holder.previewView.height
            if (w > 0 && h > 0) {
                val preview = generatePreview(brush, w, h)
                holder.previewView.background = BitmapDrawable(
                    holder.previewView.context.resources, preview
                )
            }
        }
    }

    override fun getItemCount() = brushes.size

    private fun generatePreview(brush: BrushPreset, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(0xFF1E1E1E.toInt())

        // Create a sample stroke - a smooth wave
        val stroke = Stroke(
            brushId = brush.id,
            color = Color.WHITE,
            size = brush.baseSize,
            opacity = brush.opacity
        )

        val margin = 10f
        val midY = height / 2f
        val steps = 30
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val x = margin + t * (width - margin * 2)
            val y = midY + sin(t * Math.PI * 2).toFloat() * (height * 0.2f)
            val pressure = 0.3f + 0.5f * sin(t * Math.PI).toFloat()
            stroke.addPoint(StrokePoint(
                x = x, y = y,
                pressure = pressure,
                timestamp = (i * 16).toLong()
            ))
        }

        inkEngine.renderStroke(canvas, stroke, brush)
        return bitmap
    }

    private fun sin(value: Double): Float = kotlin.math.sin(value).toFloat()
}
