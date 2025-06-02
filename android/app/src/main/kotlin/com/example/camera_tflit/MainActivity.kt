package com.example.camera_tflit

import android.graphics.Bitmap
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.example.
import com.google.android.renderscript.Toolkit


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
                        val image = Yuv420888Image.fromMap(arguments)
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
        Toolkit.yuvToRgbBitmap()
        return "Hello"

    }

}
