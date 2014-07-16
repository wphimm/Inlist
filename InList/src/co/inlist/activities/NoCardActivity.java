package co.inlist.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class NoCardActivity extends Activity implements
		ActionBar.OnNavigationListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_card_screen);

		// UtilInList.makeActionBarFullBlack(NoCardActivity.this);

		actionBarAndButtonActions();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_nocard_details_actions, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// /**
	// * On selecting action bar icons
	// * */
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Take appropriate action for each action item click
	// switch (item.getItemId()) {
	// case R.id.action_add_card:
	// // search action
	// startActivity(new Intent(NoCardActivity.this, AddCardActivity.class));
	// return true;
	//
	// case android.R.id.home:
	// finish();
	// return true;
	//
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	private void actionBarAndButtonActions() {

		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.custome_action_bar);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		actionBar.setDisplayHomeAsUpEnabled(true);

		ImageButton action_button = (ImageButton) actionBar.getCustomView()
				.findViewById(R.id.btn_action_bar);

		action_button
				.setBackgroundResource(R.drawable.no_card_action_btn_onclick);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(NoCardActivity.this,
						AddCardActivity.class));

			}
		});

	}
}
