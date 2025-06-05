package com.example.camera_tflit

import android.os.Bundle
import android.util.Log
import com.example.camera_tflit.data.TFLiteLandmarkClassifier
import com.example.camera_tflit.domain.LandmarkClassifier
import com.example.camera_tflit.presentation.Yuv420888Image
import com.example.camera_tflit.presentation.YuvNv21Adapter
import com.google.android.renderscript.Toolkit
import com.google.android.renderscript.YuvFormat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.camera_tflit/classify"
    lateinit var classifier: LandmarkClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifier = TFLiteLandmarkClassifier(this)
    }

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
                        val image = YuvNv21Adapter(arguments)
                        val res = imageProcess(image)
                        result.success(res)
                    } else {
                        result.error("INVALID_ARGS", "Invalid arguments received", null)
                    }
                }

                else -> result.notImplemented()
            }

        }


    }

    private fun imageProcess(image: Yuv420888Image): String {
        val nv21 = (image as YuvNv21Adapter).toNv21()
        val bitmap = Toolkit.yuvToRgbBitmap(nv21!!, image.width, image.height, YuvFormat.NV21)
        val res = classifier.classify(bitmap, image.rotation)
        return res.maxByOrNull {
            it.score
        }!!.name
    }

}
