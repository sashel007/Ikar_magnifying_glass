package ru.ikar.ikar_magnifyingglass

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MagnifyingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var circleRadius: Float = 200f
    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var isCircleSelected = false
    private val paintDarken = Paint().apply {
        color = Color.parseColor("#88424242")
    }
    private val circlePaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val thumbRadius: Float = 20f
    private val sliderLength: Float = 250f
    private val sliderPaint = Paint().apply {
        color = Color.WHITE
    }
    private var thumbX: Float = 0f
    private var sliderY: Float = 0f
    private var isThumbSelected = false
    private var thumbX2: Float = 0f
    private var sliderY2: Float = 0f
    private var isThumb2Selected = false


    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val sliderStartX = circleX - (sliderLength / 2)
        val sliderEndX = circleX + (sliderLength / 2)
        sliderY = circleY + circleRadius + thumbRadius + 10  // 10 is the gap between circle and slider

        // Clear the canvas first
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // Draw the semi-transparent gray layer over the entire screen
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintDarken)

        // Draw a clear circle using the circle paint
        canvas?.drawCircle(circleX, circleY, circleRadius, circlePaint)

        canvas?.drawLine(sliderStartX, sliderY, sliderEndX, sliderY, sliderPaint)
        thumbX = thumbX.takeIf { it != 0f } ?: circleX  // Initialize thumbX to circleX the first time
        canvas?.drawCircle(thumbX, sliderY, thumbRadius, sliderPaint)

        // Для второго ползунка
        sliderY2 = sliderY + 2 * thumbRadius + 20 // 20 - это пространство между ползунками
        canvas?.drawLine(sliderStartX, sliderY2, sliderEndX, sliderY2, sliderPaint)
        thumbX2 = thumbX2.takeIf { it != 0f } ?: circleX  // Initialize thumbX2 to circleX the first time
        canvas?.drawCircle(thumbX2, sliderY2, thumbRadius, sliderPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Set the circle's initial position to the center of the view
        circleX = w / 2f
        circleY = h / 2f

        // Set the thumb's initial position to the left end of the slider
        thumbX = circleX - (sliderLength / 2)
        // Set the thumb2's initial position to the left end of the slider
        thumbX2 = circleX - (sliderLength / 2)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Check if the circle is selected
                val distanceFromCenter = Math.sqrt(
                    Math.pow((circleX - event.x).toDouble(), 2.0) +
                            Math.pow((circleY - event.y).toDouble(), 2.0)
                )
                isCircleSelected = distanceFromCenter <= circleRadius

                // Check if the thumb is selected
                val thumbDistanceFromTouch = Math.sqrt(
                    Math.pow((thumbX - event.x).toDouble(), 2.0) +
                            Math.pow((sliderY - event.y).toDouble(), 2.0)
                )
                if (thumbDistanceFromTouch <= thumbRadius) {
                    isCircleSelected = false
                }
                val distanceFromThumb = Math.sqrt(
                    Math.pow((thumbX - event.x).toDouble(), 2.0) +
                            Math.pow((sliderY - event.y).toDouble(), 2.0)
                )
                isThumbSelected = distanceFromThumb <= thumbRadius
                val thumb2DistanceFromTouch = Math.sqrt(
                    Math.pow((thumbX2 - event.x).toDouble(), 2.0) +
                            Math.pow((sliderY2 - event.y).toDouble(), 2.0)
                )
                if (thumb2DistanceFromTouch <= thumbRadius) {
                    isCircleSelected = false
                    isThumbSelected = false
                }
                isThumb2Selected = thumb2DistanceFromTouch <= thumbRadius
            }
            MotionEvent.ACTION_MOVE -> {
                if (isCircleSelected) {
                    circleX = event.x
                    circleY = event.y
                    invalidate() // Redraw the view
                } else if (isThumbSelected) {
                    thumbX = event.x.coerceIn(
                        circleX - (sliderLength / 2),
                        circleX + (sliderLength / 2)
                    )
                    paintDarken.alpha = calculateAlpha()  // Update the alpha based on thumb position
                    invalidate()
                } else if (isThumb2Selected) {
                    thumbX2 = event.x.coerceIn(
                        circleX - (sliderLength / 2),
                        circleX + (sliderLength / 2)
                    )
                    invalidate() // Redraw the view with the new thumb2 position
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Reset the circle selection status
                isCircleSelected = false
                isThumbSelected = false
                isThumb2Selected = false  // Reset the thumb2 selection status
            }
        }
        return true
    }

    private fun calculateAlpha(): Int {
        val progress = (thumbX - (circleX - sliderLength / 2)) / sliderLength
        return (136 + (255 - 136) * progress).toInt()
    }
}
