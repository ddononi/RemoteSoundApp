package kr.co.rs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class SoundCheckService extends Service implements Runnable, iConstant {
	private SharedPreferences mPrefs; // 공유환경 설정
	private final Handler handler = new Handler(); // 업데이트 핸들러

	// url source
	private URL url;
	private final InputStream is = null;
	private final DataInputStream dis = null;

	private String oldIdx; 	// 이전 인덱스 값
	private String newIdx;	// 새로 받은 인덱스값

	/** 서비스가 실행될때 */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		/*
		 * 공유 환경 설정에서 액세스 토큰 가져오기
		 */
		mPrefs = getSharedPreferences("video", MODE_PRIVATE);
		oldIdx = mPrefs.getString("idx", "");
		// 안드로이드 3.0 부터는 메인 쓰레드로 네트워크 작업이 허용되지 않는다.
		// 따라서 메인쓰레드에서도 네트워크 작업을 할수 있도록 쓰레드정책을 모두 허용한다.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);

		// 일정간격으로  추출
		handler.postDelayed(this, 1000 * 6);
		return 0;
	}

	/**
	 * url 의 내용을 가져온다.
	 */
	private String getUploadCheck() {
		try {
			// http get 방식으로 url에서 내용을 가져온다.
            HttpGet request = new HttpGet(REQUEST_URL);
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responseBody = client.execute(request, responseHandler);	// 전송
            return responseBody.trim();
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if(is != null){
					is.close();
				}
			} catch (IOException ioe) {
			}
		}
		return "";
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("dservice", "stop!");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		String filename = "";
		try {
			String[] arr = getUploadCheck().trim().split(",");	// 최근 아이디값 가져오기
			newIdx = arr[0];
			filename = arr[1];
			// 인덱스값이 같으면 새로운 파일이 들어오지 않았다.
			if (oldIdx.equals(newIdx)) {
				return;
			} else {
				// 저장소에 인덱스를 저장해둔다.
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("idx", newIdx);
				Log.d("video", "do receiver");
				editor.commit();	// 커밋을 해야 저장 처리가 된다.
				if(oldIdx.equals("")){	// 이전 인덱스 값이 없으면 첫 앱 실행이라 판단
					oldIdx = newIdx;
					return;

				}
				oldIdx = newIdx;	// 인덱스값 갱신
				// 브로드케스트 리시버에 보낼 팬딩인텐트, 이전 팬딩인텐트가 있으면 취소하고 새로 실행
				Intent intent = new Intent(getBaseContext(), SoundCheckReceiver.class);
				intent.putExtra("filename", filename);
				Log.i("soundcheck", filename);
				// i.putExtra("id",checkUpdateWall()); // 아이디값도 같이 보낸다.
				// 브로드 캐스팅후 선택시 실행할 인텐트를 설정
				PendingIntent sender = PendingIntent.getBroadcast(
						getBaseContext(), 0, intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
				sender.send(); // 브로드케스팅
			}
		}catch(ArrayIndexOutOfBoundsException aiobe){
			aiobe.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			handler.postDelayed(this, 1000 * 6);
		}

	}


}
