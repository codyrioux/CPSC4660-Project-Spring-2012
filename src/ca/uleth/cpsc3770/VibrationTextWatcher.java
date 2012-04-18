package ca.uleth.cpsc3770;

import android.app.Activity;
import android.os.Vibrator;
import android.text.Editable;

public class VibrationTextWatcher extends Activity implements android.text.TextWatcher
{
	private Vibrator vibrator;
	
	public VibrationTextWatcher(Vibrator vibrator)
	{
		this.vibrator = vibrator;
	}
	
	public void afterTextChanged(Editable arg0) {
		vibrate();
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}
	
	private void vibrate()
	{
			vibrator.vibrate(100);
	}
}
