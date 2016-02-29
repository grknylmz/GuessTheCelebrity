package com.saintgames.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames  = new ArrayList<String>();
    int chosenCeleb = 0;
    int locationOfCorrectAnswer;
    int[] answers = new int[4];

    ImageView imageView;

    public  class ImageDownloader extends AsyncTask<String, Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
    }




    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            int counter = 0;

            try{
                url = new URL(urls[0]);
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

        imageView = (ImageView) findViewById(R.id.imageView);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;

        try {
            result = downloadTask.execute("http://www.posh24.com/celebrities").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while (m.find()){
                celebURLs.add(m.group(1)) ;
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while (m.find()){
                celebNames.add(m.group(1)) ;
            }


            Random random = new Random();
            chosenCeleb = random.nextInt(celebURLs.size());

            ImageDownloader imageTask = new ImageDownloader();
            Bitmap celebImage;
            celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();
            imageView.setImageBitmap(celebImage);
            locationOfCorrectAnswer = random.nextInt(4);


        } catch (InterruptedException e) {

        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
