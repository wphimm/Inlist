package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class ChangePasswordActivity extends Activity implements
		ActionBar.OnNavigationListener {

	TextView txt_forgot_pwd;
	EditText editCurrentPassword, editNewPassword, editConfirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_screen);

		init();

		txt_forgot_pwd.setText(Html.fromHtml("<p><u>"
				+ getString(R.string.forgot_pwd) + "</u></p>"));
	}

	private void init() {
		// TODO Auto-generated method stub
		txt_forgot_pwd = (TextView) findViewById(R.id.txt_forgot_pwd);

		editCurrentPassword = (EditText) findViewById(R.id.edt_current_pwd);
		editNewPassword = (EditText) findViewById(R.id.edt_new_pwd);
		editConfirmPassword = (EditText) findViewById(R.id.edt_confirm_pwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_add_card_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_save:

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editCurrentPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(editNewPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(editConfirmPassword.getWindowToken(), 0);

			if (isValidate()) {
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					new ChangePasswordAsyncTask(ChangePasswordActivity.this)
							.execute("");
				} else {
					Toast.makeText(getApplicationContext(),
							"" + Constant.network_error, Toast.LENGTH_SHORT)
							.show();

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

	public class ChangePasswordAsyncTask extends
			AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public ChangePasswordAsyncTask(Context context) {
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
							+ Constant.ACTIONS.CHANGE_PASSWORD
							+ "?apiMode=VIP&json=true"
							+ "&current_password="
							+ UtilInList.ReadSharePrefrence(
									ChangePasswordActivity.this,
									Constant.SHRED_PR.KEY_CURRENT_PASSWORD)
							+ "&password="
							+ editNewPassword.getText().toString().trim()
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									ChangePasswordActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));
			Log.e("Response In Activity-->", response);
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

					InListApplication.getParty_area().clear();

					if (str_temp.equals("success")) {
						String messages = jObject.getString("messages");
						if (messages.length() > 4) {
							messages = messages.substring(2, messages.length());
							messages = messages.substring(0,
									messages.length() - 2);
							Toast.makeText(getApplicationContext(),
									"" + messages, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Your password was successfully changed.",
									Toast.LENGTH_SHORT).show();
						}
						finish();
					} else {
						String errors = jObject.getString("errors");
						if (errors.length() > 4) {
							errors = errors.substring(2, errors.length());
							errors = errors.substring(0, errors.length() - 2);
							Toast.makeText(getApplicationContext(),
									"" + errors, Toast.LENGTH_SHORT).show();
						}
					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private boolean isValidate() {
		// TODO Auto-generated method stub
		/*
		 * if (editCurrentPassword.getText().toString().trim().length() == 0) {
		 * Toast.makeText(getApplicationContext(),
		 * "please enter current password", Toast.LENGTH_SHORT).show(); return
		 * false; }
		 */
		if (editNewPassword.getText().toString().trim().length() == 0) {
			UtilInList.validateDialog(ChangePasswordActivity.this,
					Constant.ERRORS.PLZ_NEW_PWD, Constant.ERRORS.OOPS);

			return false;
		}
		if (editConfirmPassword.getText().toString().trim().length() == 0) {
			UtilInList.validateDialog(getApplicationContext(),
					Constant.ERRORS.PLZ_CNFRM_PWD, Constant.ERRORS.OOPS);

			return false;
		}
		if (!editNewPassword.getText().toString().trim()
				.equals("" + editConfirmPassword.getText().toString().trim())) {
			UtilInList.validateDialog(getApplicationContext(),
					Constant.ERRORS.PWD_NOT_MATCH, Constant.ERRORS.OOPS);
			return false;
		}

		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
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
