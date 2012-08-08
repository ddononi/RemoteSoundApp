package kr.co.rs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * ����Ʈ ������ ������ ������ uri �������� �̿��� ���� �������� ����Ѵ�.
 */
public class SoundPlayActivity extends Activity implements OnTouchListener {
	private VideoView videoView; // ������ ���� ��
	private MediaController controller; // ��� ��Ʈ�ѷ�
	private ImageView logoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		logoView  = (ImageView)findViewById(R.id.logo);
		videoView =(VideoView)findViewById(R.id.video_view); // �������� ������ ������
		Intent intent = getIntent();	// ���� ����Ʈ
		// ����Ʈ���� �����̸��� �������� url�� �����Ͽ� ��θ� ����� �ش�.
		String filePath;
		// ���ù����� �Ѿ�� ������ �������� �Ҹ��� play ��Ų��.
		if(intent.hasExtra("filename")){
			String filename = intent.getStringExtra("filename");
			Log.i("soundcheck",filename);
			filePath = "http://ddononi.cafe24.com/videoupload/uploads/" + filename;
			// url�� ������ġ�� ã�� ���� asyncTask�� ó��
			RedirectTracerTask task = new RedirectTracerTask(videoView);
			Uri location = Uri.parse(filePath);
			task.execute(location);	// thread ó�� ����
			controller = new MediaController(this); // ��� ��Ʈ�ѷ�
			videoView.setMediaController(controller);	// ������ �� ��Ʈ�ѷ� ����
			videoView.start();	// ������ ����
		}
		logoView.setOnTouchListener(this);
		doStartService();
	}
	
	

	/**
	 * ��Ƽ��Ƽ ����� ������ ���߱�
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		videoView.stopPlayback();
	}

	private void doStartService() {
		// TODO Auto-generated method stub
		// ���񽺷� �˶�����
		Intent serviceIntent = new Intent(this, SoundCheckService.class);
		stopService(serviceIntent);		// �켱 ���񽺰� ������ �𸣴� ������
		startService(serviceIntent);	// ���� ����
	}


    /** �ɼ� �޴� ����� */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "�ɼ�").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }

    /** �ɼ� �޴� ���ÿ� ���� �ش� ó���� ���� */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
    	Intent intent = null;
    	switch(item.getItemId()){
	    	case 1:
	    		intent = new Intent(this, SettingActivity.class);
	    		startActivity(intent);
	    		break;

    	}
    	return false;
    }



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			/*
			if(videoView.isPlaying() == false){
				return false;
			}
			*/
			int position = videoView.getCurrentPosition();
			int width = logoView.getWidth();
			//Toast.makeText(this, event.getX() + "          xx    "  + width , Toast.LENGTH_SHORT).show();
			if(event.getX() <  width / 2){	// ������ ��ġ�� �ڰ���
				videoView.seekTo(position - 500);
			}else{								// ������ ��ġ�� ������ �̵�
				videoView.seekTo(position +  500);
			}
		}
		return false;
	}

	/** �ɼ� �޴� ����� */
	/*
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "�ٿ�ε�");
		return true;
	}
	*/
	/** �ɼ� �޴� ���ÿ� ���� �ش� ó���� ���� */
	/*
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent(this, DownloadActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	*/
}