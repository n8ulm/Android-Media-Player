package edu.lhup.com.advancedaudio;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Audio extends Activity {

    private final String TAG = "COMP225";

    final MediaPlayer aPlayer = new MediaPlayer();
    SeekBar seekBar;
    ImageButton playButton, pauseButton, stopButton;
    private boolean prepared = false;
    private Timer time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_audio);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_audio);



        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "AdvanceAudio: play - prepared = " + prepared);
                if (prepared)
                    aPlayer.start();
                else {
                    //Toast toast = new Toast.makeText(getApplicationContext(), "Please Wait!", Toast.LENGTH_LONG);
                    //toast.show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "AdvanceAudio: play - prepared = " + prepared);
                aPlayer.stop();
                finish();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "AdvanceAudio: play - prepared = " + prepared);
                if (prepared && aPlayer.isPlaying())
                    aPlayer.pause();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                   int current = seekBar.getProgress();
                   aPlayer.seekTo(current);
               }
           });


        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.linkshouse);

        aPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "OnPrepared Called");
                prepared = true;
                seekBar.setMax(mp.getDuration());
                seekBar.setMin(0);
            }
        });

        try {
            aPlayer.setDataSource(afd);
            aPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d(TAG, "Exception when seeting dataSource");
            Log.d(TAG, e.getMessage());
        } catch (Exception e){
            Log.d(TAG, "Audio: caught exception - " + e.getMessage());
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(aPlayer.isPlaying()){
                    seekBar.setProgress(aPlayer.getCurrentPosition());
                }
            }
        };

        time = new Timer();
        time.schedule(task, 0, 500);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (aPlayer != null) {
            aPlayer.release();
            time.cancel();
        }
    }
}


