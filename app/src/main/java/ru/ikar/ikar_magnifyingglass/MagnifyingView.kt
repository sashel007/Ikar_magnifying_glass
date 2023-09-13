package ru.ikar.ikar_magnifyingglass

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
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
    init {
        setBackgroundColor(Color.TRANSPARENT)
        circleX = width / 2f
        circleY = height / 2f
    }
    private var scale = 1f
    private var bitmap: Bitmap? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Рисуем затемненный фон
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintDarken)

        // Если у нас есть изображение для увеличения, отображаем его
        if (bitmap != null) {
            // Вычисляем исходный прямоугольник для увеличения на основе текущего масштаба
            val scaledRadius = circleRadius / scale
            val srcRect = Rect(
                (circleX - scaledRadius).toInt(),
                (circleY - scaledRadius).toInt(),
                (circleX + scaledRadius).toInt(),
                (circleY + scaledRadius).toInt()
            )

            // Назначенный прямоугольник (куда рисуем увеличенное изображение)
            val dstRect = RectF(
                circleX - circleRadius,
                circleY - circleRadius,
                circleX + circleRadius,
                circleY + circleRadius
            )

            canvas?.drawBitmap(bitmap!!, srcRect, dstRect, null)
        }

        // Рисуем прозрачный круг поверх всего
        canvas?.drawCircle(circleX, circleY, circleRadius, circlePaint)
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
            }
            MotionEvent.ACTION_MOVE -> {
                if (isCircleSelected) {
                    circleX = event.x
                    circleY = event.y
                    invalidate() // Redraw the view
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Reset the circle selection status
                isCircleSelected = false
            }
        }
        return true
    }

    fun setScale(newScale: Float) {
        Log.d("MagnifyingView", "Setting scale to: $newScale")
        this.scale = newScale
        invalidate() // Перерисовываем представление
    }

    // метод для установки битовой карты и масштаба
    fun setMagnifyingContent(bitmap: Bitmap, scale: Float) {
        this.bitmap = bitmap
        this.scale = scale
        invalidate()
    }
}