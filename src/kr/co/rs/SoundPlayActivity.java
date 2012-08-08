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
 * 리스트 아이템 선택후 가져온 uri 정보값을 이용해 원격 동영상을 재생한다.
 */
public class SoundPlayActivity extends Activity {
	private VideoView videoView; // 동영상 비디오 뷰
	private MediaController controller; // 재생 컨트롤럭

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		videoView =(VideoView)findViewById(R.id.video_view); // 동영상을 보여줄 비디오뷰
		Intent intent = getIntent();	// 이전 인텐트
		// 인텐트에서 파일이름을 추출한후 url를 조립하여 경로를 만들어 준다.
		String filePath;
		// 리시버에서 넘어온 파일이 있을때만 소리를 play 시킨다.
		if(intent.hasExtra("filename")){
			String filename = intent.getStringExtra("filename");
			Log.i("soundcheck",filename);
			filePath = "http://ddononi.cafe24.com/videoupload/uploads/" + filename;
			// url의 최종위치를 찾기 위해 asyncTask로 처리
			RedirectTracerTask task = new RedirectTracerTask(videoView);
			Uri location = Uri.parse(filePath);
			task.execute(location);	// thread 처리 실행
			controller = new MediaController(this); // 재생 컨트롤럭
			videoView.setMediaController(controller);	// 동영상 뷰 컨트롤러 포함
			videoView.start();	// 동영상 시작
		}

		doStartService();
	}

	/**
	 * 엑티비티 종료시 동영상 멈추기
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		videoView.stopPlayback();
	}

	private void doStartService() {
		// TODO Auto-generated method stub
		// 서비스로 알람설정
		Intent serviceIntent = new Intent(this, SoundCheckService.class);
		stopService(serviceIntent);		// 우선 서비스가 있을지 모르니 멈춘후
		startService(serviceIntent);	// 서비스 실행
	}


    /** 옵션 메뉴 만들기 */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "옵션").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }

    /** 옵션 메뉴 선택에 따라 해당 처리를 해줌 */
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

	/** 옵션 메뉴 만들기 */
	/*
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "다운로드");
		return true;
	}
	*/
	/** 옵션 메뉴 선택에 따라 해당 처리를 해줌 */
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