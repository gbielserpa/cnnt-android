package com.cnnt.app.ink

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
        if (stroke.points.size < 2) {
            if (stroke.points.size == 1) {
                renderDot(canvas, stroke.points[0], stroke, brush)
            }
            return
        }

        when (brush.tipShape) {
            TipShape.ROUND -> renderRoundStroke(canvas, stroke, brush)
            TipShape.FLAT -> renderFlatStroke(canvas, stroke, brush)
            TipShape.CHISEL -> renderChiselStroke(canvas, stroke, brush)
            TipShape.CUSTOM -> renderRoundStroke(canvas, stroke, brush)
        }
    }

    private fun renderDot(canvas: Canvas, point: StrokePoint, stroke: Stroke, brush: BrushPreset) {
        fillPaint.color = stroke.color
        fillPaint.alpha = (stroke.opacity * 255 * brush.opacity).toInt()
        val radius = stroke.size * point.pressure * brush.pressureSensitivity * 0.5f
        canvas.drawCircle(point.x, point.y, max(radius, 0.5f), fillPaint)
    }

    private fun renderRoundStroke(canvas: Canvas, stroke: Stroke, brush: BrushPreset) {
        val points = stroke.points
        val smoothedPoints = if (brush.smoothing > 0) {
            smoothPoints(points, brush.smoothing)
        } else points

        for (i in 1 until smoothedPoints.size) {
            val prev = smoothedPoints[i - 1]
            val curr = smoothedPoints[i]

            val t = i.toFloat() / smoothedPoints.size
            val taperFactor = calculateTaper(t, brush.startBehavior, brush.endBehavior, smoothedPoints.size)

            val pressure = curr.pressure * brush.pressureSensitivity
            val velocity = calculateVelocity(prev, curr)
            val velocityFactor = 1f - (velocity * brush.velocitySensitivity * 0.001f).coerceIn(0f, 0.7f)

            val width = stroke.size * pressure * velocityFactor * taperFactor
            val jitterOffset = if (brush.jitter > 0) {
                (Math.random().toFloat() - 0.5f) * brush.jitter * stroke.size
            } else 0f

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * brush.opacity * 255).toInt()
            strokePaint.strokeWidth = max(width, 0.5f)

            canvas.drawLine(
                prev.x + jitterOffset, prev.y + jitterOffset,
                curr.x + jitterOffset, curr.y + jitterOffset,
                strokePaint
            )

            // Grain effect
            if (brush.grain > 0 && Math.random() < brush.grain * 0.3) {
                fillPaint.color = stroke.color
                fillPaint.alpha = (stroke.opacity * brush.opacity * 100 * brush.grain).toInt()
                val gx = curr.x + (Math.random().toFloat() - 0.5f) * width * 2
                val gy = curr.y + (Math.random().toFloat() - 0.5f) * width * 2
                canvas.drawCircle(gx, gy, 0.5f, fillPaint)
            }
        }
    }

    private fun renderFlatStroke(canvas: Canvas, stroke: Stroke, brush: BrushPreset) {
        val points = stroke.points
        val smoothedPoints = if (brush.smoothing > 0) {
            smoothPoints(points, brush.smoothing)
        } else points

        for (i in 1 until smoothedPoints.size) {
            val prev = smoothedPoints[i - 1]
            val curr = smoothedPoints[i]

            val t = i.toFloat() / smoothedPoints.size
            val taperFactor = calculateTaper(t, brush.startBehavior, brush.endBehavior, smoothedPoints.size)

            val direction = atan2((curr.y - prev.y).toDouble(), (curr.x - prev.x).toDouble()).toFloat()
            val dirFactor = abs(sin(direction)) * brush.directionSensitivity + (1f - brush.directionSensitivity)

            val width = stroke.size * curr.pressure * dirFactor * taperFactor

            strokePaint.color = stroke.color
            strokePaint.alpha = (stroke.opacity * brush.opacity * 255).toInt()
            strokePaint.strokeWidth = max(width, 0.5f)
            strokePaint.strokeCap = Paint.Cap.SQUARE

            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, strokePaint)
        }
        strokePaint.strokeCap = Paint.Cap.ROUND
    }

    private fun renderChiselStroke(canvas: Canvas, stroke: Stroke, brush: BrushPreset) {
        val points = stroke.points
        val smoothedPoints = if (brush.smoothing > 0) {
            smoothPoints(points, brush.smoothing)
        } else points

        if (smoothedPoints.size < 2) return

        val path = Path()
        val topPoints = mutableListOf<Pair<Float, Float>>()
        val bottomPoints = mutableListOf<Pair<Float, Float>>()

        val chiselAngle = Math.PI.toFloat() / 4f // 45 degrees

        for (i in smoothedPoints.indices) {
            val point = smoothedPoints[i]
            val t = i.toFloat() / smoothedPoints.size
            val taperFactor = calculateTaper(t, brush.startBehavior, brush.endBehavior, smoothedPoints.size)

            val direction = if (i > 0) {
                atan2(
                    (point.y - smoothedPoints[i - 1].y).toDouble(),
                    (point.x - smoothedPoints[i - 1].x).toDouble()
                ).toFloat()
            } else if (smoothedPoints.size > 1) {
                atan2(
                    (smoothedPoints[1].y - point.y).toDouble(),
                    (smoothedPoints[1].x - point.x).toDouble()
                ).toFloat()
            } else 0f

            val dirFactor = abs(sin(direction + chiselAngle)) * brush.directionSensitivity +
                    (1f - brush.directionSensitivity) * 0.3f
            val width = stroke.size * point.pressure * brush.pressureSensitivity * dirFactor * taperFactor

            val perpAngle = direction + Math.PI.toFloat() / 2f + chiselAngle * brush.tiltSensitivity
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

        // Build path from outline
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
            fillPaint.alpha = (stroke.opacity * brush.opacity * 255).toInt()
            canvas.drawPath(path, fillPaint)
        }
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
                    factor *= st * st * (3f - 2f * st) // smoothstep
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
