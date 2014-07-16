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
	View viewCategories, viewArchive;
	ListView lst;
	ReservedEventsAdapter adapterReservedEvents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		profObj = this;

		init();

		actionBarAndButtonActions();

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
				InListApplication.getListReservedEvents(), ProfileActivity.this);
		
		lst.setAdapter(new ReservedEventsAdapter(InListApplication
				.getListEvents(), ProfileActivity.this));

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
			}
		});

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
	public void onTaskComplete(JSONObject jObject) {
		// TODO Auto-generated method stub
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
					map.put("event_id", "" + obj.getString("event_id"));
					map.put("event_title", "" + obj.getString("event_title"));

					InListApplication.getListReservedEvents().add(map);
				}

				Log.i("size:", "" + InListApplication.getListReservedEvents().size());
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
			}
		});

	}
}
