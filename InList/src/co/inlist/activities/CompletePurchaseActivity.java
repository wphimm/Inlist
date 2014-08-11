package co.inlist.activities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class CompletePurchaseActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	HashMap<String, String> map;
	public static CompletePurchaseActivity cpObj;
	RelativeLayout relativeCost;
	TextView txtPoints, txtTable, txtTotal, txtDate, txtCardNum, txtCardName,
			txtSubtotal, txtTax, txtGratuity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_purchase);

		map = InListApplication.getListEvents().get(
				Integer.parseInt(UtilInList.ReadSharePrefrence(
						CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_CURRENT_POSITION).toString()));
		cpObj = this;

		init();
		actionBarAndButtonActions();
		
		String strHTML = "&#8226; I will arrive on-time before 12.30AM <br/>"
				+ "&#8226; I will dress approprately for the venue <br/>"
				+ "&#8226; I will arrive sober <br/>"
				+ "&#8226; I understand this sale is final. Certain changes can be made in exchange for credit.<br/>";
		txtPoints.setText(Html.fromHtml(strHTML));

		// ***** Date Format ************************************//
		String strDate = "" + map.get("event_start_date");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1;

		try {
			date1 = sdf.parse(strDate);

			SimpleDateFormat format = new SimpleDateFormat("d");
			String date = format.format(date1);

			if (date.endsWith("1") && !date.endsWith("11"))
				format = new SimpleDateFormat("EEE, MMM d'st'");
			else if (date.endsWith("2") && !date.endsWith("12"))
				format = new SimpleDateFormat("EEE, MMM d'nd'");
			else if (date.endsWith("3") && !date.endsWith("13"))
				format = new SimpleDateFormat("EEE, MMM d'rd'");
			else
				format = new SimpleDateFormat("EEE, MMM d'th'");

			strDate = format.format(date1);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String strDateTime = "" + strDate;
		try {
			String strStartTime = ""
					+ map.get("event_start_time").replace(" ", "");
			if (!strStartTime.equals("null")) {
				strDateTime = "" + strDate + " " + strStartTime;
				String strEndTime = ""
						+ map.get("event_end_time").replace(" ", "");
				if (strEndTime.equals("null")) {
					strDateTime = "" + strDate + " " + strStartTime;
				} else {
					strDateTime = "" + strDate + " " + strStartTime + " - "
							+ strEndTime;
				}
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}

		// ***** Date Format ************************************//

		String strTable = ""
				+ InListApplication
						.getPricing()
						.get(Integer.parseInt(UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_PRICE_POSITION)
								.toString())).get("club_section_name") + ",\n"
				+ map.get("event_title") + ",\n" + strDateTime;
		txtTable.setText("" + strTable);

		txtSubtotal.setText("$"
				+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_YOUR_MINIMUM).toString());
		txtTax.setText("$" + map.get("tax"));
		txtGratuity.setText("$" + map.get("gratuity"));

		txtTotal.setText("$"
				+ UtilInList.ReadSharePrefrence(CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_YOUR_MINIMUM).toString() + " ");

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

		String strMonthYear = new DecimalFormat("00").format(Integer
				.parseInt(UtilInList.ReadSharePrefrence(
						CompletePurchaseActivity.this,
						Constant.SHRED_PR.KEY_USER_CARD_EXP_MONTH).toString()))
				+ "/" + strYear;
		txtDate.setText("" + strMonthYear);

	}

	private void init() {
		// TODO Auto-generated method stub
		relativeCost = (RelativeLayout) findViewById(R.id.r3);
		txtPoints = (TextView) findViewById(R.id.txt_points);
		txtTable = (TextView) findViewById(R.id.txt_table);
		txtTotal = (TextView) findViewById(R.id.txt_total);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtCardNum = (TextView) findViewById(R.id.txt_card_num);
		txtCardName = (TextView) findViewById(R.id.txt_card_name);
		txtSubtotal = (TextView) findViewById(R.id.txt_subtotal);
		txtTax = (TextView) findViewById(R.id.txt_tax);
		txtGratuity = (TextView) findViewById(R.id.txt_gratuity);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_complete_purchase_actions, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.hold_top, R.anim.exit_in_left);
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
				overridePendingTransition(R.anim.enter_from_left,
						R.anim.hold_bottom);
			} else {
				UtilInList.validateDialog(CompletePurchaseActivity.this, result
						.getJSONArray("errors").getString(0),
						Constant.ERRORS.OOPS);
			}
		} catch (Exception e) {
			Log.v("", "Exception : " + e);
		}

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
		
		if(map.get("card_required").equals("0")){
			action_button.setVisibility(View.GONE);
			relativeCost.setVisibility(View.GONE);
		}

		action_button.setBackgroundResource(R.drawable.confirm_update_onclick);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String strCapacity = ""
						+ InListApplication
								.getPricing()
								.get(Integer
										.parseInt(UtilInList
												.ReadSharePrefrence(
														CompletePurchaseActivity.this,
														Constant.SHRED_PR.KEY_PRICE_POSITION)
												.toString()))
								.get("table_capacity");
				String strPriceId = ""
						+ InListApplication
								.getPricing()
								.get(Integer
										.parseInt(UtilInList
												.ReadSharePrefrence(
														CompletePurchaseActivity.this,
														Constant.SHRED_PR.KEY_PRICE_POSITION)
												.toString()))
								.get("event_pricing_id");

				String strCardId = ""
						+ UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_ID).toString();

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("event_id", ""
						+ UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_EVENT_ID).toString()));
				params.add(new BasicNameValuePair("party_size", ""
						+ strCapacity));
				params.add(new BasicNameValuePair("bookingItem", "event"));
				params.add(new BasicNameValuePair("event_pricing_id", ""
						+ strPriceId));
				params.add(new BasicNameValuePair("user_card_id", ""
						+ strCardId));
				params.add(new BasicNameValuePair("device_type", "android"));
				params.add(new BasicNameValuePair("PHPSESSIONID", ""
						+ UtilInList.ReadSharePrefrence(
								CompletePurchaseActivity.this,
								Constant.SHRED_PR.KEY_SESSIONID)));

				if (strCardId.equals("0")) {
					params.add(new BasicNameValuePair("card_number", ""
							+ UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_NUMBER)
									.toString()));
					params.add(new BasicNameValuePair(
							"card_name",
							""
									+ UtilInList
											.ReadSharePrefrence(
													CompletePurchaseActivity.this,
													Constant.SHRED_PR.KEY_USER_CARD_HOLDER_NAME)
											.toString()));
					params.add(new BasicNameValuePair("card_cvc", ""
							+ UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_CVV)
									.toString()));
					params.add(new BasicNameValuePair("card_exp_year", ""
							+ UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_EXP_YEAR)
									.toString()));
					params.add(new BasicNameValuePair("card_exp_month", ""
							+ UtilInList.ReadSharePrefrence(
									CompletePurchaseActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_EXP_MONTH)
									.toString()));
				}

				new WebServiceDataPosterAsyncTask(
						CompletePurchaseActivity.this, params, Constant.API
								+ Constant.ACTIONS.BOOK_EVENT_TABLE).execute();

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.hold_top, R.anim.exit_in_left);
	}
}
