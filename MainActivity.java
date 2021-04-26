package com.example.drawnguess;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //for timer
    private long startTime = 600000;
    private boolean timerOn;
    private CountDownTimer countDT;
    private long msecLeft = startTime;

    //declare buttons
    TextView timeTextView;
    Button startButton;
    Button resetButton;
    Button incButton;
    Button decButton;
    Button clrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add PaintView
        PaintView view=new PaintView(this);
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.mainAct);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.leftMargin = 100;

        mRelativeLayout.addView(view, params);

        //assign buttons
        timeTextView = findViewById(R.id.time_Text_View);
        startButton = findViewById(R.id.start_Button);
        resetButton = findViewById(R.id.reset_Button);
        incButton = findViewById(R.id.increment_Button);
        decButton = findViewById(R.id.decrement_Button);
        clrButton = findViewById(R.id.clear_Button);

        incButton.setVisibility(View.INVISIBLE); //timer cannot go above 60 seconds

        //set onClickListeners
        clrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear screen
                view.clear();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerOn) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                resetTimer();
            }
        });

        incButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View V) {
               incrementTimer();
           }
        });

        decButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                decrementTimer();
            }
        });

        timeTextView.setText("60");
    }

    private void startTimer() {
        //create timer object using CountDownTimer
        countDT = new CountDownTimer(msecLeft, 1000){
            @Override
            public void onTick(long millisUntilFinished){
                //onTick occurs every 1000 miliseconds, as defined by countDownInterval
                msecLeft = millisUntilFinished;
                updateTimer();
                incButton.setVisibility(View.INVISIBLE);
                decButton.setVisibility(View.INVISIBLE);
            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            public  void onFinish(){ //timer reached 0
                timerOn = false;
                startButton.setText("start");
                startButton.setVisibility(View.INVISIBLE);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 3000 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(3000);
                }

            }
        }.start();
        timerOn = true;
        startButton.setText("pause");
    }

    private void pauseTimer() {
        countDT.cancel(); //stop timer
        timerOn = false;
        startButton.setText("start");
        incButton.setVisibility(View.VISIBLE);
        decButton.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        //resets timer
        msecLeft = startTime;
        incButton.setVisibility(View.VISIBLE);
        updateTimer();
        startButton.setVisibility(View.VISIBLE);
        decButton.setVisibility(View.VISIBLE);
    }

    private void updateTimer() {
        //updates timer's text view
        int sec = (int) (msecLeft/1000) % 60; // convert milliseconds to seconds
        // store sec as string for use in setText()
        String timeLeft = String.format(Locale.getDefault(),"%02d",sec);
        timeTextView.setText(timeLeft);
        if(msecLeft == 60000){
            timeTextView.setText("60");
            incButton.setVisibility(View.INVISIBLE);
        }
    }

    private void incrementTimer() {
        //increments timer by 5 seconds
        if(msecLeft < 60000) {
            msecLeft += 5000;
            startTime += 5000;
        }
        if(msecLeft > 60000){
            msecLeft = 60000;
            startTime = 60000;
        }
        updateTimer();
    }

    private void decrementTimer() {
        //decrements timer by 5 seconds
        if(msecLeft > 0){
            msecLeft -= 5000;
            startTime -= 5000;
        }
        if(msecLeft < 0){
            msecLeft = 0;
            startTime = 0;
        }
        updateTimer();
        incButton.setVisibility(View.VISIBLE);
    }
}






// reference: https://codinginflow.com/tutorials/android/countdowntimer/part-1-countdown-timer
