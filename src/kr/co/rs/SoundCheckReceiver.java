package kr.co.rs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class SoundCheckReceiver extends BroadcastReceiver {
	private final int YOURAPP_NOTIFICATION_ID = 1;	// �� ���̵�
	private SharedPreferences sp;				// �Ҹ� �� ���� ����
	private String filename;					// ���ε�� ���ϸ�
	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.i("video", "broadcast catch!!");
		filename = intent.getStringExtra("filename");
		sp = PreferenceManager.getDefaultSharedPreferences(context);	// ȯ�漳���� ��������
		showNotification(context, R.drawable.icon32);	// �����ϱ�
	}

	/**
	 *	���¹ٿ� �˶��� �˸��� Ȯ�ν� ��Ƽ��Ƽ  �̵���Ų��.
	 * @param context
	 * @param statusBarIconID
	 * 		���¹ٿ� ��Ÿ�� ������
	 */
	private void showNotification(final Context context, final int statusBarIconID) {
		// MyScheduleActivity �� ��Ƽ��Ƽ ����

		Intent contentIntent = new Intent(context, SoundPlayActivity.class);
		contentIntent.putExtra("filename", this.filename);
		contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	// ���� ��Ƽ��Ƽ�� ��������
		// �˸�Ŭ���� �̵��� ��Ƽ��Ƽ ����
		PendingIntent theappIntent = PendingIntent.getActivity(context, 0,contentIntent, 0);
		CharSequence title = "������ε�"; // �˸� Ÿ��Ʋ
		CharSequence message = "���� ���尡 ���۵Ǿ����ϴ�."; // �˸� ����

		Notification notif = new Notification(statusBarIconID, null,
				System.currentTimeMillis());

		notif.flags |= Notification.FLAG_AUTO_CANCEL;	// Ŭ���� �������
		notif.defaults |= Notification.DEFAULT_LIGHTS;	// led�� Ű��
		//notif.defaults |= Notification.DEFAULT_SOUND;	// �⺻ sound�� Ű��

		//	�����˶��� ���������� ������ �︰��.

		if( sp.getBoolean("vibration", true) ){
			long[] vibrate = {1000, 1000};  // 1�ʰ� 2��
			notif.vibrate = vibrate;
		}

		//	�Ҹ��˶��� ���������� �Ҹ��� �︰��.
		if( sp.getBoolean("sound", true) ){
			notif.sound = Uri.parse("android.resource://"+context.getPackageName()+"/raw/sound");
		}

		notif.flags |= Notification.FLAG_ONLY_ALERT_ONCE;	// �˶��� �ѹ���
		notif.setLatestEventInfo(context, title, message, theappIntent);	// ������ ����
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);	// �����ϱ�
	}
}
