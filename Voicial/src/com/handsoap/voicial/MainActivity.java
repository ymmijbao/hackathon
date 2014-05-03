package com.handsoap.voicial;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements MySpeechRecognizer.ContinuousRecognizerCallback {
	private boolean is_sending_txt = false;
	
	/* User commands must have these following prefixes. */
	private static final String SEND = "send message to";
	private static final String READ = "read message from";
	private static final String CALL = "call";  
	
	public Button mListenButton;
	public boolean bIsListening = false;
	public TextView mResultTextView;
	private MySpeechRecognizer mContinuousRecognizer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.continuous_speech, menu);
		return true;
	}

	@Override
	public void onResult(String result) {	
		if (is_sending_txt) {
			
		} else {
			result = result.toLowerCase();
			if (result.startsWith(READ)) {
				System.out.println("result starts with read!");
				mContinuousRecognizer.stopListening();
			} else if (result.startsWith(SEND)) {
				System.out.println("result starts with send!");
				is_sending_txt = true;
			} else if (result.startsWith(CALL)) {
				System.out.println("result starts with call!");
				mContinuousRecognizer.stopListening();
			}
		}	
	}
}
