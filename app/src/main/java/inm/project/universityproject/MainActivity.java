package inm.project.universityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button start, pause, stop;
    TextView tv, tv2;
    String time;
    int minutes, seconds, time_full;
    int check = 0, secs;
    boolean click = false;

    private Handler mainHandler = new Handler();

    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.buttonStart);
        pause = findViewById(R.id.buttonPause);
        stop = findViewById(R.id.buttonStop);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);

        start.setOnClickListener(onClickListener);
        pause.setOnClickListener(onClickListener);
        stop.setOnClickListener(onClickListener);

        time = tv.getText().toString();
        String[] array = time.split(":");
        //minutes = Integer.valueOf(array[0]);
        //seconds = Integer.valueOf(array[1]);
        minutes = 0;
        seconds = 49;
        time_full = minutes * 60 + seconds;
        secs = time_full * 10;

        tv2.setText(minutes + ":" + seconds);
    }

    public void startThread() {
        stopThread = false;
        //int secs = minutes * 60;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < time_full; i++) {
                    if(stopThread) {return;}
                    Log.d(TAG, "startThread: " + i);
                    try {
                        Thread.sleep(1000);
                        if(seconds / 10 == 0 && seconds != 0) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(minutes + ":0" + seconds);
                                }
                            });
                        }
                        else{
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(minutes + ":" + seconds);
                                }
                            });
                        }
                        time_full = time_full - 1;
                        minutes = time_full / 60;
                        seconds = time_full - minutes * 60;
                        if(seconds == 0 && minutes == 0){
                            stopThread();
                            tv2.setText("stop");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void startThreadNew() {
        stopThread = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < secs; i++) {
                    if(stopThread) {return;}
                    Log.d(TAG, "startThread: " + i);
                    try {
                        check = check + 1;
                        Thread.sleep(100);
                        if(seconds / 10 == 0 && seconds != 0) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(minutes + ":0" + seconds);
                                    tv2.setText(String.valueOf(check));
                                }
                            });
                        }
                        else if(seconds == 0){
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(minutes + ":00");
                                    tv2.setText(String.valueOf(check));
                                }
                            });
                        }
                        else{
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(minutes + ":" + seconds);
                                    tv2.setText(String.valueOf(check));
                                }
                            });
                        }
                        //secs = secs - 1;
                        if(check % 10 == 0) {
                            time_full = time_full - 1;
                            minutes = time_full / 60;
                            seconds = time_full - minutes * 60;
                        }
                        if(seconds == 0 && minutes == 0){
                            stopThread();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv2.setText("stop");
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopThread() {
        stopThread = true;

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.buttonStart:
                    if(!click) {
                        startThreadNew();
                        click = true;
                    }
                    break;
                case R.id.buttonPause:
                    break;
                case R.id.buttonStop:
                    stopThread();
                    click = false;
                    break;
            }
        }
    };
}