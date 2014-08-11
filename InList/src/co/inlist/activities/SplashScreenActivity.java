package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class SplashScreenActivity extends Activity {

	public static final int SPLASH_TIMEOUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		try {
			Parse.initialize(this, Constant.YOUR_APP_ID,
					Constant.YOUR_CLIENT_KEY);
			PushService
					.setDefaultPushCallback(this, SplashScreenActivity.class);
			ParseInstallation.getCurrentInstallation().saveInBackground();
			ParseAnalytics.trackAppOpened(getIntent());
			Log.e("parse id:", ">>"
					+ ParseInstallation.getCurrentInstallation()
							.getInstallationId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (UtilInList.isInternetConnectionExist(getApplicationContext())) {

			new AddDeviceAsyncTask().execute();

		} else {
			UtilInList.validateDialog(SplashScreenActivity.this, ""
					+ Constant.ERRORS.NO_INTERNET_CONNECTION,
					Constant.ERRORS.NO_INTERNET_CONNECTION_TITLE);

		}

	}

	public class AddDeviceAsyncTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("device_type", "android"));
			params1.add(new BasicNameValuePair("device_id", ""
					+ UtilInList.getDeviceId(getApplicationContext())));
			params1.add(new BasicNameValuePair("parse_object_id", ""
					+ ParseInstallation.getCurrentInstallation().getObjectId()));
			params1.add(new BasicNameValuePair("PHPSESSIONID", ""
					+ UtilInList.ReadSharePrefrence(SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID)));

			String response = UtilInList.postData(getApplicationContext(),
					params1, Constant.API + Constant.ACTIONS.ADD_DEVICE);
			Log.e("Response In Activity-->", response);

			return response;
		}

		@Override
		protected void onPostExecute(String result1) {
			// TODO Auto-generated method stub
			super.onPostExecute(result1);

			Log.e("result..", ">>" + result1);

			try {
				JSONObject result = new JSONObject(result1);
				UtilInList.writeToFile(result.getJSONObject("data").toString(),
						Constant.PREF_VAL.OFFLINE_FILE_PRE_REGISTER,
						SplashScreenActivity.this);

				String str_temp = result.getString("status");
				if (str_temp.equals("success")) {

					UtilInList.WriteSharePrefrence(
							SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID,
							result.getJSONObject("session")
									.getJSONObject("userInfo")
									.getString("sessionId"));

					UtilInList.WriteSharePrefrence(
							SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_VIP_STATUS,
							result.getJSONObject("session")
									.getJSONObject("userInfo")
									.getString("vip_status"));

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			new PrepRegAsyncTask().execute();

		}

	}

	class PrepRegAsyncTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("device_type", "android"));
			params1.add(new BasicNameValuePair("PHPSESSIONID", ""
					+ UtilInList.ReadSharePrefrence(SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID)));

			String response = UtilInList.postData(getApplicationContext(),
					params1, Constant.API + Constant.ACTIONS.PREPARE_REGISTER);
			Log.e("Response In Activity-->", response);

			return response;
		}

		@Override
		protected void onPostExecute(String result1) {
			// TODO Auto-generated method stub
			super.onPostExecute(result1);

			try {
				/*
				 * Prepare registration response write in file mode private
				 */
				JSONObject result = new JSONObject(result1);
				UtilInList.writeToFile(result.getJSONObject("data").toString(),
						Constant.PREF_VAL.OFFLINE_FILE_PRE_REGISTER,
						SplashScreenActivity.this);

				String str_temp = result.getString("status");
				if (str_temp.equals("success")) {
					JSONObject jObjectData = new JSONObject(
							result.getString("data"));
					JSONArray data = jObjectData.getJSONArray("music_types");
					Log.e("Length of json array ----->", "" + data.length());
					InListApplication.getList_music_types().clear();
					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("music_type_id",
								"" + obj.getString("music_type_id"));
						map.put("title", "" + obj.getString("title"));
						InListApplication.getList_music_types().add(map);
					}

					UtilInList.WriteSharePrefrence(
							SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID,
							result.getJSONObject("session")
									.getJSONObject("userInfo")
									.getString("sessionId"));
					UtilInList.WriteSharePrefrence(
							SplashScreenActivity.this,
							Constant.SHRED_PR.KEY_VIP_STATUS,
							result.getJSONObject("session")
									.getJSONObject("userInfo")
									.getString("vip_status"));

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			new PartyAreaAsyncTask(getApplicationContext()).execute("");
		}

	}

	public class PartyAreaAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public PartyAreaAsyncTask(Context context) {
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
			String response = UtilInList.postData(getApplicationContext(),
					nameValuePairs, "" + Constant.API
							+ Constant.ACTIONS.PARTY_AREA
							+ "?apiMode=VIP&json=true");
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

					InListApplication.getParty_area().clear();

					if (str_temp.equals("success")) {
						JSONArray data = jObject.getJSONArray("data");
						Log.e("Length of json array ----->", "" + data.length());
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("party_area_id",
									"" + obj.getString("party_area_id"));
							map.put("title", "" + obj.getString("title"));
							map.put("icon", "" + obj.getString("icon"));
							map.put("latitude", "" + obj.getString("latitude"));
							map.put("longitude",
									"" + obj.getString("longitude"));
							map.put("timezone", "" + obj.getString("timezone"));
							map.put("is_dst", "" + obj.getString("is_dst"));
							map.put("timezone_text",
									"" + obj.getString("timezone_text"));
							map.put("order", "" + obj.getString("order"));
							map.put("status", "" + obj.getString("status"));
							map.put("distance", "" + obj.getString("distance"));

							InListApplication.getParty_area().add(map);
						}

					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			checkPrefsAndSplash();
		}

	}

	private void checkPrefsAndSplash() {
		try {
			new Timer().schedule(new TimerTask() {
				public void run() {
					proceed();
				}
			}, SPLASH_TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void proceed() {
		try {
			if (this.isFinishing()) {

				return;
			}

			if (UtilInList.ifConditionDataExist(SplashScreenActivity.this)) {
				if (UtilInList.ReadSharePrefrence(SplashScreenActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {
					startActivity(new Intent(SplashScreenActivity.this,
							HomeScreenActivity.class));
					overridePendingTransition(R.anim.enter_from_bottom,
							R.anim.hold_bottom);
				} else {
					startActivity(new Intent(SplashScreenActivity.this,
							LeadingActivity.class));
					overridePendingTransition(R.anim.enter_from_bottom,
							R.anim.hold_bottom);
				}
			} else {

				startActivity(new Intent(SplashScreenActivity.this,
						LeadingActivity.class));
				overridePendingTransition(R.anim.enter_from_bottom,
						R.anim.hold_bottom);

			}
			finish();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

}
