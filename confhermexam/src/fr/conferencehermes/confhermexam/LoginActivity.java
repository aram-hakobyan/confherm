package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.connection.BaseNetworkManager;
import fr.conferencehermes.confhermexam.connection.NetworkReachability;
import fr.conferencehermes.confhermexam.connectionhelper.ActionDelegate;
import fr.conferencehermes.confhermexam.connectionhelper.RequestCreator;
import fr.conferencehermes.confhermexam.connectionhelper.RequestHelper;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;
import fr.conferencehermes.confhermexam.util.ViewTracker;

public class LoginActivity extends Activity implements ActionDelegate {
  private static Profile lData;
  private EditText username;
  private EditText password;
  private ProgressBar progressBarLogin;
  private LinearLayout loginContentLayout;
  public static String authToken;
  private static SharedPreferences.Editor authKeyEditor;
  private SharedPreferences authKeyPrefs;
  private SharedPreferences.Editor logoutEditor;
  private SharedPreferences logoutPrefs;
  public static Context context;
  boolean logout;
  private String loginInfoText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_login);

    ViewTracker.getInstance().setCurrentContext(this);
    ViewTracker.getInstance().setCurrentViewName(Constants.LOGIN_VIEW);
    context = LoginActivity.this;
    requestLoginInfoText();
    username = (EditText) findViewById(R.id.loginRow);
    password = (EditText) findViewById(R.id.passwordRow);
    loginContentLayout = (LinearLayout) findViewById(R.id.loginContentLayout);
    progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

    if (NetworkReachability.isReachable()) {

      authKeyEditor = getPreferences(MODE_PRIVATE).edit();

      logoutPrefs = getSharedPreferences("logoutPrefs", MODE_PRIVATE);
      logout = logoutPrefs.getBoolean(Constants.LOGOUT_SHAREDPREFS_KEY, false);
      if (logout == false) {
        loginAction();

      } else {
        loginContentLayout.setVisibility(View.VISIBLE);
        progressBarLogin.setVisibility(View.GONE);
      }

      Button loginButton = (Button) findViewById(R.id.loginButton);
      loginButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          loginAction();

        }
      });
    } else {
      authKeyPrefs = getPreferences(MODE_PRIVATE);
      String restoredAuthKey = authKeyPrefs.getString(Constants.AUTHKEY_SHAREDPREFS_KEY, null);
      if (restoredAuthKey != null) {

        Intent hIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(hIntent);
        finish();
      }

    }

    // SharedPreferences profilePreferences = getSharedPreferences(
    // "fr.conferencehermes.confhermexam.fragments.MYPROFILE",
    // Context.MODE_PRIVATE);
    // String information = profilePreferences.getString("pInformation",
    // "");

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

    logoutEditor = getSharedPreferences("logoutPrefs", MODE_PRIVATE).edit();
    logoutEditor.putBoolean(Constants.LOGOUT_SHAREDPREFS_KEY, false);
    logoutEditor.commit();

    authKeyPrefs = getPreferences(MODE_PRIVATE);
    String restoredAuthKey = authKeyPrefs.getString(Constants.AUTHKEY_SHAREDPREFS_KEY, null);

    final String uname = username.getText().toString().trim();
    final String pass = password.getText().toString().trim();
    RequestCreator creator = new RequestCreator();
    if (restoredAuthKey != null && logout != true) {
      logonOrAuth = Constants.SERVER_URL_AUTH;
      params = creator.createAppropriateMapRequest("auth_key", restoredAuthKey);

      loginContentLayout.setVisibility(View.GONE);
      progressBarLogin.setVisibility(View.VISIBLE);

    } else {

      logonOrAuth = Constants.SERVER_URL;

      params = creator.createAppropriateMapRequest("username", uname, "password", pass);

      loginContentLayout.setVisibility(View.VISIBLE);
      progressBarLogin.setVisibility(View.GONE);

    }

    if (!uname.isEmpty() && !pass.isEmpty() || restoredAuthKey != null) {
      // Utilities.showOrHideActivityIndicator(LoginActivity.this, 0,
      // "Logging into Hermes...");
      BaseNetworkManager baseNetworkManager = new BaseNetworkManager();

      final RequestHelper reqHelper = new RequestHelper();
      final List<NameValuePair> paramsList = reqHelper.createPostDataWithKeyValuePair(params);

      baseNetworkManager.constructConnectionAndHitPOST("Login Successful", "Login Request Started",
          paramsList, this, "LogView", "LogService", logonOrAuth);
    } else {
      Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_alert),
          Toast.LENGTH_LONG).show();
    }

  }

  @Override
  public void didFinishRequestProcessing() {

    /***** Share Preferences Save */

    Intent hIntent = new Intent(getApplicationContext(), HomeActivity.class);

    final String needToWriteLogin = Utilities.readString(this, Utilities.IS_LOGGED_IN, null);
    if (needToWriteLogin == null) {
      Utilities.writeString(this, Utilities.IS_LOGGED_IN, "YES");
    } else if (needToWriteLogin.equals("NO")) {
      Utilities.writeString(this, Utilities.IS_LOGGED_IN, "YES");
    }

    // Utilities.showOrHideActivityIndicator(LoginActivity.this, 1,
    // "Logging into Hermes...");

    startActivity(hIntent);
    finish();

  }

  @Override
  public void didFinishRequestProcessing(ArrayList<HashMap<String, String>> list, String service) {

    Utilities.showOrHideActivityIndicator(LoginActivity.this, 1, "Logging into Hermes...");
  }

  @Override
  public void didFailRequestProcessing(String Message) {

    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_login_data),
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return false;
  }

  public static void setLoginData(Profile loginData) {
    lData = loginData;
    Utilities.writeString(context, "auth_key", lData.getAuthKey());
    if (lData != null) {
      authKeyEditor.putString(Constants.AUTHKEY_SHAREDPREFS_KEY, lData.getAuthKey());
    }
    authKeyEditor.commit();

  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
  }

  public void requestLoginInfoText() {

    AQuery aq = new AQuery(LoginActivity.this);

    HashMap<String, Object> params = new HashMap<String, Object>();

    if (Utilities.isNetworkAvailable(LoginActivity.this)) {
      aq.ajax(Constants.LOGIN_INFO_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject json, AjaxStatus status) {

          try {
            if (json.has(Constants.KEY_STATUS) && json.get(Constants.KEY_STATUS) != null) {
              if (json.getInt("status") == 200) {
                loginInfoText = JSONParser.parseLoginInfo(json);
                ((TextView) findViewById(R.id.loginInfoText)).setText(loginInfoText);
              }
            }
          } catch (JSONException e) {
            e.printStackTrace();

          }
        }
      });
    } else {

      Toast.makeText(getApplicationContext(),
          getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

    }
  }

}
