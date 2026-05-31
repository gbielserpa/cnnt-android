package com.cnnt.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cnnt.app.CnntApplication
import com.cnnt.app.data.model.*
import com.cnnt.app.data.repository.CnntRepository
import com.cnnt.app.flashcard.FlashcardManager
import com.cnnt.app.ocr.OcrEngine
import com.cnnt.app.export.ExportManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CnntRepository = (application as CnntApplication).repository
    val flashcardManager = FlashcardManager()
    val ocrEngine = OcrEngine()
    val exportManager = ExportManager(application)

    private val _currentNotebook = MutableStateFlow<Notebook?>(null)
    val currentNotebook: StateFlow<Notebook?> = _currentNotebook

    private val _currentBoard = MutableStateFlow<Board?>(null)
    val currentBoard: StateFlow<Board?> = _currentBoard

    private val _currentBrush = MutableStateFlow(BrushPreset.gelPen())
    val currentBrush: StateFlow<BrushPreset> = _currentBrush

    private val _currentColor = MutableStateFlow(0xFFFFFFFF.toInt())
    val currentColor: StateFlow<Int> = _currentColor

    private val _brushes = MutableStateFlow(BrushPreset.defaultBrushes())
    val brushes: StateFlow<List<BrushPreset>> = _brushes

    private val _palettes = MutableStateFlow(listOf(
        ColorPalette.defaultPalette(),
        ColorPalette.studyPalette(),
        ColorPalette.darkPalette()
    ))
    val palettes: StateFlow<List<ColorPalette>> = _palettes

    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards

    private var autoSaveJob: kotlinx.coroutines.Job? = null

    fun loadOrCreateDefaultNotebook() {
        viewModelScope.launch {
            // Try to load existing notebook or create new one
            val notebook = Notebook(name = "Meu Caderno")
            _currentNotebook.value = notebook
            _currentBoard.value = notebook.activeBoard
            startAutoSave()
        }
    }

    fun setCurrentBrush(brush: BrushPreset) {
        _currentBrush.value = brush
    }

    fun setCurrentColor(color: Int) {
        _currentColor.value = color
    }

    fun switchBoard(index: Int) {
        val notebook = _currentNotebook.value ?: return
        if (index in notebook.boards.indices) {
            _currentBoard.value = notebook.boards[index]
        }
    }

    fun addNewBoard() {
        val notebook = _currentNotebook.value ?: return
        val newBoard = Board(
            name = "Page ${notebook.boards.size + 1}",
            notebookId = notebook.id,
            order = notebook.boards.size
        )
        notebook.boards.add(newBoard)
        _currentBoard.value = newBoard
    }

    fun saveStroke(stroke: Stroke) {
        viewModelScope.launch {
            repository.saveStroke(stroke)
        }
    }

    fun deleteStroke(strokeId: String) {
        viewModelScope.launch {
            repository.deleteStroke(strokeId)
        }
    }

    fun addSpatialObject(obj: SpatialObject) {
        val board = _currentBoard.value ?: return
        val layer = board.activeLayer
        val updatedObj = obj.copy(layerId = layer.id)
        layer.objects.add(updatedObj)
        viewModelScope.launch {
            repository.saveSpatialObject(updatedObj, layer.id)
        }
        // Trigger canvas refresh
        _currentBoard.value = board
    }

    fun updateSpatialObject(obj: SpatialObject) {
        viewModelScope.launch {
            repository.saveSpatialObject(obj, obj.layerId)
        }
    }

    fun addFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.saveFlashcard(flashcard)
            _flashcards.value = _flashcards.value + flashcard
        }
    }

    private fun startAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(5000)
                saveCurrentState()
            }
        }
    }

    private suspend fun saveCurrentState() {
        val notebook = _currentNotebook.value ?: return
        repository.saveNotebook(notebook)
    }

    override fun onCleared() {
        super.onCleared()
        autoSaveJob?.cancel()
        ocrEngine.close()
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
