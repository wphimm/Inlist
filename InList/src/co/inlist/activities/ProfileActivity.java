package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.inlist.adapter.ReservedEventsAdapter;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class ProfileActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	TextView txtName, txtEmail, txtPhone;
	public static ProfileActivity profObj;
	RelativeLayout relativeCategories, relativeArchive, relativeVip;
	private TextView txt_vip_membership_req;

	View viewCategories, viewArchive;
	ListView lst;
	ReservedEventsAdapter adapterReservedEvents;

	Typeface typeAkzidgrobeligex, typeAkzidgrobemedex, typeAvenir,
			typeLeaguegothic_condensedregular;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		profObj = this;

		init();

		actionBarAndButtonActions();

		txtName.setTypeface(typeAkzidgrobemedex);
		txtEmail.setTypeface(typeAkzidgrobemedex);
		txtPhone.setTypeface(typeAkzidgrobemedex);
		txt_vip_membership_req.setTypeface(typeAkzidgrobemedex);

		txtName.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_FIRSTNAME)
				+ " "
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_LASTNAME));
		txtEmail.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_EMAIL));
		txtPhone.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_PHONE));

		adapterReservedEvents = new ReservedEventsAdapter(
				InListApplication.getListReservedEvents(),
				ProfileActivity.this, ProfileActivity.this);

		lst.setAdapter(adapterReservedEvents);

		Handler hn = new Handler();
		hn.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {

					int pageNo = ((int) (InListApplication
							.getListReservedEvents().size() / 10)) + 1;

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("page", "" + pageNo));
					params.add(new BasicNameValuePair("sort_option", "by_date"));
					params.add(new BasicNameValuePair("device_type", "android"));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									ProfileActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(ProfileActivity.this,
							params, Constant.API
									+ Constant.ACTIONS.RESERVATION_LIST)
							.execute();

				} else {
					UtilInList.validateDialog(ProfileActivity.this, "" + ""
							+ Constant.network_error, Constant.ERRORS.OOPS);

				}
			}
		}, 100);

		relativeCategories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewCategories.setVisibility(View.VISIBLE);
				viewArchive.setVisibility(View.GONE);
			}
		});

		relativeArchive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewArchive.setVisibility(View.VISIBLE);
				viewCategories.setVisibility(View.GONE);
			}
		});

		relativeVip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this,
						VipMemberShipActivity.class));
				overridePendingTransition(R.anim.enter_from_left,
						R.anim.hold_bottom);
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapterReservedEvents.notifyDataSetChanged();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		txtName = (TextView) findViewById(R.id.txtName);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtPhone = (TextView) findViewById(R.id.txtPhone);
		relativeCategories = (RelativeLayout) findViewById(R.id.linearCategory);
		relativeArchive = (RelativeLayout) findViewById(R.id.linearArchive);
		relativeVip = (RelativeLayout) findViewById(R.id.relativeVIP);
		viewCategories = (View) findViewById(R.id.viewCategories);
		viewArchive = (View) findViewById(R.id.viewArchive);
		lst = (ListView) findViewById(R.id.lst);

		typeAkzidgrobeligex = Typeface.createFromAsset(getAssets(),
				"akzidgrobeligex.ttf");
		typeAkzidgrobemedex = Typeface.createFromAsset(getAssets(),
				"helve_unbold.ttf");
		typeLeaguegothic_condensedregular = Typeface.createFromAsset(
				getAssets(), "leaguegothic_condensedregular.otf");
		typeAvenir = Typeface.createFromAsset(getAssets(), "avenir.ttc");

		txt_vip_membership_req = (TextView) findViewById(R.id.txt_vip_membership_req);

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
			overridePendingTransition(R.anim.hold_top, R.anim.exit_in_left);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTaskComplete(JSONObject jObject) {
		// TODO Auto-generated method stub

		InListApplication.getListReservedEvents().clear();
		try {
			String str_temp = jObject.getString("status");
			if (str_temp.equals("success")) {
				JSONObject jObjectData = new JSONObject(
						jObject.getString("data"));
				JSONArray data = jObjectData.getJSONArray("entries");
				Log.e("Length of json array ----->", "" + data.length());
				for (int i = 0; i < data.length(); i++) {
					JSONObject obj = data.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("order_id", "" + obj.getString("order_id"));
					map.put("event_id", "" + obj.getString("event_id"));
					map.put("event_title", "" + obj.getString("event_title"));
					map.put("event_start_date",
							"" + obj.getString("event_start_date"));
					map.put("event_start_time",
							"" + obj.getString("event_start_time"));
					map.put("event_min_price",
							"" + obj.getString("event_min_price"));
					map.put("event_description",
							"" + obj.getString("event_description"));

					map.put("event_location_address",
							"" + obj.getString("event_location_address"));
					map.put("event_location_city",
							"" + obj.getString("event_location_city"));
					map.put("event_location_state",
							"" + obj.getString("event_location_state"));
					map.put("event_location_zip",
							"" + obj.getString("event_location_zip"));
					map.put("event_location_latitude",
							"" + obj.getString("event_location_latitude"));
					map.put("event_location_longitude",
							"" + obj.getString("event_location_longitude"));
					map.put("event_location_club",
							"" + obj.getString("event_location_club"));
					try {
						map.put("event_end_time",
								"" + obj.getString("event_end_time"));
						map.put("tables_total",
								"" + obj.getString("tables_total"));
						map.put("tables_available",
								"" + obj.getString("tables_available"));
					} catch (Exception e) {
						// TODO: handle exception
					}

					map.put("tax", "" + obj.getString("tax"));
					map.put("gratuity", "" + obj.getString("gratuity"));

					map.put("event_poster_url",
							"" + obj.getString("event_poster_url"));

					map.put("atmosphere", "" + obj.getString("atmosphere"));
					map.put("music_type", "" + obj.getString("music_type"));

					InListApplication.getListReservedEvents().add(map);
				}

				Log.i("size:", ""
						+ InListApplication.getListReservedEvents().size());
			} else {

				UtilInList.validateDialog(ProfileActivity.this, jObject
						.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}

		} catch (JSONException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapterReservedEvents.notifyDataSetChanged();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
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

		action_button.setBackgroundResource(R.drawable.edit_onclick);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this,
						EditProfileActivity.class));
				overridePendingTransition(R.anim.enter_from_left,
						R.anim.hold_bottom);
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.hold_top, R.anim.exit_in_left);
	}
}
