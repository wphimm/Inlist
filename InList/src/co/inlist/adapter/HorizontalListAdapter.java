package co.inlist.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.inlist.activities.R;
import co.inlist.util.HorizontalListView;

public class HorizontalListAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> locallist;
	Context context;
	HorizontalListView listview;
	int selected_pos;

	public HorizontalListAdapter(ArrayList<HashMap<String, String>> list,
			Context context, HorizontalListView listview, int selected_pos) {
		// TODO Auto-generated constructor stub
		locallist = list;
		this.context = context;
		this.listview = listview;
		this.selected_pos = selected_pos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return locallist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		if (row == null) {
			row = LayoutInflater.from(context).inflate(
					R.layout.horizontallist_row, null);
		}

		final RelativeLayout main = (RelativeLayout) row.findViewById(R.id.main);
		main.setBackgroundColor(Color.WHITE);
		final TextView txtMusic = (TextView) row.findViewById(R.id.music);
		txtMusic.setText("" + locallist.get(position).get("title"));
		txtMusic.setTextColor(Color.parseColor("#CCCCCC"));

		if (selected_pos == position) {
			main.setBackgroundColor(Color.parseColor("#DFBB6A"));
			txtMusic.setTextColor(Color.WHITE);
		}

		return row;
	}

}
