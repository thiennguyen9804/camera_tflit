package com.example.camera_tflit

open data class Yuv420888Image(
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Yuv420888Image

        if (width != other.width) return false
        if (height != other.height) return false
        if (yRowStride != other.yRowStride) return false
        if (uvRowStride != other.uvRowStride) return false
        if (yPixelStride != other.yPixelStride) return false
        if (uvPixelStride != other.uvPixelStride) return false
        if (!y.contentEquals(other.y)) return false
        if (!u.contentEquals(other.u)) return false
        if (!v.contentEquals(other.v)) return false

        return true
    }



    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + yRowStride
        result = 31 * result + uvRowStride
        result = 31 * result + yPixelStride
        result = 31 * result + uvPixelStride
        result = 31 * result + y.contentHashCode()
        result = 31 * result + u.contentHashCode()
        result = 31 * result + v.contentHashCode()
        return result
    }

    companion object {
        fun fromMap(map: Map<*, *>): Yuv420888Image {
            val image = Yuv420888Image(
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

            return image
        }
    }
}