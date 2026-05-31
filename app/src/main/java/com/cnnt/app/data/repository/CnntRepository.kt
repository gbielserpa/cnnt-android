package com.cnnt.app.data.repository

import com.cnnt.app.data.dao.*
import com.cnnt.app.data.model.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CnntRepository(private val database: CnntDatabase) {
    private val gson = Gson()

    private val notebookDao = database.notebookDao()
    private val boardDao = database.boardDao()
    private val strokeDao = database.strokeDao()
    private val spatialObjectDao = database.spatialObjectDao()
    private val flashcardDao = database.flashcardDao()

    // --- Notebooks ---

    fun getAllNotebooks(): Flow<List<NotebookEntity>> = notebookDao.getAllNotebooks()

    suspend fun saveNotebook(notebook: Notebook) {
        notebookDao.insert(NotebookEntity(
            id = notebook.id,
            name = notebook.name,
            coverColor = notebook.coverColor,
            tags = gson.toJson(notebook.tags),
            createdAt = notebook.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        for (board in notebook.boards) {
            saveBoard(board, notebook.id)
        }
    }

    suspend fun saveBoard(board: Board, notebookId: String) {
        boardDao.insert(BoardEntity(
            id = board.id,
            notebookId = notebookId,
            name = board.name,
            order = board.order,
            backgroundColor = board.backgroundColor,
            createdAt = board.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        for (layer in board.layers) {
            saveLayer(layer, board.id)
        }
    }

    suspend fun saveLayer(layer: Layer, boardId: String) {
        boardDao.insertLayer(LayerEntity(
            id = layer.id,
            boardId = boardId,
            name = layer.name,
            visible = layer.visible,
            locked = layer.locked,
            opacity = layer.opacity,
            order = layer.order
        ))
        val strokeEntities = layer.strokes.map { stroke ->
            StrokeEntity(
                id = stroke.id,
                layerId = layer.id,
                brushId = stroke.brushId,
                color = stroke.color,
                size = stroke.size,
                opacity = stroke.opacity,
                pointsData = serializePoints(ArrayList(stroke.points)),
                createdAt = stroke.createdAt
            )
        }
        strokeDao.insertAll(strokeEntities)

        for (obj in layer.objects) {
            saveSpatialObject(obj, layer.id)
        }
    }

    suspend fun saveStroke(stroke: Stroke) {
        strokeDao.insert(StrokeEntity(
            id = stroke.id,
            layerId = stroke.layerId,
            brushId = stroke.brushId,
            color = stroke.color,
            size = stroke.size,
            opacity = stroke.opacity,
            pointsData = serializePoints(stroke.points),
            createdAt = stroke.createdAt
        ))
    }

    suspend fun deleteStroke(strokeId: String) {
        strokeDao.deleteById(strokeId)
    }

    suspend fun loadBoard(boardId: String): Board? {
        val boardEntity = boardDao.getBoardById(boardId) ?: return null
        val layerEntities = boardDao.getLayersForBoard(boardId)
        val layers = layerEntities.map { layerEntity ->
            val strokes = strokeDao.getStrokesForLayer(layerEntity.id).map { strokeEntity ->
                Stroke(
                    id = strokeEntity.id,
                    points = deserializePoints(strokeEntity.pointsData),
                    brushId = strokeEntity.brushId,
                    color = strokeEntity.color,
                    size = strokeEntity.size,
                    opacity = strokeEntity.opacity,
                    layerId = strokeEntity.layerId,
                    createdAt = strokeEntity.createdAt
                )
            }
            val objects = spatialObjectDao.getObjectsForLayer(layerEntity.id).map { objEntity ->
                deserializeSpatialObject(objEntity)
            }
            Layer(
                id = layerEntity.id,
                name = layerEntity.name,
                boardId = boardId,
                visible = layerEntity.visible,
                locked = layerEntity.locked,
                opacity = layerEntity.opacity,
                order = layerEntity.order,
                strokes = strokes.toMutableList(),
                objects = objects.toMutableList()
            )
        }
        return Board(
            id = boardEntity.id,
            name = boardEntity.name,
            notebookId = boardEntity.notebookId,
            order = boardEntity.order,
            backgroundColor = boardEntity.backgroundColor,
            layers = layers.toMutableList().ifEmpty { mutableListOf(Layer()) },
            createdAt = boardEntity.createdAt,
            updatedAt = boardEntity.updatedAt
        )
    }

    // --- Spatial Objects ---

    suspend fun saveSpatialObject(obj: SpatialObject, layerId: String) {
        spatialObjectDao.insert(SpatialObjectEntity(
            id = obj.id,
            layerId = layerId,
            type = obj.type.name,
            x = obj.x,
            y = obj.y,
            width = obj.width,
            height = obj.height,
            rotation = obj.rotation,
            zIndex = obj.zIndex,
            locked = obj.locked,
            groupId = obj.groupId,
            contentJson = gson.toJson(obj.content),
            styleJson = gson.toJson(obj.style),
            createdAt = obj.createdAt,
            updatedAt = obj.updatedAt
        ))
    }

    // --- Flashcards ---

    fun getAllFlashcards(): Flow<List<FlashcardEntity>> = flashcardDao.getAllFlashcards()
    fun getDueFlashcards(): Flow<List<FlashcardEntity>> = flashcardDao.getDueFlashcards()

    suspend fun saveFlashcard(flashcard: Flashcard) {
        flashcardDao.insert(FlashcardEntity(
            id = flashcard.id,
            front = flashcard.front,
            back = flashcard.back,
            tags = gson.toJson(flashcard.tags),
            difficulty = flashcard.difficulty.name,
            linkedRegionId = flashcard.linkedRegionId,
            boardId = flashcard.boardId,
            reviewHistoryJson = gson.toJson(flashcard.reviewHistory),
            nextReview = flashcard.nextReview,
            createdAt = flashcard.createdAt
        ))
    }

    // --- Serialization helpers ---

    private fun serializePoints(points: List<StrokePoint>): ByteArray {
        // Each point: x(4) + y(4) + pressure(4) + tiltX(4) + tiltY(4) + orientation(4) + timestamp(8) = 32 bytes
        val buffer = ByteBuffer.allocate(points.size * 32).order(ByteOrder.LITTLE_ENDIAN)
        for (point in points) {
            buffer.putFloat(point.x)
            buffer.putFloat(point.y)
            buffer.putFloat(point.pressure)
            buffer.putFloat(point.tiltX)
            buffer.putFloat(point.tiltY)
            buffer.putFloat(point.orientation)
            buffer.putLong(point.timestamp)
        }
        return buffer.array()
    }

    private fun deserializePoints(data: ByteArray): MutableList<StrokePoint> {
        val points = mutableListOf<StrokePoint>()
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)
        while (buffer.remaining() >= 32) {
            points.add(StrokePoint(
                x = buffer.getFloat(),
                y = buffer.getFloat(),
                pressure = buffer.getFloat(),
                tiltX = buffer.getFloat(),
                tiltY = buffer.getFloat(),
                orientation = buffer.getFloat(),
                timestamp = buffer.getLong()
            ))
        }
        return points
    }

    private fun deserializeSpatialObject(entity: SpatialObjectEntity): SpatialObject {
        return SpatialObject(
            id = entity.id,
            type = SpatialObjectType.valueOf(entity.type),
            x = entity.x,
            y = entity.y,
            width = entity.width,
            height = entity.height,
            rotation = entity.rotation,
            zIndex = entity.zIndex,
            locked = entity.locked,
            groupId = entity.groupId,
            content = deserializeContent(entity.contentJson, entity.type),
            style = gson.fromJson(entity.styleJson, ObjectStyle::class.java) ?: ObjectStyle(),
            layerId = entity.layerId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    private fun deserializeContent(json: String, type: String): ObjectContent {
        return try {
            when (SpatialObjectType.valueOf(type)) {
                SpatialObjectType.TEXT -> gson.fromJson(json, ObjectContent.Text::class.java)
                SpatialObjectType.CHECKLIST -> gson.fromJson(json, ObjectContent.Checklist::class.java)
                SpatialObjectType.IMAGE -> gson.fromJson(json, ObjectContent.Image::class.java)
                SpatialObjectType.PDF -> gson.fromJson(json, ObjectContent.Pdf::class.java)
                SpatialObjectType.LINK -> gson.fromJson(json, ObjectContent.Link::class.java)
                SpatialObjectType.FILE -> gson.fromJson(json, ObjectContent.File::class.java)
                SpatialObjectType.FLASHCARD -> gson.fromJson(json, ObjectContent.FlashcardContent::class.java)
                SpatialObjectType.GROUP -> gson.fromJson(json, ObjectContent.Group::class.java)
                else -> ObjectContent.Empty
            }
        } catch (e: Exception) {
            ObjectContent.Empty
        }
    }
}
