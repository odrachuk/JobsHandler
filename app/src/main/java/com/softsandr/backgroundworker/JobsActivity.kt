package com.softsandr.backgroundworker

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class JobsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        setupToolbar()

        val adapter = JobsAdapter()
        val recycler: RecyclerView = findViewById(R.id.activity_jobs__recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val textViewEmpty: TextView = findViewById(R.id.activity_jobs__empty)

        findViewById<View>(R.id.activity_jobs__start_btn).setOnClickListener {
            // simulating of executing 10 jobs in the background
            var jobs = adapter.itemCount
            repeat(10) {
                JobsHandler.startJob(JobsProducer.makeJob(++jobs) { jobItem ->
                    // here we handle a job's callback
                    runOnUiThread {
                        if (textViewEmpty.visibility == View.VISIBLE) {
                            textViewEmpty.visibility = View.GONE
                        }

                        adapter.addItem(jobItem)

                        if (recycler.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                            recycler.smoothScrollToPosition(adapter.itemCount)
                        }
                    }
                }, Runnable {
                    // here we handle a callback about empty queue
                    runOnUiThread {
                        Snackbar.make(findViewById(R.id.activity_jobs__root),
                            getString(R.string.queue_empty), Snackbar.LENGTH_SHORT).show();
                    }
                })
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onDestroy() {
        // not a big deal for an application with alone activity
        JobsHandler.stopProcessing()
        super.onDestroy()
    }
}
