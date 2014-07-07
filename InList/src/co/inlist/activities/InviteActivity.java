package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import co.inlist.activities.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
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

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		editFirst = (EditText) findViewById(R.id.edt_invite_first_name);
		editLast = (EditText) findViewById(R.id.edt_invite_last_name);
		editEmail = (EditText) findViewById(R.id.edt_invite_e_mail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_invite_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_send:

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editFirst.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(editLast.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);

			if (isValid()) {
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					new InviteAsyncTask(InviteActivity.this)
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

	public class InviteAsyncTask extends
			AsyncTask<String, String, String> {

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
							Toast.makeText(
									getApplicationContext(),
									"Thank you, your friend was successfully invited.",
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

	private boolean isValid() {
		// TODO Auto-generated method stub
		if (editFirst.getText().toString().trim().length() < 3) {
			Toast.makeText(getApplicationContext(),
					"Invitee's first name must be minimum 2 characters",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (editLast.getText().toString().trim().length() < 3) {
			Toast.makeText(getApplicationContext(),
					"Invitee's last name must be minimum 2 characters",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (editEmail.getText().toString().trim().length() == 0) {
			Toast.makeText(getApplicationContext(),
					"please enter invitee's email", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ((android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()
				.toString().trim()).matches()) == false) {
			Toast.makeText(getApplicationContext(), "please enter valid email",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
}
