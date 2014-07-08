package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class SettingAccountListActivity extends Activity implements
		ActionBar.OnNavigationListener {
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settting_list_screen);
//
//		UtilInList.makeActionBarFullBlack(SettingAccountListActivity.this);

		listView = (ListView) findViewById(R.id.lst_setting);

		String[] values = new String[] { "Change Password", "Billing Details",
				"Invite", "Notification Settings", "Terms & Conditions" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.setting_activity_row, R.id.txt_setting_lst_title,
				values);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (position == 0) {
					startActivity(new Intent(SettingAccountListActivity.this,
							ChangePasswordActivity.class));
				} else if (position == 1) {
					startActivity(new Intent(SettingAccountListActivity.this,
							NoCardActivity.class));
				} else if (position == 2) {
					startActivity(new Intent(SettingAccountListActivity.this,
							InviteActivity.class));
				} else if (position == 3) {
					startActivity(new Intent(SettingAccountListActivity.this,
							NotificationsSettingsActivity.class));
				} else if (position == 4) {
					startActivity(new Intent(SettingAccountListActivity.this,
							TermsConditionsActivity.class));
				}

			}
		});

		if (UtilInList.isInternetConnectionExist(getApplicationContext())) {
			new Push_notificationsAsyncTask(getApplicationContext())
					.execute("");
		}

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
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.e("Name Value Pair", nameValuePairs.toString());
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API_LIVE
							+ Constant.ACTIONS.PUSHNOTIFICATIONS
							+ "?apiMode=VIP&json=true"
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									SettingAccountListActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));
			Log.e("Response In Activity-->", response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// fragment_addconnection_search

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