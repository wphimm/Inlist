package co.inlist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.inlist.activities.R;

public class LeadingFragment extends Fragment {
	// Store instance variables
	@SuppressWarnings("unused")
	private String title;
	private int page;

	// newInstance constructor for creating fragment with arguments
	public static LeadingFragment newInstance(int page, String title) {
		LeadingFragment fragmentFirst = new LeadingFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.leading_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		page = getArguments().getInt("someInt", 0);
		title = getArguments().getString("someTitle");

		TextView tvLabel = (TextView) getView().findViewById(R.id.txt);
		ImageView img = (ImageView) getView().findViewById(R.id.img);
		
		switch (page) {
		case 0:
			img.setBackgroundResource(R.drawable.splash_logo);
			tvLabel.setText("");
			break;
		case 1:
			img.setBackgroundResource(R.drawable.onboarding_image1);
			tvLabel.setText("InList is your key to the most prestigious events in your city.");
			break;
		case 2:
			img.setBackgroundResource(R.drawable.onboarding_image2);
			tvLabel.setText("Find the best venue, guarantee entry, and book your table, right from your phone.");
			break;
		case 3:
			img.setBackgroundResource(R.drawable.onboarding_image3);
			tvLabel.setText("Made for the night, by the Night Life.");
			break;

		default:
			break;
		}
	}
}
