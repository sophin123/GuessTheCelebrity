package com.example.guessthecelebrity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    TextView textView;

    int chosenCeleb = 0;
    String[] answers = new String[4];
    int locationOfCorrectAnswer = 0;


    Button button0, button1, button2, button3;

    ArrayList<String> celebUrls = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.celebrityiv);
        textView = (TextView) findViewById(R.id.textView);

        button0 = findViewById(R.id.button1);
        button1 = findViewById(R.id.button2);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button4);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;

        try {
                result = downloadTask.execute("http://www.posh24.se/kandisar").get();

                Pattern pattern = Pattern.compile("<img src=\"(.*?)\""); //<img src="http://cdn.posh24.se/images/:profile/c/2066697" alt="Bianca Ingrosso"/>
                Matcher matcher = pattern.matcher(result);

                while (matcher.find()) {
                    System.out.println(matcher.group(1));
                }

                pattern = Pattern.compile("alt=\"(.*?)\"/>"); //<img src="http://cdn.posh24.se/images/:profile/c/2066697" alt="Bianca Ingrosso"/>
                matcher = pattern.matcher(result);


                while (matcher.find()){
                    System.out.println(matcher.group(1));
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

}
