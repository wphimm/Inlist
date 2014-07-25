package co.inlist.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.inlist.activities.EventDetailsActivity;
import co.inlist.activities.HomeScreenActivity;
import co.inlist.activities.R;
import co.inlist.imageloaders.ImageLoader;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class EventsAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
	Context context;
	protected ImageLoader imageLoader;

	Typeface typeAkzidgrobeligex, typeAkzidgrobemedex, typeAvenir,
			typeLeaguegothic_condensedregular;
	Activity objAct;

	private ArrayList<String> mSectionLetters;

	public EventsAdapter(ArrayList<HashMap<String, String>> list,
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

		// mSectionLetters = getSectionLetters();

		Log.e("getCount", "" + getCount());
	}

	private ArrayList<String> getSectionLetters() {

		ArrayList<String> letters = new ArrayList<String>();
		for (int j = 0; j < locallist.size(); j++) {
			boolean flag = true;
			for (int i = 0; i < j; i++) {
				if (locallist.get(i).get("event_start_date")
						.equals(locallist.get(j).get("event_start_date")))
					flag = false;
			}
			if (flag) {
				letters.add("" + locallist.get(j).get("event_start_date"));
			}
		}

		Log.e("letters size:", "" + letters.size());
		return letters;
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
		ImageView img_event_poster_url;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.events_list_row, null);
		}

		Log.e("position in getView", ">>>>>>" + position);

		RelativeLayout relativeHeader = (RelativeLayout) convertView
				.findViewById(R.id.header);

		TextView txt_event_title = (TextView) convertView
				.findViewById(R.id.event_title);
		TextView txt_event_location_city = (TextView) convertView
				.findViewById(R.id.event_location_city);
		img_event_poster_url = (ImageView) convertView.findViewById(R.id.img);
		img_event_poster_url
				.setBackgroundResource(R.drawable.event_details_overlay);
		TextView txt_event_start_date = (TextView) convertView
				.findViewById(R.id.event_start_date);

		txt_event_title.setShadowLayer(2, 2, 0, Color.BLACK);
		txt_event_title.setText(locallist.get(position).get("event_title")
				.toString().toUpperCase());
		txt_event_location_city.setText(""
				+ locallist.get(position).get("event_location_club") + ", "
				+ locallist.get(position).get("event_location_city"));

		txt_event_title.setTypeface(typeAkzidgrobemedex);
		txt_event_location_city.setTypeface(typeAkzidgrobemedex);
		// txt_event_start_date.setTypeface(typeAvenir);

		// ***** Date Format ************************************//
		String strDate = "" + locallist.get(position).get("event_start_date");
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

		txt_event_start_date.setText("" + strDate.toUpperCase());

		// ***** Date Format ************************************//

		boolean flag = true;
		for (int i = 0; i < position; i++) {
			if (locallist.get(i).get("event_start_date")
					.equals(locallist.get(position).get("event_start_date")))
				flag = false;
		}
		if (flag) {
			relativeHeader.setVisibility(View.VISIBLE);
		} else {
			relativeHeader.setVisibility(View.GONE);
		}

		String image_url = locallist.get(position).get("event_poster_url");
		// imageLoader.displayImage(image_url, img_event_poster_url, options);
		imageLoader.DisplayImage(image_url, Color.BLACK, img_event_poster_url);

		if (position == (getCount() - 1)) {
			if (UtilInList.isInternetConnectionExist(context
					.getApplicationContext())) {
				HomeScreenActivity.flagReset = false;
				HomeScreenActivity.flagIfProgress = false;
				HomeScreenActivity.HomeScreenObj.new EventsAsyncTask(
						context.getApplicationContext()).execute("");
			} else {

				Toast.makeText(context.getApplicationContext(),
						"" + Constant.network_error, Toast.LENGTH_SHORT).show();
			}
		}

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, EventDetailsActivity.class);
				i.putExtra("pos", position);
				context.startActivity(i);
				objAct.overridePendingTransition(R.anim.enter_from_left,
						R.anim.hold_bottom);
			}
		});

		return convertView;
	}

	// @Override
	// public View getHeaderView(int position, View convertView, ViewGroup
	// parent) {
	// HeaderViewHolder holder;
	//
	// if (convertView == null) {
	// holder = new HeaderViewHolder();
	// convertView = LayoutInflater.from(context).inflate(
	// R.layout.headerlayout, parent, false);
	// holder.text_header = (TextView) convertView
	// .findViewById(R.id.event_start_date);
	// convertView.setTag(holder);
	// } else {
	// holder = (HeaderViewHolder) convertView.getTag();
	// }
	//
	// // set header text as first char in name
	// holder.text_header.setText("" + mSectionLetters.get(position));
	// return convertView;
	// }
	//
	// /**
	// * Remember that these have to be static, postion=1 should always return
	// the
	// * same Id that is.
	// */
	// @Override
	// public long getHeaderId(int position) {
	// // return the first character of the country as ID because this is what
	// // headers are based upon
	// return mSectionLetters.get(position).subSequence(0, 1).charAt(0);
	// }
	//
	// @Override
	// public int getPositionForSection(int section) {
	// return section;
	// }
	//
	// @Override
	// public int getSectionForPosition(int position) {
	// return position;
	// }
	//
	// @Override
	// public String[] getSections() {
	// String[] mHeaders = new String[mSectionLetters.size()];
	// for (int i = 0; i < mSectionLetters.size(); i++) {
	// mHeaders[i] = mSectionLetters.get(i);
	// }
	// Log.e("sections size:", "" + mHeaders.length);
	// return mHeaders;
	// }
	//
	// class HeaderViewHolder {
	// TextView text_header;
	// }
}
