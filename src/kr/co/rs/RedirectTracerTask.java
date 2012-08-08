/**
 *
 */
package kr.co.rs;

import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.VideoView;

/**
 * 동영상 uri 리다이렉트 처리 클랙스
 * 트레킹 및 보안 목적의 공개 미디어의 url 리다이렉트처리를 위해
 * url의 최종 도착지를 찾아내어 실제 url 경로를 찾는다.
 */
public class RedirectTracerTask extends AsyncTask<Uri, Void, Uri> {
	private final VideoView mVideo;
	private Uri initiaUri;


	public RedirectTracerTask(final VideoView video){
		super();
		mVideo = video;
	}

	/**
	 * http 해더로부터 최종 위치를 추적
	 * 제공된 uri에 리다이렉트가 없는 경우 백그라운드 작업은 null을 반환하고
	 * 끝나며 VideoView로는 원본 Uri를 넘겨준다.
	 */
	@Override
	protected Uri doInBackground(final Uri... params) {
		initiaUri = params[0];
		String redirected = null;
		try{
			URL url = new URL(initiaUri.toString());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// 연결된후 어디로 이어지는지 확인한다.
			redirected = conn.getHeaderField("Location");

			return Uri.parse(redirected);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(final Uri resultUri) {
		if(resultUri != null){
			mVideo.setVideoURI(resultUri);
		}else{
			mVideo.setVideoURI(initiaUri);
		}
	}



}



