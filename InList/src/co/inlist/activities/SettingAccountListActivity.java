package co.inlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingAccountListActivity extends Activity {
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settting_list_screen);

		listView = (ListView) findViewById(R.id.lst_setting);

		String[] values = new String[] { "Change Password", "Billing Details",
				"Invite", "Notification Settings", "Terms & Conditions" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.setting_activity_row, R.id.txt_setting_lst_title,
				values);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (position == 0) {
					startActivity(new Intent(SettingAccountListActivity.this,
							ChangePasswordActivity.class));
				} else if (position == 1) {
					startActivity(new Intent(SettingAccountListActivity.this,
							NoCardActivity.class));
				} else if (position == 2) {
					startActivity(new Intent(SettingAccountListActivity.this,
							InviteActivity.class));
				} else if (position == 3) {
					startActivity(new Intent(SettingAccountListActivity.this,
							NotificationsSettingsActivity.class));
				} else if (position == 4) {
					startActivity(new Intent(SettingAccountListActivity.this,
							TermsConditionsActivity.class));
				}
			}
		});
	}
}