package co.inlist.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class CompletePurchaseActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	public static CompletePurchaseActivity cpObj;
	TextView txtPoints, txtTable, txtTotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_purchase);
		
		init();
		
		cpObj=this;
		
		String strHTML="<html><ul><li>I will arrive on-time before 12.30AM</li>" +
				"<li>I will dress approprately for the venue</li>" +
				"<li>I will arrive sober</li>" +
				"<li>I understand this sale is final. Certain changes can be made in exchange for credit.</li></ul></html>";
		txtPoints.setText(Html.fromHtml(strHTML));
		
		String strTable=""+InListApplication.getPricing().get(Integer.parseInt(UtilInList
				.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_PRICE_POSITION)
				.toString())).get("club_section_name");
		txtTable.setText(""+strTable);
		txtTotal.setText("$ "+UtilInList
				.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_YOUR_MINIMUM)
				.toString());
	}

	private void init() {
		// TODO Auto-generated method stub
		txtPoints = (TextView) findViewById(R.id.txt_points);
		txtTable = (TextView) findViewById(R.id.txt_table);
		txtTotal = (TextView) findViewById(R.id.txt_total);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_complete_purchase_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.complete_purchase:

			startActivity(new Intent(CompletePurchaseActivity.this,
					PurchaseSummaryActivity.class));

			return true;

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
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
