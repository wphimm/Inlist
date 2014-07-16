package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class NotificationsSettingsActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	ImageButton btnDailyNotification, btnBillingIssues;
	boolean flagDaily = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_settings_screen);

		init();

		actionBarAndButtonActions();
		btnDailyNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flagDaily = true;
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {

					String strSwitch, strType;
					if (flagDaily) {
						strType = "daily";
						if (UtilInList
								.ReadSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_DAILY).toString()
								.equals("1")) {
							strSwitch = "disable";
						} else {
							strSwitch = "enable";
						}
					} else {
						strType = "billing";
						if (UtilInList
								.ReadSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_BILLING)
								.toString().equals("1")) {
							strSwitch = "disable";
						} else {
							strSwitch = "enable";
						}
					}

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type", "" + strType));
					params.add(new BasicNameValuePair("device_type", "android"));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									NotificationsSettingsActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(
							NotificationsSettingsActivity.this, params,
							Constant.API + Constant.ACTIONS.PUSHNOTIFICATIONS
									+ strSwitch + "/?apiMode=VIP&json=true")
							.execute();
				} else {
					UtilInList.validateDialog(
							NotificationsSettingsActivity.this, "" + ""
									+ Constant.network_error,
							Constant.ERRORS.OOPS);

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

					String strSwitch, strType;
					if (flagDaily) {
						strType = "daily";
						if (UtilInList
								.ReadSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_DAILY).toString()
								.equals("1")) {
							strSwitch = "disable";
						} else {
							strSwitch = "enable";
						}
					} else {
						strType = "billing";
						if (UtilInList
								.ReadSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_BILLING)
								.toString().equals("1")) {
							strSwitch = "disable";
						} else {
							strSwitch = "enable";
						}
					}

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type", "" + strType));
					params.add(new BasicNameValuePair("device_type", "android"));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									NotificationsSettingsActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(
							NotificationsSettingsActivity.this, params,
							Constant.API + Constant.ACTIONS.PUSHNOTIFICATIONS
									+ strSwitch + "/?apiMode=VIP&json=true")
							.execute();

				} else {
					UtilInList.validateDialog(
							NotificationsSettingsActivity.this, "" + ""
									+ Constant.network_error,
							Constant.ERRORS.OOPS);

				}

			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		btnDailyNotification = (ImageButton) findViewById(R.id.btnDailyNotification);
		btnBillingIssues = (ImageButton) findViewById(R.id.btnBillingIssues);

		if (UtilInList
				.ReadSharePrefrence(NotificationsSettingsActivity.this,
						Constant.SHRED_PR.KEY_DAILY).toString().equals("1")) {
			btnDailyNotification.setBackgroundResource(R.drawable.on);
		} else {
			btnDailyNotification.setBackgroundResource(R.drawable.off);
		}

		if (UtilInList
				.ReadSharePrefrence(NotificationsSettingsActivity.this,
						Constant.SHRED_PR.KEY_BILLING).toString().equals("1")) {
			btnBillingIssues.setBackgroundResource(R.drawable.on);
		} else {
			btnBillingIssues.setBackgroundResource(R.drawable.off);
		}
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
				if (UtilInList
						.ReadSharePrefrence(NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_DAILY).toString()
						.equals("1")) {
					strSwitch = "disable";
				} else {
					strSwitch = "enable";
				}
			} else {
				strType = "billing";
				if (UtilInList
						.ReadSharePrefrence(NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_BILLING).toString()
						.equals("1")) {
					strSwitch = "disable";
				} else {
					strSwitch = "enable";
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
							+ "&type="
							+ strType
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

					if (str_temp.equals("success")) {
						if (flagDaily) {
							if (UtilInList
									.ReadSharePrefrence(
											NotificationsSettingsActivity.this,
											Constant.SHRED_PR.KEY_DAILY)
									.toString().equals("1")) {
								btnDailyNotification
										.setBackgroundResource(R.drawable.off);
								UtilInList.WriteSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_DAILY, "0");

							} else {
								btnDailyNotification
										.setBackgroundResource(R.drawable.on);
								UtilInList.WriteSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_DAILY, "1");
							}
						} else {
							if (UtilInList
									.ReadSharePrefrence(
											NotificationsSettingsActivity.this,
											Constant.SHRED_PR.KEY_BILLING)
									.toString().equals("1")) {
								UtilInList.WriteSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_BILLING, "0");
								btnBillingIssues
										.setBackgroundResource(R.drawable.off);
							} else {
								btnBillingIssues
										.setBackgroundResource(R.drawable.on);
								UtilInList.WriteSharePrefrence(
										NotificationsSettingsActivity.this,
										Constant.SHRED_PR.KEY_BILLING, "1");
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
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub
		try {
			if (result.getString("success").equals("true")) {

				if (flagDaily) {
					if (UtilInList
							.ReadSharePrefrence(
									NotificationsSettingsActivity.this,
									Constant.SHRED_PR.KEY_DAILY).toString()
							.equals("1")) {
						btnDailyNotification
								.setBackgroundResource(R.drawable.off);
						UtilInList.WriteSharePrefrence(
								NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_DAILY, "0");

					} else {
						btnDailyNotification
								.setBackgroundResource(R.drawable.on);
						UtilInList.WriteSharePrefrence(
								NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_DAILY, "1");
					}
				} else {
					if (UtilInList
							.ReadSharePrefrence(
									NotificationsSettingsActivity.this,
									Constant.SHRED_PR.KEY_BILLING).toString()
							.equals("1")) {
						UtilInList.WriteSharePrefrence(
								NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_BILLING, "0");
						btnBillingIssues.setBackgroundResource(R.drawable.off);
					} else {
						btnBillingIssues.setBackgroundResource(R.drawable.on);
						UtilInList.WriteSharePrefrence(
								NotificationsSettingsActivity.this,
								Constant.SHRED_PR.KEY_BILLING, "1");
					}
				}

			} else {
				UtilInList.validateDialog(NotificationsSettingsActivity.this,
						result.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}

	}

	private void actionBarAndButtonActions() {
		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.custome_action_bar);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		actionBar.setDisplayHomeAsUpEnabled(true);

		ImageButton action_button = (ImageButton) actionBar.getCustomView()
				.findViewById(R.id.btn_action_bar);

		action_button.setBackgroundResource(R.drawable.sign_up_action_bar);
		action_button.setVisibility(View.INVISIBLE);

	}

}
