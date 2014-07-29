package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import co.inlist.adapter.EventsAdapter;
import co.inlist.adapter.TitleNavigationAdapter;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class HomeScreenActivity extends Activity implements
		ActionBar.OnNavigationListener, OnRefreshListener {

	// action bar
	private static ActionBar actionBar;

	// Navigation adapter
	private static TitleNavigationAdapter adapter;

	private static PullToRefreshLayout mPullToRefreshLayout;

	int selected_position = 0;
	public static HomeScreenActivity HomeScreenObj;
	private static EventsAdapter adapterEvents;
	private Context context = this;
	public static boolean flagReset, flagIfProgress;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);
		actionBar = getActionBar();
		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(false);
		// Enabling Spinner dropdown navigation
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// actionBar.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.actionbar_gradiant));

		for (int i = 0; i < InListApplication.getParty_area().size(); i++) {
			Log.e("title",
					"" + InListApplication.getParty_area().get(i).get("title")
							+ "\n");
		}
		// title drop down adapter
		adapter = new TitleNavigationAdapter(getApplicationContext(),
				InListApplication.getParty_area());
		// assigning the spinner navigation
		actionBar.setListNavigationCallbacks(adapter, this);

		HomeScreenObj = this;

		options = new DisplayImageOptions.Builder().showStubImage(0)
				.showImageForEmptyUri(0).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

		ActionBarPullToRefresh.from(this).options(Options.create().build())
				.allChildrenArePullable().listener(this)
				.setup(mPullToRefreshLayout);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_profile)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_profile:
			// search action

			if (UtilInList.ReadSharePrefrence(HomeScreenActivity.this,
					Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

				startActivity(new Intent(HomeScreenActivity.this,
						ProfileActivity.class));
				overridePendingTransition(R.anim.enter_from_bottom,
						R.anim.hold_bottom);

			} else {
				makeAlert();
			}

			return true;
		case R.id.action_settings:
			// location found

			if (UtilInList.ReadSharePrefrence(HomeScreenActivity.this,
					Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

				startActivity(new Intent(HomeScreenActivity.this,
						SettingAccountListActivity.class));
				overridePendingTransition(R.anim.enter_from_bottom,
						R.anim.hold_bottom);

			} else {
				makeAlert();
			}

			return true;

		case R.id.action_concierge:
			// location found
			if (UtilInList.ReadSharePrefrence(HomeScreenActivity.this,
					Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

				makeDialogAlert();

			} else {
				makeAlert();
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void makeDialogAlert() {

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.concierge_dialog_screen);
		dialog.show();

		LinearLayout linearCall = (LinearLayout) dialog
				.findViewById(R.id.linearCall);
		LinearLayout linearEmail = (LinearLayout) dialog
				.findViewById(R.id.linearEmail);

		linearCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(
						HomeScreenActivity.this);
				alert.setTitle("Confirmation Required");
				alert.setMessage("Are you sure you want to call the concierge at 8887057714 ?");
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent callIntent = new Intent(
										Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:8887057714"));
								startActivity(callIntent);
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
				alert.create();
				alert.show();

			}
		});

		linearEmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String strSubject = ""
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_FIRSTNAME)
						+ " "
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_LASTNAME)
						+ " Re: General Questions";

				String strExtra = "\n\n\n\n\nContact Information:\n\n"
						+ ""
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_FIRSTNAME)
						+ " "
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_LASTNAME)
						+ "\n"
						+ ""
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_EMAIL)
						+ "\n"
						+ UtilInList.ReadSharePrefrence(
								getApplicationContext(),
								Constant.SHRED_PR.KEY_PHONE) + "\n\n";

				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						strSubject);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, ""
						+ strExtra);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { "concierge@inlist.com" });
				startActivity(Intent.createChooser(emailIntent, "Email:"));
			}
		});
	}

	/*
	 * Actionbar navigation item select listener
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// Action to be taken after selecting a spinner item

		if (InListApplication.getParty_area().get(itemPosition).get("status")
				.equals("0")) {
			UtilInList.validateDialog(HomeScreenActivity.this, "" + ""
					+ Constant.ERRORS.NO_EVENTS_FOUND, Constant.ERRORS.OOPS);
			return false;
		}
		selected_position = itemPosition;

		if (UtilInList.isInternetConnectionExist(getApplicationContext())) {
			new PartyAreaAsyncTask(HomeScreenActivity.this).execute("");
		} else {
			UtilInList.validateDialog(HomeScreenActivity.this, "" + ""
					+ Constant.network_error, Constant.ERRORS.OOPS);
		}

		return false;
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
							+ Constant.ACTIONS.PARTY_AREA_SET
							+ "?apiMode=VIP&json=true"
							+ "&party_area_id="
							+ InListApplication.getParty_area()
									.get(selected_position)
									.get("party_area_id")
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									HomeScreenActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));
			Log.e("Response In Activity-->", response);
			Log.e("KEY_SESSIONID",
					""
							+ UtilInList.ReadSharePrefrence(
									HomeScreenActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));

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
						dialog.cancel();
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

						UtilInList.WriteSharePrefrence(
								HomeScreenActivity.this,
								Constant.SHRED_PR.KEY_SESSIONID,
								jObject.getJSONObject("session")
										.getJSONObject("userInfo")
										.getString("sessionId"));
						Log.e("session::",
								""
										+ jObject.getJSONObject("session")
												.getJSONObject("userInfo")
												.getString("sessionId"));

						if (UtilInList
								.isInternetConnectionExist(getApplicationContext())) {
							flagReset = true;
							flagIfProgress = true;
							HomeScreenObj.new EventsAsyncTask(
									HomeScreenActivity.this).execute("");
						} else {
							UtilInList.validateDialog(HomeScreenActivity.this,
									"" + "" + Constant.network_error,
									Constant.ERRORS.OOPS);
							mPullToRefreshLayout.setRefreshComplete();
						}
					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	private void makeAlert() {

		final CharSequence[] items = { "Log In", "Sign Up" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Account");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				if (items[item].equals("Log In")) {
					UtilInList.WriteSharePrefrence(HomeScreenActivity.this,
							Constant.SHRED_PR.KEY_LOGIN_FROM, "1");
					startActivity(new Intent(HomeScreenActivity.this,
							LoginActivity.class));
					overridePendingTransition(R.anim.enter_from_bottom,
							R.anim.hold_bottom);
				} else {
					startActivity(new Intent(HomeScreenActivity.this,
							SignUpActivity.class));
					overridePendingTransition(R.anim.enter_from_bottom,
							R.anim.hold_bottom);
				}
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	public class EventsAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public EventsAsyncTask(Context context) {
			dialog = new MyProgressbar(context);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.setCanceledOnTouchOutside(false);
			if (flagIfProgress)
				dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			int pageNo = 1;
			if (flagReset) {
				pageNo = 1;
			} else {
				pageNo = ((int) (InListApplication.getListEvents().size() / 10)) + 1;
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.e("Name Value Pair", nameValuePairs.toString());
			String response = UtilInList.postData(
					nameValuePairs,
					""
							+ Constant.API
							+ Constant.ACTIONS.EVENTS
							+ pageNo
							+ "/?apiMode=VIP&json=true"
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									HomeScreenActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID));

			Log.e("Response In Activity-->", response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// fragment_addconnection_search

			mPullToRefreshLayout.setRefreshComplete();
			try {
				if (dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (result != null) {
				if (flagReset) {
					InListApplication.getListEvents().clear();
				}
				reload(result);

			}

		}

	}

	public void reload(String result) {
		// TODO Auto-generated method stub
		try {
			JSONObject jObject = new JSONObject(result);
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

					if (flagReset) {
						InListApplication.getListEvents().add(map);
					} else {
						adapterEvents.add(map);
					}
				}

				Log.i("size:", "" + InListApplication.getListEvents().size());
			} else {

				UtilInList.validateDialog(HomeScreenActivity.this, jObject
						.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}

		} catch (JSONException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (flagReset) {
			adapterEvents = new EventsAdapter(
					InListApplication.getListEvents(), this,
					HomeScreenActivity.this);
			mPullToRefreshLayout.setAdapter(adapterEvents);

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		if (UtilInList.ReadSharePrefrence(HomeScreenActivity.this,
				Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

		} else {
			overridePendingTransition(R.anim.hold_top, R.anim.exit_in_bottom);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		if (UtilInList.isInternetConnectionExist(HomeScreenActivity.this)) {
			flagReset = true;
			flagIfProgress = false;
			HomeScreenObj.new EventsAsyncTask(HomeScreenActivity.this)
					.execute("");
		} else {
			UtilInList.validateDialog(HomeScreenActivity.this, "" + ""
					+ Constant.network_error, Constant.ERRORS.OOPS);
		}
	}

}
