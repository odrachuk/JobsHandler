package com.softsandr.backgroundworker

import java.util.concurrent.TimeUnit
import kotlin.random.Random

object JobsProducer {

    /**
     * Use to get a method with long operation inside
     *
     * @return a method with logic that simulates long operation
     */
    fun makeJob(index: Int, callback: (JobItem) -> Unit): () -> Unit = {
        val startTime = System.currentTimeMillis()
        TimeUnit.SECONDS.sleep(Random.nextLong(1L, 5L))
        val endTime = System.currentTimeMillis()
        callback.invoke(JobItem(index.toString(), TimeUnit.MILLISECONDS.toSeconds(endTime - startTime).toString()))
    }
}