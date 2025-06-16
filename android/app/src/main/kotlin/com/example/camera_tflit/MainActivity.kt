package com.example.camera_tflit

import android.os.Bundle
import android.util.Log
import com.example.camera_tflit.data.TFLiteMyImageClassifier
import com.example.camera_tflit.domain.MyImageClassifier
import com.example.camera_tflit.presentation.Yuv420888Image
import com.example.camera_tflit.presentation.Yuv2Nv21Adapter
import com.example.camera_tflit.presentation.centerCrop
import com.google.android.renderscript.Toolkit
import com.google.android.renderscript.YuvFormat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.camera_tflit/classify"
    lateinit var classifier: MyImageClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifier = TFLiteMyImageClassifier(this)
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
                        val image = Yuv2Nv21Adapter(arguments)
                        val res = imageProcess(image)
                        result.success(res)
                    } else {
                        result.error("INVALID_ARGS", "Invalid arguments received", "Invalid arguments received")
                    }
                }

                else -> result.notImplemented()
            }

        }


    }

    private fun imageProcess(image: Yuv420888Image): String? {
        val nv21 = (image as Yuv2Nv21Adapter).toNv21()
        val bitmap = Toolkit.yuvToRgbBitmap(nv21!!, image.width, image.height, YuvFormat.NV21)
            .centerCrop(321, 321)
        val res = classifier.classify(bitmap, image.rotation)
        Log.i("Food result length: ", res.size.toString())
        return res.maxByOrNull {
            it.score
        }?.name
    }

}
