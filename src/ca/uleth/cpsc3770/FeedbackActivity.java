package ca.uleth.cpsc3770;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.uleth.cpsc3770.feedback.R;

public class FeedbackActivity extends Activity {
    
	// Make strings for logging
	private final String TAG = this.getClass().getSimpleName();
	
	// Variables for tracking the current experiment
	private Date startTime;
	private UUID experimentSetId;
	private Boolean experimentStarted = false;
	private int currentExperimentId;
	private int order;
	
	// The collection of possible experiments and pangrams
	private LinkedList<String> pangramList = new LinkedList<String>(); 
	private LinkedList<Boolean[]> experimentList = new LinkedList<Boolean[]>();
	
	// The watchers to hook into the text fields
	Vibrator vibrator;
	AudioTextWatcher audioWatcher;
	VibrationTextWatcher vibrationWatcher;
	TimerTextWatcher timerWatcher;
	
	private Random randomGenerator = new Random();

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.main);
		
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		audioWatcher = new AudioTextWatcher();
		vibrationWatcher = new VibrationTextWatcher(vibrator);
		timerWatcher = new TimerTextWatcher();
		
		resetExperiment();
	}

	private void resetExperiment() {
		Button submitButton = (Button) findViewById(R.id.button1);
		submitButton.setEnabled(true);
		experimentSetId = UUID.randomUUID();
		initializePangramList();
		initializeExperimentList();
		initializeRandomExperiment();
		order = 0;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
        case R.id.exportResults:
        	Export.emailTestResults(this, new ExperimentRepository(this, "ExperimentDB_test1.db"), new String[] { "cody.rioux@gmail.com", "scottcisobrien@gmail.com", "neal.woodruff@gmail.com" });
            return true;
        case R.id.resetExperiment:
        	resetExperiment();
            return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	// ////////////////////////////////////////////////////////////////////////////
	// Event Handlers
	// ////////////////////////////////////////////////////////////////////////////
	
    public void submitClickHandler(View view) {
 
    	EditText inputArea = (EditText) findViewById(R.id.editText1);
    	TextView displayArea = (TextView) findViewById(R.id.pangram);
    	Date endTime = new Date();
    	
    	int distance = Distance.getLevenshteinDistance(inputArea.getText().toString(), displayArea.getText().toString());
    	long timeDiff = endTime.getTime() - startTime.getTime();
    	int length = displayArea.getText().toString().length();
    	
    	Log.i(TAG, "Just before db call.");
    	ExperimentRepository db = new ExperimentRepository(this, "ExperimentDB_test1.db");
    	
    	Log.i(TAG, "Just before db insert.");
    	db.insertResult(db.getWritableDatabase(), currentExperimentId, experimentSetId, distance, timeDiff, length, ++order);
    	
    	if (experimentList.size() > 0) {
    		initializeRandomExperiment();
    	}
    	else {
    		displayArea.setText("Experiments Completed!");
    		inputArea.setText("");
    		Toast.makeText(this, "Experiments Completed", Toast.LENGTH_SHORT).show();
    		Button submitButton = (Button) findViewById(R.id.button1);
    		submitButton.setEnabled(false);
    	}
    }
    
	// ////////////////////////////////////////////////////////////////////////////
	// Utility Functions
	// ////////////////////////////////////////////////////////////////////////////
    
	private void initializeRandomExperiment() {
		int pangramIndex = pangramList.size() > 1 ? randomGenerator.nextInt(pangramList.size()) : 0;
		String pangram = pangramList.get(pangramIndex);
		pangramList.remove(pangramIndex);
		
		int experimentIndex = experimentList.size() > 1 ? randomGenerator.nextInt(experimentList.size()) : 0;
		Boolean[] experiment = experimentList.get(experimentIndex);
		experimentList.remove(experimentIndex);
		
		currentExperimentId = getExperimentId(experiment);
		
		initializeExperiment(experiment[0], experiment[1], pangram);
	}
    
	private void initializePangramList() {	
		pangramList.clear();
		pangramList.add("The wizard quickly jinxed the gnomes before they vaporized");
		pangramList.add("All questions asked by five watched experts amaze the judge");
		pangramList.add("A quick movement of the enemy will jeopardize six gunboats");
		pangramList.add("Just keep examining every low bid quoted for zinc etchings");
		pangramList.add("The quick onyx goblin jumps over the lazy dwarf");
	}
	
	private void initializeExperimentList() {
		experimentList.clear();
		experimentList.add(new Boolean[] { false, false });
		experimentList.add(new Boolean[] { false, true });
		experimentList.add(new Boolean[] { true, false });
		experimentList.add(new Boolean[] { true, true });
	}
    
    private void initializeExperiment(Boolean haptic, Boolean audio, String pangram) {
    	
    	experimentStarted = false;
    	startTime = null;

    	EditText inputArea = (EditText) findViewById(R.id.editText1);
    	inputArea.setText("");
    	inputArea.requestFocus();
    	inputArea.removeTextChangedListener(audioWatcher);
    	inputArea.removeTextChangedListener(vibrationWatcher);
    	inputArea.removeTextChangedListener(timerWatcher);
    	
    	if (haptic) {
    		inputArea.addTextChangedListener(vibrationWatcher);
    	}
    	if (audio) {
    		inputArea.addTextChangedListener(audioWatcher);
    	}
    	inputArea.addTextChangedListener(timerWatcher);
    	
		TextView displayArea = (TextView) findViewById(R.id.pangram);
		displayArea.setText(pangram);
    }
    
	private int getExperimentId(Boolean[] experiment) {
		int experimentId = 0;
		if (experiment[0])
			experimentId += 1;
		
		if (experiment[1])
			experimentId += 2;
		
		return experimentId;
	}
	
	public class TimerTextWatcher extends Activity implements android.text.TextWatcher
	{
		
		public TimerTextWatcher() {}
		public void afterTextChanged(Editable arg0) {}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			if (!experimentStarted) {
				startTime = new Date();
				experimentStarted = true;
			}
		}
	}

}