package org.tq.asmproject

import android.app.Application
import org.tq.privacy.ContextProvider

class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.inject(this)
    }
    
}