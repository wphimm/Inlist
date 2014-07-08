package co.inlist.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GalleryActivity extends Activity implements
		ActionBar.OnNavigationListener {

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ViewPager pager;
	Context context = this;

	LinearLayout linearFooter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallary);

		options = new DisplayImageOptions.Builder().showStubImage(0)
				.showImageForEmptyUri(0).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		linearFooter = (LinearLayout) findViewById(R.id.footer);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(InListApplication.getGallery()));

		for (int i = 0; i < InListApplication.getGallery().size(); i++) {
			ImageView image = new ImageView(GalleryActivity.this);
			if (i == 0) {
				image.setBackgroundResource(R.drawable.pageview_dot_active);
			} else {
				image.setBackgroundResource(R.drawable.pageview_dot);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(10, 0, 0, 0);
			image.setLayoutParams(lp);
			linearFooter.addView(image);
		}

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				linearFooter.removeAllViews();
				for (int i = 0; i < InListApplication.getGallery().size(); i++) {
					ImageView image = new ImageView(GalleryActivity.this);
					if (i == pos) {
						image.setBackgroundResource(R.drawable.pageview_dot_active);
					} else {
						image.setBackgroundResource(R.drawable.pageview_dot);
					}
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(10, 0, 0, 0);
					image.setLayoutParams(lp);
					linearFooter.addView(image);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class ImagePagerAdapter extends PagerAdapter {

		// private String[] images;
		private LayoutInflater inflater;
		ArrayList<HashMap<String, String>> locallist;

		ImagePagerAdapter(ArrayList<HashMap<String, String>> list) {

			locallist = list;
			inflater = getLayoutInflater();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return locallist.size();
		}

		@SuppressLint("NewApi")
		@Override
		public Object instantiateItem(View view, int position) {
			final View imageLayout = inflater.inflate(
					R.layout.item_pager_image, null);

			final ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);

			imageLoader.displayImage(
					"" + locallist.get(position).get("source"), imageView,
					options);

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
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
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
