package ca.uleth.cpsc3770;

import android.app.Activity;
import android.text.Editable;
import android.media.AudioManager;
import android.media.ToneGenerator;

public class AudioTextWatcher extends Activity implements android.text.TextWatcher 
{
	private ToneGenerator generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
	private int tone = 0;
	
	public AudioTextWatcher()
	{
		super();
		this.tone = 16;
	}
	
	public AudioTextWatcher(int tone)
	{
		super();
		this.tone = tone % 19;
	}

	public void afterTextChanged(Editable s) {
		playAudio();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	private void playAudio()
	{
		try
		{
			generator.startTone(tone, 100);
		}
		catch (Exception ex)
		{
			generator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
		}
	}
}
