package com.handsoap.voicial;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;

public class VoicialReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (MainActivity.tts != null) {
		
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
						MainActivity.tts.speak("Message received from" + getContactDisplayNameByNumber(str), 1, null);
					}
					
					str += msgs[i].getMessageBody().toString();
				}
			}
			
			this.abortBroadcast();
		}
	}
	
	public String getContactDisplayNameByNumber(String number) {
	    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    String name = "Unknown";

	    ContentResolver contentResolver = MainActivity.myContext.getContentResolver();
	    Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
	        }
	        
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }

	    return name;
	}
}
