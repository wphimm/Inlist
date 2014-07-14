package co.inlist.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.inlist.adapter.EventsAdapter;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class ProfileActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	TextView txtName, txtEmail, txtPhone;
	public static ProfileActivity profObj;
	RelativeLayout relativeCategories, relativeArchive,relativeVip;
	View viewCategories, viewArchive;
	ListView lst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		profObj = this;

		init();

		txtName.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_FIRSTNAME)
				+ " "
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_LASTNAME));
		txtEmail.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_EMAIL));
		txtPhone.setText(""
				+ UtilInList.ReadSharePrefrence(getApplicationContext(),
						Constant.SHRED_PR.KEY_PHONE));

		lst.setAdapter(new EventsAdapter(InListApplication.getListEvents(),
				ProfileActivity.this));

		relativeCategories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewCategories.setVisibility(View.VISIBLE);
				viewArchive.setVisibility(View.GONE);
			}
		});

		relativeArchive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewArchive.setVisibility(View.VISIBLE);
				viewCategories.setVisibility(View.GONE);
			}
		});
		
		relativeVip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this,
						VipMemberShipActivity.class));
			}
		});

	}

	private void init() {
		// TODO Auto-generated method stub
		txtName = (TextView) findViewById(R.id.txtName);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtPhone = (TextView) findViewById(R.id.txtPhone);
		relativeCategories = (RelativeLayout) findViewById(R.id.linearCategory);
		relativeArchive = (RelativeLayout) findViewById(R.id.linearArchive);
		relativeVip = (RelativeLayout) findViewById(R.id.relativeVIP);
		viewCategories = (View) findViewById(R.id.viewCategories);
		viewArchive = (View) findViewById(R.id.viewArchive);
		lst = (ListView) findViewById(R.id.lst);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_profile_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_edit:
			startActivity(new Intent(ProfileActivity.this,
					EditProfileActivity.class));
			return true;

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub

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
