package com.handsoap.voicial;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements MySpeechRecognizer.ContinuousRecognizerCallback {
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
	private StringBuilder text_buffer = new StringBuilder(); 
	
	public Button mListenButton;
	public boolean bIsListening = false;
	public TextView mResultTextView;
	private MySpeechRecognizer mContinuousRecognizer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text); // Change this later 

		mContinuousRecognizer = new MySpeechRecognizer(getApplicationContext());
		mContinuousRecognizer.setContinuousRecognizerCallback(this);

		mResultTextView = (TextView) findViewById(R.id.resultText);
		mListenButton = (Button) findViewById(R.id.listenButton);
		mListenButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bIsListening = !bIsListening;
				if (bIsListening) {
					mListenButton.setText("Stop");
					mContinuousRecognizer.startListening();
				} else {
					mListenButton.setText("Listen");
					mContinuousRecognizer.stopListening();
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		mContinuousRecognizer.stopListening();
		bIsListening = false;
		mListenButton.setText("Listen");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mContinuousRecognizer.startListening();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.continuous_speech, menu);
		return true;
	}

	@Override
	public void onResult(String result) {	
		if (is_sending_txt) {
			if (result.equals(END_TXT_CMD)) {
				SmsManager.getDefault().sendTextMessage(cur_num, null, text_buffer.toString(), null, null);
				text_buffer = new StringBuilder();
				cur_num = "";
				is_sending_txt = false;
			} else {
				System.out.print(result);
				text_buffer.append(result + " ");
			}
		} else {			
			if (result.startsWith(READ_TXT_CMD)) {
				System.out.println("result starts with read!");
				String name = result.substring(READ_TXT_OFFSET);
				String number = ContactLookup.lookUp(name, getApplicationContext());
				
				if (number != null) {
					mContinuousRecognizer.stopListening();
					// Do something
				}
				
			} else if (result.startsWith(SEND_TXT_CMD)) {
				System.out.println("result starts with send!");
				String name = result.substring(SEND_TXT_OFFSET);
				String number = ContactLookup.lookUp(name, getApplicationContext());
				
				if (number != null) {
					is_sending_txt = true;
					cur_num = number;
				}
			} else if (result.startsWith(CALL_CMD)) {
				System.out.println("result starts with call!");
				String name = result.substring(CALL_OFFSET);
				String number = ContactLookup.lookUp(name, getApplicationContext());
				
				if (number != null) {
					mContinuousRecognizer.stopListening();
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+number));
					startActivity(callIntent);
				}
			}
		}	
	}
}
