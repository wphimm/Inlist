package co.inlist.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

public class AddCardActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	private EditText edt_card_num;
	private EditText edt_card_name;
	private EditText edt_card_num_cvv;
	private Spinner sp_month;
	private Spinner sp_year;
	private String selected_month, selected_year;
	LinearLayout linearScan;
	boolean flagCardDelete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_card_screen);

		// UtilInList.makeActionBarFullBlack(AddCardActivity.this);

		init();

		actionBarAndButtonActions();

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		List<String> list = new ArrayList<String>();

		list.add(0, "Year");
		for (int i = 1; i < 15; i++) {

			list.add(String.valueOf(year + i));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_year.setAdapter(dataAdapter);

		if (UtilInList
				.ReadSharePrefrence(AddCardActivity.this,
						Constant.SHRED_PR.KEY_USER_CARD_ADDED).toString()
				.equals("1")) {

			String strCardNum = ""
					+ UtilInList.ReadSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_NUMBER).toString();
			edt_card_num.setText("" + strCardNum);

			try {
				String subString = strCardNum.substring(
						strCardNum.length() - 4, strCardNum.length());
				edt_card_num.setText("************" + subString);
			} catch (Exception e) {
				// TODO: handle exception
			}

			edt_card_num_cvv.setText(""
					+ UtilInList.ReadSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_CVV).toString());
			edt_card_name.setText(""
					+ UtilInList.ReadSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_HOLDER_NAME)
							.toString());

			sp_month.setSelection(Integer.parseInt(UtilInList
					.ReadSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_EXP_MONTH)
					.toString()));

			int yearPos = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i)
						.toString()
						.equals(UtilInList.ReadSharePrefrence(
								AddCardActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_EXP_YEAR)
								.toString())) {
					yearPos = i;
				}
			}
			sp_year.setSelection(yearPos);

			linearScan.setVisibility(View.GONE);
		} else {
			edt_card_num.setText("");
			edt_card_num_cvv.setText("");
			edt_card_name.setText("");

			linearScan.setVisibility(View.VISIBLE);
		}

		sp_month.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selected_month = String.valueOf(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		// final String selected;
		sp_year.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selected_year = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*
	 * Initialize all UI Views and objects
	 */
	private void init() {

		linearScan = (LinearLayout) findViewById(R.id.linearScan);

		edt_card_num = (EditText) findViewById(R.id.edt_card_num);
		edt_card_num_cvv = (EditText) findViewById(R.id.edt_card_num_cvv);
		edt_card_name = (EditText) findViewById(R.id.edt_card_name);
		sp_month = (Spinner) findViewById(R.id.sp_month);
		sp_year = (Spinner) findViewById(R.id.sp_year);

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_add_card_actions, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//
//		if (UtilInList
//				.ReadSharePrefrence(AddCardActivity.this,
//						Constant.SHRED_PR.KEY_USER_CARD_ADDED).toString()
//				.equals("1")) {
//			menu.getItem(0).setIcon(R.drawable.btn_delete);
//		} else {
//			menu.getItem(0).setIcon(R.drawable.btn_save);
//		}
//
//		return super.onPrepareOptionsMenu(menu);
//	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_save:

			if (UtilInList
					.ReadSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ADDED).toString()
					.equals("1")) {

				flagCardDelete = true;

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("user_card_id", ""
						+ UtilInList.ReadSharePrefrence(AddCardActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_ID).toString()));
				params.add(new BasicNameValuePair("PHPSESSIONID", ""
						+ UtilInList.ReadSharePrefrence(AddCardActivity.this,
								Constant.SHRED_PR.KEY_SESSIONID)));

				new WebServiceDataPosterAsyncTask(AddCardActivity.this, params,
						Constant.API + Constant.ACTIONS.REMOVE_CARD).execute();

			} else {

				if (edt_card_num.getText().toString().equals("")) {

					UtilInList.validateDialog(AddCardActivity.this,
							Constant.ERRORS.PLZ_CARD_NUMBER,
							Constant.ERRORS.OOPS);
				} else if (edt_card_name.getText().toString().equals("")) {
					UtilInList
							.validateDialog(AddCardActivity.this,
									Constant.ERRORS.PLZ_CARD_NAME,
									Constant.ERRORS.OOPS);
				} else if (selected_month.equals("Month")) {
					UtilInList.validateDialog(AddCardActivity.this,
							Constant.ERRORS.PLZ_CARD_MONTH,
							Constant.ERRORS.OOPS);
				} else if (selected_year.equals("Year")) {
					UtilInList
							.validateDialog(AddCardActivity.this,
									Constant.ERRORS.PLZ_CARD_YEAR,
									Constant.ERRORS.OOPS);
				} else {

					flagCardDelete = false;

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("user_card_id", "0"));
					params.add(new BasicNameValuePair("card_type", "visa"));
					params.add(new BasicNameValuePair("card_number",
							edt_card_num.getText().toString().trim()));
					params.add(new BasicNameValuePair("card_name",
							edt_card_name.getText().toString().trim()));
					params.add(new BasicNameValuePair("card_exp_year",
							selected_year));
					params.add(new BasicNameValuePair("card_exp_month",
							selected_month));
					params.add(new BasicNameValuePair("set_default", "1"));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									AddCardActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(AddCardActivity.this,
							params, Constant.API + Constant.ACTIONS.ADD_CARD)
							.execute();

				}
			}

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
		if (flagCardDelete) {
			try {
				if (result.getString("success").equals("true")) {

					linearScan.setVisibility(View.VISIBLE);

					edt_card_name.setText("");
					edt_card_num.setText("");
					edt_card_num_cvv.setText("");

					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ID, "0");

					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ADDED, "0");

					UtilInList.validateDialog(AddCardActivity.this, result
							.getJSONArray("messages").getString(0),
							Constant.ERRORS.OOPS);

					invalidateOptionsMenu();

				} else {
					UtilInList.validateDialog(AddCardActivity.this, result
							.getJSONArray("errors").getString(0),
							Constant.ERRORS.OOPS);
				}
			} catch (Exception e) {
				Log.v("", "Exception : " + e);
			}
		} else {
			try {
				if (result.getString("success").equals("true")) {

					linearScan.setVisibility(View.GONE);

					UtilInList.WriteSharePrefrence(
							AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ID,
							result.getJSONObject("data").getString(
									"user_card_id"));
					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_NUMBER, ""
									+ edt_card_num.getText().toString().trim());
					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_CVV, ""
									+ edt_card_num_cvv.getText().toString()
											.trim());

					UtilInList
							.WriteSharePrefrence(
									AddCardActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_HOLDER_NAME,
									""
											+ edt_card_name.getText()
													.toString().trim());

					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_EXP_MONTH, ""
									+ selected_month);

					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_EXP_YEAR, ""
									+ selected_year);

					UtilInList.WriteSharePrefrence(AddCardActivity.this,
							Constant.SHRED_PR.KEY_USER_CARD_ADDED, "1");

					if (UtilInList
							.ReadSharePrefrence(AddCardActivity.this,
									Constant.SHRED_PR.KEY_ADDCARD_FROM)
							.toString().equals("1")) {
						startActivity(new Intent(AddCardActivity.this,
								CompletePurchaseActivity.class));
						finish();
					} else {
						UtilInList.validateDialog(AddCardActivity.this, result
								.getJSONArray("messages").getString(0),
								Constant.AppName);

						invalidateOptionsMenu();
					}

				} else {
					UtilInList.validateDialog(AddCardActivity.this, result
							.getJSONArray("errors").getString(0),
							Constant.ERRORS.OOPS);
				}
			} catch (Exception e) {
				Log.v("", "Exception : " + e);
			}
		}
	}

	private void actionBarAndButtonActions() {

		ActionBar actionBar = getActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.login_custome_action_bar);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		actionBar.setDisplayHomeAsUpEnabled(true);

		ImageButton action_button = (ImageButton) actionBar.getCustomView()
				.findViewById(R.id.btn_action_bar);

		action_button.setBackgroundResource(R.drawable.delete_card_onclick);

		action_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (UtilInList
						.ReadSharePrefrence(AddCardActivity.this,
								Constant.SHRED_PR.KEY_USER_CARD_ADDED)
						.toString().equals("1")) {

					flagCardDelete = true;

					List<NameValuePair> params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("user_card_id", ""
							+ UtilInList.ReadSharePrefrence(
									AddCardActivity.this,
									Constant.SHRED_PR.KEY_USER_CARD_ID)
									.toString()));
					params.add(new BasicNameValuePair("PHPSESSIONID", ""
							+ UtilInList.ReadSharePrefrence(
									AddCardActivity.this,
									Constant.SHRED_PR.KEY_SESSIONID)));

					new WebServiceDataPosterAsyncTask(AddCardActivity.this,
							params, Constant.API + Constant.ACTIONS.REMOVE_CARD)
							.execute();

				} else {

					if (edt_card_num.getText().toString().equals("")) {

						UtilInList.validateDialog(AddCardActivity.this,
								Constant.ERRORS.PLZ_CARD_NUMBER,
								Constant.ERRORS.OOPS);
					} else if (edt_card_name.getText().toString().equals("")) {
						UtilInList.validateDialog(AddCardActivity.this,
								Constant.ERRORS.PLZ_CARD_NAME,
								Constant.ERRORS.OOPS);
					} else if (selected_month.equals("Month")) {
						UtilInList.validateDialog(AddCardActivity.this,
								Constant.ERRORS.PLZ_CARD_MONTH,
								Constant.ERRORS.OOPS);
					} else if (selected_year.equals("Year")) {
						UtilInList.validateDialog(AddCardActivity.this,
								Constant.ERRORS.PLZ_CARD_YEAR,
								Constant.ERRORS.OOPS);
					} else {

						flagCardDelete = false;

						List<NameValuePair> params = new ArrayList<NameValuePair>();

						params.add(new BasicNameValuePair("user_card_id", "0"));
						params.add(new BasicNameValuePair("card_type", "visa"));
						params.add(new BasicNameValuePair("card_number",
								edt_card_num.getText().toString().trim()));
						params.add(new BasicNameValuePair("card_name",
								edt_card_name.getText().toString().trim()));
						params.add(new BasicNameValuePair("card_exp_year",
								selected_year));
						params.add(new BasicNameValuePair("card_exp_month",
								selected_month));
						params.add(new BasicNameValuePair("set_default", "1"));
						params.add(new BasicNameValuePair("PHPSESSIONID", ""
								+ UtilInList.ReadSharePrefrence(
										AddCardActivity.this,
										Constant.SHRED_PR.KEY_SESSIONID)));

						new WebServiceDataPosterAsyncTask(AddCardActivity.this,
								params, Constant.API
										+ Constant.ACTIONS.ADD_CARD).execute();

					}
				}

			}
		});

	}

}
