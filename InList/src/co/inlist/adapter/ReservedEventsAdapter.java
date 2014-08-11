package co.inlist.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.StickyListHeadersAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import co.inlist.activities.InListApplication;
import co.inlist.activities.ProfileActivity;
import co.inlist.activities.R;
import co.inlist.activities.ReservedEventDetailsActivity;
import co.inlist.imageloaders.ImageLoader;
import co.inlist.util.Constant;
import co.inlist.util.MyProgressbar;
import co.inlist.util.UtilInList;

@SuppressLint("SimpleDateFormat")
public class ReservedEventsAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {

	ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
	Context context;
	protected ImageLoader imageLoader;
	Typeface typeAkzidgrobeligex, typeAkzidgrobemedex, typeAvenir,
			typeLeaguegothic_condensedregular;
	Activity objAct;
	int currentPos;

	private ArrayList<HashMap<String, String>> mSectionLetters;

	public ReservedEventsAdapter(ArrayList<HashMap<String, String>> list,
			Context context, Activity objAct) {
		// TODO Auto-generated constructor stub
		locallist = list;
		this.context = context;
		this.objAct = objAct;

		imageLoader = new ImageLoader(context);

		typeAkzidgrobeligex = Typeface.createFromAsset(context.getAssets(),
				"akzidgrobeligex.ttf");
		typeAkzidgrobemedex = Typeface.createFromAsset(context.getAssets(),
				"helve_unbold.ttf");
		typeLeaguegothic_condensedregular = Typeface.createFromAsset(
				context.getAssets(), "leaguegothic_condensedregular.otf");
		typeAvenir = Typeface
				.createFromAsset(context.getAssets(), "avenir.ttc");

		mSectionLetters = getSectionLetters();
	}

	private ArrayList<HashMap<String, String>> getSectionLetters() {

		ArrayList<HashMap<String, String>> letters = new ArrayList<HashMap<String, String>>();
		for (int j = 0; j < locallist.size(); j++) {
			boolean flag = true;
			for (int i = 0; i < j; i++) {
				if (locallist.get(i).get("event_start_date")
						.equals(locallist.get(j).get("event_start_date")))
					flag = false;
			}
			if (flag) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", "" + j);
				map.put("event_start_date",
						"" + locallist.get(j).get("event_start_date"));
				letters.add(map);
			} else {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", "" + letters.get(letters.size() - 1).get("id"));
				map.put("event_start_date",
						"" + locallist.get(j).get("event_start_date"));
				letters.add(map);
			}
		}

		Log.e("letters size:", "" + letters.size());
		return letters;
	}

	public void add(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		locallist.add(map);
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}

	public void refresh() {
		// TODO Auto-generated method stub
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return locallist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.events_list_row, null);
		}

		TextView txt_event_title = (TextView) convertView
				.findViewById(R.id.event_title);
		TextView txt_event_location_city = (TextView) convertView
				.findViewById(R.id.event_location_city);
		ImageView img_event_poster_url = (ImageView) convertView
				.findViewById(R.id.img);

		txt_event_title.setShadowLayer(2, 2, 0, Color.BLACK);
		txt_event_title.setText(locallist.get(position).get("event_title")
				.toString().toUpperCase());
		txt_event_location_city.setText(""
				+ locallist.get(position).get("event_location_club") + ", "
				+ locallist.get(position).get("event_location_city"));

		txt_event_title.setTypeface(typeAkzidgrobemedex);
		txt_event_location_city.setTypeface(typeAkzidgrobemedex);

		String image_url = locallist.get(position).get("event_poster_url");
		imageLoader.DisplayImage(image_url, Color.BLACK, img_event_poster_url);

		if (position == (getCount() - 1)) {
			if (UtilInList.isInternetConnectionExist(context
					.getApplicationContext())) {
				ProfileActivity.flagReset = false;
				ProfileActivity.flagIfProgress = false;
				ProfileActivity.profObj.new ReservationEventsAsyncTask(
						context.getApplicationContext()).execute("");
			}
		}

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,
						ReservedEventDetailsActivity.class);
				i.putExtra("pos", position);
				context.startActivity(i);
				objAct.overridePendingTransition(R.anim.enter_from_left,
						R.anim.hold_bottom);
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(objAct);
				alert.setTitle(Constant.AppName);
				alert.setMessage("Are you sure you want to hide this reservation?");
				alert.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (UtilInList
										.isInternetConnectionExist(context)) {

									currentPos = position;
									new HideReservationAsyncTask(objAct)
											.execute("");

								} else {
									UtilInList.validateDialog(objAct, ""
											+ Constant.network_error,
											Constant.AppName);
								}

							}
						});
				alert.setNegativeButton("NO",
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
				return false;
			}
		});

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.headerlayout, parent, false);
			holder.text_header = (TextView) convertView
					.findViewById(R.id.event_start_date);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// ***** Date Format ************************************//
		String strDate = ""
				+ mSectionLetters.get(position).get("event_start_date");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1;

		try {
			date1 = sdf.parse(strDate);

			SimpleDateFormat format = new SimpleDateFormat("d");
			String date = format.format(date1);

			if (date.endsWith("1") && !date.endsWith("11"))
				format = new SimpleDateFormat("EEEE, MMMM d'st'");
			else if (date.endsWith("2") && !date.endsWith("12"))
				format = new SimpleDateFormat("EEEE, MMMM d'nd'");
			else if (date.endsWith("3") && !date.endsWith("13"))
				format = new SimpleDateFormat("EEEE, MMMM d'rd'");
			else
				format = new SimpleDateFormat("EEEE, MMMM d'th'");

			strDate = format.format(date1);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.text_header.setText("" + strDate.toUpperCase());

		// ***** Date Format ************************************//

		return convertView;
	}

	/**
	 * Remember that these have to be static, postion=1 should always return the
	 * same Id that is.
	 */
	@Override
	public long getHeaderId(int position) {
		// return the first character of the country as ID because this is what
		// headers are based upon
		return Long.parseLong(mSectionLetters.get(position).get("id"));
	}

	@Override
	public int getPositionForSection(int section) {
		return section;
	}

	@Override
	public int getSectionForPosition(int position) {
		return position;
	}

	@Override
	public String[] getSections() {
		String[] mHeaders = new String[mSectionLetters.size()];
		for (int i = 0; i < mSectionLetters.size(); i++) {
			mHeaders[i] = mSectionLetters.get(i).get("id");
		}
		Log.e("sections size:", "" + mHeaders.length);
		return mHeaders;
	}

	class HeaderViewHolder {
		TextView text_header;
	}

	public class HideReservationAsyncTask extends
			AsyncTask<String, String, String> {

		private MyProgressbar dialog;

		public HideReservationAsyncTask(Context context) {
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
					context,
					nameValuePairs,
					""
							+ Constant.API
							+ "reservation/hide"
							+ "/?apiMode=VIP&json=true"
							+ "&reservation_id="
							+ locallist.get(currentPos).get("order_id")
							+ "&PHPSESSIONID="
							+ UtilInList.ReadSharePrefrence(objAct,
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

				if (result != null) {
					try {
						JSONObject jObject = new JSONObject(result);
						String str_temp = jObject.getString("status");
						if (str_temp.equals("success")) {
							InListApplication.getListReservedEvents().remove(
									currentPos);
							refresh();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

}
