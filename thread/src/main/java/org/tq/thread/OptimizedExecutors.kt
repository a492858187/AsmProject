package org.tq.thread

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object OptimizedExecutors {
    private const val defaultThreadKeepAliveTime = 5000L
    
    @JvmStatic
    fun newFixedThreadPool(nThreads: Int, className: String): ExecutorService {
        return newFixedThreadPool(nThreads, null, className)
    }
    
    @JvmStatic
    fun newFixedThreadPool(nThreads: Int, threadFactory: ThreadFactory?, className: String): ExecutorService {
        return getOptimizedExecutorService(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            LinkedBlockingDeque(),threadFactory,className)
    }

    private fun getOptimizedExecutorService(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit,
        workQueue: BlockingQueue<Runnable>,
        threadFactory: ThreadFactory? = null,
        className: String,
    ): ExecutorService {
        val executor = ThreadPoolExecutor(
            corePoolSize, maximumPoolSize,
            keepAliveTime, unit,
            workQueue,
            NamedThreadFactory(threadFactory, className)
        )
        executor.setKeepAliveTime(defaultThreadKeepAliveTime, TimeUnit.MILLISECONDS)
        executor.allowCoreThreadTimeOut(true)
        return executor
    }
    
    private class NamedThreadFactory(
        private val threadFactory: ThreadFactory?,
        private val className: String
    ): ThreadFactory {
        
        private val threadId = AtomicInteger(0)
        
        override fun newThread(runnable: Runnable?): Thread {
            val originThread = threadFactory?.newThread(runnable)
            val threadName = className + "-" + threadId.getAndIncrement() + if (originThread != null) {
                "-" + originThread.name
            } else {
                ""
            }
            val thread = originThread ?: Thread(runnable)
            thread.name = threadName
            if(thread.isDaemon) {
                thread.isDaemon = false
            }
            if(thread.priority != Thread.NORM_PRIORITY) {
                thread.priority = Thread.NORM_PRIORITY
            }
            return thread
        }

    }
}