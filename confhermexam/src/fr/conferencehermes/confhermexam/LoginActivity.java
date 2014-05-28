package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.conferencehermes.confhermexam.connection.BaseNetworkManager;
import fr.conferencehermes.confhermexam.connectionhelper.ActionDelegate;
import fr.conferencehermes.confhermexam.connectionhelper.RequestCreator;
import fr.conferencehermes.confhermexam.connectionhelper.RequestHelper;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;
import fr.conferencehermes.confhermexam.util.ViewTracker;

public class LoginActivity extends Activity implements ActionDelegate {

	private EditText username;
	private EditText password;
	public static String authToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.loginRow);
		password = (EditText) findViewById(R.id.passwordRow);

		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(LoginActivity.this,
				// HomeActivity.class);
				// startActivity(intent);
				// finish();
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

		// ------------------- Setting up login request here -------------- //

		BaseNetworkManager baseNetworkManager = new BaseNetworkManager();
		RequestCreator creator = new RequestCreator();

		final String uname = username.getText().toString().trim();
		final String pass = password.getText().toString().trim();

		if (!uname.isEmpty() && !pass.isEmpty()) {
			Utilities.showOrHideActivityIndicator(LoginActivity.this, 0,
					"Logging into Hermes...");

			Map<String, String> params = creator.createAppropriateMapRequest(
					"username", uname, "password", pass);

			// ----------------------- Construct POST DATA
			// ---------------------------//
			final RequestHelper reqHelper = new RequestHelper();
			final List<NameValuePair> paramsList = reqHelper
					.createPostDataWithKeyValuePair(params);

			baseNetworkManager.constructConnectionAndHitPOST(
					"Login Successful", "Login Request Started", paramsList,

					this, "LogView", "LogService");
		} else {
			Toast.makeText(LoginActivity.this,
					"Username or Password can not be empty", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void didFinishRequestProcessing() {

		/***** Share Preferences Save */
		// String nameText = username.getText().toString();
		// String surnameText = password.getText().toString();

		// if (nameText != null)
		// Utilities.writeString(this, Utilities.LOGIN, nameText);
		// if (surnameText != null)
		// Utilities.writeString(this, Utilities.PASSWORD, surnameText);

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
				"Logging into Hermes...");
		Toast.makeText(getApplicationContext(), "Request Failed",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
