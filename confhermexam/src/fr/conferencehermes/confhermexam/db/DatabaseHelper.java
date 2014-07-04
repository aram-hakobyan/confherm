package fr.conferencehermes.confhermexam.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.conferencehermes.confhermexam.parser.Answer;
import fr.conferencehermes.confhermexam.parser.Event;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.ExerciseAnswer;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.parser.Question;

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
	private static final String TABLE_EXERCISE_ANSWERS = "ExerciseAnswers";

	// EVENT column names
	private static final String KEY_EVENT_ID = "eventId";
	private static final String KEY_EVENT_NAME = "eventName";
	private static final String KEY_EVENT_VALIDATION = "validation";
	private static final String KEY_EVENT_TEST_ID = "testId";
	private static final String KEY_EVENT_CREATION_DATE = "creationDate";
	private static final String KEY_EVENT_AUTHOR = "author";

	// EXAM column names
	private static final String KEY_EXAM_ID = "examId";
	private static final String KEY_EXAM_NAME = "examName";
	private static final String KEY_EXAM_EVENT_ID = "eventId";
	private static final String KEY_EXAM_EVENT_NAME = "examEventName";
	private static final String KEY_EXAM_START_DATE = "startDate";
	private static final String KEY_EXAM_END_DATE = "endDate";
	private static final String KEY_EXAM_PASSWORD = "creationDate";
	private static final String KEY_EXAM_TYPE = "categoryType";
	private static final String KEY_EXAM_LAST_EDIT_TIME = "lastEditTime";
	private static final String KEY_EXAM_STATUS = "status";

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
	private static final String KEY_EXERCISE_EXAM_ID = "examId";

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

	// EXERCISE ANSWERS column names
	private static final String KEY_EXERCISE_ANSWER_EXERCISE_ID = "exerciseId";
	private static final String KEY_EXERCISE_ANSWER_EXAM_ID = "examId";
	private static final String KEY_EXERCISE_ANSWER_EVENT_ID = "eventId";
	private static final String KEY_EXERCISE_ANSWER_JSONSTRING = "jsonString";

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
			+ " TEXT," + KEY_EXAM_NAME + " TEXT," + KEY_EXAM_TYPE + " TEXT,"
			+ KEY_EXAM_LAST_EDIT_TIME + " INTEGER," + KEY_EXAM_STATUS
			+ " INTEGER," + KEY_EXAM_EVENT_NAME + " TEXT" + ")";

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
			+ KEY_EXERCISE_CREATED_BY + " TEXT," + KEY_EXERCISE_EXAM_ID
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

	// Exercise Answers table create statement
	private static final String CREATE_TABLE_EXERCISE_ANSWERS = "CREATE TABLE "
			+ TABLE_EXERCISE_ANSWERS + "(" + KEY_EXERCISE_ANSWER_EXERCISE_ID
			+ " INTEGER PRIMARY KEY," + KEY_EXERCISE_ANSWER_EXAM_ID
			+ " INTEGER," + KEY_EXERCISE_ANSWER_EVENT_ID + " INTEGER,"
			+ KEY_EXERCISE_ANSWER_JSONSTRING + " TEXT" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_EVENT);
		db.execSQL(CREATE_TABLE_EXAM);
		db.execSQL(CREATE_TABLE_EXERCISE);
		db.execSQL(CREATE_TABLE_EXERCISE_FILES);
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_QUESTION_FILES);
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_ANSWERS);
		db.execSQL(CREATE_TABLE_EXERCISE_ANSWERS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_FILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_FILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_ANSWERS);

		// create new tables
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
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

		Event event = new Event();
		if (c != null && c.moveToFirst()) {
			event.setId(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
			event.setAuthor((c.getString(c.getColumnIndex(KEY_EVENT_AUTHOR))));
			event.setCreationDate(c.getInt(c
					.getColumnIndex(KEY_EVENT_CREATION_DATE)));
			event.setName(c.getString(c.getColumnIndex(KEY_EVENT_NAME)));
			event.setTestId(c.getInt(c.getColumnIndex(KEY_EVENT_TEST_ID)));
			event.setValidation(c.getInt(c.getColumnIndex(KEY_EVENT_VALIDATION)));
			c.close();
		}

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
		if (c != null && c.moveToFirst()) {
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
			c.close();
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
		values.put(KEY_EXAM_NAME, exam.getTitle());
		values.put(KEY_EXAM_EVENT_NAME, exam.getEvent_name());
		values.put(KEY_EXAM_LAST_EDIT_TIME, exam.getLastEditTime());
		values.put(KEY_EXAM_STATUS, exam.getStatus());
		values.put(KEY_EXAM_TYPE, exam.getCategoryType());

		// insert row
		db.beginTransaction();
		long exam_id = -1;
		try {
			exam_id = db.insertWithOnConflict(TABLE_EXAMS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
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

		Exam exam = new Exam();
		if (c != null && c.moveToFirst()) {
			exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
			exam.setEventId((c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID))));
			exam.setStartDate(c.getLong(c.getColumnIndex(KEY_EXAM_START_DATE)));
			exam.setEndDate(c.getLong(c.getColumnIndex(KEY_EXAM_END_DATE)));
			exam.setPassword(c.getString(c.getColumnIndex(KEY_EXAM_PASSWORD)));
			exam.setTitle(c.getString(c.getColumnIndex(KEY_EXAM_NAME)));
			exam.setEvent_name(c.getString(c
					.getColumnIndex(KEY_EXAM_EVENT_NAME)));
			exam.setCategoryType(c.getString(c.getColumnIndex(KEY_EXAM_TYPE)));
			exam.setStatus(c.getInt(c.getColumnIndex(KEY_EXAM_STATUS)));
			exam.setLastEditTime(c.getLong(c
					.getColumnIndex(KEY_EXAM_LAST_EDIT_TIME)));

			c.close();
		}

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
		if (c != null && c.moveToFirst()) {
			do {
				Exam exam = new Exam();
				exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
				exam.setEventId((c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID))));
				exam.setEvent_name(c.getString(c
						.getColumnIndex(KEY_EXAM_EVENT_NAME)));
				exam.setStartDate(c.getInt(c
						.getColumnIndex(KEY_EXAM_START_DATE)));
				exam.setEndDate(c.getInt(c.getColumnIndex(KEY_EXAM_END_DATE)));
				exam.setPassword(c.getString(c
						.getColumnIndex(KEY_EXAM_PASSWORD)));
				exam.setTitle(c.getString(c.getColumnIndex(KEY_EXAM_NAME)));
				exam.setCategoryType(c.getString(c
						.getColumnIndex(KEY_EXAM_TYPE)));
				exam.setStatus(c.getInt(c.getColumnIndex(KEY_EXAM_STATUS)));
				exam.setLastEditTime(c.getLong(c
						.getColumnIndex(KEY_EXAM_LAST_EDIT_TIME)));

				exams.add(exam);
			} while (c.moveToNext());
			c.close();
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
		values.put(KEY_EXAM_EVENT_NAME, exam.getEvent_name());
		values.put(KEY_EXAM_EVENT_ID, exam.getEventId());
		values.put(KEY_EXAM_PASSWORD, exam.getPassword());
		values.put(KEY_EXAM_START_DATE, exam.getStartDate());
		values.put(KEY_EXAM_NAME, exam.getTitle());
		values.put(KEY_EXAM_LAST_EDIT_TIME, exam.getLastEditTime());
		values.put(KEY_EXAM_STATUS, exam.getStatus());
		values.put(KEY_EXAM_TYPE, exam.getCategoryType());

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
		if (c != null && c.moveToFirst()) {
			do {
				if (c.getInt(c.getColumnIndex(KEY_EXAM_EVENT_ID)) == eventId) {
					Exam exam = new Exam();
					exam.setId(c.getInt(c.getColumnIndex(KEY_EXAM_ID)));
					exam.setEventId((c.getInt(c
							.getColumnIndex(KEY_EXAM_EVENT_ID))));
					exam.setStartDate(c.getInt(c
							.getColumnIndex(KEY_EXAM_START_DATE)));
					exam.setEndDate(c.getInt(c
							.getColumnIndex(KEY_EXAM_END_DATE)));
					exam.setPassword(c.getString(c
							.getColumnIndex(KEY_EXAM_PASSWORD)));
					exam.setTitle(c.getString(c.getColumnIndex(KEY_EXAM_NAME)));
					exam.setEvent_name(c.getString(c
							.getColumnIndex(KEY_EXAM_EVENT_NAME)));
					exam.setCategoryType(c.getString(c
							.getColumnIndex(KEY_EXAM_TYPE)));
					exam.setStatus(c.getInt(c.getColumnIndex(KEY_EXAM_STATUS)));
					exam.setLastEditTime(c.getLong(c
							.getColumnIndex(KEY_EXAM_LAST_EDIT_TIME)));

					exams.add(exam);
				}

			} while (c.moveToNext());
			c.close();
		}
		return exams;
	}

	/*
	 * Creating an question
	 */
	public long createQuestion(Question q) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_ID, q.getId());
		values.put(KEY_QUESTION_INPUT_COUNT, q.getInputCount());
		values.put(KEY_QUESTION_TEXT, q.getQuestionText());
		values.put(KEY_QUESTION_TYPE, q.getType());
		values.put(KEY_QUESTION_EXERCISE_ID, q.getExerciseId());

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_QUESTIONS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single question
	 */
	public Question getQuestion(long question_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE "
				+ KEY_QUESTION_ID + " = " + question_id;

		Cursor c = db.rawQuery(selectQuery, null);

		Question q = new Question();
		if (c != null && c.moveToFirst()) {
			q.setId((c.getInt(c.getColumnIndex(KEY_QUESTION_ID))));
			q.setExerciseId((c.getInt(c
					.getColumnIndex(KEY_QUESTION_EXERCISE_ID))));
			q.setInputCount(c.getString(c
					.getColumnIndex(KEY_QUESTION_INPUT_COUNT)));
			q.setQuestionText(c.getString(c.getColumnIndex(KEY_QUESTION_TEXT)));
			q.setType(c.getString(c.getColumnIndex(KEY_QUESTION_TYPE)));
			c.close();
		}
		return q;
	}

	/*
	 * getting all questions
	 */
	public ArrayList<Question> getAllQuestions() {
		ArrayList<Question> questions = new ArrayList<Question>();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				Question q = new Question();
				q.setId((c.getInt(c.getColumnIndex(KEY_QUESTION_ID))));
				q.setExerciseId((c.getInt(c
						.getColumnIndex(KEY_QUESTION_EXERCISE_ID))));
				q.setInputCount(c.getString(c
						.getColumnIndex(KEY_QUESTION_INPUT_COUNT)));
				q.setQuestionText(c.getString(c
						.getColumnIndex(KEY_QUESTION_TEXT)));
				q.setType(c.getString(c.getColumnIndex(KEY_QUESTION_TYPE)));

				questions.add(q);
			} while (c.moveToNext());
			c.close();
		}

		return questions;
	}

	/*
	 * Updating a question
	 */
	public int updateQuestion(Question q) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_ID, q.getId());
		values.put(KEY_QUESTION_INPUT_COUNT, q.getInputCount());
		values.put(KEY_QUESTION_TEXT, q.getQuestionText());
		values.put(KEY_QUESTION_TYPE, q.getType());
		values.put(KEY_QUESTION_EXERCISE_ID, q.getExerciseId());

		// updating row
		return db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + " = ?",
				new String[] { String.valueOf(q.getId()) });
	}

	/*
	 * Deleting a question
	 */
	public void deleteQuestion(long question_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTIONS, KEY_QUESTION_ID + " = ?",
				new String[] { String.valueOf(question_id) });
	}

	/*
	 * getting all questions by exercise id
	 */
	public ArrayList<Question> getAllQuestionsByExerciseId(int exerciseId) {
		ArrayList<Question> questions = new ArrayList<Question>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS + " WHERE "
				+ KEY_QUESTION_EXERCISE_ID + " = " + exerciseId;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.moveToFirst()) {
			do {
				if (c.getInt(c.getColumnIndex(KEY_QUESTION_EXERCISE_ID)) == exerciseId) {
					Question q = new Question();
					q.setId((c.getInt(c.getColumnIndex(KEY_QUESTION_ID))));
					q.setInputCount(c.getString(c
							.getColumnIndex(KEY_QUESTION_INPUT_COUNT)));
					q.setQuestionText(c.getString(c
							.getColumnIndex(KEY_QUESTION_TEXT)));
					q.setType(c.getString(c.getColumnIndex(KEY_QUESTION_TYPE)));
					q.setExerciseId(exerciseId);
					questions.add(q);
				}

			} while (c.moveToNext());

			c.close();
		}
		return questions;
	}

	/*
	 * Creating a question file
	 */
	public long createQuestionFile(Question q) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_FILES_ID, q.getId());
		values.put(KEY_QUESTION_FILES_AUDIO, q.getFiles().get("sound"));
		values.put(KEY_QUESTION_FILES_IMAGE, q.getFiles().get("image"));
		values.put(KEY_QUESTION_FILES_VIDEO, q.getFiles().get("video"));

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_QUESTION_FILES, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single question files by question id
	 */
	public HashMap<String, String> getQuestionFile(long question_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION_FILES
				+ " WHERE " + KEY_QUESTION_FILES_ID + " = " + question_id;
		HashMap<String, String> qFiles = new HashMap<String, String>();

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null && c.moveToFirst()) {
			qFiles.put("image",
					c.getString(c.getColumnIndex(KEY_QUESTION_FILES_IMAGE)));
			qFiles.put("sound",
					c.getString(c.getColumnIndex(KEY_QUESTION_FILES_AUDIO)));
			qFiles.put("video",
					c.getString(c.getColumnIndex(KEY_QUESTION_FILES_VIDEO)));
		}

		return qFiles;
	}

	/*
	 * getting all question files
	 */
	public ArrayList<HashMap<String, String>> getAllQuestionFiles() {
		ArrayList<HashMap<String, String>> questionFiles = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION_FILES;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				HashMap<String, String> qFiles = new HashMap<String, String>();
				qFiles.put("image",
						c.getString(c.getColumnIndex(KEY_QUESTION_FILES_IMAGE)));
				qFiles.put("sound",
						c.getString(c.getColumnIndex(KEY_QUESTION_FILES_AUDIO)));
				qFiles.put("video",
						c.getString(c.getColumnIndex(KEY_QUESTION_FILES_VIDEO)));

				questionFiles.add(qFiles);
			} while (c.moveToNext());
			c.close();
		}

		return questionFiles;
	}

	/*
	 * Updating a question file
	 */
	public int updateQuestionFiles(Question q) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION_FILES_ID, q.getId());
		values.put(KEY_QUESTION_FILES_AUDIO, q.getFiles().get("sound"));
		values.put(KEY_QUESTION_FILES_IMAGE, q.getFiles().get("image"));
		values.put(KEY_QUESTION_FILES_VIDEO, q.getFiles().get("video"));

		// updating row
		return db.update(TABLE_QUESTION_FILES, values, KEY_QUESTION_FILES_ID
				+ " = ?", new String[] { String.valueOf(q.getId()) });
	}

	/*
	 * Deleting a question file
	 */
	public void deleteQuestionFiles(long question_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTION_FILES, KEY_QUESTION_FILES_ID + " = ?",
				new String[] { String.valueOf(question_id) });
	}

	public long createExercise(Exercise exercise) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_ID, exercise.getId());
		values.put(KEY_EXERCISE_NAME, exercise.getName());
		values.put(KEY_EXERCISE_TYPE, exercise.getType());
		values.put(KEY_EXERCISE_CREATED_BY, exercise.getTeacher());
		values.put(KEY_EXERCISE_EXAM_ID, exercise.getExamId());

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_EXERCISES, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single exercise
	 */
	public Exercise getExercise(long exercise_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES + " WHERE "
				+ KEY_EXERCISE_ID + " = " + exercise_id;
		Exercise exercise = new Exercise();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null && c.moveToFirst()) {
			exercise.setId(c.getInt(c.getColumnIndex(KEY_EXERCISE_ID)));
			exercise.setName((c.getString(c.getColumnIndex(KEY_EXERCISE_NAME))));
			exercise.setType(c.getString(c.getColumnIndex(KEY_EXERCISE_TYPE)));
			exercise.setTeacher(c.getString(c
					.getColumnIndex(KEY_EXERCISE_CREATED_BY)));
		}
		return exercise;
	}

	/*
	 * getting all exercises
	 */
	public ArrayList<Exercise> getAllExercise() {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				Exercise exercise = new Exercise();
				exercise.setId(c.getInt(c.getColumnIndex(KEY_EXERCISE_ID)));
				exercise.setName((c.getString(c
						.getColumnIndex(KEY_EXERCISE_NAME))));
				exercise.setType(c.getString(c
						.getColumnIndex(KEY_EXERCISE_TYPE)));
				exercise.setTeacher(c.getString(c
						.getColumnIndex(KEY_EXERCISE_CREATED_BY)));

				exercises.add(exercise);
			} while (c.moveToNext());
			c.close();
		}

		return exercises;
	}

	/*
	 * Updating an exercise
	 */
	public int updateExercise(Exercise exercise) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_ID, exercise.getId());
		values.put(KEY_EXERCISE_NAME, exercise.getName());
		values.put(KEY_EXERCISE_TYPE, exercise.getType());
		values.put(KEY_EXERCISE_CREATED_BY, exercise.getTeacher());
		values.put(KEY_EXERCISE_EXAM_ID, exercise.getExamId());

		// updating row
		return db.update(TABLE_EXERCISES, values, KEY_EXERCISE_ID + " = ?",
				new String[] { String.valueOf(exercise.getId()) });
	}

	/*
	 * Deleting an exercise
	 */
	public void deleteExercise(long exercise_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXERCISES, KEY_EXERCISE_ID + " = ?",
				new String[] { String.valueOf(exercise_id) });
	}

	/*
	 * getting all exercises by exam id
	 */
	public ArrayList<Exercise> getAllExercisesByExamId(int examId) {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES + " WHERE "
				+ KEY_EXERCISE_EXAM_ID + " = " + examId;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null && c.moveToFirst()) {
			do {
				if (c.getInt(c.getColumnIndex(KEY_EXERCISE_EXAM_ID)) == examId) {
					Exercise exercise = new Exercise();
					exercise.setId(c.getInt(c.getColumnIndex(KEY_EXERCISE_ID)));
					exercise.setName((c.getString(c
							.getColumnIndex(KEY_EXERCISE_NAME))));
					exercise.setType(c.getString(c
							.getColumnIndex(KEY_EXERCISE_TYPE)));
					exercise.setTeacher(c.getString(c
							.getColumnIndex(KEY_EXERCISE_CREATED_BY)));
					exercises.add(exercise);
				}

			} while (c.moveToNext());
			c.close();
		}
		return exercises;
	}

	/*
	 * Creating a EXERCISE file
	 */
	public long createExerciseFile(Exercise e) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_FILES_ID, e.getId());
		values.put(KEY_EXERCISE_FILES_AUDIO, e.getFiles().get("sound"));
		values.put(KEY_EXERCISE_FILES_IMAGE, e.getFiles().get("image"));
		values.put(KEY_EXERCISE_FILES_VIDEO, e.getFiles().get("video"));

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_EXERCISE_FILES, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single EXERCISE files by EXERCISE id
	 */
	public HashMap<String, String> getExerciseFile(long exercise_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_FILES
				+ " WHERE " + KEY_EXERCISE_FILES_ID + " = " + exercise_id;

		Cursor c = db.rawQuery(selectQuery, null);

		HashMap<String, String> eFiles = new HashMap<String, String>();

		if (c != null && c.moveToFirst()) {
			eFiles.put("image",
					c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_IMAGE)));
			eFiles.put("sound",
					c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_AUDIO)));
			eFiles.put("video",
					c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_VIDEO)));
			c.close();
		}

		return eFiles;
	}

	/*
	 * getting all EXERCISE files
	 */
	public ArrayList<HashMap<String, String>> getAllExerciseFiles() {
		ArrayList<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_FILES;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				HashMap<String, String> eFiles = new HashMap<String, String>();
				eFiles.put("image",
						c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_IMAGE)));
				eFiles.put("sound",
						c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_AUDIO)));
				eFiles.put("video",
						c.getString(c.getColumnIndex(KEY_EXERCISE_FILES_VIDEO)));

				files.add(eFiles);
			} while (c.moveToNext());
			c.close();
		}

		return files;
	}

	/*
	 * Updating an EXERCISE file
	 */
	public int updateExerciseFiles(Exercise e) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_FILES_ID, e.getId());
		values.put(KEY_EXERCISE_FILES_AUDIO, e.getFiles().get("sound"));
		values.put(KEY_EXERCISE_FILES_IMAGE, e.getFiles().get("image"));
		values.put(KEY_EXERCISE_FILES_VIDEO, e.getFiles().get("video"));

		// updating row
		return db.update(TABLE_EXERCISE_FILES, values, KEY_EXERCISE_FILES_ID
				+ " = ?", new String[] { String.valueOf(e.getId()) });
	}

	/*
	 * Deleting a question file
	 */
	public void deleteExerciseFiles(long exercise_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXERCISE_FILES, KEY_EXERCISE_FILES_ID + " = ?",
				new String[] { String.valueOf(exercise_id) });
	}

	/*
	 * Creating a User
	 */
	public long createUser(Profile p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, p.getId());
		values.put(KEY_USER_EMAIL, p.getEmailAdress());
		values.put(KEY_USER_FIRSTNAME, p.getFirstName());
		values.put(KEY_USER_LASTNAME, p.getLastName());
		values.put(KEY_USERNAME, p.getUserName());
		// values.put(KEY_USER_GROUPS, event.getValidation());

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_USERS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single user
	 */
	public Profile getUser(long user_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USER_ID + " = " + user_id;

		Cursor c = db.rawQuery(selectQuery, null);
		Profile p = new Profile();
		if (c != null && c.moveToFirst()) {
			p.setId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
			p.setFirstName((c.getString(c.getColumnIndex(KEY_USER_FIRSTNAME))));
			p.setLastName(c.getString(c.getColumnIndex(KEY_USER_LASTNAME)));
			p.setUserName(c.getString(c.getColumnIndex(KEY_USERNAME)));
			p.setEmailAdress(c.getString(c.getColumnIndex(KEY_USER_EMAIL)));
			c.close();
		}

		return p;
	}

	/*
	 * getting all users
	 */
	public ArrayList<Profile> getAllUsers() {
		ArrayList<Profile> users = new ArrayList<Profile>();
		String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				Profile p = new Profile();
				p.setId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
				p.setFirstName((c.getString(c
						.getColumnIndex(KEY_USER_FIRSTNAME))));
				p.setLastName(c.getString(c.getColumnIndex(KEY_USER_LASTNAME)));
				p.setUserName(c.getString(c.getColumnIndex(KEY_USERNAME)));
				p.setEmailAdress(c.getString(c.getColumnIndex(KEY_USER_EMAIL)));

				users.add(p);
			} while (c.moveToNext());
			c.close();
		}

		return users;
	}

	/*
	 * Updating a user
	 */
	public int updateUser(Profile p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, p.getId());
		values.put(KEY_USER_EMAIL, p.getEmailAdress());
		values.put(KEY_USER_FIRSTNAME, p.getFirstName());
		values.put(KEY_USER_LASTNAME, p.getLastName());
		values.put(KEY_USERNAME, p.getUserName());
		// values.put(KEY_USER_GROUPS, event.getValidation());

		// updating row
		return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(p.getId()) });
	}

	/*
	 * Deleting a user
	 */
	public void deleteUser(long user_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user_id) });
	}

	/*
	 * Creating an answer
	 */
	public long createAnswer(Answer a) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ANSWER_ID, a.getId());
		values.put(KEY_ANSWER_NAME, a.getAnswer());
		values.put(KEY_ANSWER_QUESTION_ID, a.getQuestionId());

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_ANSWERS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single answer
	 */
	public Answer getAnswer(long answer_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_ANSWERS + " WHERE "
				+ KEY_ANSWER_ID + " = " + answer_id;

		Cursor c = db.rawQuery(selectQuery, null);

		Answer a = new Answer();
		if (c != null && c.moveToFirst()) {
			a.setId((c.getInt(c.getColumnIndex(KEY_ANSWER_ID))));
			a.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER_NAME)));
			a.setQuestionId(c.getInt(c.getColumnIndex(KEY_ANSWER_QUESTION_ID)));
			c.close();
		}

		return a;
	}

	/*
	 * getting all answers
	 */
	public ArrayList<Answer> getAllAnswers() {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		String selectQuery = "SELECT  * FROM " + TABLE_ANSWERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				Answer a = new Answer();
				a.setId((c.getInt(c.getColumnIndex(KEY_ANSWER_ID))));
				a.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER_NAME)));
				a.setQuestionId(c.getInt(c
						.getColumnIndex(KEY_ANSWER_QUESTION_ID)));

				answers.add(a);
			} while (c.moveToNext());
			c.close();
		}

		return answers;
	}

	/*
	 * Updating an answer
	 */
	public int updateAnswer(Answer a) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ANSWER_ID, a.getId());
		values.put(KEY_ANSWER_NAME, a.getAnswer());
		values.put(KEY_ANSWER_QUESTION_ID, a.getQuestionId());

		// updating row
		return db.update(TABLE_ANSWERS, values, KEY_ANSWER_ID + " = ?",
				new String[] { String.valueOf(a.getId()) });
	}

	/*
	 * Deleting an answer
	 */
	public void deleteAnswer(long answer_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ANSWERS, KEY_ANSWER_ID + " = ?",
				new String[] { String.valueOf(answer_id) });
	}

	/*
	 * getting all answers by question id
	 */
	public ArrayList<Answer> getAllAnswersByQuestionId(int questionId) {
		ArrayList<Answer> answers = new ArrayList<Answer>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_ANSWERS + " WHERE "
				+ KEY_ANSWER_QUESTION_ID + " = " + questionId;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null && c.moveToFirst()) {
			do {
				Answer a = new Answer();
				a.setId((c.getInt(c.getColumnIndex(KEY_ANSWER_ID))));
				a.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER_NAME)));
				a.setQuestionId(c.getInt(c
						.getColumnIndex(KEY_ANSWER_QUESTION_ID)));
				answers.add(a);

			} while (c.moveToNext());
			c.close();
		}
		return answers;
	}

	/*
	 * Creating an exercise answer
	 */
	public long createExerciseAnswer(ExerciseAnswer a) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_ANSWER_EXERCISE_ID, a.getExerciseId());
		values.put(KEY_EXERCISE_ANSWER_EXAM_ID, a.getExamId());
		values.put(KEY_EXERCISE_ANSWER_EVENT_ID, a.getEventId());
		values.put(KEY_EXERCISE_ANSWER_JSONSTRING, a.getJsonString());

		// insert row
		db.beginTransaction();
		long id = -1;
		try {
			id = db.insertWithOnConflict(TABLE_EXERCISE_ANSWERS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			db.endTransaction();
		}

		return id;
	}

	/*
	 * get single exercise answer
	 */
	public ExerciseAnswer getExerciseAnswer(long answer_id) {

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_ANSWERS
				+ " WHERE " + KEY_EXERCISE_ANSWER_EXERCISE_ID + " = "
				+ answer_id;

		Cursor c = db.rawQuery(selectQuery, null);

		ExerciseAnswer a = new ExerciseAnswer();
		if (c != null && c.moveToFirst()) {
			a.setExerciseId((c.getInt(c
					.getColumnIndex(KEY_EXERCISE_ANSWER_EXERCISE_ID))));
			a.setExamId(c.getInt(c.getColumnIndex(KEY_EXERCISE_ANSWER_EXAM_ID)));
			a.setEventId(c.getInt(c
					.getColumnIndex(KEY_EXERCISE_ANSWER_EVENT_ID)));
			a.setJsonString(c.getString(c
					.getColumnIndex(KEY_EXERCISE_ANSWER_JSONSTRING)));
			c.close();
		}

		return a;
	}

	/*
	 * getting all exercise answers
	 */
	public ArrayList<ExerciseAnswer> getAllExerciseAnswers() {
		ArrayList<ExerciseAnswer> answers = new ArrayList<ExerciseAnswer>();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_ANSWERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null && c.moveToFirst()) {
			do {
				ExerciseAnswer a = new ExerciseAnswer();
				a.setExerciseId((c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EXERCISE_ID))));
				a.setExamId(c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EXAM_ID)));
				a.setEventId(c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EVENT_ID)));
				a.setJsonString(c.getString(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_JSONSTRING)));

				answers.add(a);
			} while (c.moveToNext());
			c.close();
		}

		return answers;
	}

	/*
	 * Updating an exercise answer
	 */
	public int updateExerciseAnswer(ExerciseAnswer a) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EXERCISE_ANSWER_EXERCISE_ID, a.getExerciseId());
		values.put(KEY_EXERCISE_ANSWER_EXAM_ID, a.getExamId());
		values.put(KEY_EXERCISE_ANSWER_EVENT_ID, a.getExamId());
		values.put(KEY_EXERCISE_ANSWER_JSONSTRING, a.getJsonString());

		// updating row
		return db.update(TABLE_EXERCISE_ANSWERS, values,
				KEY_EXERCISE_ANSWER_EXERCISE_ID + " = ?",
				new String[] { String.valueOf(a.getExerciseId()) });
	}

	/*
	 * Deleting an exercise answer
	 */
	public void deleteExerciseAnswer(long exercise_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXERCISE_ANSWERS, KEY_EXERCISE_ANSWER_EXERCISE_ID
				+ " = ?", new String[] { String.valueOf(exercise_id) });
	}

	/*
	 * getting all exercise answers by exam id
	 */
	public ArrayList<ExerciseAnswer> getAllExerciseAnswersByExamId(int examId) {
		ArrayList<ExerciseAnswer> answers = new ArrayList<ExerciseAnswer>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_ANSWERS
				+ " WHERE " + KEY_EXERCISE_ANSWER_EXAM_ID + " = " + examId;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null && c.moveToFirst()) {
			do {
				ExerciseAnswer a = new ExerciseAnswer();
				a.setExerciseId((c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EXERCISE_ID))));
				a.setExamId(c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EXAM_ID)));
				a.setExamId(c.getInt(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_EVENT_ID)));
				a.setJsonString(c.getString(c
						.getColumnIndex(KEY_EXERCISE_ANSWER_JSONSTRING)));
				answers.add(a);

			} while (c.moveToNext());
			c.close();
		}
		return answers;
	}
}