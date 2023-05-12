package com.example.samapp.tool

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.samapp.R
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class En_KrActivity : AppCompatActivity() {

    private var en_kr: EditText? = null
    private var en_krButton: Button? = null
    private var en_krText: TextView? = null
    private var en_krResult: String? = null

    // 백 그라운드에서 파파고 API와 연결하여 번역 결과를 가져옵니다.
    internal inner class BackgroundTask :
        AsyncTask<Int?, Int?, Int?>() {
        override fun onPreExecute() {}
        protected override fun doInBackground(vararg params: Int?): Int? {
            val output = StringBuilder()
            val clientId = "p9OYM7ZlYD7JbNrdzeCV" // 애플리케이션 클라이언트 아이디 값";
            val clientSecret = "KeI93u9hcN" // 애플리케이션 클라이언트 시크릿 값";
            try {
                // 번역문을 UTF-8으로 인코딩합니다.
                val text = URLEncoder.encode(en_kr!!.text.toString(), "UTF-8")
                val apiURL = "https://openapi.naver.com/v1/papago/n2mt"

                // 파파고 API와 연결을 수행합니다.
                val url = URL(apiURL)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("X-Naver-Client-Id", clientId)
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret)

                // 번역할 문장을 파라미터로 전송합니다.
                val en_krParams = "source=en&target=ko&text=$text"
                con.doOutput = true
                val wr = DataOutputStream(con.outputStream)
                wr.writeBytes(en_krParams)
                wr.flush()
                wr.close()

                // 번역 결과를 받아옵니다.
                val responseCode = con.responseCode
                val br: BufferedReader
                br = if (responseCode == 200) {
                    BufferedReader(InputStreamReader(con.inputStream))
                } else {
                    BufferedReader(InputStreamReader(con.errorStream))
                }
                var inputLine: String?
                while (br.readLine().also { inputLine = it } != null) {
                    output.append(inputLine)
                }
                br.close()
            } catch (ex: Exception) {
                Log.e("SampleHTTP", "Exception in processing response.", ex)
                ex.printStackTrace()
            }
            en_krResult = output.toString()
            return null
        }

        override fun onPostExecute(a: Int?) {
            val parser = JsonParser()
            val element = parser.parse(en_krResult)
            if (element.asJsonObject["errorMessage"] != null) {
                Log.e("번역 오류", "번역 오류가 발생했습니다. " +
                        "[오류 코드: " + element.asJsonObject["errorCode"].asString + "]")
            } else if (element.asJsonObject["message"] != null) {
                // 번역 결과 출력
                en_krText!!.text = element.asJsonObject["message"].asJsonObject["result"]
                    .asJsonObject["translatedText"].asString
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_en_kr)
        en_kr = findViewById<View>(R.id.en_kr) as EditText
        en_krButton = findViewById<View>(R.id.en_krButton) as Button
        en_krText = findViewById<View>(R.id.en_krText) as TextView
        en_krButton!!.setOnClickListener { BackgroundTask().execute() }
    }
}