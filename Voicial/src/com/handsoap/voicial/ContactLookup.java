package com.handsoap.voicial;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactLookup {
		
	public static String lookUp(String name, Context context) {
	    String number = null;	
	    ContentResolver cr = context.getContentResolver();
		  
	    String[] nameStrings = name.split(" ");
	    name = "";
		  
	    for (String partName : nameStrings) {
	    	name += partName.substring(0, 1).toUpperCase() + partName.substring(1) + " ";
	    }
		  
	    name = name.trim();
		  
	    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + name + "'", null, null);
		  
	    if (cursor.moveToFirst()) {
	    	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	    	Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
		
	    	while (phones.moveToNext()) {
	    		number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
	    		int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
				  
				  /**
				  switch (type) {
				      case Phone.TYPE_HOME:
				    	  break;
				      case Phone.TYPE_MOBILE:
				    	  
				      case Phone.TYPE_WORK:
				    	  break;
				  }
				  **/
	    	}
			  
	    	phones.close();
	         
	    }
	    
	    cursor.close();
	    
	    return number;
	}
}