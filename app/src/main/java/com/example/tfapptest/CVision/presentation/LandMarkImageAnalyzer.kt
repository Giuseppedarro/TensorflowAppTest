package com.example.bottomapp.vbt.presentation

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.bottomapp.vbt.domain.Classification
import com.example.bottomapp.vbt.domain.LandMarkClassifier

class LandMarkImageAnalyzer(
    private val classifier: LandMarkClassifier,
    private val onResult: (List<Classification>) ->  Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter  =  0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321,321)
            val result = classifier.classify(bitmap, rotationDegrees)
            onResult(result)
        }
        frameSkipCounter++
        image.close()
    }


}

fun Bitmap.centerCrop(newWidth: Int, newHeight: Int): Bitmap {
    val xStart = (width - newWidth) / 2
    val yStart = (height - newHeight) / 2
    if (xStart < 0 || yStart < 0 || newWidth > width || newHeight > height) {
        throw IllegalArgumentException("illegal argument for centerCrop")
    }
    return Bitmap.createBitmap(this, xStart, yStart, newWidth, newHeight)
}