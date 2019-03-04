package com.example.cloo2019

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_landing.*
import java.net.URL

class ToiletEntry {
    var toiletId: String = ""
    var toiletName: String = ""
    var toiletAddress: String = ""

    override fun toString(): String {
        return """
            id = $toiletId
            name = $toiletName
            address = $toiletAddress
        """.trimIndent()
    }
}


class LandingActivity : AppCompatActivity() {

    private val TAG = "LandingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContentView(R.layout.activity_landing)
        setSupportActionBar(toolbar)

        val downloadData = DownloadData()
        downloadData.execute("http://192.168.1.6:5000/result")
        Log.d(TAG, "onCreate done")



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>(){
            private val TAG = "DownloadData"
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute : parameter is $result")
            }
    
            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "doInBackground: starts with ${params[0]}")
                val toiletMasterFeed = downloadToiletMaster(params[0])
                if(toiletMasterFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error in getting the feed")
                }
                return toiletMasterFeed
            }

            private fun downloadToiletMaster(urlPath: String?) : String {
                return URL(urlPath).readText()
            }
        }
    }

}
