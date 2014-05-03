package com.handsoap.voicial;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextActivity extends Activity implements MySpeechRecognizer.ContinuousRecognizerCallback {

	private static final int ON_CMD = (1 << 0);
	private static final int ON_TXT = (1 << 1);
	private static final int ON_CALL = (1 << 2);
	
	private char state = 0;
	
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
		String[] speech_result = result.split(" ");
		if ((speech_result[0].equals("send")) && (speech_result[1].equals("message")) && (speech_result[2].equals("to"))) {
			sendMessage(speech_result); 
		}
		
		
		
		
		/**
		String s = (String) mResultTextView.getText();
		s = s + "\n" + result;
		mResultTextView.setText(s);
		**/
		
		
		
	}
	
	public void readMessage() {
		// TODO
	}
	
	public void sendMessage(String[] result) {
		// TODO
	}
}
