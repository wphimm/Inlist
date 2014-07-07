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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class NotificationsSettingsActivity extends Activity implements
		ActionBar.OnNavigationListener {

	ImageButton btnDailyNotification, btnBillingIssues;
	boolean flagDaily = true;
	boolean flagDailyNotification = true, flagBillingIssues = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_settings_screen);

		init();

		btnDailyNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flagDaily = true;
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					new Push_notificationsAsyncTask(NotificationsSettingsActivity.this)
							.execute("");
				} else {
					Toast.makeText(getApplicationContext(),
							"" + Constant.network_error, Toast.LENGTH_SHORT)
							.show();

				}

			}
		});

		btnBillingIssues.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flagDaily = false;
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					new Push_notificationsAsyncTask(NotificationsSettingsActivity.this)
							.execute("");
				} else {
					Toast.makeText(getApplicationContext(),
							"" + Constant.network_error, Toast.LENGTH_SHORT)
							.show();

				}

			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		btnDailyNotification = (ImageButton) findViewById(R.id.btnDailyNotification);
		btnBillingIssues = (ImageButton) findViewById(R.id.btnBillingIssues);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

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

	public class Push_notificationsAsyncTask extends
			AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public Push_notificationsAsyncTask(Context context) {
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

			String strSwitch, strType;
			if (flagDaily) {
				strType = "daily";
				if (flagDailyNotification) {
					strSwitch = "enable";
				} else {
					strSwitch = "disable";
				}
			} else {
				strType = "billing";
				if (flagBillingIssues) {
					strSwitch = "enable";
				} else {
					strSwitch = "disable";
				}
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.e("Name Value Pair", nameValuePairs.toString());
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API_LIVE
							+ Constant.ACTIONS.PUSHNOTIFICATIONS
							+ strSwitch
							+ "/?apiMode=VIP&json=true"
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									NotificationsSettingsActivity.this,
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
						if (flagDaily) {
							if (flagDailyNotification) {
								btnDailyNotification
										.setBackgroundResource(R.drawable.off);
								flagDailyNotification = false;
							} else {
								btnDailyNotification
										.setBackgroundResource(R.drawable.on);
								flagDailyNotification = true;
							}
						} else {
							if (flagBillingIssues) {
								flagBillingIssues = false;
								btnBillingIssues
										.setBackgroundResource(R.drawable.off);
							} else {
								btnBillingIssues
										.setBackgroundResource(R.drawable.on);
								flagBillingIssues = true;
							}
						}
					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
