package com.cnnt.app.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cnnt.app.canvas.CanvasMode
import com.cnnt.app.canvas.InfiniteCanvasView
import com.cnnt.app.data.model.*
import com.cnnt.app.databinding.ActivityMainBinding
import com.cnnt.app.ui.toolbar.ToolbarManager
import com.cnnt.app.ui.sidebar.SidebarManager
import com.cnnt.app.ui.dialogs.BrushPickerDialog
import com.cnnt.app.ui.dialogs.ColorPickerDialog
import com.cnnt.app.ui.dialogs.ExportDialog
import com.cnnt.app.ui.dialogs.FlashcardDialog
import com.cnnt.app.ui.dialogs.OcrDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), InfiniteCanvasView.CanvasListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var toolbarManager: ToolbarManager
    private lateinit var sidebarManager: SidebarManager

    private var focusModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application))
            .get(MainViewModel::class.java)

        setupImmersiveMode()
        setupCanvas()
        setupToolbar()
        setupSidebar()
        setupObservers()

        // Load or create default notebook
        viewModel.loadOrCreateDefaultNotebook()
    }

    private fun setupImmersiveMode() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("CNNT", "Immersive mode error: ${e.message}")
        }
    }

    private fun setupCanvas() {
        binding.canvasView.listener = this
        binding.canvasView.setPalmRejection(true)
        binding.canvasView.setFingerDrawing(false)
    }

    private fun setupToolbar() {
        toolbarManager = ToolbarManager(binding, this)
        toolbarManager.onBrushSelected = { brush ->
            viewModel.setCurrentBrush(brush)
            binding.canvasView.setBrush(brush)
            binding.canvasView.setMode(CanvasMode.DRAW)
        }
        toolbarManager.onColorSelected = { color ->
            viewModel.setCurrentColor(color)
            binding.canvasView.setDrawColor(color)
        }
        toolbarManager.onSizeChanged = { size ->
            binding.canvasView.setDrawSize(size)
        }
        toolbarManager.onModeSelected = { mode ->
            binding.canvasView.setMode(mode)
        }
        toolbarManager.onLassoModeSelected = { lassoMode ->
            binding.canvasView.setLassoMode(lassoMode)
        }
        toolbarManager.onUndoClicked = {
            binding.canvasView.undo()
        }
        toolbarManager.onRedoClicked = {
            binding.canvasView.redo()
        }
        toolbarManager.onFocusModeToggled = {
            toggleFocusMode()
        }
        toolbarManager.onExportClicked = {
            showExportDialog()
        }
        toolbarManager.onOcrClicked = {
            showOcrDialog()
        }
        toolbarManager.onFlashcardClicked = {
            showFlashcardDialog()
        }
        toolbarManager.onBrushSettingsChanged = { brush ->
            viewModel.setCurrentBrush(brush)
            binding.canvasView.setBrush(brush)
        }
    }

    private fun setupSidebar() {
        sidebarManager = SidebarManager(binding, this)
        sidebarManager.onPageSelected = { boardIndex ->
            viewModel.switchBoard(boardIndex)
        }
        sidebarManager.onNewPageClicked = {
            viewModel.addNewBoard()
        }
        sidebarManager.onInsertBlockClicked = { blockType ->
            insertBlock(blockType)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.currentBoard.collect { board ->
                board?.let {
                    binding.canvasView.setBoard(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.currentBrush.collect { brush ->
                toolbarManager.updateBrushIndicator(brush)
            }
        }

        lifecycleScope.launch {
            viewModel.currentColor.collect { color ->
                toolbarManager.updateColorIndicator(color)
            }
        }
    }

    private fun toggleFocusMode() {
        focusModeActive = !focusModeActive
        if (focusModeActive) {
            binding.toolbarScroll.visibility = View.GONE
            binding.sidebarToggle.visibility = View.GONE
        } else {
            binding.toolbarScroll.visibility = View.VISIBLE
            binding.sidebarToggle.visibility = View.VISIBLE
        }
    }

    private fun insertBlock(type: SpatialObjectType) {
        val obj = SpatialObject(
            type = type,
            x = 100f,
            y = 100f,
            width = 200f,
            height = 150f,
            content = when (type) {
                SpatialObjectType.TEXT -> ObjectContent.Text("", 14f)
                SpatialObjectType.CHECKLIST -> ObjectContent.Checklist(emptyList())
                else -> ObjectContent.Empty
            }
        )
        viewModel.addSpatialObject(obj)
        binding.canvasView.setMode(CanvasMode.SELECT)
    }

    private fun showExportDialog() {
        ExportDialog(this, viewModel).show()
    }

    private fun showOcrDialog() {
        OcrDialog(this, viewModel).show()
    }

    private fun showFlashcardDialog() {
        FlashcardDialog(this, viewModel).show()
    }

    // CanvasListener implementations

    override fun onStrokeCompleted(stroke: Stroke) {
        viewModel.saveStroke(stroke)
    }

    override fun onStrokeErased(strokeId: String) {
        viewModel.deleteStroke(strokeId)
    }

    override fun onObjectSelected(obj: SpatialObject?) {
        // Update UI to show object properties
    }

    override fun onObjectMoved(obj: SpatialObject, newX: Float, newY: Float) {
        viewModel.updateSpatialObject(obj)
    }

    override fun onCanvasTransformChanged(scale: Float, translateX: Float, translateY: Float) {
        // Update minimap if enabled
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (focusModeActive) {
            toggleFocusMode()
        } else {
            super.onBackPressed()
        }
    }
}
