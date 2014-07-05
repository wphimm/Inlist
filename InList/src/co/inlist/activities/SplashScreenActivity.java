package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import co.inlist.activities.HomeScreenActivity.EventsAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

public class SplashScreenActivity extends Activity {

	public static final int SPLASH_TIMEOUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		if (UtilInList.isInternetConnectionExist(getApplicationContext())) {
			new PartyAreaAsyncTask(getApplicationContext()).execute("");
		} else {
			Toast.makeText(getApplicationContext(),
					"" + Constant.network_error, Toast.LENGTH_SHORT).show();

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

			// if (CommonUtility.ReadSharePrefrence(SplashScreenActivity.this,
			// Constant.SHRED_PR.KEY_USERID).equals(Constant.BLANK)) {
			// startActivity(new Intent(SplashScreenActivity.this,
			// Leading_Activity.class));
			// } else {

			if (UtilInList.ifConditionDataExist(SplashScreenActivity.this)) {
				if (UtilInList.ReadSharePrefrence(SplashScreenActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {
					startActivity(new Intent(SplashScreenActivity.this,
							HomeScreenActivity.class));
				} else {
					startActivity(new Intent(SplashScreenActivity.this,
							LeadingActivity.class));
				}
			} else {

				startActivity(new Intent(SplashScreenActivity.this,
						LeadingActivity.class));

			}
			finish();

		} catch (Exception e) {
			e.printStackTrace();
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
			String response = UtilInList.postData(nameValuePairs, ""
					+ Constant.API_LIVE + Constant.ACTIONS.PARTY_AREA
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
						Log.e("Date Splas ----->", "" + result);
						for (int i = 0; i < data.length(); i++) {
							JSONObject obj = data.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("party_area_id",
									"" + obj.getString("party_area_id"));
							map.put("title", "" + obj.getString("title"));
							map.put("icon", "" + obj.getString("icon"));
							map.put("latitude", "" + obj.getString("latitude"));
							map.put("longitude", "" + obj.getString("longitude"));
							map.put("timezone", "" + obj.getString("timezone"));
							map.put("is_dst", "" + obj.getString("is_dst"));
							map.put("timezone_text", "" + obj.getString("timezone_text"));
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

}
