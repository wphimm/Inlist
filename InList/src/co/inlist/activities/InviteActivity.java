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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class InviteActivity extends Activity implements
		ActionBar.OnNavigationListener {

	EditText editFirst, editLast, editEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_person_screen);

		// UtilInList.makeActionBarFullBlack(InviteActivity.this);

		init();

		actionBarAndButtonActions();
	}

	private void init() {
		// TODO Auto-generated method stub
		editFirst = (EditText) findViewById(R.id.edt_invite_first_name);
		editLast = (EditText) findViewById(R.id.edt_invite_last_name);
		editEmail = (EditText) findViewById(R.id.edt_invite_e_mail);
	}

	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_invite_actions, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }

	/**
	 * On selecting action bar icons
	 * */
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Take appropriate action for each action item click
	// switch (item.getItemId()) {
	// case R.id.action_send:
	//
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(editFirst.getWindowToken(), 0);
	// imm.hideSoftInputFromWindow(editLast.getWindowToken(), 0);
	// imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
	//
	// if (isValid()) {
	// if (UtilInList
	// .isInternetConnectionExist(getApplicationContext())) {
	// new InviteAsyncTask(InviteActivity.this).execute("");
	// } else {
	// UtilInList.validateDialog(InviteActivity.this, "" + ""
	// + Constant.network_error, Constant.ERRORS.OOPS);
	//
	// }
	// }
	// return true;
	//
	// case android.R.id.home:
	// finish();
	// return true;
	//
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	public class InviteAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public InviteAsyncTask(Context context) {
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
							+ Constant.ACTIONS.USER_INVITE
							+ "?apiMode=VIP&json=true"
							+ "&first_name="
							+ editFirst.getText().toString().trim()
							+ "&last_name="
							+ editLast.getText().toString().trim()
							+ "&email="
							+ editEmail.getText().toString().trim()
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									InviteActivity.this,
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

					try {
						if (jObject.getString("success").equals("true")) {
							UtilInList.validateDialog(
									InviteActivity.this,
									jObject.getJSONArray("messages").getString(
											0), Constant.AppName);
							editFirst.setText("");
							editLast.setText("");
							editEmail.setText("");

						} else {
							UtilInList
									.validateDialog(InviteActivity.this,
											jObject.getJSONArray("errors")
													.getString(0),
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

	private boolean isValid() {
		// TODO Auto-generated method stub
		if (editFirst.getText().toString().trim().length() < 2) {
			UtilInList.validateDialog(InviteActivity.this, "" + ""
					+ Constant.ERRORS.PLZ_FIRST_NAME, Constant.ERRORS.OOPS);
			return false;
		}
		if (editLast.getText().toString().trim().length() < 2) {
			UtilInList.validateDialog(InviteActivity.this, "" + ""
					+ Constant.ERRORS.PLZ_LAST_NAME, Constant.ERRORS.OOPS);
			return false;
		}
		if (editEmail.getText().toString().trim().length() == 0) {
			UtilInList.validateDialog(InviteActivity.this, "" + ""
					+ Constant.ERRORS.PLZ_EMAIL, Constant.ERRORS.OOPS);
			return false;
		}
		if ((android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()
				.toString().trim()).matches()) == false) {
			UtilInList.validateDialog(InviteActivity.this, "" + ""
					+ Constant.ERRORS.PLZ_VALID_EMAIL, Constant.ERRORS.OOPS);
			return false;
		}
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	private void actionBarAndButtonActions() {

		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.login_custome_action_bar);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		actionBar.setDisplayHomeAsUpEnabled(true);

		ImageButton action_button = (ImageButton) actionBar.getCustomView()
				.findViewById(R.id.btn_action_bar);

		action_button.setBackgroundResource(R.drawable.send_onclick);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editFirst.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editLast.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);

				if (isValid()) {
					if (UtilInList
							.isInternetConnectionExist(getApplicationContext())) {
						new InviteAsyncTask(InviteActivity.this).execute("");
					} else {
						UtilInList.validateDialog(InviteActivity.this, "" + ""
								+ Constant.network_error, Constant.ERRORS.OOPS);

					}
				}

			}
		});

	}

}
