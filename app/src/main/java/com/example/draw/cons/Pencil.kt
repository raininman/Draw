package com.example.draw.cons

import android.graphics.Path

data class Pencil(
    var color: Int = 0,
    var strokeWidth:Float = 16f,
    var path: Path? = null
)