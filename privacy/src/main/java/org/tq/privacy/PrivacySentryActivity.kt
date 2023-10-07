package org.tq.privacy

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.tq.track.privacy.R

class PrivacySentryActivity: AppCompatActivity() {

    private val btnGetDeviceId by lazy {
        findViewById<Button>(R.id.btnGetDeviceId)
    }

    private val btnGetDeviceBrand by lazy {
        findViewById<Button>(R.id.btnGetDeviceBrand)
    }

    private val tvLog by lazy {
        findViewById<Button>(R.id.tvLog)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnGetDeviceId.setOnClickListener { 
            tvLog.append("\n" + "deviceId: " + DeviceUtils.getDeviceId(this))
        }
        btnGetDeviceBrand.setOnClickListener {
            tvLog.append("\n" + "deviceBrand: " + DeviceUtils.getBrand())
        }
    }
    
}