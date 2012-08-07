package kr.co.rs;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyService extends Service implements Runnable{
	// 내 담벼락 게시물 정보가 들어있는 uri
	private static final String UPDATE_CHECK_URL = WebClientActivity.HOME_PAGE_URL + "check.php";
	private SharedPreferences mPrefs;			// 공유환경 설정
	private final Handler handler = new Handler();	// 업데이트 핸들러
	private int repeatTime;						// 업데이트 주기
	private int lastestIdx;

	/** 서비스가 실행될때  */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		mPrefs = getSharedPreferences("homepage", MODE_PRIVATE);
		lastestIdx = mPrefs.getInt("idx", -1);
		// setting 에서 업데이트 주기를 가져온다.
		SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		repeatTime = Integer.valueOf(defaultSharedPref.getString("repeat", "10"));

		// 일정간격으로 아이디값 추출
	//	handler.postDelayed(this, 1000 * 6 * repeatTime );
		handler.postDelayed(this, 1000 * 5 );
		Log.d("homepage", "starting service!!");
		return 0;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("homepage", "stop!");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void run() {
		Vector<NameValuePair> vars = new Vector<NameValuePair>();
		try {
			// HTTP post 메서드를 이용하여 데이터 업로드 처리
            vars.add(new BasicNameValuePair("lastest_idx", String.valueOf(lastestIdx)));
            HttpPost request = new HttpPost(UPDATE_CHECK_URL);
			UrlEncodedFormEntity entity = null;
			entity = new UrlEncodedFormEntity(vars, "UTF-8");
			request.setEntity(entity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = new DefaultHttpClient();
            final String responseBody = client.execute(request, responseHandler);	// 전송
        	  Log.i("homepage", responseBody.trim());
            if (responseBody.trim().contains("ok")) {	// 정상 메세지일때만 종료

            	  String[] arr =responseBody.trim().split(",");
            	  // 최신 번호를 공유환경 설정에 저장한다.
            	  int lastIdx = Integer.valueOf(arr[1]);
            	  if(lastIdx != lastestIdx && lastestIdx != -1){
	            	  // 이전인덱스값보다 새로운 가져온 인덱스 값이 더 크면 새로운 글이 등록됬다고 판단한다.
	            	  if(lastestIdx < lastIdx){
	            		  lastestIdx = lastIdx;
	            		  // 공유 환경 설정에 새로운 인덱스값을 저장한다.
	    				  SharedPreferences.Editor editor = mPrefs.edit();
	    				  editor.putInt("idx", lastestIdx);
	    				  editor.commit();
	    				  // 브로드캐스팅 처리
	    				  Intent i = new Intent(getBaseContext(), MyReceiver.class);
	    			      //i.putExtra("id",checkUpdateWall());	// 아이디값도 같이 보낸다.
	    				  PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
	    						0,  i, PendingIntent.FLAG_CANCEL_CURRENT);
	    				  sender.send();		// 브로드케스팅
	            	  }
            	  }
            }

            handler.postDelayed(this, 1000 * 5 );
        } catch (ClientProtocolException e) {
        	Log.e("homepage", "ClientProtocolException: ", e);
        } catch (IOException e) {
        	Log.e("homepage", "IOException: ", e);
		} catch (Exception e) {
			Log.e("homepage", "Exception", e);
		}

	}

}
