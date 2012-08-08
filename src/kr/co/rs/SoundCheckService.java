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
	private SharedPreferences mPrefs; // ����ȯ�� ����
	private final Handler handler = new Handler(); // ������Ʈ �ڵ鷯

	// url source
	private URL url;
	private final InputStream is = null;
	private final DataInputStream dis = null;

	private String oldIdx; 	// ���� �ε��� ��
	private String newIdx;	// ���� ���� �ε�����

	/** ���񽺰� ����ɶ� */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		/*
		 * ���� ȯ�� �������� �׼��� ��ū ��������
		 */
		mPrefs = getSharedPreferences("video", MODE_PRIVATE);
		oldIdx = mPrefs.getString("idx", "");
		// �ȵ���̵� 3.0 ���ʹ� ���� ������� ��Ʈ��ũ �۾��� ������ �ʴ´�.
		// ���� ���ξ����忡���� ��Ʈ��ũ �۾��� �Ҽ� �ֵ��� ��������å�� ��� ����Ѵ�.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);

		// ������������  ����
		handler.postDelayed(this, 1000 * 6);
		return 0;
	}

	/**
	 * url �� ������ �����´�.
	 */
	private String getUploadCheck() {
		try {
			// http get ������� url���� ������ �����´�.
            HttpGet request = new HttpGet(REQUEST_URL);
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responseBody = client.execute(request, responseHandler);	// ����
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
			String[] arr = getUploadCheck().trim().split(",");	// �ֱ� ���̵� ��������
			newIdx = arr[0];
			filename = arr[1];
			// �ε������� ������ ���ο� ������ ������ �ʾҴ�.
			if (oldIdx.equals(newIdx)) {
				return;
			} else {
				// ����ҿ� �ε����� �����صд�.
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("idx", newIdx);
				Log.d("video", "do receiver");
				editor.commit();	// Ŀ���� �ؾ� ���� ó���� �ȴ�.
				if(oldIdx.equals("")){	// ���� �ε��� ���� ������ ù �� �����̶� �Ǵ�
					oldIdx = newIdx;
					return;

				}
				oldIdx = newIdx;	// �ε����� ����
				// ��ε��ɽ�Ʈ ���ù��� ���� �ҵ�����Ʈ, ���� �ҵ�����Ʈ�� ������ ����ϰ� ���� ����
				Intent intent = new Intent(getBaseContext(), SoundCheckReceiver.class);
				intent.putExtra("filename", filename);
				Log.i("soundcheck", filename);
				// i.putExtra("id",checkUpdateWall()); // ���̵𰪵� ���� ������.
				// ��ε� ĳ������ ���ý� ������ ����Ʈ�� ����
				PendingIntent sender = PendingIntent.getBroadcast(
						getBaseContext(), 0, intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
				sender.send(); // ��ε��ɽ���
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
