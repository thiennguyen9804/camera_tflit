package com.example.camera_tflit.presentation

import android.util.Log

class Yuv2Nv21Adapter : Yuv420888Image {
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
    fun toNv21(): ByteArray? {
        var nv21 = ByteArray((width * height * 1.5).toInt())
        if(!yuv2Nv21(width, height, y, u, v, yRowStride, uvRowStride, yPixelStride, uvPixelStride, nv21)) {
            Log.i("Yuv2Nv21", "nv21 is null")
            return null
        }
        return nv21
    }

    private external fun yuv2Nv21(
        width: Int, height: Int,
        y: ByteArray, u: ByteArray, v: ByteArray,
        yRowStride: Int, uvRowStride: Int,
        yPixelStride: Int, uvPixelStride: Int,
        nv21Output: ByteArray
    ): Boolean


}