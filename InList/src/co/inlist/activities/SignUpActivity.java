package co.inlist.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class SignUpActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	private EditText edt_su_fname;
	private EditText edt_su_lname;
	private EditText edt_su_e_mail;
	private EditText edt_su_pwd;
	private EditText edt_su_phno;
	private EditText edt_su_ans;
	private RelativeLayout rl_fb;
	private TextView txt_su_que;

	private Facebook facebook = new Facebook(Constant.FB_API_KEY);
	private AsyncFacebookRunner mAsyncRunner;
	private SharedPreferences mPrefs;
	private static String question_id;
	private boolean fb_regiter_flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_screen);
		init();

		txt_su_que.setText(getQuestion());

		rl_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToFacebook();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_signup_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
		long expires = mPrefs.getLong("access_expires", 0);

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(SignUpActivity.this, new String[] { "email",
					"publish_stream" }, new DialogListener() {

				@Override
				public void onCancel() {

					try {
						facebook.logout(SignUpActivity.this);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onComplete(Bundle values) {
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
					final JSONObject jObj = new JSONObject(response);

					Log.v("",
							"chk facebook response : "
									+ jObj.getString("email"));

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								edt_su_e_mail.setText(jObj.getString("email"));

								edt_su_fname.setText(jObj
										.getString("first_name"));
								edt_su_lname.setText(jObj
										.getString("last_name"));

								fb_regiter_flag = true;
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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

	private void init() {

		edt_su_fname = (EditText) findViewById(R.id.edt_su_fname);
		edt_su_lname = (EditText) findViewById(R.id.edt_su_lname);
		edt_su_e_mail = (EditText) findViewById(R.id.edt_su_e_mail);
		edt_su_pwd = (EditText) findViewById(R.id.edt_su_pwd);
		edt_su_phno = (EditText) findViewById(R.id.edt_su_phno);
		edt_su_ans = (EditText) findViewById(R.id.edt_su_ans);

		txt_su_que = (TextView) findViewById(R.id.txt_su_que);

		rl_fb = (RelativeLayout) findViewById(R.id.rl_fb);

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

			if (edt_su_fname.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_FIRST_NAME);
			} else if (edt_su_lname.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_LAST_NAME);
			} else if (edt_su_e_mail.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_EMAIL);
			} else if (edt_su_pwd.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_PASSWORD);
			} else if (edt_su_phno.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_CONTACT_NO);
			} else if (edt_su_ans.getText().toString().trim().equals("")) {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.PLZ_ANS);

			} else {

				if (fb_regiter_flag) {

					new WebServiceDataCollectorAsyncTask(
							Constant.API
									+ String.format(
											Constant.ACTIONS.REGISTER_FB,
											Constant.TAGS.VIP,
											"true",
											UtilInList
													.getDeviceId(SignUpActivity.this),
											edt_su_e_mail.getText().toString()
													.trim(),
											edt_su_pwd.getText().toString()
													.trim(),
											edt_su_fname.getText().toString()
													.trim(),
											edt_su_lname.getText().toString()
													.trim(),
											edt_su_phno.getText().toString()
													.trim(),
											question_id,
											edt_su_ans.getText().toString(),
											facebook.getAccessToken()
													.toString().trim(), "true")
											.replace(" ", "%20"),
							SignUpActivity.this).execute();
				} else {
					new WebServiceDataCollectorAsyncTask(
							Constant.API
									+ String.format(
											Constant.ACTIONS.REGISTRATION,
											Constant.TAGS.VIP,
											"true",
											UtilInList
													.getDeviceId(SignUpActivity.this),
											edt_su_e_mail.getText().toString()
													.trim(),
											edt_su_pwd.getText().toString()
													.trim(),
											edt_su_fname.getText().toString()
													.trim(),
											edt_su_lname.getText().toString()
													.trim(),
											edt_su_phno.getText().toString()
													.trim(),
											question_id,
											edt_su_ans.getText().toString()
													.trim(), "true").replace(
											" ", "%20"), SignUpActivity.this)
							.execute();
				}

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
			Log.v("", ">>>>> chk register response : " + result.toString());
			if (result.getString("success").toString().equals("true")) {
				UtilInList.WriteSharePrefrence(SignUpActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_STATUS, "true");
				UtilInList.WriteSharePrefrence(SignUpActivity.this,
						Constant.SHRED_PR.KEY_USERID,
						result.getJSONObject("data").getString("user_id"));
				UtilInList.WriteSharePrefrence(SignUpActivity.this,
						Constant.SHRED_PR.KEY_EMAIL, edt_su_e_mail.getText()
								.toString().trim());
				UtilInList.WriteSharePrefrence(SignUpActivity.this,
						Constant.SHRED_PR.KEY_FIRSTNAME, edt_su_fname.getText()
								.toString().trim());
				UtilInList.WriteSharePrefrence(SignUpActivity.this,
						Constant.SHRED_PR.KEY_LASTNAME, edt_su_lname.getText()
								.toString().trim());

				// startActivity(new Intent(SignUpActivity.this,
				// HomeScreenActivity.class));

				Intent i = new Intent(getApplicationContext(),
						HomeScreenActivity.class);
				i.setAction(Intent.ACTION_SEND);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

				finish();

			} else {
				UtilInList.makeToast(SignUpActivity.this,
						Constant.ERRORS.SOMETHING_GOES_WRONG);
			}

		} catch (Exception e) {
			UtilInList.validateDialog(SignUpActivity.this,
					Constant.ERRORS.SOMETHING_GOES_WRONG);
			Log.v("", "Excption : " + e);
		}
	}

	public String getQuestion() {
		String prepare_reg_data = UtilInList.readFromFile(
				Constant.PREF_VAL.OFFLINE_FILE_PRE_REGISTER,
				SignUpActivity.this);
		String question = "";

		try {
			JSONObject jObj = new JSONObject(prepare_reg_data)
					.getJSONObject("membership_question");

			question = jObj.getString("membership_question_text").toString();

			question_id = jObj.getString("membership_question_id").toString();

			Log.v("", ">>> Que_id : " + question_id);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return question;
	}

}
