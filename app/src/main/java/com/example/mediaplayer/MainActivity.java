package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OpenForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView movingDuration,totalDuration;
    SeekBar seekTime,seekVolume;
    ImageView playButton;
    MediaPlayer quranPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        movingDuration =findViewById(R.id.movingDuration);
        totalDuration =findViewById(R.id.totalDuration);
        seekTime =findViewById(R.id.seekTime);
        seekVolume =findViewById(R.id.seekVolume);
        playButton =findViewById(R.id.playButton);

        quranPlayer=MediaPlayer.create(this,R.raw.elinfitar);
        quranPlayer.setLooping(true);
        quranPlayer.seekTo(500);
        quranPlayer.setVolume(0.5f,0.5f);
        String duration = msToString(quranPlayer.getDuration());
        totalDuration.setText(duration);
        playButton.setOnClickListener(this);
        seekVolume.setProgress(50);
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                quranPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekTime.setMax(quranPlayer.getDuration());
        seekTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    quranPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }else{

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(quranPlayer != null){
                    if(quranPlayer.isPlaying()){
                        try {
                            final double current = quranPlayer.getCurrentPosition();
                            final String elapsedTime = msToString((int)current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    movingDuration.setText(elapsedTime);
                                    seekTime.setProgress((int) current);
                                }
                            });


                            Thread.sleep(1000);
                        }catch (InterruptedException e){}
                    }
                }
            }
        }).start();
    }


    public String msToString(int time){
        String elapsedTime="";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime=minutes+":";
        if (seconds < 10){
            elapsedTime+="0"+seconds;
        }else{
            elapsedTime+=seconds;
        }
        return elapsedTime;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playButton){
            if(quranPlayer.isPlaying()){
                quranPlayer.pause();
                playButton.setBackgroundResource(R.drawable.play);
            }else{
                quranPlayer.start();
                playButton.setBackgroundResource(R.drawable.pause);
            }
        }
    }
}