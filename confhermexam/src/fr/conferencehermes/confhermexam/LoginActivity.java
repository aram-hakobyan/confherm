package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import fr.conferencehermes.confhermexam.connection.BaseNetworkManager;
import fr.conferencehermes.confhermexam.connectionhelper.ActionDelegate;
import fr.conferencehermes.confhermexam.connectionhelper.RequestCreator;
import fr.conferencehermes.confhermexam.connectionhelper.RequestHelper;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;
import fr.conferencehermes.confhermexam.util.ViewTracker;

public class LoginActivity extends Activity implements ActionDelegate {
	private static Profile pData;
	private EditText username;
	private EditText password;
	private ProgressBar progressBarLogin;
	private LinearLayout loginContentLayout;
	public static String authToken;
	private SharedPreferences.Editor authKeyEditor;
	private SharedPreferences authKeyPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ViewTracker.getInstance().setCurrentContext(this);
		ViewTracker.getInstance().setCurrentViewName(Constants.LOGIN_VIEW);
		username = (EditText) findViewById(R.id.loginRow);
		password = (EditText) findViewById(R.id.passwordRow);
		loginContentLayout =(LinearLayout) findViewById(R.id.loginContentLayout);
		progressBarLogin =(ProgressBar) findViewById(R.id.progressBarLogin);
		
		authKeyEditor = getPreferences(MODE_PRIVATE).edit();
		
		if (pData != null){
		authKeyEditor.putString(Constants.AUTHKEY_SHAREDPREFS_KEY,
				pData.getAuthKey());
		}
		authKeyEditor.commit();
		loginAction();
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginAction();

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		ViewTracker.getInstance().setCurrentContext(this);
		ViewTracker.getInstance().setCurrentViewName(Constants.LOGIN_VIEW);
	}

	public void loginAction() {
		String logonOrAuth = null;
		Map<String, String> params = null;


		authKeyPrefs = getPreferences(MODE_PRIVATE);
		String restoredAuthKey = authKeyPrefs.getString(
				Constants.AUTHKEY_SHAREDPREFS_KEY, null);

		final String uname = "STUDENT";// username.getText().toString().trim();
		final String pass = "123456"; // password.getText().toString().trim();//
		RequestCreator creator = new RequestCreator();
		if (restoredAuthKey != null) {
			logonOrAuth = Constants.SERVER_URL_AUTH;
			params = creator.createAppropriateMapRequest("auth_key", restoredAuthKey);

			loginContentLayout.setVisibility(View.GONE);
			progressBarLogin.setVisibility(View.VISIBLE);
			
		} else {

			logonOrAuth = Constants.SERVER_URL;
			
			params = creator.createAppropriateMapRequest("username", uname,
					"password", pass);

			loginContentLayout.setVisibility(View.VISIBLE);
			progressBarLogin.setVisibility(View.GONE);

		}

		if (!uname.isEmpty() && !pass.isEmpty() || restoredAuthKey != null) {
			//Utilities.showOrHideActivityIndicator(LoginActivity.this, 0,
				//	"Logging into Hermes...");
			BaseNetworkManager baseNetworkManager = new BaseNetworkManager();
	
			final RequestHelper reqHelper = new RequestHelper();
			final List<NameValuePair> paramsList = reqHelper
					.createPostDataWithKeyValuePair(params);

			baseNetworkManager.constructConnectionAndHitPOST(
					"Login Successful", "Login Request Started", paramsList,
					this, "LogView", "LogService", logonOrAuth);
		} else {
			//Toast.makeText(LoginActivity.this,
				//	"Username or Password can not be empty", Toast.LENGTH_LONG)
					//.show();
		}

	}
	  
	

	@Override
	public void didFinishRequestProcessing() {

		/***** Share Preferences Save */

		Intent hIntent = new Intent(getApplicationContext(), HomeActivity.class);

		final String needToWriteLogin = Utilities.readString(this,
				Utilities.IS_LOGGED_IN, null);
		if (needToWriteLogin == null) {
			Utilities.writeString(this, Utilities.IS_LOGGED_IN, "YES");
		} else if (needToWriteLogin.equals("NO")) {
			Utilities.writeString(this, Utilities.IS_LOGGED_IN, "YES");
		}

		Utilities.showOrHideActivityIndicator(LoginActivity.this, 1,
				"Logging into Hermes...");

		startActivity(hIntent);
		finish();

	}

	@Override
	public void didFinishRequestProcessing(
			ArrayList<HashMap<String, String>> list, String service) {

		Utilities.showOrHideActivityIndicator(LoginActivity.this, 1,
				"Logging into Hermes...");
	}

	@Override
	public void didFailRequestProcessing() {
		Utilities.showOrHideActivityIndicator(LoginActivity.this, 1,
				"Something went wrong...");
		Toast.makeText(getApplicationContext(), "Request Failed",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
