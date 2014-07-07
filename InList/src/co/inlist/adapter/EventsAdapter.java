package co.inlist.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import co.inlist.activities.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("SimpleDateFormat")
public class EventsAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
	Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	Typeface typeAkzidgrobeligex,typeAkzidgrobemedex,typeAvenir,typeLeaguegothic_condensedregular;

	@SuppressWarnings("deprecation")
	public EventsAdapter(ArrayList<HashMap<String, String>> list,Context context) {
		// TODO Auto-generated constructor stub
		locallist = list;
		this.context=context;
		
		options = new DisplayImageOptions.Builder().showStubImage(0)
				.showImageForEmptyUri(0).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		typeAkzidgrobeligex = Typeface.createFromAsset(context.getAssets(), "akzidgrobeligex.ttf");
		typeAkzidgrobemedex = Typeface.createFromAsset(context.getAssets(), "helve_unbold.ttf");
		typeLeaguegothic_condensedregular = Typeface.createFromAsset(context.getAssets(), "leaguegothic_condensedregular.otf");
		typeAvenir = Typeface.createFromAsset(context.getAssets(), "avenir.ttc");
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
			convertView = LayoutInflater.from(context).inflate(R.layout.events_list_row,
					null);
		}

		RelativeLayout relativeHeader = (RelativeLayout) convertView
				.findViewById(R.id.header);
		TextView txt_event_title = (TextView) convertView
				.findViewById(R.id.event_title);
		TextView txt_event_location_city = (TextView) convertView
				.findViewById(R.id.event_location_city);
		ImageView img_event_poster_url = (ImageView) convertView
				.findViewById(R.id.img);
		TextView txt_event_start_date = (TextView) convertView
				.findViewById(R.id.event_start_date);

//		txt_event_title.setShadowLayer(2, 2, 0, Color.BLACK);
		txt_event_title.setText(locallist.get(position).get("event_title").toString().toUpperCase());
		txt_event_location_city.setText(""+locallist.get(position).get(
				"event_location_club")+", "+locallist.get(position).get(
				"event_location_city"));
		txt_event_location_city.setText("Nightclub, City");
		
		txt_event_title.setTypeface(typeAkzidgrobemedex);
		txt_event_location_city.setTypeface(typeAkzidgrobemedex);
		txt_event_start_date.setTypeface(typeAvenir);

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
		imageLoader.displayImage(image_url, img_event_poster_url, options);

		if (position == (getCount() - 1)) {
			if (UtilInList.isInternetConnectionExist(context.getApplicationContext())) {
				HomeScreenActivity.flagReset = false;
				HomeScreenActivity.flagIfProgress = false;
				HomeScreenActivity.HomeScreenObj.new EventsAsyncTask(context.getApplicationContext())
						.execute("");
			} else {
				Toast.makeText(context.getApplicationContext(),
						"" + Constant.network_error, Toast.LENGTH_SHORT).show();
			}
		}

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,
						EventDetailsActivity.class);
				i.putExtra("pos", position);
				context.startActivity(i);
			}
		});

		return convertView;
	}
}
