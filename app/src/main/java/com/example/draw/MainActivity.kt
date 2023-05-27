package com.example.draw

import android.content.ContentValues
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.draw.databinding.ActivityMainBinding
import java.io.OutputStream


class MainActivity() : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var isPencilIconClicked = false
    private var isRectangleIconClicked = false
    private var isCircleIconClicked = false
    private var isPaletteIconClicked = false
    private var isSliderIconClicked = false


    companion object {
        var path = Path()
        var paintBrush = Paint()
        var colorList = ArrayList<Int>()
        var currentBrush = Color.BLACK
        var currentWidth = 4f
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            btnSlider.setOnClickListener{
                isSliderIconClicked = !isSliderIconClicked

                if (isSliderIconClicked) {
                    strokeWidth.visibility = View.VISIBLE

                    btnSlider.setImageResource(R.drawable.ac_height)
                    btnSlider.setBackgroundResource(R.drawable.background_cards)

                    unselectPencil()
                    unselectRectangle()
                    unselectEllipse()
                } else {
                    unselectSlider()
                    strokeWidth.visibility = View.INVISIBLE
                }
            }

            rangeSlider.addOnChangeListener { _, value, _ ->
                paintBrush.strokeWidth = value
                currentWidth(paintBrush.strokeWidth)
            }


            btnPencil.setOnClickListener {
                isPencilIconClicked = !isPencilIconClicked

                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                    btnPencil.setBackgroundResource(R.drawable.background_cards)


                    unselectEllipse()
                    unselectPalette()
                    unselectRectangle()
                    unselectSlider()

                    drawPencil.visibility = View.VISIBLE
                    drawEllipse.visibility = View.GONE
                    drawRectangle.visibility = View.GONE

                } else {
                    unselectPencil()
                }
            }

            btnRectangle.setOnClickListener {

                isRectangleIconClicked = !isRectangleIconClicked
                if (isRectangleIconClicked) {

                    btnRectangle.setImageResource(R.drawable.ic_selected_rectangle)
                    btnRectangle.setBackgroundResource(R.drawable.background_cards)

                    unselectPencil()
                    unselectEllipse()
                    unselectPalette()
                    unselectSlider()

                    drawRectangle.visibility = View.VISIBLE
                    drawPencil.visibility = View.GONE
                    drawEllipse.visibility = View.GONE

                } else {
                    unselectRectangle()
                }
            }

            btnEllipse.setOnClickListener {
                isCircleIconClicked = !isCircleIconClicked

                if (isCircleIconClicked) {
                    btnEllipse.setImageResource(R.drawable.ic_selected_circle)
                    btnEllipse.setBackgroundResource(R.drawable.background_cards)

                    unselectPencil()
                    unselectRectangle()
                    unselectPalette()
                    unselectSlider()

                    drawEllipse.visibility = View.VISIBLE
                    drawPencil.visibility = View.GONE
                    drawRectangle.visibility = View.GONE

                } else {
                    unselectEllipse()
                }
            }

            btnPallete.setOnClickListener {
                isPaletteIconClicked = !isPaletteIconClicked

                if (isPaletteIconClicked) {
                    colorPalate.visibility = View.VISIBLE

                    btnPallete.setImageResource(R.drawable.ic_selected_palette)
                    btnPallete.setBackgroundResource(R.drawable.background_cards)

                    unselectPencil()
                    unselectRectangle()
                    unselectEllipse()
                } else {
                    unselectPalette()
                    colorPalate.visibility = View.INVISIBLE
                }
            }

            btnBlue.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.google_blue)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                unselectPalette()
            }

            btnRed.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.google_red)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                unselectPalette()
            }

            btnYellow.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.google_yellow)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                unselectPalette()
            }

            btnGreen.setOnClickListener {
                paintBrush.color = resources.getColor(R.color.google_green)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                unselectPalette()
            }

            btnBlack.setOnClickListener {
                paintBrush.color = Color.BLACK
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                unselectPalette()
            }

            btnArrow.setOnClickListener(View.OnClickListener {
                val v = findViewById<View>(android.R.id.content).getRootView()
                val bmp: Bitmap = takeScreenshotOfView(v, v.measuredHeight, v.measuredWidth)
                var imageOutStream: OutputStream? = null
                val cv = ContentValues()
                cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")
                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                val uri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
                try {
                    imageOutStream = uri?.let { it1 -> contentResolver.openOutputStream(it1) }
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
                    imageOutStream?.close()
                    val toast = Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT)
                    toast.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
        }

    }

    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }

    private fun currentWidth(width: Float) {
        currentWidth = width
        path = Path()
    }


    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    private fun unselectEllipse(){
        binding.btnEllipse.setImageResource(R.drawable.ic_unselected_circle)
        binding.btnEllipse.setBackgroundResource(R.drawable.background_card)
    }

    private fun unselectRectangle(){
        binding.btnRectangle.setImageResource(R.drawable.ic_unselected_rectangle)
        binding.btnRectangle.setBackgroundResource(R.drawable.background_card)
    }

    private fun unselectPalette(){
        binding.btnPallete.setImageResource(R.drawable.ic_unselected_palette)
        binding.btnPallete.setBackgroundResource(R.drawable.background_card)
    }

    private fun unselectPencil(){
        binding.btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
        binding.btnPencil.setBackgroundResource(R.drawable.background_card)
    }

    private fun unselectSlider(){
        binding.btnSlider.setImageResource(R.drawable.ic_unselected_circle)
        binding.btnSlider.setBackgroundResource(R.drawable.background_card)
    }
}