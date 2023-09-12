package org.tq.asmproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.tq.track.R
import org.tq.track.click.ViewClickCheckActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btnViewClick).setOnClickListener {
            startActivity<ViewClickCheckActivity>()
        }
    }

    private inline fun <reified T: Activity> startActivity() {
        Intent(this, T::class.java).apply {
            startActivity(this)
        }
    }
}