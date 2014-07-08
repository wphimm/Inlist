package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class ProfileActivity extends Activity implements
		ActionBar.OnNavigationListener {

	EditText editFirst, editLast, editEmail, editPhone;
	Button btnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		init();

		editFirst.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_FIRSTNAME));
		editLast.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_LASTNAME));
		editEmail.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_EMAIL));
		editPhone.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_PHONE));

		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editFirst.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editLast.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);

				if (isValid()) {
					if (UtilInList
							.isInternetConnectionExist(getApplicationContext())) {
						new ProfileAsyncTask(ProfileActivity.this)
								.execute("");
					} else {
						UtilInList.validateDialog(getApplicationContext(), ""+Constant.network_error, Constant.AppName);
					}
				}
			}
		});

	}

	private void init() {
		// TODO Auto-generated method stub
		editFirst = (EditText) findViewById(R.id.editFirst);
		editLast = (EditText) findViewById(R.id.editLast);
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPhone = (EditText) findViewById(R.id.editPhone);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_profile_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_logout:
			// search action
			AlertDialog.Builder alert = new AlertDialog.Builder(
					ProfileActivity.this);
			alert.setTitle(Constant.AppName);
			alert.setMessage("Are you sure you want to logout?");
			alert.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (UtilInList
									.isInternetConnectionExist(getApplicationContext())) {
								/*new LogoutAsyncTask(ProfileActivity.this)
										.execute("");*/
								UtilInList.WriteSharePrefrence(ProfileActivity.this,
										Constant.SHRED_PR.KEY_LOGIN_STATUS, "false");
								finish();
							} else {
								UtilInList.validateDialog(getApplicationContext(), ""+Constant.network_error, Constant.AppName);
							}

						}
					});
			alert.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
			alert.create();
			alert.show();

			return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class LogoutAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public LogoutAsyncTask(Context context) {
			dialog = new MyProgressbar(context);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.e("Name Value Pair", nameValuePairs.toString());
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API
							+ Constant.ACTIONS.LOGOUT
							+ "?apiMode=VIP&json=true"
							+ "&device_id="
							+ UtilInList.getDeviceId(getApplicationContext())
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									ProfileActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));
			Log.e("Response In Activity-->", ">>" + response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// fragment_addconnection_search

			try {
				if (dialog != null) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (result != null) {
				try {
					JSONObject jObject = new JSONObject(result);
					String str_temp = jObject.getString("status");

					if (str_temp.equals("success")) {

					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isValid() {
		// TODO Auto-generated method stub
		if (editFirst.getText().toString().trim().length() < 2) {
			UtilInList.validateDialog(getApplicationContext(), "first name must be minimum 2 characters", Constant.ERRORS.OOPS);
			return false;
		}
		if (editLast.getText().toString().trim().length() < 2) {
			UtilInList.validateDialog(getApplicationContext(), "last name must be minimum 2 characters", Constant.ERRORS.OOPS);
			return false;
		}
		if (editEmail.getText().toString().trim().length() == 0) {
			UtilInList.validateDialog(getApplicationContext(), "please enter email", Constant.ERRORS.OOPS);
			return false;
		}
		if ((android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()
				.toString().trim()).matches()) == false) {
			UtilInList.validateDialog(getApplicationContext(), "please enter valid email", Constant.ERRORS.OOPS);
			return false;
		}
		if (editPhone.getText().toString().trim().length() < 10) {
			UtilInList.validateDialog(getApplicationContext(), "phone must be minimum 10 characters", Constant.ERRORS.OOPS);
			return false;
		}
		return true;
	}

	public class ProfileAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public ProfileAsyncTask(Context context) {
			dialog = new MyProgressbar(context);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			/*nameValuePairs.add(new BasicNameValuePair("first_name", ""+editFirst.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("last_name", ""+editLast.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("email", ""+editEmail.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("phone", ""+editPhone.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("PHPSESSIONID", ""+UtilInList.ReadSharePrefrence(
					ProfileActivity.this,
					Constant.SHRED_PR.KEY_SESSIONID)));
			Log.e("Name Value Pair", nameValuePairs.toString());
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API
							+ Constant.ACTIONS.PROFILE
							+ "?apiMode=VIP&json=true");*/
			
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API
							+ Constant.ACTIONS.PROFILE
							+ "?apiMode=VIP&json=true"
							+ "&first_name="
							+ editFirst.getText().toString().trim()
							+ "&last_name="
							+ editLast.getText().toString().trim()
							+ "&email="
							+ editEmail.getText().toString().trim()
							+ "&phone="
							+ editPhone.getText().toString().trim()
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									ProfileActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));
			
			Log.e("Response In Activity-->", ">>"+response);
			
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// fragment_addconnection_search

			try {
				if (dialog != null) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (result != null) {
				try {
					JSONObject jObject = new JSONObject(result);

					try {
						if (jObject.getString("success").equals("true")) {
							UtilInList.validateDialog(getApplicationContext(), jObject
									.getJSONArray("messages").getString(0),
									Constant.AppName);
							editFirst.setText("");
							editLast.setText("");
							editEmail.setText("");
							editPhone.setText("");
						
						} else {
							UtilInList.validateDialog(getApplicationContext(), jObject
									.getJSONArray("errors").getString(0),
									Constant.ERRORS.OOPS);
						}
					} catch (Exception e) {
						Log.v("", "Exception : " + e);
					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
