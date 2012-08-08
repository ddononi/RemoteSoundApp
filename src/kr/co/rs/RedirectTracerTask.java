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
 * ������ uri �����̷�Ʈ ó�� Ŭ����
 * Ʈ��ŷ �� ���� ������ ���� �̵���� url �����̷�Ʈó���� ����
 * url�� ���� �������� ã�Ƴ��� ���� url ��θ� ã�´�.
 */
public class RedirectTracerTask extends AsyncTask<Uri, Void, Uri> {
	private final VideoView mVideo;
	private Uri initiaUri;


	public RedirectTracerTask(final VideoView video){
		super();
		mVideo = video;
	}

	/**
	 * http �ش��κ��� ���� ��ġ�� ����
	 * ������ uri�� �����̷�Ʈ�� ���� ��� ��׶��� �۾��� null�� ��ȯ�ϰ�
	 * ������ VideoView�δ� ���� Uri�� �Ѱ��ش�.
	 */
	@Override
	protected Uri doInBackground(final Uri... params) {
		initiaUri = params[0];
		String redirected = null;
		try{
			URL url = new URL(initiaUri.toString());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// ������� ���� �̾������� Ȯ���Ѵ�.
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



