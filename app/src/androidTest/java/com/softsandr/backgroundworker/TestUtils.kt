package com.softsandr.backgroundworker

import org.junit.Assert

object TestUtils {
    abstract class TestCondition(val failedMessage: String) {

        abstract fun conditionMet(): Boolean

        fun conditionResolved(): Boolean {
            var conditionMet = false

            try {
                conditionMet = conditionMet()
            } catch (ignored: Throwable) {
            }

            return conditionMet
        }
    }

    fun waitFor(condition: TestCondition, timeoutInMilliseconds: Long) {
        val endTime = System.currentTimeMillis() + timeoutInMilliseconds

        while (System.currentTimeMillis() < endTime) {
            if (condition.conditionResolved()) {
                return
            }

            try {
                Thread.sleep(200)
            } catch (ignored: InterruptedException) {
            }

        }

        // If you reach this point, the condition was never met. This is also the condition's last chance to prove itself
        Assert.assertTrue("Condition failed: " + condition.failedMessage, condition.conditionMet())
    }
}