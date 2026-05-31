package com.cnnt.app.ink

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import com.cnnt.app.data.model.BrushPreset
import com.cnnt.app.data.model.Stroke
import com.cnnt.app.data.model.StrokePoint
import com.cnnt.app.data.model.TipShape
import com.cnnt.app.data.model.StrokeBehavior
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class InkEngine {

    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val fillPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    fun renderStroke(canvas: Canvas, stroke: Stroke, brush: BrushPreset) {
        val pointsCopy = ArrayList(stroke.points)
        if (pointsCopy.size < 2) {
            if (pointsCopy.size == 1) {
                renderDot(canvas, pointsCopy[0], stroke, brush)
            }
            return
        }

        // Dispatch by brush ID for visually distinct rendering
        when (brush.id) {
            "brush_gel_pen" -> renderGelPen(canvas, stroke, brush, pointsCopy)
            "brush_soft_pencil" -> renderSoftPencil(canvas, stroke, brush, pointsCopy)
            "brush_marker" -> renderMarker(canvas, stroke, brush, pointsCopy)
            "brush_highlighter" -> renderHighlighter(canvas, stroke, brush, pointsCopy)
            "brush_chisel" -> renderChiselStroke(canvas, stroke, brush, pointsCopy)
            "brush_cnnt_special" -> renderCnntSpecial(canvas, stroke, brush, pointsCopy)
            "brush_wet_ink" -> renderWetInk(canvas, stroke, brush, pointsCopy)
            "brush_technical_pen" -> renderTechnicalPen(canvas, stroke, brush, pointsCopy)
            "brush_grain" -> renderGrainBrush(canvas, stroke, brush, pointsCopy)
            "brush_neon" -> renderNeonLaser(canvas, stroke, brush, pointsCopy)
            "brush_spray" -> renderSprayAirbrush(canvas, stroke, brush, pointsCopy)
            "brush_experimental" -> renderExperimental(canvas, stroke, brush, pointsCopy)
            else -> renderGelPen(canvas, stroke, brush, pointsCopy)
        }
    }

    private fun renderDot(canvas: Canvas, point: StrokePoint, stroke: Stroke, brush: BrushPreset) {
        fillPaint.color = stroke.color
        fillPaint.alpha = (stroke.opacity * 255 * brush.opacity).toInt()
        val radius = stroke.size * point.pressure * brush.pressureSensitivity * 0.5f
        canvas.drawCircle(point.x, point.y, max(radius, 0.5f), fillPaint)
    }

    // --- Gel Pen: smooth, pressure-responsive, tapered ends ---
    private fun renderGelPen(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.4f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.strokeJoin = Paint.Join.ROUND
        strokePaint.maskFilter = null

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]
            val t = i.toFloat() / smoothed.size
            val taper = calculateTaper(t, StrokeBehavior.SMOOTH_TAPER, StrokeBehavior.SMOOTH_TAPER, smoothed.size)

            val pressure = curr.pressure.coerceIn(0.1f, 1f)
            val width = stroke.size * pressure * 0.8f * taper

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * 255).toInt()
            strokePaint.strokeWidth = max(width, 0.5f)

            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, strokePaint)
        }
    }

    // --- Soft Pencil: textured, grainy, variable opacity by pressure ---
    private fun renderSoftPencil(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.2f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.maskFilter = null

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]

            val pressure = curr.pressure.coerceIn(0.05f, 1f)
            val width = stroke.size * pressure * 1.2f
            // Pencil: opacity varies with pressure
            val alpha = (stroke.opacity * pressure * 0.7f * 255).toInt().coerceIn(20, 220)

            strokePaint.color = stroke.color
            strokePaint.alpha = alpha
            strokePaint.strokeWidth = max(width, 0.3f)
            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, strokePaint)

            // Grain texture: scatter dots along the stroke
            val grainCount = (width * 0.8f).toInt().coerceIn(1, 6)
            fillPaint.color = stroke.color
            for (g in 0 until grainCount) {
                val gx = curr.x + (Math.random().toFloat() - 0.5f) * width * 1.5f
                val gy = curr.y + (Math.random().toFloat() - 0.5f) * width * 1.5f
                fillPaint.alpha = (alpha * 0.4f * Math.random().toFloat()).toInt().coerceIn(5, 100)
                canvas.drawCircle(gx, gy, (0.3f + Math.random().toFloat() * 0.5f), fillPaint)
            }
        }
    }

    // --- Marker: flat tip, constant width, bold, square cap ---
    private fun renderMarker(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.6f)

        strokePaint.strokeCap = Paint.Cap.SQUARE
        strokePaint.strokeJoin = Paint.Join.BEVEL
        strokePaint.maskFilter = null
        strokePaint.color = stroke.color
        strokePaint.alpha = (stroke.opacity * 0.9f * 255).toInt()
        // Marker: mostly constant width, slight direction variation
        val baseWidth = stroke.size * 2.0f

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]

            val direction = atan2((curr.y - prev.y).toDouble(), (curr.x - prev.x).toDouble()).toFloat()
            val dirFactor = 0.6f + abs(sin(direction)) * 0.4f
            strokePaint.strokeWidth = max(baseWidth * dirFactor, 1f)

            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, strokePaint)
        }
        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.strokeJoin = Paint.Join.ROUND
    }

    // --- Highlighter: wide, transparent, flat ---
    private fun renderHighlighter(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.8f)

        strokePaint.strokeCap = Paint.Cap.SQUARE
        strokePaint.strokeJoin = Paint.Join.BEVEL
        strokePaint.maskFilter = null
        strokePaint.color = stroke.color
        strokePaint.alpha = (stroke.opacity * 0.30f * 255).toInt()
        strokePaint.strokeWidth = stroke.size * 3f

        val path = Path()
        path.moveTo(smoothed[0].x, smoothed[0].y)
        for (i in 1 until smoothed.size) {
            path.lineTo(smoothed[i].x, smoothed[i].y)
        }
        canvas.drawPath(path, strokePaint)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.strokeJoin = Paint.Join.ROUND
    }

    // --- Chisel/Calligraphy: direction-dependent width, elegant ---
    private fun renderChiselStroke(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.3f)
        if (smoothed.size < 2) return

        val path = Path()
        val topPoints = mutableListOf<Pair<Float, Float>>()
        val bottomPoints = mutableListOf<Pair<Float, Float>>()

        val chiselAngle = Math.PI.toFloat() / 4f

        for (i in smoothed.indices) {
            val point = smoothed[i]
            val t = i.toFloat() / smoothed.size
            val taper = calculateTaper(t, StrokeBehavior.TAPER, StrokeBehavior.TAPER, smoothed.size)

            val direction = if (i > 0) {
                atan2(
                    (point.y - smoothed[i - 1].y).toDouble(),
                    (point.x - smoothed[i - 1].x).toDouble()
                ).toFloat()
            } else if (smoothed.size > 1) {
                atan2(
                    (smoothed[1].y - point.y).toDouble(),
                    (smoothed[1].x - point.x).toDouble()
                ).toFloat()
            } else 0f

            val dirFactor = abs(sin(direction + chiselAngle)) * 0.8f + 0.2f
            val width = stroke.size * point.pressure * dirFactor * taper * 1.5f

            val perpAngle = direction + Math.PI.toFloat() / 2f + chiselAngle * 0.8f
            val halfW = width * 0.5f

            topPoints.add(Pair(
                point.x + cos(perpAngle) * halfW,
                point.y + sin(perpAngle) * halfW
            ))
            bottomPoints.add(Pair(
                point.x - cos(perpAngle) * halfW,
                point.y - sin(perpAngle) * halfW
            ))
        }

        if (topPoints.isNotEmpty()) {
            path.moveTo(topPoints[0].first, topPoints[0].second)
            for (i in 1 until topPoints.size) {
                val prev = topPoints[i - 1]
                val curr = topPoints[i]
                val cx = (prev.first + curr.first) / 2f
                val cy = (prev.second + curr.second) / 2f
                path.quadTo(prev.first, prev.second, cx, cy)
            }
            path.lineTo(topPoints.last().first, topPoints.last().second)

            for (i in bottomPoints.indices.reversed()) {
                val point = bottomPoints[i]
                if (i == bottomPoints.size - 1) {
                    path.lineTo(point.first, point.second)
                } else {
                    val next = bottomPoints[i + 1]
                    val cx = (point.first + next.first) / 2f
                    val cy = (point.second + next.second) / 2f
                    path.quadTo(next.first, next.second, cx, cy)
                }
            }
            path.close()

            fillPaint.color = stroke.color
            fillPaint.alpha = (stroke.opacity * 255).toInt()
            fillPaint.maskFilter = null
            canvas.drawPath(path, fillPaint)
        }
    }

    // --- CNNT Special: velocity + direction + smooth taper, dynamic ---
    private fun renderCnntSpecial(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.5f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.maskFilter = null

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]

            val t = i.toFloat() / smoothed.size
            val taper = calculateTaper(t, StrokeBehavior.SMOOTH_TAPER, StrokeBehavior.SMOOTH_TAPER, smoothed.size)

            val pressure = curr.pressure.coerceIn(0.1f, 1f)
            val velocity = calculateVelocity(prev, curr)
            val velFactor = 1f - (velocity * 0.5f * 0.001f).coerceIn(0f, 0.5f)
            val direction = atan2((curr.y - prev.y).toDouble(), (curr.x - prev.x).toDouble()).toFloat()
            val dirFactor = 0.7f + abs(sin(direction)) * 0.3f

            val width = stroke.size * pressure * velFactor * dirFactor * taper * 1.3f

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * 255).toInt()
            strokePaint.strokeWidth = max(width, 0.5f)

            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, strokePaint)
        }
    }

    // --- Wet Ink: blob at start, flowing, slightly irregular ---
    private fun renderWetInk(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.6f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.maskFilter = null

        // Draw blob at start
        if (smoothed.isNotEmpty()) {
            fillPaint.color = stroke.color
            fillPaint.alpha = (stroke.opacity * 200).toInt()
            val blobSize = stroke.size * 1.8f * smoothed[0].pressure
            canvas.drawCircle(smoothed[0].x, smoothed[0].y, blobSize, fillPaint)
        }

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]

            val t = i.toFloat() / smoothed.size
            val pressure = curr.pressure.coerceIn(0.1f, 1f)
            val velocity = calculateVelocity(prev, curr)
            val velFactor = 1f - (velocity * 0.7f * 0.001f).coerceIn(0f, 0.6f)
            val fadeFactor = if (t > 0.7f) ((1f - t) / 0.3f) else 1f

            val width = stroke.size * pressure * 1.3f * velFactor * fadeFactor
            val jitterX = (Math.random().toFloat() - 0.5f) * 0.1f * stroke.size
            val jitterY = (Math.random().toFloat() - 0.5f) * 0.1f * stroke.size

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * 255 * fadeFactor).toInt().coerceIn(30, 255)
            strokePaint.strokeWidth = max(width, 0.5f)

            canvas.drawLine(prev.x + jitterX, prev.y + jitterY, curr.x + jitterX, curr.y + jitterY, strokePaint)
        }
    }

    // --- Technical Pen: uniform width, no pressure variation ---
    private fun renderTechnicalPen(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.7f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.strokeJoin = Paint.Join.ROUND
        strokePaint.maskFilter = null
        strokePaint.color = stroke.color
        strokePaint.alpha = (stroke.opacity * 255).toInt()
        strokePaint.strokeWidth = stroke.size // Fixed width, no pressure

        val path = Path()
        path.moveTo(smoothed[0].x, smoothed[0].y)
        for (i in 1 until smoothed.size) {
            val mid = smoothed[i]
            path.lineTo(mid.x, mid.y)
        }
        canvas.drawPath(path, strokePaint)
    }

    // --- Grain Brush: heavy texture, scattered particles ---
    private fun renderGrainBrush(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.3f)

        // Draw the base line thin
        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.maskFilter = null
        strokePaint.color = stroke.color
        strokePaint.alpha = (stroke.opacity * 150).toInt()
        strokePaint.strokeWidth = stroke.size * 0.5f

        for (i in 1 until smoothed.size) {
            canvas.drawLine(smoothed[i-1].x, smoothed[i-1].y, smoothed[i].x, smoothed[i].y, strokePaint)
        }

        // Draw heavy grain/texture
        fillPaint.color = stroke.color
        for (i in smoothed.indices) {
            val point = smoothed[i]
            val pressure = point.pressure.coerceIn(0.1f, 1f)
            val radius = stroke.size * pressure * 1.5f
            val particleCount = (radius * 3).toInt().coerceIn(3, 20)

            for (p in 0 until particleCount) {
                val gx = point.x + (Math.random().toFloat() - 0.5f) * radius * 3f
                val gy = point.y + (Math.random().toFloat() - 0.5f) * radius * 3f
                fillPaint.alpha = (stroke.opacity * 80 * Math.random().toFloat()).toInt().coerceIn(10, 120)
                val dotSize = 0.3f + Math.random().toFloat() * 1.2f
                canvas.drawCircle(gx, gy, dotSize, fillPaint)
            }
        }
    }

    // --- Neon/Laser: glow effect, bright core ---
    private fun renderNeonLaser(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.5f)

        // Outer glow (wide, faded)
        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.color = stroke.color
        strokePaint.alpha = (stroke.opacity * 40).toInt()
        strokePaint.strokeWidth = stroke.size * 4f
        strokePaint.maskFilter = BlurMaskFilter(stroke.size * 2f, BlurMaskFilter.Blur.NORMAL)

        val glowPath = Path()
        glowPath.moveTo(smoothed[0].x, smoothed[0].y)
        for (i in 1 until smoothed.size) {
            glowPath.lineTo(smoothed[i].x, smoothed[i].y)
        }
        canvas.drawPath(glowPath, strokePaint)

        // Middle glow
        strokePaint.alpha = (stroke.opacity * 100).toInt()
        strokePaint.strokeWidth = stroke.size * 2f
        strokePaint.maskFilter = BlurMaskFilter(stroke.size, BlurMaskFilter.Blur.NORMAL)
        canvas.drawPath(glowPath, strokePaint)

        // Bright core (white-ish)
        strokePaint.maskFilter = null
        strokePaint.color = brightenColor(stroke.color)
        strokePaint.alpha = (stroke.opacity * 255).toInt()
        strokePaint.strokeWidth = stroke.size * 0.6f
        canvas.drawPath(glowPath, strokePaint)
    }

    // --- Spray/Airbrush: scattered dots, soft cloud ---
    private fun renderSprayAirbrush(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        fillPaint.color = stroke.color
        fillPaint.maskFilter = null

        for (point in points) {
            val pressure = point.pressure.coerceIn(0.1f, 1f)
            val radius = stroke.size * pressure * 2f
            val count = (radius * 4).toInt().coerceIn(5, 40)

            for (s in 0 until count) {
                val angle = Math.random() * Math.PI * 2
                val dist = Math.random().toFloat() * radius
                val sx = point.x + (cos(angle) * dist).toFloat()
                val sy = point.y + (sin(angle) * dist).toFloat()

                val distFactor = 1f - (dist / radius)
                fillPaint.alpha = (stroke.opacity * 0.3f * distFactor * 255).toInt().coerceIn(5, 80)
                val dotSize = 0.4f + Math.random().toFloat() * 0.8f
                canvas.drawCircle(sx, sy, dotSize, fillPaint)
            }
        }
    }

    // --- Experimental: chaotic, all effects combined ---
    private fun renderExperimental(canvas: Canvas, stroke: Stroke, brush: BrushPreset, points: List<StrokePoint>) {
        val smoothed = smoothPoints(points, 0.2f)

        strokePaint.strokeCap = Paint.Cap.ROUND
        strokePaint.maskFilter = null

        for (i in 1 until smoothed.size) {
            val prev = smoothed[i - 1]
            val curr = smoothed[i]

            val t = i.toFloat() / smoothed.size
            val pressure = curr.pressure.coerceIn(0.1f, 1f)
            val velocity = calculateVelocity(prev, curr)
            val velFactor = 1f - (velocity * 1.0f * 0.001f).coerceIn(0f, 0.7f)
            val direction = atan2((curr.y - prev.y).toDouble(), (curr.x - prev.x).toDouble()).toFloat()
            val dirFactor = abs(sin(direction * 2f)) * 0.5f + 0.5f

            val width = stroke.size * pressure * 1.5f * velFactor * dirFactor

            val jitterX = (Math.random().toFloat() - 0.5f) * 0.3f * stroke.size
            val jitterY = (Math.random().toFloat() - 0.5f) * 0.3f * stroke.size

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * (0.5f + pressure * 0.5f) * 255).toInt()
            strokePaint.strokeWidth = max(width, 0.5f)

            canvas.drawLine(prev.x + jitterX, prev.y + jitterY, curr.x + jitterX, curr.y + jitterY, strokePaint)

            // Scatter particles
            if (Math.random() < 0.4) {
                fillPaint.color = stroke.color
                fillPaint.alpha = (stroke.opacity * 100).toInt()
                val gx = curr.x + (Math.random().toFloat() - 0.5f) * width * 4
                val gy = curr.y + (Math.random().toFloat() - 0.5f) * width * 4
                canvas.drawCircle(gx, gy, Math.random().toFloat() * 1.5f + 0.3f, fillPaint)
            }
        }
    }

    private fun brightenColor(color: Int): Int {
        val r = min(255, Color.red(color) + 100)
        val g = min(255, Color.green(color) + 100)
        val b = min(255, Color.blue(color) + 100)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    private fun calculateTaper(
        t: Float, startBehavior: StrokeBehavior, endBehavior: StrokeBehavior, totalPoints: Int
    ): Float {
        var factor = 1f
        val taperLength = min(0.15f, 5f / totalPoints)

        when (startBehavior) {
            StrokeBehavior.TAPER -> {
                if (t < taperLength) factor *= (t / taperLength)
            }
            StrokeBehavior.SMOOTH_TAPER -> {
                if (t < taperLength) {
                    val st = t / taperLength
                    factor *= st * st * (3f - 2f * st)
                }
            }
            StrokeBehavior.FADE -> {
                if (t < taperLength) factor *= sqrt(t / taperLength)
            }
            StrokeBehavior.BLOB -> {
                if (t < taperLength * 0.5f) factor *= 1.3f
            }
            else -> {}
        }

        when (endBehavior) {
            StrokeBehavior.TAPER -> {
                if (t > 1f - taperLength) factor *= ((1f - t) / taperLength)
            }
            StrokeBehavior.SMOOTH_TAPER -> {
                if (t > 1f - taperLength) {
                    val st = (1f - t) / taperLength
                    factor *= st * st * (3f - 2f * st)
                }
            }
            StrokeBehavior.FADE -> {
                if (t > 1f - taperLength) factor *= sqrt((1f - t) / taperLength)
            }
            StrokeBehavior.SCATTER -> {
                if (t > 1f - taperLength) {
                    factor *= (1f - t) / taperLength
                    factor *= (0.5f + Math.random().toFloat() * 0.5f)
                }
            }
            else -> {}
        }

        return factor.coerceIn(0.05f, 2f)
    }

    private fun calculateVelocity(prev: StrokePoint, curr: StrokePoint): Float {
        val dx = curr.x - prev.x
        val dy = curr.y - prev.y
        val dt = max(1L, curr.timestamp - prev.timestamp)
        return hypot(dx, dy) / dt
    }

    private fun smoothPoints(points: List<StrokePoint>, smoothing: Float): List<StrokePoint> {
        if (points.size < 3) return points
        val result = mutableListOf<StrokePoint>()
        result.add(points[0])

        val weight = smoothing.coerceIn(0f, 0.9f)
        for (i in 1 until points.size - 1) {
            val prev = points[i - 1]
            val curr = points[i]
            val next = points[i + 1]

            val smoothX = curr.x * (1f - weight) + (prev.x + next.x) * weight * 0.5f
            val smoothY = curr.y * (1f - weight) + (prev.y + next.y) * weight * 0.5f

            result.add(curr.copy(x = smoothX, y = smoothY))
        }
        result.add(points.last())
        return result
    }

    fun isPointNearStroke(stroke: Stroke, x: Float, y: Float, threshold: Float): Boolean {
        for (point in stroke.points) {
            val dist = hypot((point.x - x).toDouble(), (point.y - y).toDouble())
            if (dist < threshold + stroke.size) return true
        }
        return false
    }

    fun getStrokeBounds(stroke: Stroke): RectF {
        val bounds = stroke.getBounds()
        val padding = stroke.size
        return RectF(
            bounds[0] - padding, bounds[1] - padding,
            bounds[2] + padding, bounds[3] + padding
        )
    }
}
