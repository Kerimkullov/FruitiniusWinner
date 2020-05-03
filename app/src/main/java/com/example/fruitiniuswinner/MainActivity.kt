package com.example.fruitiniuswinner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bolts.AppLinks
import com.facebook.FacebookSdk
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = "fruitiniusWinner"
    val MILLISECONDS = 50000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            val url = "https://fruktovayapobeda.com/pobeda"
            GetLink().execute(url)
            getDeepLink()
    }

    private fun getDeepLink() {
        //store deepLink in shared pref
        val sharedPreferences= this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        FacebookSdk.sdkInitialize(applicationContext)
        val targetUrl: Uri? = AppLinks.getTargetUrlFromInboundIntent(this, intent)
        if(targetUrl != null) {
            var deepLink = targetUrl.toString().split("app://param")[1]
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("deepLink",deepLink)
            editor.apply()
            editor.commit()
        }
    }

    inner class GetLink : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            // Before doInBackground
        }

        override fun doInBackground(vararg urls: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urls[0])

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = MILLISECONDS
                urlConnection.readTimeout = MILLISECONDS

                var inString = streamToString(urlConnection.inputStream)

                publishProgress(inString)
            } catch (ex: Exception) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = values[0]
                val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
                val sharedPrefDeepLink = sharedPreferences.getString("deepLink", "")
                if (json != "" || sharedPrefDeepLink != "") {
                    chromeStart()
                } else {
                    gameStart()
                }
            } catch (ex: Exception) {

            }
        }

        override fun onPostExecute(result: String?) {

        }
    }

    fun streamToString(inputStream: InputStream): String {

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: Exception) {

        }

        return result
    }
    private fun chromeStart() {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        val sharedPrefDeepLink = sharedPreferences.getString("deepLink", "")
        val intent = Intent(this, ChromeActivity::class.java)
        intent.putExtra("targetUrl", sharedPrefDeepLink)
        startActivity(intent)
    }
    private fun gameStart() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}




