package co.inlist.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import co.inlist.facebook.android.DialogError;
import co.inlist.facebook.android.Facebook;
import co.inlist.facebook.android.Facebook.DialogListener;
import co.inlist.facebook.android.FacebookError;
import co.inlist.twitter.android.TwitterApp;
import co.inlist.twitter.android.TwitterApp.TwDialogListener;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class PurchaseSummaryActivity extends Activity implements
		ActionBar.OnNavigationListener {

	// FaceBook
	private static String APP_ID = Constant.FB_API_KEY;
	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	// Twitter
	private TwitterApp mTwitter;
	// Client keys
	private static final String CONSUMER_KEY = Constant.CONSUMER_KEY;
	private static final String CONSUMER_SECRET = Constant.CONSUMER_SECRET;

	private enum FROM {
		TWITTER_POST, TWITTER_LOGIN
	};

	private enum MESSAGE {
		SUCCESS, DUPLICATE, FAILED, CANCELLED
	};

	private Button btnFacebook, btnTwitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_summary);

		init();

		btnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					loginToFacebook();
				} else {
					UtilInList.validateDialog(PurchaseSummaryActivity.this, "" + ""
							+ Constant.network_error, Constant.ERRORS.OOPS);
				}
			}
		});

		btnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (UtilInList
						.isInternetConnectionExist(getApplicationContext())) {
					loginToTwitter();
				} else {
					UtilInList.validateDialog(PurchaseSummaryActivity.this, "" + ""
							+ Constant.network_error, Constant.ERRORS.OOPS);
				}
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnTwitter = (Button) findViewById(R.id.btnTwitter);

		// Twitter
		mTwitter = new TwitterApp(PurchaseSummaryActivity.this, CONSUMER_KEY,
				CONSUMER_SECRET);
	}

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

	public void loginToFacebook() {

		mPrefs = PreferenceManager
				.getDefaultSharedPreferences(PurchaseSummaryActivity.this);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
			Log.d("access_token", "here..");
			postOnWall();
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(PurchaseSummaryActivity.this, new String[] {
					"email", "publish_stream" }, new DialogListener() {

				public void onCancel() {
					// Function to handle cancel event
					/*
					 * if (progressDialog.isShowing()) {
					 * progressDialog.dismiss(); }
					 */
				}

				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					Log.d("complete", "here..");
					postOnWall();

				}

				public void onError(DialogError error) {
					// Function to handle error
					/*
					 * if (progressDialog.isShowing()) {
					 * progressDialog.dismiss(); }
					 */
				}

				public void onFacebookError(FacebookError fberror) {
					// Function to handle Facebook errors
					/*
					 * if (progressDialog.isShowing()) {
					 * progressDialog.dismiss(); }
					 */
				}

			});
		}
	}

	public void postOnWall() {
		new post_fb().execute();
	}

	class post_fb extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response);
			if (response == null || response.equals("")
					|| response.equals("false")) {
				Log.v("Error", "Blank response");
				UtilInList
						.validateDialog(
								PurchaseSummaryActivity.this,
								"There is some server issue to share App in your Facebook account.",
								Constant.ERRORS.OOPS);

			} else {

				UtilInList.validateDialog(PurchaseSummaryActivity.this,
						"App share successfully on your Facebook account.",
						Constant.AppName);
			}

		}

		@Override
		protected String doInBackground(String... params) {
			Log.d("Tests", "Testing graph API wall post");
			String response = null;
			try {

				StringBuffer msg = new StringBuffer("");
				msg.append("Hi..");

				response = facebook.request("me");

				Bundle parameters = new Bundle();
				parameters.putString("message", msg.toString());
				response = facebook.request("me/feed", parameters, "POST");
				Log.d("Tests", "got response: " + response);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private void loginToTwitter() {
		// TODO Auto-generated method stub
		mTwitter.setListener(mTwLoginDialogListener);
		mTwitter.resetAccessToken();
		mTwitter.authorize();
	}

	private void postAsToast(FROM twitterPost, MESSAGE success) {
		switch (twitterPost) {
		case TWITTER_LOGIN:
			switch (success) {
			case SUCCESS:
				UtilInList.validateDialog(PurchaseSummaryActivity.this, "Login Successful", Constant.AppName);
				break;
			case FAILED:
				UtilInList.validateDialog(PurchaseSummaryActivity.this, "Login Failed", Constant.ERRORS.OOPS);
			default:
				break;
			}
			break;
		case TWITTER_POST:
			switch (success) {
			case SUCCESS:
				UtilInList.validateDialog(PurchaseSummaryActivity.this, "App share successfully on your Twitter account.", Constant.AppName);
				break;
			case FAILED:
				UtilInList.validateDialog(PurchaseSummaryActivity.this, "Posting Failed" , Constant.ERRORS.OOPS);
				break;
			case DUPLICATE:
				UtilInList.validateDialog(PurchaseSummaryActivity.this, "Posting Failed because of duplicate message.", Constant.ERRORS.OOPS);
			default:
				break;
			}
			break;
		}
	}

	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {

			postAsToast(FROM.TWITTER_LOGIN, MESSAGE.FAILED);
			Log.e("TWITTER", value);
			mTwitter.resetAccessToken();

		}

		public void onComplete(String value) {

			new Post_Twitter().execute();

		}
	};

	class Post_Twitter extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
			mTwitter.resetAccessToken();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String post = "Hi.";
			if (post.length() > 140) {
				post = post.substring(0, Math.min(post.length(), 137)) + "...";
			}
			try {
				mTwitter.updateStatus(post);

				Log.d("twitter post..", "success");

			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}