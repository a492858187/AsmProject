package org.tq.thread

import java.util.concurrent.atomic.AtomicInteger

class OptimizedThread(runnable: Runnable?, name: String?, className: String): 
    Thread(runnable, generateThreadName(name,className)){
    
    companion object {
        private val threadId = AtomicInteger(0)
        
        private fun generateThreadName(name: String?, className: String): String {
            return "$className-${threadId.getAndIncrement()}" + if (name.isNullOrBlank()) {
                ""
            } else {
                "-$name"
            }
        }
    }
    
    constructor(runnable: Runnable, className: String): this(runnable, null, className)
    
    constructor(name: String, className: String): this(null, name, className)
}