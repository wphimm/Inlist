package co.inlist.activities;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.GPSTracker;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("SimpleDateFormat")
public class EventDetailsActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	public static EventDetailsActivity edObj;
	private ScrollView scrollMain;
	private RelativeLayout relative_zoom_map, relative_google_map;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private RelativeLayout relativeThumb;
	private ImageButton btnDirection;
	private TextView txt_event_title;
	private TextView txt_event_location_city;
	private ImageView img_event_poster_url;
	private TextView txt_date_time;
	private TextView txt_details;
	private TextView txt_atmosphere;
	private TextView txt_music;
	private Spinner spinnerTable;
	private TextView txt_minimum;
	private TextView txtaddress;
	private TextView txtcity;

	private GoogleMap googleMap, zoomMap;
	int position;
	HashMap<String, String> map;
	Context context = this;

	Double latitude, longitude;

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);

		init();

		actionBarAndButtonActions();

		edObj = this;
		// UtilInList.makeActionBarGradiant(EventDetailsActivity.this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt("pos");
		}

		map = InListApplication.getListEvents().get(position);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.event_details_overlay)
				.showImageForEmptyUri(R.drawable.event_details_overlay)
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		Typeface typeAkzidgrobemedex = Typeface.createFromAsset(
				context.getAssets(), "helve_unbold.ttf");
		txt_event_title.setTypeface(typeAkzidgrobemedex);
		txt_event_location_city.setTypeface(typeAkzidgrobemedex);

		txt_event_title.setText("" + map.get("event_title"));
		txt_event_location_city.setText("" + map.get("event_location_club")
				+ ", " + map.get("event_location_city"));

		String image_url = "" + map.get("event_poster_url");
		imageLoader.displayImage(image_url, img_event_poster_url, options);

		// ***** Date Format ************************************//
		String strDate = "" + map.get("event_start_date");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1;

		try {
			date1 = sdf.parse(strDate);

			SimpleDateFormat format = new SimpleDateFormat("d");
			String date = format.format(date1);

			if (date.endsWith("1") && !date.endsWith("11"))
				format = new SimpleDateFormat("EEE, MMM d'st'");
			else if (date.endsWith("2") && !date.endsWith("12"))
				format = new SimpleDateFormat("EEE, MMM d'nd'");
			else if (date.endsWith("3") && !date.endsWith("13"))
				format = new SimpleDateFormat("EEE, MMM d'rd'");
			else
				format = new SimpleDateFormat("EEE, MMM d'th'");

			strDate = format.format(date1);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String strDateTime = "" + strDate;
		try {
			String strStartTime = ""
					+ map.get("event_start_time").replace(" ", "");
			if (!strStartTime.equals("null")) {
				strDateTime = "" + strDate + " " + strStartTime;
				String strEndTime = ""
						+ map.get("event_end_time").replace(" ", "");
				if (strEndTime.equals("null")) {
					txt_date_time.setText("" + strDate + " " + strStartTime);
					strDateTime = "" + strDate + " " + strStartTime;
				} else {
					txt_date_time.setText("" + strDate + " " + strStartTime
							+ " - " + strEndTime);
					strDateTime = "" + strDate + " " + strStartTime + " - "
							+ strEndTime;
				}
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}
		txt_date_time.setText("" + strDateTime);

		// ***** Date Format ************************************//

		txt_details.setText("" + map.get("event_description") + " ");

		UtilInList.makeTextViewResizable(txt_details, 3, "MORE", true);

		txt_atmosphere.setText("" + map.get("atmosphere"));
		txt_music.setText("" + map.get("music_type"));

		txt_minimum.setText("$" + map.get("event_min_price"));

		txtaddress.setText("" + map.get("event_location_address"));
		txtcity.setText("" + map.get("event_location_city") + ", "
				+ map.get("event_location_state") + " "
				+ map.get("event_location_zip"));

		// ********** Google Map ************//

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		if (googleMap != null) {

			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setCompassEnabled(false);

			latitude = Double.parseDouble(""
					+ map.get("event_location_latitude"));
			longitude = Double.parseDouble(""
					+ map.get("event_location_longitude"));

			LatLng HAMBURG = new LatLng(latitude, longitude);
			Marker humburg = googleMap.addMarker(new MarkerOptions().position(
					HAMBURG).icon(
					BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));

			googleMap
					.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(HAMBURG) // Sets the center of the map to Mountain
										// View
					.zoom(15) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}

		zoomMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.zoom_map)).getMap();

		if (zoomMap != null) {

			zoomMap.getUiSettings().setCompassEnabled(false);
			latitude = Double.parseDouble(""
					+ map.get("event_location_latitude"));
			longitude = Double.parseDouble(""
					+ map.get("event_location_longitude"));

			LatLng HAMBURG = new LatLng(latitude, longitude);
			Marker humburg = zoomMap.addMarker(new MarkerOptions().position(
					HAMBURG).icon(
					BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
			zoomMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
			zoomMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(HAMBURG) // Sets the center of the map to Mountain
										// View
					.zoom(15) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			zoomMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}

		if (googleMap != null) {
			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub
					/*
					 * ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f,
					 * 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5,
					 * Animation.RELATIVE_TO_SELF, (float) 1.0);
					 * animation.setDuration(500); AnimationSet zoom = new
					 * AnimationSet(true); zoom.addAnimation(animation);
					 */

					relative_zoom_map.setVisibility(View.VISIBLE);
					// relative_zoom_map.setAnimation(zoom);
					// scrollMain.setVisibility(View.GONE);
					// relative_google_map.setVisibility(View.INVISIBLE);

				}
			});
		}

		// ******************************************** //

		btnDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LeadingActivity.gps = new GPSTracker(EventDetailsActivity.this);
				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr="
								+ LeadingActivity.gps.getLatitude() + ","
								+ LeadingActivity.gps.getLongitude()
								+ "&daddr=" + latitude + "," + longitude)));
			}
		});

		relativeThumb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (InListApplication.getGallery().size() > 0) {
					startActivity(new Intent(EventDetailsActivity.this,
							GalleryActivity.class));
				}
			}
		});

		Handler hn = new Handler();
		hn.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					new EventEntryAsyncTask(EventDetailsActivity.this)
							.execute("");
				} else {
					UtilInList
							.validateDialog(EventDetailsActivity.this, "" + ""
									+ Constant.network_error,
									Constant.ERRORS.OOPS);
				}
			}
		}, 100);

	}

	private void init() {
		// TODO Auto-generated method stub
		scrollMain = (ScrollView) findViewById(R.id.scrollmain);
		relativeThumb = (RelativeLayout) findViewById(R.id.r1);
		relative_google_map = (RelativeLayout) findViewById(R.id.relative_google_map);
		relative_zoom_map = (RelativeLayout) findViewById(R.id.relative_zoom_map);
		txt_event_title = (TextView) findViewById(R.id.event_title);
		txt_event_title.setShadowLayer(2, 2, 0, Color.BLACK);
		txt_event_location_city = (TextView) findViewById(R.id.event_location_city);
		img_event_poster_url = (ImageView) findViewById(R.id.img);
		txt_date_time = (TextView) findViewById(R.id.txt_date_time);
		txt_details = (TextView) findViewById(R.id.txt_details);
		btnDirection = (ImageButton) findViewById(R.id.btnDirection);

		txt_atmosphere = (TextView) findViewById(R.id.txt_atmosphere);
		txt_music = (TextView) findViewById(R.id.txt_music);
		spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
		txt_minimum = (TextView) findViewById(R.id.txt_minimum);
		txtaddress = (TextView) findViewById(R.id.txtaddress);
		txtcity = (TextView) findViewById(R.id.txtcity);
	}

	public class MyAdapter extends ArrayAdapter<HashMap<String, String>> {

		ArrayList<HashMap<String, String>> local;

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, String>> spinner_data1) {
			super(context, textViewResourceId, spinner_data1);
			local = spinner_data1;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.spinnertable_row, parent, false);
			}
			TextView label = (TextView) convertView
					.findViewById(R.id.spinnerTarget);
			label.setText(local.get(position).get("club_section_name"));

			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.spinnertable_row, parent, false);
			}
			TextView label = (TextView) convertView
					.findViewById(R.id.spinnerTarget);
			label.setText(local.get(position).get("club_section_name"));

			return convertView;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (relative_zoom_map.getVisibility() == View.VISIBLE) {
			relative_zoom_map.setVisibility(View.GONE);
			scrollMain.setVisibility(View.VISIBLE);
			relative_google_map.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
			finish();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		invalidateOptionsMenu();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_event_details_actions, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	//
	// if (UtilInList.ReadSharePrefrence(EventDetailsActivity.this,
	// Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {
	// menu.getItem(0).setIcon(R.drawable.btn_purchase);
	// } else {
	// menu.getItem(0).setIcon(R.drawable.btn_login);
	// }
	//
	// return super.onPrepareOptionsMenu(menu);
	// }

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case R.id.action_sign:

			UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
					Constant.SHRED_PR.KEY_EVENT_ID, "" + map.get("event_id"));
			UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
					Constant.SHRED_PR.KEY_YOUR_MINIMUM,
					"" + map.get("event_min_price"));
			UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
					Constant.SHRED_PR.KEY_PRICE_POSITION,
					"" + spinnerTable.getSelectedItemPosition());

			if (UtilInList.ReadSharePrefrence(EventDetailsActivity.this,
					Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {

					String strCapacity = ""
							+ InListApplication
									.getPricing()
									.get(Integer
											.parseInt(UtilInList
													.ReadSharePrefrence(
															EventDetailsActivity.this,
															Constant.SHRED_PR.KEY_PRICE_POSITION)
													.toString()))
									.get("table_capacity");

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("event_id", ""
							+ map.get("event_id")));
					params.add(new BasicNameValuePair("capacity", ""
							+ strCapacity));
					params.add(new BasicNameValuePair("device_type", "android"));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									EventDetailsActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(
							EventDetailsActivity.this, params, Constant.API
									+ Constant.ACTIONS.GET_EVENT_TABLE)
							.execute();

				} else {
					UtilInList
							.validateDialog(EventDetailsActivity.this, "" + ""
									+ Constant.network_error,
									Constant.ERRORS.OOPS);

				}

			} else {
				UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_FROM, "1");

				startActivity(new Intent(EventDetailsActivity.this,
						LoginActivity.class));
			}

			return true;

		case android.R.id.home:
			if (relative_zoom_map.getVisibility() == View.VISIBLE) {
				relative_zoom_map.setVisibility(View.GONE);
				scrollMain.setVisibility(View.VISIBLE);
				relative_google_map.setVisibility(View.VISIBLE);
			} else {
				finish();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class EventEntryAsyncTask extends AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public EventEntryAsyncTask(Context context) {
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
							+ Constant.API_LIVE
							+ "event/"
							+ map.get("event_id")
							+ "/?apiMode=VIP&json=true"
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(
									EventDetailsActivity.this,
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

			InListApplication.getGallery().clear();
			InListApplication.getPricing().clear();

			try {
				JSONObject jObject = new JSONObject(result);
				String str_temp = jObject.getString("status");
				if (str_temp.equals("success")) {
					JSONObject jObjectData = new JSONObject(
							jObject.getString("data"));
					JSONArray data = jObjectData.getJSONArray("gallery");
					Log.e("Length of json array ----->", "" + data.length());

					for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("thumbnail", "" + obj.getString("thumbnail"));
						map.put("source", "" + obj.getString("source"));
						map.put("description",
								"" + obj.getString("description"));

						InListApplication.getGallery().add(map);
					}

					JSONArray data1 = jObjectData.getJSONArray("pricing");
					Log.e("Length of json array ----->", "" + data1.length());

					for (int i = 0; i < data1.length(); i++) {
						JSONObject obj = data1.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("event_pricing_id",
								"" + obj.getString("event_pricing_id"));
						map.put("club_section_name",
								"" + obj.getString("club_section_name"));
						map.put("club_section_id",
								"" + obj.getString("club_section_id"));
						map.put("table_capacity",
								"" + obj.getString("table_capacity"));
						map.put("net_price", "" + obj.getString("net_price"));
						map.put("tax", "" + obj.getString("tax"));
						map.put("gratuity", "" + obj.getString("gratuity"));
						map.put("payment_type",
								"" + obj.getString("payment_type"));
						map.put("commission", "" + obj.getString("commission"));
						map.put("price", "" + obj.getString("price"));
						map.put("total_price",
								"" + obj.getString("total_price"));
						map.put("to_pay_in_app",
								"" + obj.getString("to_pay_in_app"));

						InListApplication.getPricing().add(map);
					}

					spinnerTable.setAdapter(new MyAdapter(
							EventDetailsActivity.this,
							R.layout.spinnertable_row, InListApplication
									.getPricing()));
				}

			} catch (JSONException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub
		try {
			if (result.getString("success").equals("true")) {

				if (UtilInList
						.ReadSharePrefrence(EventDetailsActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_ADDED)
						.toString().equals("1")) {
					startActivity(new Intent(EventDetailsActivity.this,
							CompletePurchaseActivity.class));
				} else {
					UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
							Constant.SHRED_PR.KEY_ADDCARD_FROM, "1");
					startActivity(new Intent(EventDetailsActivity.this,
							AddCardActivity.class));
				}

			} else {
				UtilInList.validateDialog(EventDetailsActivity.this, result
						.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}

	}

	private void actionBarAndButtonActions() {
		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.event_details_custome_action_bar);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		actionBar.setDisplayHomeAsUpEnabled(true);

		ImageButton action_button = (ImageButton) actionBar.getCustomView()
				.findViewById(R.id.btn_action_bar);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
						Constant.SHRED_PR.KEY_EVENT_ID,
						"" + map.get("event_id"));
				UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
						Constant.SHRED_PR.KEY_YOUR_MINIMUM,
						"" + map.get("event_min_price"));
				UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
						Constant.SHRED_PR.KEY_PRICE_POSITION,
						"" + spinnerTable.getSelectedItemPosition());

				if (UtilInList.ReadSharePrefrence(EventDetailsActivity.this,
						Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {

					if (UtilInList
							.isInternetConnectionExist(getApplicationContext())) {

						String strCapacity = ""
								+ InListApplication
										.getPricing()
										.get(Integer
												.parseInt(UtilInList
														.ReadSharePrefrence(
																EventDetailsActivity.this,
																Constant.SHRED_PR.KEY_PRICE_POSITION)
														.toString()))
										.get("table_capacity");

						List<NameValuePair> params = new ArrayList<NameValuePair>();

						params.add(new BasicNameValuePair("event_id", ""
								+ map.get("event_id")));
						params.add(new BasicNameValuePair("capacity", ""
								+ strCapacity));
						params.add(new BasicNameValuePair("device_type",
								"android"));
						params.add(new BasicNameValuePair("PHPSESSIONID", ""
								+ UtilInList.ReadSharePrefrence(
										EventDetailsActivity.this,
										Constant.SHRED_PR.KEY_SESSIONID)));

						new WebServiceDataPosterAsyncTask(
								EventDetailsActivity.this, params, Constant.API
										+ Constant.ACTIONS.GET_EVENT_TABLE)
								.execute();

					} else {
						UtilInList.validateDialog(EventDetailsActivity.this, ""
								+ "" + Constant.network_error,
								Constant.ERRORS.OOPS);

					}

				} else {
					UtilInList.WriteSharePrefrence(EventDetailsActivity.this,
							Constant.SHRED_PR.KEY_LOGIN_FROM, "1");

					startActivity(new Intent(EventDetailsActivity.this,
							LoginActivity.class));
				}
			}
		});

		if (UtilInList.ReadSharePrefrence(EventDetailsActivity.this,
				Constant.SHRED_PR.KEY_LOGIN_STATUS).equals("true")) {
			action_button.setBackgroundResource(R.drawable.purchanse_onclick);
		} else {
			action_button.setBackgroundResource(R.drawable.login_onclick);
		}

	}
}
