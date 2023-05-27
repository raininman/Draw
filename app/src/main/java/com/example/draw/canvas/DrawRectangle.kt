package com.example.draw.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.draw.MainActivity
import com.example.draw.MainActivity.Companion.colorList
import com.example.draw.MainActivity.Companion.currentBrush
import com.example.draw.MainActivity.Companion.currentWidth
import com.example.draw.cons.Rectangle

class DrawRectangle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dataRectangle = mutableListOf<Rectangle>()

    private val paintBrush = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = currentBrush
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = currentWidth
        alpha = 0xff
    }


    fun updateColor(newColor: Int) {
        paintBrush.color = newColor
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                colorList.add(currentBrush)
                dataRectangle.add(Rectangle(event.x, event.y, event.x, event.y, currentBrush, currentWidth))
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val current = dataRectangle[dataRectangle.size - 1]
                current.stopX = event.x
                current.stopY = event.y
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                val current = dataRectangle[dataRectangle.size - 1]
                current.stopX = event.x
                current.stopY = event.y
                invalidate()
                return true
            }

            else -> {
                return false
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        for (r in dataRectangle) {
            paintBrush.strokeWidth = r.width
            paintBrush.color = r.color
            canvas.drawRect(r.startX, r.startY, r.stopX, r.stopY, paintBrush)
        }
    }

    fun undo() {
        if (dataRectangle.size != 0) {
            dataRectangle.removeAt(dataRectangle.size - 1)
            invalidate()
        }
    }

}