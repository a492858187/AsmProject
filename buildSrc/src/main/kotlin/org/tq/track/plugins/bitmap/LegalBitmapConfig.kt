package org.tq.track.plugins.bitmap

import java.io.Serializable

internal data class LegalBitmapConfig(
    val imageViewClass: String
): Serializable {
    companion object {
        operator fun invoke(pluginParameter: LegalBitmapPluginParameter): LegalBitmapConfig {
            return LegalBitmapConfig(
                imageViewClass = pluginParameter.imageViewClass.replace(".", "/")
            )
        }
    }
}

open class LegalBitmapPluginParameter {
    var imageViewClass = ""
}