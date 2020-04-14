package com.example.guessthecelebrity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

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

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;

        try {
                result = downloadTask.execute("http://www.posh24.se/kandisar").get();

                Pattern pattern = Pattern.compile("<img src=\"(.*?)\""); //<img src="http://cdn.posh24.se/images/:profile/c/2066697" alt="Bianca Ingrosso"/>
                Matcher matcher = pattern.matcher(result);

                while (matcher.find()) {
                    celebUrls.add(matcher.group(1));
                }

                pattern = Pattern.compile("alt=\"(.*?)\"/>"); //<img src="http://cdn.posh24.se/images/:profile/c/2066697" alt="Bianca Ingrosso"/>
                matcher = pattern.matcher(result);


                while (matcher.find()){
                    celebNames.add(matcher.group(1));
                }

                Log.i("Celeb Size", String.valueOf(celebNames.size()));
                Log.i("URLS Size", String.valueOf(celebNames.size()));

                Random rand = new Random();
                chosenCeleb = rand.nextInt(11);

                Log.i("ChosenCeleb Number", String.valueOf(chosenCeleb));


                //Image Code
                ImageDownloader imageDownloader = new ImageDownloader();

                Bitmap celebImage = imageDownloader.execute(celebUrls.get(chosenCeleb)).get();

                imageView.setImageBitmap(celebImage);

                //Image Code

                locationOfCorrectAnswer = rand.nextInt(4);

                int incorrectAnswerLocation;

                for(int i=0; i<4; i++){
                    if(i == locationOfCorrectAnswer){
                        answers[i] = celebNames.get(chosenCeleb);

                        Log.i("Correct Answer", answers[i]);
                    }else{
                        incorrectAnswerLocation = rand.nextInt(11);

                        Log.i("IncorrectAL First", String.valueOf(incorrectAnswerLocation));


                        while(incorrectAnswerLocation == chosenCeleb){
                            incorrectAnswerLocation = rand.nextInt(11);
                            Log.i("IncorrectAL while", String.valueOf(incorrectAnswerLocation));
                        }
                        answers[i] = celebNames.get(incorrectAnswerLocation);

                        Log.i("Incorrect Answer", answers[i]);
                    }
                }


            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);


            Log.i("Celeb Name", celebNames.get(0));
            Log.i("Celeb Name", celebNames.get(1));
            Log.i("Celeb Name", celebNames.get(2));
            Log.i("Celeb Name", celebNames.get(3));

            Log.i("Celeb Url", celebUrls.get(0));
            Log.i("Celeb Url", celebUrls.get(1));
            Log.i("Celeb Url", celebUrls.get(2));
            Log.i("Celeb Url", celebUrls.get(3));




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

    public void celebChosen(View view){
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong! It was " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
        }
    }

}
