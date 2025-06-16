package com.example.camera_tflit.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.camera_tflit.domain.Classification
import com.example.camera_tflit.domain.MyImageClassifier
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

private val mockClassification = listOf<Classification>(
    Classification(
        name = "Eiffel",
        score = 1f
    )
)

class TFLiteMyImageClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResult: Int = 1,
) : MyImageClassifier {
    lateinit var classifier: ImageClassifier
    private fun setUpClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResult)
            .setScoreThreshold(threshold)
            .build()
        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "model.tflite",
                options
            )
        } catch (e: Exception) {
            Log.e("Model Error", "Cannot load model file")
            e.printStackTrace()
        }
    }

    override fun classify(
        bitmap: Bitmap,
        rotation: Int
    ): List<Classification> {
        if(!::classifier.isInitialized) {
            setUpClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .build()
        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val results = classifier.classify(tensorImage, imageProcessingOptions)
        val classes = results.flatMap { classification ->
            classification.categories.map { category ->
                Classification(
                    name = category.displayName,
                    score = category.score
                )
            }
        }.distinctBy { it.name }
        classes.forEach {
            Log.i("LandmarkClassifier", it.name)
        }
        return classes
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when(rotation) {
            270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}