package com.handsoap.voicial;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextActivity extends Activity implements MySpeechRecognizer.ContinuousRecognizerCallback {

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
		String s = (String) mResultTextView.getText();
		s = s + "\n" + result;
		mResultTextView.setText(s);
	}
}
