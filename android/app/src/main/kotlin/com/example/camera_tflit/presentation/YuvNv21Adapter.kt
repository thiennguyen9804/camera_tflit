package com.example.camera_tflit.presentation

class Yuv420888ImageAdapter : Yuv420888Image {
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
    ) : super(width,height, y, u, v, yRowStride, uvRowStride, yPixelStride, uvPixelStride)
    constructor(map: Map<*, *>): super(map)

    companion object {
        init {
            System.loadLibrary("yuv2rgb-lib")
        }

    }
    fun toNv21(): ByteArray? {
        var nv21 = ByteArray((width * height * 1.5).toInt())
        if(!yuv2Nv21(width, height, y, u, v, yRowStride, uvRowStride, yPixelStride, uvPixelStride, nv21)) {
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