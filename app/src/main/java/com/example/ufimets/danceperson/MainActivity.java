package com.example.ufimets.danceperson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String LOG_TAG = "main1";
    TextView screen;
    TextView score;
    TextView best_scoreViev;
    TextView flex;

    private final String FILENAME = "save.txt";
    Integer best_score;
    int randomArrow = 0;
    Integer k = 0;
    float xDown = 0;
    float yDown = 0;
    float xUp = 0;
    float yUp = 0;
    float x;
    float y;
    String xMove;
    String yMove;
    String arrow;
    String directionViev[] = {"\uF102", "\uF101", "\uF100", "\uF103"};
    String direction[] = {"Up", "Right", "Left", "Down"};
    final int CODE = 1;
    Integer time_left = 20;
    String name_user;
    Boolean toggle = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        screen = findViewById(R.id.screen);
        score = findViewById(R.id.score);
        best_scoreViev = findViewById(R.id.best_score);
        flex = findViewById(R.id.flex);


        VideoView mVideoView2 = findViewById(R.id.videoView);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            score.setText("Нет разрешения");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CODE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        String uriPath2 = "android.resource://com.example.ufimets.danceperson/" + R.raw.ricardo_webm;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();

        best_score = load();
        randomArrow = (int) (Math.random() * 4);
        screen.setText(directionViev[randomArrow]);
        screen.setOnTouchListener(this);
        name_user = getIntent().getStringExtra("CODE");
        Log.i(LOG_TAG, "/ " + name_user + " /");

        flex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle = true;
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i(LOG_TAG, "Времни осталось " + time_left);
                        flex.setText(time_left.toString());
                        if (time_left == 0) {

                            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                            String[] str = {name_user, k.toString()};
                            k = 0;
                            intent.putExtra("CODE", str);
                            startActivity(intent);
                            cancel();
                        }
                        time_left--;
                    }
                }, 500, 1000);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    best_score = load();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                xDown = x;
                yDown = y;
                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                xUp = x;
                yUp = y;
                break;
        }


        float fx = xUp - xDown;
        xMove = fx > 0 ? "Right" : "Left";
        float fy = yUp - yDown;
        yMove = fy > 0 ? "Down" : "Up";

        if (Math.abs(fx) > Math.abs(fy)) arrow = xMove;
        else arrow = yMove;


        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {


            if (arrow == direction[randomArrow] && toggle == true) {
                k++;
                best_score = k > best_score ? k : best_score;
                save(best_score);
                score.setText(k + "");
                best_scoreViev.setText(best_score + "");
                randomArrow = (int) (Math.random() * 4);
                screen.setText(directionViev[randomArrow]);
            }
        }

        return true;
    }


    public void save(Integer best_score) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/" + FILENAME);
        try {
            String str = best_score.toString();
            try {
                FileWriter fWr = new FileWriter(file);
                fWr.write(str);
                fWr.flush();
                fWr.close();
            } catch (Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage() + " Throwable 1\nМетод load", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e1) {
            Toast.makeText(getApplicationContext(), e1.getMessage() + " Exception\nМетод load", Toast.LENGTH_LONG).show();
        }
    }

    public Integer load() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/" + FILENAME);

        if (!file.exists()) {
            try {
                String str = "0";
                file.createNewFile();
                try {
                    FileWriter fWr = new FileWriter(file);
                    fWr.write(str);
                    fWr.flush();
                    fWr.close();
                } catch (Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage() + " Throwable 1\nМетод load", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e1) {
                Toast.makeText(getApplicationContext(), e1.getMessage() + " Exception\nМетод load", Toast.LENGTH_LONG).show();
            }
            return 0;
        } else {
            try {
                FileReader fRd = new FileReader(file);
                BufferedReader reader = new BufferedReader(fRd);
                String str;
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                fRd.close();
//                    text.setText("Бюджет - " + buffer.toString() + "р.");
                Log.i(LOG_TAG, buffer.toString());
                return Integer.parseInt(buffer.toString());
            } catch (Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage() + " Throwable 2\nМетод load", Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }

}
