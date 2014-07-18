package co.inlist.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataCollectorAsyncTask;
import co.inlist.serverutils.WebServiceDataPosterAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

/*
 * Forgot password class
 */
public class ForgotPassworActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, AsyncTaskCompleteListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_passwod_frame_to_replace);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment_Enter_Email())
					.commit();
		}
	}

	/*
	 * Fragment where enter email id option you will find
	 */
	public static class PlaceholderFragment_Enter_Email extends Fragment {

		private EditText edt_frg_e_mail;

		private void actionBarAndButtonActions() {
			ActionBar actionBar = getActivity().getActionBar();
			// add the custom view to the action bar
			actionBar.setCustomView(R.layout.custome_action_bar);

			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_SHOW_HOME);

			actionBar.setDisplayHomeAsUpEnabled(true);

			ImageButton action_button = (ImageButton) actionBar.getCustomView()
					.findViewById(R.id.btn_action_bar);

			action_button.setBackgroundResource(R.drawable.done_btn_action_bar);

			action_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (edt_frg_e_mail.getText().toString().equals("")) {
						UtilInList
								.validateDialog(getActivity(),
										Constant.ERRORS.PLZ_EMAIL,
										Constant.ERRORS.OOPS);
					} else {
						List<NameValuePair> params = new ArrayList<NameValuePair>();

						params.add(new BasicNameValuePair("email", ""
								+ edt_frg_e_mail.getText().toString().trim()));

						params.add(new BasicNameValuePair("device_type",
								"android"));

						params.add(new BasicNameValuePair("PHPSESSIONID", ""
								+ UtilInList.ReadSharePrefrence(getActivity(),
										Constant.SHRED_PR.KEY_SESSIONID)));

						new WebServiceDataPosterAsyncTask(
								getActivity(),
								params,
								Constant.API
										+ "request_password_reset/?apiMode=VIP&json=true")
								.execute();
					}

				}
			});

		}

		public PlaceholderFragment_Enter_Email() {

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Take appropriate action for each action item click
			switch (item.getItemId()) {

			case android.R.id.home:
				getActivity().finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			setHasOptionsMenu(true);

			View rootView = inflater.inflate(R.layout.forgot_password_screen,
					container, false);

			actionBarAndButtonActions();

			edt_frg_e_mail = (EditText) rootView
					.findViewById(R.id.edt_frg_e_mail);

			return rootView;
		}
	}

	public static class PlaceholderFragment_SendLink extends Fragment {

		public PlaceholderFragment_SendLink() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater
					.inflate(R.layout.forgot_password_screen_send_link,
							container, false);

			return rootView;
		}

	}

	/**
	 * On selecting action bar icons
	 * */

	@Override
	public void onTaskComplete(JSONObject result) {
		// TODO Auto-generated method stub

		if (result != null) {
			try {

				if (result.getString("success").equals("true")) {
					Fragment myCurrentFragment = new PlaceholderFragment_SendLink();
					android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

					myCurrentFragment = new PlaceholderFragment_SendLink();
					manager.beginTransaction()
							.replace(R.id.container, myCurrentFragment)
							.commit();
				} else {
					UtilInList.validateDialog(ForgotPassworActivity.this,
							result.getJSONArray("errors").getString(0),
							Constant.ERRORS.OOPS);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			UtilInList.validateDialog(ForgotPassworActivity.this,
					Constant.ERRORS.SOMETHING_GOES_WRONG, Constant.ERRORS.OOPS);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

}
