package com.cnnt.app.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnnt.app.R
import com.cnnt.app.data.model.ColorPalette

class ColorPickerDialog(
    context: Context,
    private val onColorSelected: (Int) -> Unit
) : Dialog(context, R.style.Theme_CNNT_Dialog) {

    private val palettes = listOf(
        ColorPalette.defaultPalette(),
        ColorPalette.studyPalette(),
        ColorPalette.darkPalette()
    )
    private var currentPaletteIndex = 0
    private var selectedColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_color_picker)

        setupPalette()
        setupOpacitySlider()
    }

    private fun setupPalette() {
        val grid = findViewById<RecyclerView>(R.id.colorGrid)
        grid.layoutManager = GridLayoutManager(context, 6)
        updatePaletteGrid(grid)

        findViewById<View>(R.id.btnNextPalette)?.setOnClickListener {
            currentPaletteIndex = (currentPaletteIndex + 1) % palettes.size
            updatePaletteGrid(grid)
        }
    }

    private fun updatePaletteGrid(grid: RecyclerView) {
        val palette = palettes[currentPaletteIndex]
        grid.adapter = ColorAdapter(palette.colors) { color ->
            selectedColor = color
            onColorSelected(color)
            dismiss()
        }
    }

    private fun setupOpacitySlider() {
        val slider = findViewById<SeekBar>(R.id.opacitySlider)
        slider?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val alpha = (progress / 100f * 255).toInt()
                selectedColor = Color.argb(alpha, Color.red(selectedColor), Color.green(selectedColor), Color.blue(selectedColor))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onColorSelected(selectedColor)
            }
        })
    }
}

class ColorAdapter(
    private val colors: List<Int>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorView: View = view.findViewById(R.id.colorSwatch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colors[position]
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setStroke(2, 0x44FFFFFF)
        }
        holder.colorView.background = drawable
        holder.itemView.setOnClickListener { onClick(color) }
    }

    override fun getItemCount() = colors.size
}
