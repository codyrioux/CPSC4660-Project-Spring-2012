package ca.uleth.cpsc3770;

import java.util.UUID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import android.util.Log;

public class ExperimentRepository extends SQLiteOpenHelper
{
	
	public static final int DATABASE_VERSION = 1;
	public static final String TAG = "ExperimentRepository";
	
	public ExperimentRepository(Context context, String name)
	{
		super(context, name, new ExperimentCursor.Factory(), DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTables(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{	
		Log.i(TAG, "Database onUpgrade method ran to completion.");
	}
	
	private void createTables(SQLiteDatabase db) {
		String resultCreateSQL = "CREATE TABLE TestResult("
							   + "ResultID INTEGER PRIMARY KEY AUTOINCREMENT, "
							   + "TestType INTEGER NOT NULL, "
							   + "SubjectId TEXT NOT NULL, "
							   + "Distance INTEGER NOT NULL, "
							   + "TimeDiff REAL NOT NULL, "
							   + "StringLength INTEGER NOT NULL, "
							   + "TestOrder INTEGER NOT NULL);";
		db.execSQL(resultCreateSQL);
		Log.i(TAG, "Database createTables method ran to completion.");
	}
	
	public ExperimentCursor getExperiments() {
		String sql = "select * from TestResult";
		SQLiteDatabase d = getReadableDatabase();
		ExperimentCursor c = (ExperimentCursor) d.rawQueryWithFactory(new ExperimentCursor.Factory(), sql, null, "TestResult");
		c.moveToFirst();
		return c;
	}
	
	public static class ExperimentCursor extends SQLiteCursor {
		private ExperimentCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}
		
		private static class Factory implements SQLiteDatabase.CursorFactory {
			public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
				return new ExperimentCursor(db, driver, editTable, query);
			}
		}
	}
	
	public void insertResult(SQLiteDatabase db, int testType, UUID subjectId, int distance, long timeDiff, int sLength, int order) {
		ContentValues values = new ContentValues();
		values.put("TestType", testType);
		values.put("SubjectId", subjectId.toString());
		values.put("Distance", distance);
		values.put("TimeDiff", timeDiff);
		values.put("StringLength", sLength);
		values.put("TestOrder", order);
		db.insert("TestResult", null, values);
		db.close();
	}
}
