package co.inlist.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

//himanshu new comit

public class AddCardActivity extends Activity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	private EditText edt_card_num;
	private EditText edt_card_name;
	private Spinner sp_month;
	private Spinner sp_year;
	private String selected, spinner_item, selected_month, selected_year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_card_screen);

		init();

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
		edt_card_num = (EditText) findViewById(R.id.edt_card_num);
		edt_card_name = (EditText) findViewById(R.id.edt_card_name);
		sp_month = (Spinner) findViewById(R.id.sp_month);
		sp_year = (Spinner) findViewById(R.id.sp_year);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_add_card_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_save:

			if (edt_card_num.getText().toString().equals("")) {

				UtilInList.validateDialog(getApplicationContext(),
						Constant.ERRORS.PLZ_CARD_NUMBER, Constant.ERRORS.OOPS);
			} else if (edt_card_name.getText().toString().equals("")) {
				UtilInList.validateDialog(getApplicationContext(),
						Constant.ERRORS.PLZ_CARD_NAME, Constant.ERRORS.OOPS);
			} else if (selected_month.equals("Month")) {
				UtilInList.validateDialog(getApplicationContext(),
						Constant.ERRORS.PLZ_CARD_MONTH, Constant.ERRORS.OOPS);
			} else if (selected_year.equals("Year")) {
				UtilInList.validateDialog(getApplicationContext(),
						Constant.ERRORS.PLZ_CARD_YEAR, Constant.ERRORS.OOPS);
			} else {
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("user_card_id", "0"));
				params.add(new BasicNameValuePair("card_type", "visa"));
				params.add(new BasicNameValuePair("card_number", edt_card_num
						.getText().toString().trim()));
				params.add(new BasicNameValuePair("card_name", edt_card_name
						.getText().toString().trim()));
				params.add(new BasicNameValuePair("card_exp_year",
						selected_year));
				params.add(new BasicNameValuePair("card_exp_month",
						selected_month));
				params.add(new BasicNameValuePair("set_default", "1"));

				new WebServiceDataPosterAsyncTask(AddCardActivity.this, params,
						Constant.API + Constant.ACTIONS.ADD_CARD).execute();

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
		try {
			if (result.getString("success").equals("true")) {
				UtilInList.validateDialog(AddCardActivity.this, result
						.getJSONArray("messages").getString(0),
						Constant.ERRORS.OOPS);
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
