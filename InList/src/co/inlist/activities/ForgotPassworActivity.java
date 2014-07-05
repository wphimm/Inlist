package co.inlist.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import co.inlist.interfaces.AsyncTaskCompleteListener;
import co.inlist.serverutils.WebServiceDataCollectorAsyncTask;
import co.inlist.util.Constant;
import co.inlist.util.UtilInList;

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

		public PlaceholderFragment_Enter_Email() {

		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.activity_login_actions, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Take appropriate action for each action item click
			switch (item.getItemId()) {
			case R.id.action_sign:
				// search action

				if (edt_frg_e_mail.getText().toString().equals("")) {
					UtilInList.makeToast(getActivity(),
							Constant.ERRORS.PLZ_EMAIL);
				} else {
					new WebServiceDataCollectorAsyncTask(
							"http://www.inlistdev.com/request_password_reset/?apiMode=VIP&json=true&email=himanshufd@webplanex.com",
							getActivity()).execute();
				}

				return true;

			case android.R.id.home:
				// getActivity().finish();
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

		try {
			Log.v("",
					">>>>>>>>> This is response : "
							+ result.getString("success"));

			if (result.getString("success").equals("true")) {
				Fragment myCurrentFragment = new PlaceholderFragment_SendLink();
				android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

				myCurrentFragment = new PlaceholderFragment_SendLink();
				manager.beginTransaction()
						.replace(R.id.container, myCurrentFragment).commit();
			} else {
				UtilInList.makeToast(ForgotPassworActivity.this,
						"Something goes wrong");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
}
