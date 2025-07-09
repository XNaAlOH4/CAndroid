package com.example.candroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var textCurrentTime: TextView
    private lateinit var textTotalTime: TextView
    private lateinit var buttonPlay: ImageView
    private lateinit var buttonPause: ImageView
    private lateinit var buttonStop: ImageView
    private lateinit var a2048Button: ImageView

    private var handler: Handler = Handler(Looper.getMainLooper())

    private var updateSeekBar: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition())
                textCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()))
                handler.postDelayed(this, 1000)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        textCurrentTime = findViewById(R.id.textCurrentTime)
        textTotalTime = findViewById(R.id.textTotalTime)
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonStop = findViewById(R.id.buttonStop)
        a2048Button = findViewById(R.id.a2048Button)

        a2048Button.setOnClickListener({v ->
            startActivity(Intent(this, a2048::class.java))
        })

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.setOnPreparedListener({ mp ->
            seekBar.setMax(mp.getDuration())
            textTotalTime.setText(formatTime(mp.getDuration()))
        })

        buttonPlay.setOnClickListener({v ->
            mediaPlayer.start()
            handler.post(updateSeekBar)
        })

        buttonPause.setOnClickListener({v -> mediaPlayer.pause()})

        buttonStop.setOnClickListener({v ->
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(this, R.raw.sound)
            seekBar.setProgress(0)
            textCurrentTime.setText("0:00")
            textTotalTime.setText(formatTime(mediaPlayer.getDuration()))
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar:SeekBar, progress: Int, fromUser: Boolean) {
                if(fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress)
                    textCurrentTime.setText(formatTime(progress))
                }
            }

            override fun onStartTrackingTouch(seekBar:SeekBar) {}

            override fun onStopTrackingTouch(seekBar:SeekBar) {}
        })
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(millis: Int) : String {
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes((millis as Number).toLong())
        val sec: Long = TimeUnit.MILLISECONDS.toSeconds((millis as Number).toLong()) % 60
        return String.format("%d:%02d", minutes, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSeekBar)
        if(mediaPlayer != null) {
            mediaPlayer.release()
        }
    }
}