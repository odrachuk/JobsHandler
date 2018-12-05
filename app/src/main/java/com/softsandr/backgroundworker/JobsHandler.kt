package com.softsandr.backgroundworker

import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

object JobsHandler {
    private const val MSG_QUEUE_EMPTY = 1111

    private class JobsWorker : HandlerThread(JobsWorker::class.java.simpleName) {
        // this handler uses the same queue, that is why we can user a marker message after each job
        // if the marker message is done, that means all previous jobs have been done too
        lateinit var queueWatcher: Handler

        override fun onLooperPrepared() {
            queueWatcher = object : Handler(looper) {
                override fun handleMessage(msg: Message) {
                    if (msg.what == MSG_QUEUE_EMPTY && msg.obj is Runnable) {
                        (msg.obj as Runnable).run()
                    }
                }
            }
        }
    }

    private val jobsWorker by lazy { JobsWorker().apply { start() } }
    private val jobsWorkerHandler by lazy { Handler(jobsWorker.looper) }

    fun startJob(job: () -> Unit, emptyQueueCallback: Runnable) {
        jobsWorkerHandler.post(job)
        jobsWorkerHandler.post { sendWatcherMessage(emptyQueueCallback) }
    }

    private fun sendWatcherMessage(emptyQueueCallback: Runnable) {
        jobsWorker.queueWatcher.removeMessages(MSG_QUEUE_EMPTY)
        val queueEmptyMsg = jobsWorker.queueWatcher.obtainMessage(MSG_QUEUE_EMPTY, emptyQueueCallback)
        jobsWorker.queueWatcher.sendMessage(queueEmptyMsg)
    }

    fun stopProcessing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            jobsWorker.quitSafely()
        } else {
            jobsWorker.quit()
        }
    }
}