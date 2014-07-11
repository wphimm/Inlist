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
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class SettingAccountListActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settting_list_screen);
		//
		// UtilInList.makeActionBarFullBlack(SettingAccountListActivity.this);

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
					
					UtilInList.WriteSharePrefrence(SettingAccountListActivity.this,
							Constant.SHRED_PR.KEY_ADDCARD_FROM, "0");
					
					if (UtilInList
							.ReadSharePrefrence(
									SettingAccountListActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_ADDED)
							.toString().equals("1")) {
						startActivity(new Intent(
								SettingAccountListActivity.this,
								AddCardActivity.class));
					} else {
						startActivity(new Intent(
								SettingAccountListActivity.this,
								NoCardActivity.class));
					}
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
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("device_id", ""
					+ UtilInList.getDeviceId(getApplicationContext())));
			params.add(new BasicNameValuePair("device_type", "android"));
			params.add(new BasicNameValuePair("PHPSESSIONID", ""
					+ UtilInList.ReadSharePrefrence(
							SettingAccountListActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID)));

			new WebServiceDataPosterAsyncTask(SettingAccountListActivity.this,
					params, Constant.API + Constant.ACTIONS.PUSHNOTIFICATIONS
							+ "/?apiMode=VIP&json=true").execute();
		}

	}

	@Override
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub
		try {
			if (result.getString("success").equals("true")) {
				UtilInList.WriteSharePrefrence(SettingAccountListActivity.this,
						Constant.SHRED_PR.KEY_DAILY,
						result.getJSONObject("data").getString("daily"));
				UtilInList.WriteSharePrefrence(SettingAccountListActivity.this,
						Constant.SHRED_PR.KEY_BILLING,
						result.getJSONObject("data").getString("billing"));
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
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