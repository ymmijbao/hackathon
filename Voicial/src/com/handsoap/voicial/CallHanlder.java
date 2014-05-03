package com.handsoap.voicial;

import android.content.Intent;
import android.net.Uri;

public class CallHanlder {
	
	public static void call(String number) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+number));
		//startActivity(callIntent);
	}
}
