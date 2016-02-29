package com.saintgames.guessthecelebrity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            int counter = 0;

            try{
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection ();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is);
                int data = isReader.read();

                while (data != -1){
                    char current = (char) data;
                    counter++;
                    Log.i("Content " + counter , result);
                    result += current;
                }

                return  result;

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;

        try {
            result = downloadTask.execute("http://www.posh24.com/celebrities").get();
            Log.i("All Content : ", result);
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
