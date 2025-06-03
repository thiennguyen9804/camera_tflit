package com.example.camera_tflit

import android.graphics.Bitmap
import android.util.Log
import com.google.android.renderscript.Toolkit
import com.google.android.renderscript.YuvFormat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMethodCodec



class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.camera_tflit/classify"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getLandmark" -> {
                    val arguments = call.arguments as? Map<*, *>
                    if (arguments != null) {
                        val image = Yuv420888ImageAdapter(arguments)
                        detectLandmark(image)
                    } else {
                        result.error("INVALID_ARGS", "Invalid arguments received", null)
                    }
                }

                else -> result.notImplemented()
            }

        }


    }

    private fun detectLandmark(image: Yuv420888Image): String {
        val nv21 = (image as Yuv420888ImageAdapter).toNv21()
        Toolkit.yuvToRgbBitmap(nv21!!, image.width, image.height, YuvFormat.NV21)
        return "Hello"
    }

}
