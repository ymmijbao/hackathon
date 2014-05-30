package com.handsoap.voicial;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

public class VoicialCaller extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (MainActivity.tts != null) {
			final Context appContext = context;
			if (!intent.getAction().equals("android.intent.action.PHONE_STATE")) {
				return;
			}
			
			String extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (extraState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				   @Override
				   public void run() {
						Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
						i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
						appContext.sendOrderedBroadcast(i, null);
				   }
				 }, 5000);
			}
		}
	}
}
