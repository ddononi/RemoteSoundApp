package kr.co.rs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadCastReceiver extends BroadcastReceiver implements iConstant{
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals(BOOT_REVICER)) {
			Log.i("BOOTSVC", "Intent received");
			context.startService(new Intent(context, SoundCheckService.class));
			/*
			if (svcName == null) {
				Log.e("BOOTSVC", "Could not start service " + cn.toString());
			}
			*/

		}
	}
}
