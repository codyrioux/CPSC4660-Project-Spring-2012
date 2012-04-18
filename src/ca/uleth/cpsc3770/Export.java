package ca.uleth.cpsc3770;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

public class Export {
	public static void emailTestResults(Activity activity, ExperimentRepository db, String[] emails)
	{
		Cursor testsCursor = db.getReadableDatabase().rawQuery("select * from TestResult", null);
		testsCursor.moveToFirst();
		
		int columnCount = testsCursor.getColumnCount();
		
		String emailText = "";
		
		String[] names = testsCursor.getColumnNames();
		
		for(int i = 0; i < names.length - 1; ++i) {
			emailText += names[i];
			emailText += ", ";
		}
		emailText += names[names.length - 1];
		emailText += "\r\n";
		
		do {
			for(int i = 0; i < columnCount - 1; ++i) {
				emailText += testsCursor.getString(i);
				emailText += ", ";
			}
			emailText += testsCursor.getString(columnCount - 1);
			emailText += "\r\n";
		} while (testsCursor.moveToNext());
		
		testsCursor.close();
		
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("plain/text");
		email.putExtra(Intent.EXTRA_EMAIL, emails);
		email.putExtra(Intent.EXTRA_SUBJECT, "Test Results");
		email.putExtra(Intent.EXTRA_TEXT, emailText);
		activity.startActivity(Intent.createChooser(email, "Send Email"));
	}
}
