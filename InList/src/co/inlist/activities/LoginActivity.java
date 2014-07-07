package co.inlist.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.inlist.facebook.android.AsyncFacebookRunner;
import co.inlist.facebook.android.AsyncFacebookRunner.RequestListener;
import co.inlist.facebook.android.DialogError;
import co.inlist.facebook.android.Facebook;
import co.inlist.facebook.android.Facebook.DialogListener;
import co.inlist.facebook.android.FacebookError;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataCollectorAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class LoginActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	private TextView txt_lgn_forgot_pwd;
	private EditText edt_lgn_e_mail;
	private EditText edt_lgn_pwd;
	private Facebook facebook = new Facebook(Constant.FB_API_KEY);
	private AsyncFacebookRunner mAsyncRunner;
	// private String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	// private String fbId = Constant.BLANK;
	// private String fb_user_fname = Constant.BLANK;
	// private String fb_user_lname = Constant.BLANK;
	// private String fb_user_name = Constant.BLANK;
	// private String fb_user_email = Constant.BLANK;
	// private String fb_user_pic_url = Constant.BLANK;
	private RelativeLayout rl_fb_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		init();

		txt_lgn_forgot_pwd.setText(Html.fromHtml("<p><u>"
				+ getString(R.string.forgot_pwd) + "</u></p>"));
		// get the action bar
		ActionBar actionBar = getActionBar();

		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		txt_lgn_forgot_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						ForgotPassworActivity.class));
			}
		});

		rl_fb_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToFacebook();
			}
		});
	}

	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(LoginActivity.this, new String[] { "email",
					"publish_stream" }, new DialogListener() {

				@Override
				public void onCancel() {
					try {
						facebook.logout(LoginActivity.this);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					getProfileInformation();
				}

				@Override
				public void onError(DialogError error) {
				}

				@Override
				public void onFacebookError(FacebookError fberror) {
				}

			});

		} else {
			getProfileInformation();
		}
	}

	public void getProfileInformation() {

		mAsyncRunner.request("me", new RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				try {
					JSONObject jObj = new JSONObject(response);

					Log.v("", "chk facebook response : " + jObj.toString());

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {

								new WebServiceDataCollectorAsyncTask(
										Constant.API
												+ String.format(
														Constant.ACTIONS.LOGIN_FB,
														Constant.TAGS.VIP,
														Constant.TAGS.REQUEST_TYPE_JSON,
														UtilInList
																.getDeviceId(LoginActivity.this),
														facebook.getAccessToken()
																.toString()),
										LoginActivity.this).execute();

							} catch (Exception e) {

							}
						}
					});

				} catch (Exception e) {

					Log.v("", "Exception : " + e);
				}

			}

			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_login_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void init() {
		edt_lgn_e_mail = (EditText) findViewById(R.id.edt_lgn_e_mail);
		edt_lgn_pwd = (EditText) findViewById(R.id.edt_lgn_pwd);
		txt_lgn_forgot_pwd = (TextView) findViewById(R.id.txt_lgn_forgot_pwd);
		rl_fb_login = (RelativeLayout) findViewById(R.id.rl_fb_login);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_sign:
			// search action

			if (edt_lgn_e_mail.getText().toString().trim().equals("")) {
				Toast toast = Toast.makeText(LoginActivity.this,
						Constant.ERRORS.PLZ_EMAIL, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
		/*	} else if (edt_lgn_pwd.getText().toString().trim().equals("")) {
				Toast toast = Toast.makeText(LoginActivity.this,
						Constant.ERRORS.PLZ_PASSWORD, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();*/
			} else {
				new WebServiceDataCollectorAsyncTask(Constant.API
						+ String.format(Constant.ACTIONS.LOGIN, "VIP", "true",
								UtilInList.getDeviceId(LoginActivity.this),
								edt_lgn_e_mail.getText().toString().trim(),
								edt_lgn_pwd.getText().toString().trim())
								.replace(" ", "%20"), LoginActivity.this)
						.execute();

			}

			return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub

		try {
			if (result.getString("success").toString().equals("true")) {

				Log.v("", ">>> chk this : "
						+ result.getJSONObject("data").getString("last_name"));

				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_STATUS, "true");
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_USERID,
						result.getJSONObject("data").getString("user_id"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_EMAIL,
						result.getJSONObject("data").getString("email"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_FIRSTNAME,
						result.getJSONObject("data").getString("first_name"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_LASTNAME,
						result.getJSONObject("data").getString("last_name"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_PHONE,
						result.getJSONObject("data").getString("phone"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_SESSIONID,
						result.getJSONObject("session").getJSONObject("userInfo").getString("sessionId"));
				UtilInList.WriteSharePrefrence(LoginActivity.this,
						Constant.SHRED_PR.KEY_CURRENT_PASSWORD,edt_lgn_pwd.getText().toString().trim());
				
				startActivity(new Intent(LoginActivity.this,
						HomeScreenActivity.class));
				finish();
			} else {
				Toast.makeText(LoginActivity.this, "Wrong id/password",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
			Toast.makeText(LoginActivity.this, "Wrong id/password",
					Toast.LENGTH_SHORT).show();
		}
	}
}
