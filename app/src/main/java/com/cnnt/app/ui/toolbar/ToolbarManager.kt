package com.cnnt.app.ui.toolbar

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.cnnt.app.canvas.CanvasMode
import com.cnnt.app.data.model.BrushPreset
import com.cnnt.app.databinding.ActivityMainBinding
import com.cnnt.app.ui.dialogs.BrushPickerDialog
import com.cnnt.app.ui.dialogs.ColorPickerDialog

class ToolbarManager(
    private val binding: ActivityMainBinding,
    private val activity: AppCompatActivity
) {
    var onBrushSelected: ((BrushPreset) -> Unit)? = null
    var onColorSelected: ((Int) -> Unit)? = null
    var onSizeChanged: ((Float) -> Unit)? = null
    var onModeSelected: ((CanvasMode) -> Unit)? = null
    var onUndoClicked: (() -> Unit)? = null
    var onRedoClicked: (() -> Unit)? = null
    var onFocusModeToggled: (() -> Unit)? = null
    var onExportClicked: (() -> Unit)? = null
    var onOcrClicked: (() -> Unit)? = null
    var onFlashcardClicked: (() -> Unit)? = null

    init {
        setupButtons()
        setupSizeSlider()
    }

    private fun setupButtons() {
        binding.btnBrush.setOnClickListener {
            BrushPickerDialog(activity) { brush ->
                onBrushSelected?.invoke(brush)
            }.show()
        }

        binding.btnColor.setOnClickListener {
            ColorPickerDialog(activity) { color ->
                onColorSelected?.invoke(color)
            }.show()
        }

        binding.btnEraser.setOnClickListener {
            onModeSelected?.invoke(CanvasMode.ERASE)
            highlightButton(binding.btnEraser)
        }

        binding.btnSelect.setOnClickListener {
            onModeSelected?.invoke(CanvasMode.SELECT)
            highlightButton(binding.btnSelect)
        }

        binding.btnLasso.setOnClickListener {
            onModeSelected?.invoke(CanvasMode.LASSO)
            highlightButton(binding.btnLasso)
        }

        binding.btnUndo.setOnClickListener {
            onUndoClicked?.invoke()
        }

        binding.btnRedo.setOnClickListener {
            onRedoClicked?.invoke()
        }

        binding.btnFocus.setOnClickListener {
            onFocusModeToggled?.invoke()
        }

        binding.btnExport.setOnClickListener {
            onExportClicked?.invoke()
        }

        binding.btnOcr.setOnClickListener {
            onOcrClicked?.invoke()
        }

        binding.btnFlashcard.setOnClickListener {
            onFlashcardClicked?.invoke()
        }
    }

    private fun setupSizeSlider() {
        binding.sizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val size = 1f + (progress / 100f) * 49f // 1-50
                onSizeChanged?.invoke(size)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun updateBrushIndicator(brush: BrushPreset) {
        binding.btnBrush.text = brush.name
    }

    fun updateColorIndicator(color: Int) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setStroke(2, Color.WHITE)
        }
        binding.btnColor.background = drawable
    }

    private fun highlightButton(activeBtn: View) {
        listOf<View>(binding.btnEraser, binding.btnSelect, binding.btnLasso).forEach { btn ->
            btn.alpha = if (btn == activeBtn) 1.0f else 0.6f
        }
    }
}
