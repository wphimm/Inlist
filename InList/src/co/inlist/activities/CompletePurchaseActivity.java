package co.inlist.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

@SuppressLint("DefaultLocale")
public class CompletePurchaseActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	public static CompletePurchaseActivity cpObj;
	TextView txtPoints, txtTable, txtTotal, txtDate, txtCardNum, txtCardName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_purchase);

		init();

		cpObj = this;

		String strHTML = "&#8226; I will arrive on-time before 12.30AM <br/>"
				+ "&#8226; I will dress approprately for the venue <br/>"
				+ "&#8226; I will arrive sober <br/>"
				+ "&#8226; I understand this sale is final. Certain changes can be made in exchange for credit.<br/>";
		txtPoints.setText(Html.fromHtml(strHTML));

		String strTable = ""
				+ InListApplication
						.getPricing()
						.get(Integer.parseInt(UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_PRICE_POSITION)
								.toString())).get("club_section_name");
		txtTable.setText("" + strTable);
		txtTotal.setText("$"
				+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_YOUR_MINIMUM).toString());

		String strCardNum = ""
				+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_USER_CARD_NUMBER).toString();
		txtCardNum.setText("" + strCardNum);

		try {
			String subString = strCardNum.substring(strCardNum.length() - 4,
					strCardNum.length());
			txtCardNum.setText("**** **** **** " + subString);
		} catch (Exception e) {
			// TODO: handle exception
		}

		txtCardName
				.setText(""
						+ UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_HOLDER_NAME)
								.toString());

		String strYear = ""
				+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_USER_CARD_EXP_YEAR).toString();

		try {
			strYear = strYear.substring(strYear.length() - 2, strYear.length());
		} catch (Exception e) {
			// TODO: handle exception
		}

		String strDate = new DecimalFormat("00").format(Integer
				.parseInt(UtilInList.ReadSharePrefrence(
						CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_USER_CARD_EXP_MONTH).toString()))
				+ "/" + strYear;
		txtDate.setText("" + strDate);

	}

	private void init() {
		// TODO Auto-generated method stub
		txtPoints = (TextView) findViewById(R.id.txt_points);
		txtTable = (TextView) findViewById(R.id.txt_table);
		txtTotal = (TextView) findViewById(R.id.txt_total);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtCardNum = (TextView) findViewById(R.id.txt_card_num);
		txtCardName = (TextView) findViewById(R.id.txt_card_name);
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

			String strCapacity = ""
					+ InListApplication
							.getPricing()
							.get(Integer.parseInt(UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_PRICE_POSITION)
									.toString())).get("table_capacity");
			String strPriceId = ""
					+ InListApplication
							.getPricing()
							.get(Integer.parseInt(UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_PRICE_POSITION)
									.toString())).get("event_pricing_id");
			
			String strCardId = ""
					+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ID).toString();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("event_id", ""+UtilInList.ReadSharePrefrence(
					CompletePurchaseActivity.this,
					Constant.SHRED_PR.KEY_EVENT_ID)
					.toString()));
			params.add(new BasicNameValuePair("party_size", "" + strCapacity));
			params.add(new BasicNameValuePair("bookingItem", "event"));
			params.add(new BasicNameValuePair("event_pricing_id", "" + strPriceId));
			params.add(new BasicNameValuePair("user_card_id", ""+strCardId ));
			params.add(new BasicNameValuePair("device_type", "android"));
			params.add(new BasicNameValuePair("PHPSESSIONID", ""
					+ UtilInList.ReadSharePrefrence(
							CompletePurchaseActivity.this,
							Constant.SHRED_PR.KEY_SESSIONID)));

			new WebServiceDataPosterAsyncTask(CompletePurchaseActivity.this,
					params, Constant.API + Constant.ACTIONS.BOOK_EVENT_TABLE)
					.execute();

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
		try {
			if (result.getString("success").equals("true")) {
				startActivity(new Intent(CompletePurchaseActivity.this,
						PurchaseSummaryActivity.class));
			} else {
				UtilInList.validateDialog(CompletePurchaseActivity.this, result
						.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}

	}

}
