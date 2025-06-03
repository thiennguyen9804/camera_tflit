package com.example.camera_tflit

open class Yuv420888Image(
    val width: Int,
    val height: Int,
    val y: ByteArray,
    val u: ByteArray,
    val v: ByteArray,
    val yRowStride: Int,
    val uvRowStride: Int,
    val yPixelStride: Int,
    val uvPixelStride: Int,
) {
    constructor(map: Map<*, *>) : this(
        width = map["width"] as Int,
        height = map["height"] as Int,
        y = map["y"] as ByteArray,
        u = map["u"] as ByteArray,
        v = map["v"] as ByteArray,
        yRowStride = map["yRowStride"] as Int,
        uvRowStride = map["uvRowStride"] as Int,
        yPixelStride = map["yPixelStride"] as Int,
        uvPixelStride = map["uvPixelStride"] as Int
    )

}