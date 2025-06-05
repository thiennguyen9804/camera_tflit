package com.example.camera_tflit.presentation

import android.graphics.Bitmap

class YuvBitmapAdapter : Yuv420888Image {
    constructor(
        width: Int,
        height: Int,
        y: ByteArray,
        u: ByteArray,
        v: ByteArray,
        yRowStride: Int,
        uvRowStride: Int,
        yPixelStride: Int,
        uvPixelStride: Int,
        rotation: Int,
    ) : super(width,height, y, u, v,
        yRowStride, uvRowStride,
        yPixelStride, uvPixelStride, rotation
    )
    constructor(map: Map<*, *>): super(map)

    companion object {
        init {
            System.loadLibrary("yuv2rgb-lib")
        }

    }

    fun toBitmap(): Bitmap? {

    }

    private external fun toBitmapNative(
        width: Int, height: Int,
        y: ByteArray, u: ByteArray, v: ByteArray,
        yRowStride: Int, uvRowStride: Int,
        yPixelStride: Int, uvPixelStride: Int,
        argbResult: ByteArray
    ): Boolean

}