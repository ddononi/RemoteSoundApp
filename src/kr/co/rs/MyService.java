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
	// �� �㺭�� �Խù� ������ ����ִ� uri
	private static final String UPDATE_CHECK_URL = WebClientActivity.HOME_PAGE_URL + "check.php";
	private SharedPreferences mPrefs;			// ����ȯ�� ����
	private final Handler handler = new Handler();	// ������Ʈ �ڵ鷯
	private int repeatTime;						// ������Ʈ �ֱ�
	private int lastestIdx;

	/** ���񽺰� ����ɶ�  */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		mPrefs = getSharedPreferences("homepage", MODE_PRIVATE);
		lastestIdx = mPrefs.getInt("idx", -1);
		// setting ���� ������Ʈ �ֱ⸦ �����´�.
		SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		repeatTime = Integer.valueOf(defaultSharedPref.getString("repeat", "10"));

		// ������������ ���̵� ����
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
			// HTTP post �޼��带 �̿��Ͽ� ������ ���ε� ó��
            vars.add(new BasicNameValuePair("lastest_idx", String.valueOf(lastestIdx)));
            HttpPost request = new HttpPost(UPDATE_CHECK_URL);
			UrlEncodedFormEntity entity = null;
			entity = new UrlEncodedFormEntity(vars, "UTF-8");
			request.setEntity(entity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = new DefaultHttpClient();
            final String responseBody = client.execute(request, responseHandler);	// ����
        	  Log.i("homepage", responseBody.trim());
            if (responseBody.trim().contains("ok")) {	// ���� �޼����϶��� ����

            	  String[] arr =responseBody.trim().split(",");
            	  // �ֽ� ��ȣ�� ����ȯ�� ������ �����Ѵ�.
            	  int lastIdx = Integer.valueOf(arr[1]);
            	  if(lastIdx != lastestIdx && lastestIdx != -1){
	            	  // �����ε��������� ���ο� ������ �ε��� ���� �� ũ�� ���ο� ���� ��ω�ٰ� �Ǵ��Ѵ�.
	            	  if(lastestIdx < lastIdx){
	            		  lastestIdx = lastIdx;
	            		  // ���� ȯ�� ������ ���ο� �ε������� �����Ѵ�.
	    				  SharedPreferences.Editor editor = mPrefs.edit();
	    				  editor.putInt("idx", lastestIdx);
	    				  editor.commit();
	    				  // ��ε�ĳ���� ó��
	    				  Intent i = new Intent(getBaseContext(), MyReceiver.class);
	    			      //i.putExtra("id",checkUpdateWall());	// ���̵𰪵� ���� ������.
	    				  PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
	    						0,  i, PendingIntent.FLAG_CANCEL_CURRENT);
	    				  sender.send();		// ��ε��ɽ���
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
