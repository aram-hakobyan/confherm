package fr.conferencehermes.confhermexam.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.conferencehermes.confhermexam.parser.Event;
import fr.conferencehermes.confhermexam.parser.Exam;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "eventManager";

	// Table Names
	private static final String TABLE_EVENTS = "Events";
	private static final String TABLE_EXAMS = "Exams";
	private static final String TABLE_EXERCISE_FILES = "ExerciseFiles";
	private static final String TABLE_EXERCISES = "Exercises";
	private static final String TABLE_QUESTION_FILES = "QuestionFiles";
	private static final String TABLE_QUESTIONS = "Questions";
	private static final String TABLE_USERS = "Users";
	private static final String TABLE_ANSWERS = "Answers";

	// EVENT column names
	private static final String KEY_EVENT_ID = "eventId";
	private static final String KEY_EVENT_NAME = "eventName";
	private static final String KEY_EVENT_VALIDATION = "validation";
	private static final String KEY_EVENT_TEST_ID = "testId";
	private static final String KEY_EVENT_CREATION_DATE = "creationDate";
	private static final String KEY_EVENT_AUTHOR = "author";

	// EXAM column names
	private static final String KEY_EXAM_ID = "examId";
	private static final String KEY_EXAM_EVENT_ID = "eventId";
	private static final String KEY_EXAM_START_DATE = "startDate";
	private static final String KEY_EXAM_END_DATE = "endDate";
	private static final String KEY_EXAM_PASSWORD = "creationDate";

	// EXERCISE_FILES column names
	private static final String KEY_EXERCISE_FILES_ID = "exerciseId";
	private static final String KEY_EXERCISE_FILES_IMAGE = "image";
	private static final String KEY_EXERCISE_FILES_AUDIO = "audio";
	private static final String KEY_EXERCISE_FILES_VIDEO = "video";

	// EXERCISES column names
	private static final String KEY_EXERCISE_ID = "exerciseId";
	private static final String KEY_EXERCISE_NAME = "name";
	private static final String KEY_EXERCISE_TYPE = "type";
	private static final String KEY_EXERCISE_CREATED_BY = "createdBy";
	private static final String KEY_EXERCISE__EXAM_ID = "examId";

	// QUESTION_FILES column names
	private static final String KEY_QUESTION_FILES_ID = "questionId";
	private static final String KEY_QUESTION_FILES_IMAGE = "image";
	private static final String KEY_QUESTION_FILES_AUDIO = "audio";
	private static final String KEY_QUESTION_FILES_VIDEO = "video";

	// QUESTIONS column names
	private static final String KEY_QUESTION_ID = "questionId";
	private static final String KEY_QUESTION_TEXT = "text";
	private static final String KEY_QUESTION_TYPE = "type";
	private static final String KEY_QUESTION_INPUT_COUNT = "inputCound";
	private static final String KEY_QUESTION_EXERCISE_ID = "exerciseId";

	// USERS column names
	private static final String KEY_USER_ID = "userId";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_USER_EMAIL = "email";
	private static final String KEY_USER_FIRSTNAME = "firstname";
	private static final String KEY_USER_LASTNAME = "lastname";
	private static final String KEY_USER_GROUPS = "groups";

	// ANSWERS column names
	private static final String KEY_ANSWER_ID = "answerId";
	private static final String KEY_ANSWER_NAME = "name";
	private static final String KEY_ANSWER_QUESTION_ID = "questionId";

	// Table Create Statements
	// Event table create statement
	private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
			+ TABLE_EVENTS + "(" + KEY_EVENT_ID + " INTEGER PRIMARY KEY,"
			+ KEY_EVENT_NAME + " TEXT," + KEY_EVENT_VALIDATION + " INTEGER,"
			+ KEY_EVENT_TEST_ID + " INTEGER," + KEY_EVENT_AUTHOR + " INTEGER,"
			+ KEY_EVENT_CREATION_DATE + " INTEGER" + ")";

	// Exam table create statement
	private static final String CREATE_TABLE_EXAM = "CREATE TABLE "
			+ TABLE_EXAMS + "(" + KEY_EXAM_ID + " INTEGER PRIMARY KEY,"
			+ KEY_EXAM_EVENT_ID + " INTEGER," + KEY_EXAM_START_DATE
			+ " INTEGER," + KEY_EXAM_END_DATE + " INTEGER," + KEY_EXAM_PASSWORD
			+ " TEXT" + ")";

	// ExerciseFiles table create statement
	private static final String CREATE_TABLE_EXERCISE_FILES = "CREATE TABLE "
			+ TABLE_EXERCISE_FILES + "(" + KEY_EXERCISE_FILES_ID
			+ " INTEGER PRIMARY KEY," + KEY_EXERCISE_FILES_IMAGE + " TEXT,"
			+ KEY_EXERCISE_FILES_AUDIO + " TEXT," + KEY_EXERCISE_FILES_VIDEO
			+ " TEXT" + ")";

	// Exercises table create statement
	private static final String CREATE_TABLE_EXERCISE = "CREATE TABLE "
			+ TABLE_EXERCISES + "(" + KEY_EXERCISE_ID + " INTEGER PRIMARY KEY,"
			+ KEY_EXERCISE_NAME + " TEXT," + KEY_EXERCISE_TYPE + " TEXT,"
			+ KEY_EXERCISE_CREATED_BY + " TEXT," + KEY_EXERCISE__EXAM_ID
			+ " INTEGER" + ")";

	// QuestionFiles table create statement
	private static final String CREATE_TABLE_QUESTION_FILES = "CREATE TABLE "
			+ TABLE_QUESTION_FILES + "(" + KEY_QUESTION_FILES_ID
			+ " INTEGER PRIMARY KEY," + KEY_QUESTION_FILES_IMAGE + " TEXT,"
			+ KEY_QUESTION_FILES_AUDIO + " TEXT," + KEY_QUESTION_FILES_VIDEO
			+ " TEXT" + ")";

	// Question table create statement
	private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
			+ TABLE_QUESTIONS + "(" + KEY_QUESTION_ID + " INTEGER PRIMARY KEY,"
			+ KEY_QUESTION_TEXT + " TEXT," + KEY_QUESTION_TYPE + " TEXT,"
			+ KEY_QUESTION_INPUT_COUNT + " INTEGER," + KEY_QUESTION_EXERCISE_ID
			+ " INTEGER" + ")";

	// User table create statement
	private static final String CREATE_TABLE_USER = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY,"
			+ KEY_USERNAME + " TEXT," + KEY_USER_EMAIL + " TEXT,"
			+ KEY_USER_FIRSTNAME + " TEXT," + KEY_USER_LASTNAME + " TEXT,"
			+ KEY_USER_GROUPS + " BLOB" + ")";

	// Answers table create statement
	private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE "
			+ TABLE_ANSWERS + "(" + KEY_ANSWER_ID + " INTEGER PRIMARY KEY,"
			+ KEY_ANSWER_NAME + " TEXT," + KEY_ANSWER_QUESTION_ID + " INTEGER"
			+ ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_ANSWERS);
		db.execSQL(CREATE_TABLE_EVENT);
		db.execSQL(CREATE_TABLE_EXAM);
		db.execSQL(CREATE_TABLE_EXERCISE);
		db.execSQL(CREATE_TABLE_EXERCISE_FILES);
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_QUESTION_FILES);
		db.execSQL(CREATE_TABLE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_FILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_FILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// create new tables
		onCreate(db);
	}

	/*
	 * Creating an Event
	 */
	public long createEvent(Event event) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EVENT_ID, event.getId());
		values.put(KEY_EVENT_CREATION_DATE, (int) event.getCreationDate());
		values.put(KEY_EVENT_AUTHOR, event.getAuthor());
		values.put(KEY_EVENT_NAME, event.getName());
		values.put(KEY_EVENT_TEST_ID, event.getTestId());
		values.put(KEY_EVENT_VALIDATION, event.getValidation());

		// insert row
		db.beginTransaction();
		long event_id = -1;
		try {
			event_id = db.insertWithOnConflict(TABLE_EVENTS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return event_id;
	}

	/*
	 * get single event
	 */
	public Event getEvent(long event_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE "
				+ KEY_EVENT_ID + " = " + event_id;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null)
			c.moveToFirst();

		Event event = new Event();
		event.setId(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
		event.setAuthor((c.getString(c.getColumnIndex(KEY_EVENT_AUTHOR))));
		event.setCreationDate(c.getInt(c
				.getColumnIndex(KEY_EVENT_CREATION_DATE)));
		event.setName(c.getString(c.getColumnIndex(KEY_EVENT_NAME)));
		event.setTestId(c.getInt(c.getColumnIndex(KEY_EVENT_TEST_ID)));
		event.setValidation(c.getInt(c.getColumnIndex(KEY_EVENT_VALIDATION)));

		return event;
	}

	/*
	 * getting all events
	 */
	public List<Event> getAllEvents() {
		List<Event> events = new ArrayList<Event>();
		String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Event event = new Event();
				event.setId(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
				event.setAuthor((c.getString(c.getColumnIndex(KEY_EVENT_AUTHOR))));
				event.setCreationDate(c.getInt(c
						.getColumnIndex(KEY_EVENT_CREATION_DATE)));
				event.setName(c.getString(c.getColumnIndex(KEY_EVENT_NAME)));
				event.setTestId(c.getInt(c.getColumnIndex(KEY_EVENT_TEST_ID)));
				event.setValidation(c.getInt(c
						.getColumnIndex(KEY_EVENT_VALIDATION)));

				events.add(event);
			} while (c.moveToNext());
		}

		return events;
	}

	/*
	 * Updating an event
	 */
	public int updateEvent(Event event) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EVENT_ID, event.getId());
		values.put(KEY_EVENT_CREATION_DATE, (int) event.getCreationDate());
		values.put(KEY_EVENT_AUTHOR, event.getAuthor());
		values.put(KEY_EVENT_NAME, event.getName());
		values.put(KEY_EVENT_TEST_ID, event.getTestId());
		values.put(KEY_EVENT_VALIDATION, event.getValidation());

		// updating row
		return db.update(TABLE_EVENTS, values, KEY_EVENT_ID + " = ?",
				new String[] { String.valueOf(event.getId()) });
	}

	/*
	 * Deleting an event
	 */
	public void deleteEvent(long event_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENTS, KEY_EVENT_ID + " = ?",
				new String[] { String.valueOf(event_id) });
	}

	/*
	 * Creating an exam
	 */
	public long createExam(Exam exam) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXAM_ID, exam.getId());
		values.put(KEY_EXAM_EVENT_ID, exam.getEventId());
		values.put(KEY_EXAM_PASSWORD, exam.getPassword());
		values.put(KEY_EXAM_START_DATE, exam.getStartDate());
		values.put(KEY_EXAM_END_DATE, (int) exam.getEndDate());

		// insert row
		db.beginTransaction();
		long exam_id = -1;
		try {
			exam_id = db.insert(TABLE_EXAMS, null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return exam_id;
	}

	/*
	 * get single exam
	 */
	public Exam getExam(long exam_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMS + " WHERE "
				+ KEY_EXAM_ID + " = " + exam_id;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null)
			c.moveToFirst();

		Exam exam = new Exam();
		exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
		exam.setEventId((c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID))));
		exam.setStartDate(c.getInt(c.getColumnIndex(KEY_EXAM_START_DATE)));
		exam.setEndDate(c.getInt(c.getColumnIndex(KEY_EXAM_END_DATE)));
		exam.setPassword(c.getString(c.getColumnIndex(KEY_EXAM_PASSWORD)));

		return exam;
	}

	/*
	 * getting all exams
	 */
	public ArrayList<Exam> getAllExams() {
		ArrayList<Exam> exams = new ArrayList<Exam>();
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Exam exam = new Exam();
				exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
				exam.setEventId((c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID))));
				exam.setStartDate(c.getInt(c
						.getColumnIndex(KEY_EXAM_START_DATE)));
				exam.setEndDate(c.getInt(c.getColumnIndex(KEY_EXAM_END_DATE)));
				exam.setPassword(c.getString(c
						.getColumnIndex(KEY_EXAM_PASSWORD)));

				exams.add(exam);
			} while (c.moveToNext());
		}

		return exams;
	}

	/*
	 * Updating an exam
	 */
	public int updateExam(Exam exam) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXAM_ID, exam.getId());
		values.put(KEY_EXAM_END_DATE, (int) exam.getEndDate());
		values.put(KEY_EXAM_EVENT_ID, exam.getEventId());
		values.put(KEY_EXAM_PASSWORD, exam.getPassword());
		values.put(KEY_EXAM_START_DATE, exam.getStartDate());

		// updating row
		return db.update(TABLE_EVENTS, values, KEY_EVENT_ID + " = ?",
				new String[] { String.valueOf(exam.getId()) });
	}

	/*
	 * Deleting an exam
	 */
	public void deleteExam(long exam_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXAMS, KEY_EXAM_ID + " = ?",
				new String[] { String.valueOf(exam_id) });
	}

	/*
	 * getting all exams by event id
	 */
	public ArrayList<Exam> getAllExamsByEventId(int eventId) {
		ArrayList<Exam> exams = new ArrayList<Exam>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMS + " WHERE "
				+ KEY_EXAM_EVENT_ID + " = " + eventId;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null)
			c.moveToFirst();
		do {
			if (c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID)) == eventId) {
				Exam exam = new Exam();
				exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
				exam.setEventId((c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID))));
				exam.setStartDate(c.getInt(c
						.getColumnIndex(KEY_EXAM_START_DATE)));
				exam.setEndDate(c.getInt(c.getColumnIndex(KEY_EXAM_END_DATE)));
				exam.setPassword(c.getString(c
						.getColumnIndex(KEY_EXAM_PASSWORD)));
				exams.add(exam);
			}

		} while (c.moveToNext());

		return exams;
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}