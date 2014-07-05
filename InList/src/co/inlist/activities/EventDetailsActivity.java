//package co.inlist.activities;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.ScaleAnimation;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import co.inlist.util.Constant;
//import co.inlist.util.GPSTracker;
//import co.inlist.util.MyProgressbar;
//import co.inlist.util.UtilInList;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//
//@SuppressLint("SimpleDateFormat")
//public class EventDetailsActivity extends Activity implements
//		ActionBar.OnNavigationListener {
//
//	private ScrollView scrollMain;
//	private RelativeLayout relative_zoom_map;
//
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	DisplayImageOptions options;
//
//	private RelativeLayout relativeThumb;
//	private ImageButton btnDirection;
//	private TextView txt_event_title;
//	private TextView txt_event_location_city;
//	private ImageView img_event_poster_url;
//	private TextView txt_date_time;
//	private TextView txt_details;
//	private TextView txt_atmosphere;
//	private TextView txt_music;
//	private Spinner spinnerTable;
//	private TextView txt_minimum;
//	private TextView deposit;
//	private TextView txt_deposit;
//	private TextView txtaddress;
//	private TextView txtcity;
//
//	private GoogleMap googleMap, zoomMap;
//	int position;
//	HashMap<String, String> map;
//	Context context = this;
//
//	Double latitude, longitude;
//
//	@SuppressWarnings({ "deprecation", "unused" })
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.event_details);
//
//		init();
//
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			position = extras.getInt("pos");
//		}
//
//		map = InListApplication.getListEvents().get(position);
//
//		options = new DisplayImageOptions.Builder().showStubImage(0)
//				.showImageForEmptyUri(0).cacheInMemory().cacheOnDisc()
//				.bitmapConfig(Bitmap.Config.RGB_565).build();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//
//		// UtilInList.makeTextViewResizable(txt_details, 3, "MORE", true);
//
//		txt_event_title.setText("" + map.get("event_title"));
//		txt_event_location_city.setText("" + map.get("event_location_city"));
//
//		String image_url = "" + map.get("event_poster_url");
//		imageLoader.displayImage(image_url, img_event_poster_url, options);
//
//		// ***** Date Format ************************************//
//		String strDate = "" + map.get("event_start_date");
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		Date date1;
//
//		try {
//			date1 = sdf.parse(strDate);
//
//			SimpleDateFormat format = new SimpleDateFormat("d");
//			String date = format.format(date1);
//
//			if (date.endsWith("1") && !date.endsWith("11"))
//				format = new SimpleDateFormat("EEE, MMM d'st'");
//			else if (date.endsWith("2") && !date.endsWith("12"))
//				format = new SimpleDateFormat("EEE, MMM d'nd'");
//			else if (date.endsWith("3") && !date.endsWith("13"))
//				format = new SimpleDateFormat("EEE, MMM d'rd'");
//			else
//				format = new SimpleDateFormat("EEE, MMM d'th'");
//
//			strDate = format.format(date1);
//		} catch (java.text.ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String strStartTime = "" + map.get("event_start_time").replace(" ", "");
//		if (strStartTime.equals("null")) {
//			txt_date_time.setText("" + strDate);
//		} else {
//			String strEndTime = "" + map.get("event_end_time").replace(" ", "");
//			if (strEndTime.equals("null")) {
//				txt_date_time.setText("" + strDate + " " + strStartTime);
//			} else {
//				txt_date_time.setText("" + strDate + " " + strStartTime + " - "
//						+ strEndTime);
//			}
//		}
//
//		// ***** Date Format ************************************//
//
//		txt_details.setText("" + map.get("event_description"));
//		txt_atmosphere.setText("" + map.get("atmosphere"));
//		txt_music.setText("" + map.get("music_type"));
//
//		// *** Initialize Spinner Table ****************//
//
//		ArrayList<String> arr = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			arr.add("Balcony - Table for " + (i + 1));
//		}
//		spinnerTable.setAdapter(new MyAdapter(EventDetailsActivity.this,
//				R.layout.spinnertable_row, arr));
//		// ********************************************//
//
//		txt_minimum.setText("$" + map.get("event_min_price"));
//
//		try {
//			int minimum_price = Integer.parseInt(""
//					+ map.get("event_min_price"));
//			int deposit_percentage = Integer.parseInt("" + map.get("deposit"));
//			int int_deposit = (minimum_price * deposit_percentage) / 100;
//			deposit.setText("DEPOSIT (" + map.get("deposit") + "%)");
//			txt_deposit.setText("$" + int_deposit);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		txtaddress.setText("" + map.get("event_location_address"));
//		txtcity.setText("" + map.get("event_location_city") + ", "
//				+ map.get("event_location_state") + " "
//				+ map.get("event_location_zip"));
//
//		// ********** Google Map ************//
//
//		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//				R.id.map)).getMap();
//
//		if (googleMap != null) {
//
//			googleMap.getUiSettings().setZoomControlsEnabled(false);
//			googleMap.getUiSettings().setCompassEnabled(false);
//
//			latitude = Double.parseDouble(""
//					+ map.get("event_location_latitude"));
//			longitude = Double.parseDouble(""
//					+ map.get("event_location_longitude"));
//
//			LatLng HAMBURG = new LatLng(latitude, longitude);
//			Marker humburg = googleMap.addMarker(new MarkerOptions().position(
//					HAMBURG).icon(
//					BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
//
//			googleMap
//					.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
//			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//			CameraPosition cameraPosition = new CameraPosition.Builder()
//					.target(HAMBURG) // Sets the center of the map to Mountain
//										// View
//					.zoom(15) // Sets the zoom
//					.bearing(90) // Sets the orientation of the camera to east
//					.tilt(30) // Sets the tilt of the camera to 30 degrees
//					.build(); // Creates a CameraPosition from the builder
//			googleMap.animateCamera(CameraUpdateFactory
//					.newCameraPosition(cameraPosition));
//		}
//
//		zoomMap = ((MapFragment) getFragmentManager().findFragmentById(
//				R.id.zoom_map)).getMap();
//
//		if (zoomMap != null) {
//
//			zoomMap.getUiSettings().setCompassEnabled(false);
//			latitude = Double.parseDouble(""
//					+ map.get("event_location_latitude"));
//			longitude = Double.parseDouble(""
//					+ map.get("event_location_longitude"));
//
//			LatLng HAMBURG = new LatLng(latitude, longitude);
//			Marker humburg = zoomMap.addMarker(new MarkerOptions().position(
//					HAMBURG).icon(
//					BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
//			zoomMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
//			zoomMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//			CameraPosition cameraPosition = new CameraPosition.Builder()
//					.target(HAMBURG) // Sets the center of the map to Mountain
//										// View
//					.zoom(15) // Sets the zoom
//					.bearing(90) // Sets the orientation of the camera to east
//					.tilt(30) // Sets the tilt of the camera to 30 degrees
//					.build(); // Creates a CameraPosition from the builder
//			zoomMap.animateCamera(CameraUpdateFactory
//					.newCameraPosition(cameraPosition));
//		}
//
//		if (googleMap != null) {
//			googleMap.setOnMapClickListener(new OnMapClickListener() {
//
//				@Override
//				public void onMapClick(LatLng arg0) {
//					// TODO Auto-generated method stub
//					ScaleAnimation animation = new ScaleAnimation(0.3f, 1.0f,
//							0.3f, 1.0f, Animation.RELATIVE_TO_SELF,
//							(float) 0.5, Animation.RELATIVE_TO_SELF,
//							(float) 1.0);
//					animation.setDuration(500);
//					AnimationSet zoom = new AnimationSet(true);
//					zoom.addAnimation(animation);
//
//					relative_zoom_map.setVisibility(View.VISIBLE);
//					relative_zoom_map.setAnimation(zoom);
//					scrollMain.setVisibility(View.GONE);
//
//				}
//			});
//		}
//
//		// ******************************************** //
//
//		btnDirection.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LeadingActivity.gps = new GPSTracker(EventDetailsActivity.this);
//				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
//						Uri.parse("http://maps.google.com/maps?saddr="
//								+ LeadingActivity.gps.getLatitude() + ","
//								+ LeadingActivity.gps.getLongitude()
//								+ "&daddr=" + latitude + "," + longitude)));
//			}
//		});
//
//		relativeThumb.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (InListApplication.getGallery().size() > 0) {
//					startActivity(new Intent(EventDetailsActivity.this,
//							GalleryActivity.class));
//				}
//			}
//		});
//
//		Handler hn = new Handler();
//		hn.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (UtilInList
//						.isInternetConnectionExist(getApplicationContext())) {
//					new EventEntryAsyncTask(getApplicationContext())
//							.execute("");
//				} else {
//					Toast.makeText(getApplicationContext(),
//							"" + Constant.network_error, Toast.LENGTH_SHORT)
//							.show();
//				}
//			}
//		}, 100);
//
//	}
//
//	private void init() {
//		// TODO Auto-generated method stub
//		scrollMain = (ScrollView) findViewById(R.id.scrollmain);
//		relativeThumb = (RelativeLayout) findViewById(R.id.r1);
//		relative_zoom_map = (RelativeLayout) findViewById(R.id.relative_zoom_map);
//		txt_event_title = (TextView) findViewById(R.id.event_title);
//		txt_event_title.setShadowLayer(2, 2, 0, Color.BLACK);
//		txt_event_location_city = (TextView) findViewById(R.id.event_location_city);
//		img_event_poster_url = (ImageView) findViewById(R.id.img);
//		txt_date_time = (TextView) findViewById(R.id.txt_date_time);
//		txt_details = (TextView) findViewById(R.id.txt_details);
//		btnDirection = (ImageButton) findViewById(R.id.btnDirection);
//
//		txt_atmosphere = (TextView) findViewById(R.id.txt_atmosphere);
//		txt_music = (TextView) findViewById(R.id.txt_music);
//		spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
//		txt_minimum = (TextView) findViewById(R.id.txt_minimum);
//		deposit = (TextView) findViewById(R.id.deposit);
//		txt_deposit = (TextView) findViewById(R.id.txt_deposit);
//		txtaddress = (TextView) findViewById(R.id.txtaddress);
//		txtcity = (TextView) findViewById(R.id.txtcity);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.activity_event_details_actions, menu);
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	public class MyAdapter extends ArrayAdapter<String> {
//
//		ArrayList<String> local;
//
//		public MyAdapter(Context context, int textViewResourceId,
//				ArrayList<String> spinner_data1) {
//			super(context, textViewResourceId, spinner_data1);
//			local = spinner_data1;
//		}
//
//		@Override
//		public View getDropDownView(int position, View convertView,
//				ViewGroup parent) {
//
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(
//						R.layout.spinnertable_row, parent, false);
//			}
//			TextView label = (TextView) convertView
//					.findViewById(R.id.spinnerTarget);
//			label.setText(local.get(position));
//
//			return convertView;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(
//						R.layout.spinnertable_row, parent, false);
//			}
//			TextView label = (TextView) convertView
//					.findViewById(R.id.spinnerTarget);
//			label.setText(local.get(position));
//
//			return convertView;
//		}
//	}
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		if (relative_zoom_map.getVisibility() == View.VISIBLE) {
//			relative_zoom_map.setVisibility(View.GONE);
//			scrollMain.setVisibility(View.VISIBLE);
//		} else {
//			super.onBackPressed();
//			finish();
//		}
//	}
//
//	@Override
//	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	/**
//	 * On selecting action bar icons
//	 * */
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Take appropriate action for each action item click
//		switch (item.getItemId()) {
//
//		case android.R.id.home:
//			if (relative_zoom_map.getVisibility() == View.VISIBLE) {
//				relative_zoom_map.setVisibility(View.GONE);
//				scrollMain.setVisibility(View.VISIBLE);
//			} else {
//				finish();
//			}
//			return true;
//
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	public class EventEntryAsyncTask extends AsyncTask<String, String, String> {
//
//		private MyProgressbar dialog;
//
//		public EventEntryAsyncTask(Context context) {
//			dialog = new MyProgressbar(context);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			dialog.setMessage("Loading...");
//			dialog.setCanceledOnTouchOutside(false);
//
//		}
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			// TODO Auto-generated method stub
//
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//			Log.e("Name Value Pair", nameValuePairs.toString());
//			String response = UtilInList.postData(nameValuePairs, ""
//					+ Constant.API_LIVE + "event/" + map.get("event_id")
//					+ "/?apiMode=VIP&json=true");
//			Log.e("Response In Activity-->", response);
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			// fragment_addconnection_search
//
//			try {
//				JSONObject jObject = new JSONObject(result);
//				String str_temp = jObject.getString("status");
//				if (str_temp.equals("success")) {
//					JSONObject jObjectData = new JSONObject(
//							jObject.getString("data"));
//					JSONArray data = jObjectData.getJSONArray("gallery");
//					Log.e("Length of json array ----->", "" + data.length());
//
//					InListApplication.getGallery().clear();
//
//					for (int i = 0; i < data.length(); i++) {
//						JSONObject obj = data.getJSONObject(i);
//						HashMap<String, String> map = new HashMap<String, String>();
//						map.put("thumbnail", "" + obj.getString("thumbnail"));
//						map.put("source", "" + obj.getString("source"));
//						map.put("description",
//								"" + obj.getString("description"));
//
//						InListApplication.getGallery().add(map);
//					}
//
//				}
//
//			} catch (JSONException e) { // TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
//}
