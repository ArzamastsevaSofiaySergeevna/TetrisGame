package com.example.tetrisgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tetrisgame.models.GameModelFactory;
import com.example.tetrisgame.models.GameType;
import com.example.tetrisgame.presenters.GamePresenter;
import com.example.tetrisgame.presenters.GameTurn;
import com.example.tetrisgame.views.GameFrame;
import com.example.tetrisgame.views.GameViewFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MediaPlayer mPlayer;
    Button playButton, pauseButton, stopButton;
    SeekBar volumeControl;
    AudioManager audioManager;
    GamePresenter gamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMediaPlayer();

        initGame();

        initVolumeControl();
    }

    private void initMediaPlayer() {
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.setOnCompletionListener(mp -> stopPlay());

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private void initGame() {
        GameFrame gameFrame = findViewById(R.id.game_container);
        TextView gameScoreText = findViewById(R.id.game_score);
        TextView gameStatusText = findViewById(R.id.game_status);
        Button gameCtlBtn = findViewById(R.id.game_ctl_btn);
        gamePresenter = new GamePresenter();
        gamePresenter.setGameModel(GameModelFactory.newGameModel(GameType.TETRIS));
        gamePresenter.setGameView(GameViewFactory.newGameView(gameFrame, gameScoreText, gameStatusText, gameCtlBtn));
        Button upBtn = findViewById(R.id.up_btn);
        Button downBtn = findViewById(R.id.down_btn);
        Button leftBtn = findViewById(R.id.left_btn);
        Button rightBtn = findViewById(R.id.right_btn);
        Button fireBtn = findViewById(R.id.fire_btn);
        upBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.UP));
        downBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.DOWN));
        leftBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.LEFT));
        rightBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.RIGHT));
        fireBtn.setOnClickListener(v -> gamePresenter.turn(GameTurn.FIRE));
        gameCtlBtn.setOnClickListener(v -> gamePresenter.changeStatus());
        gamePresenter.init();
    }

    private void initVolumeControl() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeControl = findViewById(R.id.volumeControl);

        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curValue);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void stopPlay() {
        mPlayer.stop();
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
            playButton.setEnabled(true);
        } catch (Throwable t) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void play(View view) {
        mPlayer.start();
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void pause(View view) {
        mPlayer.pause();
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void stop(View view) {
        stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                stopPlay();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }
}
