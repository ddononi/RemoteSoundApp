package kr.co.rs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * ����Ʈ ������ ������ ������ uri �������� �̿��� ���� �������� ����Ѵ�.
 */
public class SoundPlayActivity extends Activity {
	private VideoView videoView; // ������ ���� ��
	private MediaController controller; // ��� ��Ʈ�ѷ�

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
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