package kr.co.rs;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RemoteSoundAppActivity extends Activity implements
	OnClickListener, OnPreparedListener, OnBufferingUpdateListener,
	OnCompletionListener, OnErrorListener {

	private MediaPlayer mediaPlayer;

	private Button stopButton;
	private Button startButton;
	private TextView bufferValueView;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        stopButton = (Button)findViewById(R.id.stop_button);
        startButton = (Button)findViewById(R.id.star_button);
        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        stopButton.setEnabled(false);
        startButton.setEnabled(false);

        bufferValueView = (TextView)findViewById(R.id.buffer_value_text_view);


        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);

        try{
        	mediaPlayer.setDataSource("http://ddononi.cafe24.com/tes.mp3");
        	mediaPlayer.prepareAsync();
        }catch(IOException e){
        	Log.v("AUDIO", e.getMessage());

        }
    }


	public void onClick(final View v) {
		if(v == stopButton){
			mediaPlayer.pause();
			startButton.setEnabled(true);
		}else if(v == startButton){
			mediaPlayer.start();
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		}
	}


	public boolean onError(final MediaPlayer arg0, final int arg1, final int arg2) {
		// TODO Auto-generated method stub
		return false;
	}


	public void onCompletion(final MediaPlayer mp) {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
	}


	public void onBufferingUpdate(final MediaPlayer mp, final int percent) {

        bufferValueView.setText("percent --> " + percent);
	}


	public void onPrepared(final MediaPlayer mp) {
		startButton.setEnabled(true);
	}
}