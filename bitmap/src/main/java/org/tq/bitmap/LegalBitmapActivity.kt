package org.tq.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.tq.track.bitmap.R

class LegalBitmapActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_bitmap)
        title = "大图检测"
    }
}