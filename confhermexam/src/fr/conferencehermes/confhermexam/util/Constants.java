package fr.conferencehermes.confhermexam.util;

public class Constants {
	public static int PROFILE_FRAGMENT = 0;
	public static int PLANNING_FRAGMENT = 1;
	public static int EXAMINE_FRAGMENT = 2;
	public static int ENTRAINMENT_FRAGMENT = 3;
	public static int RESULTATS_FRAGMENT = 4;
	public static int TELECHARG_FRAGMENT = 5;

	public static int TYPE_EXAM = 1;
	public static int TYPE_CONFERENCE = 2;

	public static boolean calledFromExam = false;

	/********************************* URLs ***********************************/

	public static String SERVER_URL = "http://ecni.conference-hermes.fr/api/login";
	public static String SERVER_URL_AUTH = "http://ecni.conference-hermes.fr/api/authenticate";
	public static String EXERCISE_RESULT_URL = "http://ecni.conference-hermes.fr/api/exerciseresult";
	public static String RESULT_LIST_URL = "http://ecni.conference-hermes.fr/api/resultlist";
	public static String TRAINING_URL = "http://ecni.conference-hermes.fr/api/training";
	public static String TRAINING_EXERCISE_URL = "http://ecni.conference-hermes.fr/api/traningexercise";
	public static String TRAINING_LIST_URL = "http://ecni.conference-hermes.fr/api/traininglist";
	public static String EXAM_LIST_URL = "http://ecni.conference-hermes.fr/api/exams";
	public static String DOWNLOADS_LIST_URL = "http://ecni.conference-hermes.fr/api/download";
	public static String LOG_OUT_URL = "http://ecni.conference-hermes.fr/api/logout.php";
	public static String CURRENT_EXAM_URL = "http://ecni.conference-hermes.fr/api/result";
	public static String PLANNING_URL = "http://ecni.conference-hermes.fr/api/planning";

	public static String EXAM_CORRECTIONS_URL = "http://ecni.conference-hermes.fr/api/exam_corrections";

	public static String EXAM_EXERCISE_CORRECTIONS_URL = "http://ecni.conference-hermes.fr/api/exam_exercise_correction";

	/********************************* Json Keys ***********************************/

	public static String KEY_AUTH_TOKEN = "auth_key";
	public static String KEY_STATUS = "status";
	public static String KEY_DATA = "data";
	public static String KEY_RESULTS = "results";
	public static String KEY_EXERCSICE_ID = "exercise_id";
	public static String KEY_EXAM_ID = "exam_id";
	public static String KEY_GLOBAL_TEST = "global_test";
	public static String KEY_GROUPS = "groups";
	public static String KEY_DEVICE_ID = "device_id";
	public static String KEY_FROM = "from";
	public static String KEY_TO = "to";

	/********************************* Other ***********************************/
	public static final String RIGHT_SLASH = "/";
	public static final String EQUAL = "=";
	public static final String FIRST_PARAM_SEPARATOR = "?";
	public static final String PARAMETER_SEPARATOR = "&";
	public static final String LOGIN_VIEW = "LoginView";
	public static final String LOGOUT_VIEW = "LogoutView";
	public static final String AUTHKEY_SHAREDPREFS_KEY = "fr.conferencehermes.confhermexam_AUTHKEY";
	public static final String LOGOUT_SHAREDPREFS_KEY = "fr.conferencehermes.confhermexam.FROM_LOGOUT";

}
