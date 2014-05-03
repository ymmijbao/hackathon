package com.handsoap.voicial;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class MySpeechRecognizer implements RecognitionListener {
	
	public static final String TAG = "ContinuousRecognizer";
	private Context mContext;
	private SpeechRecognizer mRecognizer;
	private ContinuousRecognizerCallback mCallback;
	
	public interface ContinuousRecognizerCallback {
		void onResult(String result);
	}

	public MySpeechRecognizer(Context context) {
		mContext = context;
		mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
		mRecognizer.setRecognitionListener(this);
	}

	public void setContinuousRecognizerCallback(ContinuousRecognizerCallback cb) {
		mCallback = cb;
	}


	public void startListening() {
        listen();
	}

	public void stopListening() {
		mRecognizer.stopListening();
		mRecognizer.cancel();
	}

	private void listen() {
		mRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(mContext));
	}

	@Override
	public void onBeginningOfSpeech() {
		// AUTO-GENERATED STUB
	}

	@Override
	public void onBufferReceived(byte[] arg0) {
		// AUTO-GENERATED STUB
	}

	@Override
	public void onEndOfSpeech() {
		// AUTO-GENERATED STUB
		System.out.println("Done recording!");
	}

	@Override
	public void onError(int arg0) {
		// AUTO-GENERATED STUB
		Log.d(TAG, "onError");
		Log.d(TAG, "error: " + String.valueOf(arg0));
		
		if ((arg0 == SpeechRecognizer.ERROR_NO_MATCH) || (arg0 == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) {
			mRecognizer.destroy();
			mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
			mRecognizer.setRecognitionListener(this);
			listen();
		}
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		// AUTO-GENERATED STUB
	}

	@Override
	public void onPartialResults(Bundle arg0) {
		// AUTO-GENERATED STUB
	}

	@Override
	public void onReadyForSpeech(Bundle arg0) {
		// AUTO-GENERATED STUB
	}

	@Override
	public void onResults(Bundle results) {	
		ArrayList<String> strings = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		for (String str : strings) {
			Log.d(TAG, "result=" + str);
		}

		// Google returns the best result as the first result it seems
		if (mCallback != null) {
			mCallback.onResult(strings.get(0));
		}
		
		listen();
	}

	@Override
	public void onRmsChanged(float arg0) {
		// AUTO-GENERATED STUB
	}
}