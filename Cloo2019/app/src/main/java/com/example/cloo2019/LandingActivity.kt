package com.example.cloo2019

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.content_landing.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.properties.Delegates

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
    private val downloadData by lazy { DownloadData(this, landingListView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContentView(R.layout.activity_landing)
        setSupportActionBar(toolbar)

        downloadData.execute("http://192.168.1.2:5000/result")  // not using this URL
//        downloadData.execute("http://192.168.1.2:5000/resultsub?Lat=12.9718342&Lng=77.6562343&Radius=1")
        Log.d(TAG, "onCreate done")

        val tvLocationHead = findViewById<TextView>(R.id.textViewLocation)
        if (CurrentLocation.getLastLocationProvider() == "default")
            tvLocationHead.text = "Unable to determine current location, using defaults!  Please turn on location and retry"
        else
            tvLocationHead.text = "Showing toilets near me..."

        fab.setOnClickListener {
            val i = Intent(this, MainActivityAddNewLoo::class.java)
            startActivity(i)

//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)

    }

    companion object {
        private val TAG = "DownloadData"

        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>(){

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute : parameter is $result")
                val parseApi = ParseAPI()
                parseApi.handleJson(result)

//                val arrayAdapter = Locationadapter(propContext, R.layout.loos, parseApi.toilets)
//                val arrayAdapter = ArrayAdapter<ToiletEntry>(propContext, R.layout.list_loo, parseApi.toilets)
//                propListView.adapter = arrayAdapter

                val toiletAdapter = ToiletAdapter(propContext, R.layout.loo_record, parseApi.toilets)
                propListView.adapter = toiletAdapter

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
                Log.d(TAG, "downloadToiletMaster: urlPath $urlPath")
                val paramsSting = "Lat=" + "12.9718342" + "&" + "Lng=" + "77.6562343" + "&" + "Radius=" + "1"
                val currentLocation = CurrentLocation

                val latLabel = "Lat"
                val latVal = currentLocation.getLastLatAsString()   //"12.9718342"
                val lngLabel = "Lng"
                val lngVal = currentLocation.getLastLngAsString()   //"77.6562343"
                val radiusLabel = "Radius"
                val radiusVal = currentLocation.getLookUpRadius()   // 1 km

                try {
                    val url = URL("http://192.168.1.6:5000/resultsub")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.doOutput = true

                    var data = URLEncoder.encode(latLabel, "UTF-8") + "=" + URLEncoder.encode(latVal, "UTF-8")
                    data += "&" + URLEncoder.encode(lngLabel, "UTF-8") + "=" + URLEncoder.encode(lngVal, "UTF-8")
                    data += "&" + URLEncoder.encode(radiusLabel, "UTF-8") + "=" + URLEncoder.encode(radiusVal, "UTF-8")

                    connection.connect()

                    val os = OutputStreamWriter(connection.outputStream)
                    os.write(data)
                    os.flush()

                    var stream = connection.inputStream
                    var reader = BufferedReader(InputStreamReader(stream, "UTF-8"), 8)
                    val result = reader.readText()

                    Log.d(TAG, "********** results   \n $result")

                    return result
                } catch (e : IOException) {
                    e.printStackTrace()
                    return "Error"
                }

//                return url.readText()
//                return URL(urlPath).readText()
            }
        }

    }

}
