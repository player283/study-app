package com.example.samapp.tool;


import static android.util.Log.ERROR;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.samapp.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class Kr_EnActivity extends AppCompatActivity {

    //사용할 객체 선언
    private EditText kr_en;
    private Button kr_enButton;
    private TextView kr_enText;
    private String kr_enResult;
    private TextToSpeech tts;
    private Button tts_btn;


    // 백 그라운드에서 파파고 API와 연결하여 번역 결과를 가져옵니다.
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {
            StringBuilder output = new StringBuilder();
            String clientId = "p9OYM7ZlYD7JbNrdzeCV"; // 애플리케이션 클라이언트 아이디 값";
            String clientSecret = "KeI93u9hcN"; // 애플리케이션 클라이언트 시크릿 값";
            try {
                // 번역문을 UTF-8으로 인코딩합니다.
                String text = URLEncoder.encode(kr_en.getText().toString(), "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

                //API 연결을 수행(대부분 코드 유사)
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                // 번역할 문장을 파라미터로 전송합니다.
                String kr_enParams = "source=ko&target=en&text=" + text;

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(kr_enParams);

                wr.flush();
                wr.close();

                // 번역 결과를 받아옵니다.(대부분 코드 유사)
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    output.append(inputLine);
                }
                br.close();
            } catch (Exception ex) {
                Log.e("SampleHTTP", "Exception in processing response.", ex);
                ex.printStackTrace();
            }
            kr_enResult = output.toString();
            return null;
        }
        //통신시 json 넘겨서 결과 출력값 호출
        protected void onPostExecute(Integer a) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(kr_enResult);
            if (element.getAsJsonObject().get("errorMessage") != null) {
                Log.e("번역 오류", "번역 오류가 발생했습니다. " +
                        "[오류 코드: " + element.getAsJsonObject().get("errorCode").getAsString() + "]");
            } else if (element.getAsJsonObject().get("message") != null) {
                // 번역 결과 출력
                kr_enText.setText(element.getAsJsonObject().get("message").getAsJsonObject().get("result")
                        .getAsJsonObject().get("translatedText").getAsString());
            }

        }

    }

    //tts api 설정
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kr_en);

        //사용할 xml 레이아웃 id값 가져오기
        kr_en = (EditText) findViewById(R.id.kr_en);
        kr_enButton = (Button) findViewById(R.id.kr_enButton);
        kr_enText = (TextView) findViewById(R.id.kr_enText);
        tts_btn = (Button) findViewById(R.id.tts_btn);

        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
        //tts_btn 클릭 리스너 생성
        tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kr_enText에 있는 문장을 읽는다.
                tts.speak(kr_enText.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        //ke-en btn 리스너 생성
        kr_enButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });
    }

}