package com.handsoap.voicial;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;

public class VoicialReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		
		if (bundle != null) {
			Object[] pdus = (Object[])bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				if (i == 0) {
					str += msgs[i].getDisplayOriginatingAddress() + " ";
				}
				
				str += msgs[i].getMessageBody().toString();
			}
		}
		
		this.abortBroadcast();		
	}
}
