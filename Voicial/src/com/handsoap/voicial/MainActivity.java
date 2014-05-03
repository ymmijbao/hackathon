package com.handsoap.voicial;

import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements MySpeechRecognizer.ContinuousRecognizerCallback, OnInitListener {
	private boolean is_sending_txt = false;
	
	/* User commands must have these following prefixes. */
	private static final String SEND_TXT_CMD = "send message to";
	private static final String READ_TXT_CMD = "read message from";
	private static final String END_TXT_CMD = "message done";
	private static final String CALL_CMD = "call";  
	
	private static final int SEND_TXT_OFFSET = 16;
	private static final int READ_TXT_OFFSET = 18;
	private static final int CALL_OFFSET = 5;
	
	private String cur_num = "";
	private String cur_name = "";
	private StringBuilder text_buffer = new StringBuilder(); 
	
	private HashMap latestIdByNumber = new HashMap();
	
	public Button mListenButton;
	public boolean bIsListening = false;
	public TextView mResultTextView;
	private MySpeechRecognizer mContinuousRecognizer;
	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		/** Hide the unnecessary ActionBar **/
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		mContinuousRecognizer = new MySpeechRecognizer(getApplicationContext());
		mContinuousRecognizer.setContinuousRecognizerCallback(this);
		
		/** Creating the Text-to_Speech object **/
		tts = new TextToSpeech(this, this);
		tts.setLanguage(Locale.US);
	}

	@Override
	public void onPause() {
		super.onPause();
		mContinuousRecognizer.stopListening();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mContinuousRecognizer.startListening();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.continuous_speech, menu);
		return true;
	}

	private String getPhoneNumber(String result, int offset)
	{
		String name = result.substring(offset);
		cur_name = name;
		return ContactLookup.lookUp(name, getApplicationContext());
	}
	
	@Override
	public void onResult(String result) {
	    if (is_sending_txt) {
			if (result.equals(END_TXT_CMD)) {
				String textToSend = "Your message to " + cur_name + "was" + text_buffer.toString() + "was successully sent.";
				SmsManager.getDefault().sendTextMessage(cur_num, null, text_buffer.toString(), null, null);
				tts.speak(textToSend, 0, null);
				text_buffer = new StringBuilder(); 
				cur_num = "";
				is_sending_txt = false;
			} else {
				text_buffer.append(result + " ");
			}
		} else {
			String number = null;
			if (result.startsWith(READ_TXT_CMD)) {
				number = getPhoneNumber(result, READ_TXT_OFFSET);
				if (number != null) {
					
					//mContinuousRecognizer.stopListening();
					Uri uri = Uri.parse("content://sms/inbox");
					String[] projection = new String[]{"_id", "address", "person", "body", "date"};
					
					String selector = "address='+1" + number + "' AND read=0";
					String latestId = (String)latestIdByNumber.get(number);
					if (latestId != null) {
						selector += " AND _id > " + latestId;
					}
					
					Cursor cur = getContentResolver().query(uri, projection, selector, null, null);
					boolean alreadySetId = false;
					if (cur.moveToFirst()) {
						do {
							String address = cur.getString(cur.getColumnIndex("address"));
							String body = cur.getString(cur.getColumnIndex("body"));
							System.out.println(address);
							System.out.println(body);
							tts.speak(body, 1, null);
							
							String SmsMessageId = cur.getString(cur.getColumnIndex("_id"));
							if (!alreadySetId)
							{
								latestIdByNumber.put(number, SmsMessageId);
								alreadySetId = true;
							}
						} while (cur.moveToNext());
					}
					
					//mContinuousRecognizer.startListening();
				}
			} else if (result.startsWith(SEND_TXT_CMD)) {
				number = getPhoneNumber(result, SEND_TXT_OFFSET);
				if (number != null) {
					is_sending_txt = true;
					cur_num = number;
				}
			} else if (result.startsWith(CALL_CMD)) {
				number = getPhoneNumber(result, CALL_OFFSET);
				if (number != null) {
					//mContinuousRecognizer.stopListening();
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+number));
					startActivity(callIntent);
				}
			}
		}	
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
}
