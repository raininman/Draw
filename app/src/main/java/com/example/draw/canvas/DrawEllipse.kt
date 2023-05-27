package com.example.draw.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.draw.MainActivity
import com.example.draw.MainActivity.Companion.currentBrush
import com.example.draw.MainActivity.Companion.currentWidth
import com.example.draw.cons.Ellipse

class DrawEllipse @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ellipse = mutableListOf<Ellipse>()

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
                MainActivity.colorList.add(currentBrush)
                ellipse.add(Ellipse(event.x, event.y, event.x, event.y, currentBrush, currentWidth))
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val current = ellipse[ellipse.size - 1]
                current.stopX = event.x
                current.stopY = event.y
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val current = ellipse[ellipse.size - 1]
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
        for (e in ellipse) {
            paintBrush.color = e.color
            canvas.drawOval(e.startX, e.startY, e.stopX, e.stopY, paintBrush)
        }
    }

    fun undo() {
        if (ellipse.size != 0) {
            ellipse.removeAt(ellipse.size - 1)
            invalidate()
        }
    }

}