package co.inlist.activities;

import java.io.IOException;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import co.inlist.adapter.HorizontalListAdapter;
import co.inlist.util.HorizontalListView;

public class VipMemberShipActivity extends Activity implements
		ActionBar.OnNavigationListener {

	ImageView img1, img2, img3, img4, imgProfile;
	int height, width;
	HorizontalListView horizontalList;

	Uri imageUri = null;
	static Cursor cursor = null;
	static String camera_pathname = "";
	Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_membership_screen);

		init();

		String fileName = "Camera_Example.jpg";
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		imageUri = this.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		InListApplication.getList_music_types().clear();
		for (int i = 0; i < 4; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			switch (i) {
			case 0:
				map.put("title", "Electronic Dance Music");
				break;
			case 1:
				map.put("title", "Hip Hop");
				break;
			case 2:
				map.put("title", "Top 40");
				break;
			case 3:
				map.put("title", "Other");
				break;
			default:
				map.put("title", "Other");
				break;
			}
			InListApplication.getList_music_types().add(map);
		}

		horizontalList.setAdapter(new HorizontalListAdapter(InListApplication
				.getList_music_types(), getApplicationContext(),
				horizontalList, 0));

		horizontalList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				horizontalList.setAdapter(new HorizontalListAdapter(
						InListApplication.getList_music_types(),
						getApplicationContext(), horizontalList, position));
				if (position > 0)
					horizontalList.setSelection(position - 1);
			}
		});

		ViewTreeObserver vto = img1.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				img1.getViewTreeObserver().removeOnPreDrawListener(this);
				height = img1.getMeasuredHeight();
				width = img1.getMeasuredWidth();
				return true;
			}
		});

		img1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img1.setBackgroundResource(R.drawable.circle_selected);
				img2.setBackgroundResource(R.drawable.circle_notselected);
				img3.setBackgroundResource(R.drawable.circle_notselected);
				img4.setBackgroundResource(R.drawable.circle_notselected);

				img1.getLayoutParams().width = (int) (1.25 * width);
				img1.getLayoutParams().height = (int) (1.25 * height);

				img2.getLayoutParams().width = width;
				img2.getLayoutParams().height = height;
				img3.getLayoutParams().width = width;
				img3.getLayoutParams().height = height;
				img4.getLayoutParams().width = width;
				img4.getLayoutParams().height = height;
			}
		});

		img2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img2.setBackgroundResource(R.drawable.circle_selected);
				img1.setBackgroundResource(R.drawable.circle_notselected);
				img3.setBackgroundResource(R.drawable.circle_notselected);
				img4.setBackgroundResource(R.drawable.circle_notselected);

				img2.getLayoutParams().width = (int) (1.25 * width);
				img2.getLayoutParams().height = (int) (1.25 * height);

				img1.getLayoutParams().width = width;
				img1.getLayoutParams().height = height;
				img3.getLayoutParams().width = width;
				img3.getLayoutParams().height = height;
				img4.getLayoutParams().width = width;
				img4.getLayoutParams().height = height;
			}
		});
		img3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img3.setBackgroundResource(R.drawable.circle_selected);
				img2.setBackgroundResource(R.drawable.circle_notselected);
				img1.setBackgroundResource(R.drawable.circle_notselected);
				img4.setBackgroundResource(R.drawable.circle_notselected);

				img3.getLayoutParams().width = (int) (1.25 * width);
				img3.getLayoutParams().height = (int) (1.25 * height);

				img2.getLayoutParams().width = width;
				img2.getLayoutParams().height = height;
				img1.getLayoutParams().width = width;
				img1.getLayoutParams().height = height;
				img4.getLayoutParams().width = width;
				img4.getLayoutParams().height = height;
			}
		});
		img4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img4.setBackgroundResource(R.drawable.circle_selected);
				img2.setBackgroundResource(R.drawable.circle_notselected);
				img3.setBackgroundResource(R.drawable.circle_notselected);
				img1.setBackgroundResource(R.drawable.circle_notselected);

				img4.getLayoutParams().width = (int) (1.25 * width);
				img4.getLayoutParams().height = (int) (1.25 * height);

				img2.getLayoutParams().width = width;
				img2.getLayoutParams().height = height;
				img3.getLayoutParams().width = width;
				img3.getLayoutParams().height = height;
				img1.getLayoutParams().width = width;
				img1.getLayoutParams().height = height;
			}
		});

		imgProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerForContextMenu(v);
				v.showContextMenu();
			}
		});

	}

	private void init() {
		// TODO Auto-generated method stub
		imgProfile = (ImageView) findViewById(R.id.imgprofile);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);

		horizontalList = (HorizontalListView) findViewById(R.id.listview);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		String[] menuItems = new String[] { "Photo Library", "Camera", "Cancel" };
		menu.add(Menu.NONE, 0, 0, menuItems[0]);
		menu.add(Menu.NONE, 1, 1, menuItems[1]);
		menu.add(Menu.NONE, 2, 2, menuItems[2]);

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();
		switch (menuItemIndex) {
		case 0: {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, 2);
			break;
		}
		case 1: {
			if (imageUri != null) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, 1);
			}
		}
			break;
		case 2:
			break;
		}

		return true;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {

				String imageId = convertImageUriToFile(imageUri,
						VipMemberShipActivity.this);
				new LoadImagesFromSDCard().execute("" + imageId);

			}
		} else if (requestCode == 2) {
			camera_pathname = null;
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = null;
				if (data != null) {
					try {
						uri = data.getData();

						String imageId = convertImageUriToFile(uri,
								VipMemberShipActivity.this);
						new LoadImagesFromSDCard().execute("" + imageId);

					} catch (Exception e) {
						// TODO: handle exception
						Log.i("Exception", "" + e);
					}
				}
				Log.d("Gallery Path: ", camera_pathname + bmp);

			}
		}
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/************ Convert Image Uri path to physical path **************/

	@SuppressWarnings("deprecation")
	public static String convertImageUriToFile(Uri imageUri, Activity activity) {

		int imageID = 0;
		try {

			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = activity.managedQuery(

			imageUri, // Get data for specific image URI
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnIndexThumb = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/
			if (size == 0) {

			} else {
				@SuppressWarnings("unused")
				int thumbID = 0;
				if (cursor.moveToFirst()) {
					/**************** Captured image details ************/
					/***** Used to show image on view in LoadImagesFromSDCard class ******/
					imageID = cursor.getInt(columnIndex);
					thumbID = cursor.getInt(columnIndexThumb);
					String Path = cursor.getString(file_ColumnIndex);
					camera_pathname = Path;
				}
			}
		} finally {
		}

		return "" + imageID;
	}

	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(
				VipMemberShipActivity.this);

		protected void onPreExecute() {
			/****** NOTE: You can call UI Element here. *****/

			// Progress Dialog
			Dialog.setMessage(" Loading image from Sdcard..");
			Dialog.show();
		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;

			try {

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
								+ urls[0]);

				/************** Decode an input stream into a bitmap. *********/
				bitmap = BitmapFactory.decodeStream(VipMemberShipActivity.this
						.getContentResolver().openInputStream(uri));

				if (bitmap != null) {
					/********* Creates a new bitmap, scaled from an existing bitmap. ***********/
					newBitmap = Bitmap
							.createScaledBitmap(bitmap, 110, 95, true);
					bitmap.recycle();
					if (newBitmap != null) {
						bmp = newBitmap;
					}
				}
			} catch (IOException e) {
				// Error fetching image, try to recover

				/********* Cancel execution of this task. **********/
				cancel(true);
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			if (bmp != null) {
				Bitmap newbm = decodeFile(camera_pathname);
				newbm = Bitmap.createScaledBitmap(newbm, 110, 95, true);
				imgProfile.setVisibility(View.VISIBLE);
				imgProfile.setImageBitmap(newbm);
			}
		}

		public Bitmap decodeFile(String path) {// you can provide file path here
			int orientation;
			try {
				if (path == null) {
					return null;
				}
				// decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				// Find the correct scale value. It should be the power of 2.
				final int REQUIRED_SIZE = 70;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 0;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale++;
				}
				// decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				Bitmap bm = BitmapFactory.decodeFile(path, o2);
				Bitmap bitmap = bm;

				ExifInterface exif = new ExifInterface(path);

				orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 1);

				Log.e("ExifInteface .........", "rotation =" + orientation);

				// exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

				Log.e("orientation", "" + orientation);
				Matrix m = new Matrix();

				if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
					m.postRotate(180);
					// m.postScale((float) bm.getWidth(), (float)
					// bm.getHeight());
					// if(m.preRotate(90)){
					Log.e("in orientation", "" + orientation);
					bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
							bm.getHeight(), m, true);
					return bitmap;
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
					m.postRotate(90);
					Log.e("in orientation", "" + orientation);
					bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
							bm.getHeight(), m, true);
					return bitmap;
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
					m.postRotate(270);
					Log.e("in orientation", "" + orientation);
					bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
							bm.getHeight(), m, true);
					return bitmap;
				}
				return bitmap;
			} catch (Exception e) {
				return null;
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_vip_membership_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_submit:
			// search action
			startActivity(new Intent(VipMemberShipActivity.this,VipMembershipReview.class));
			return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
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
