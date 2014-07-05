package co.inlist.util;

import co.inlist.activities.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class MyProgressbar extends ProgressDialog {

	public MyProgressbar(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myprogressbar);
	}
}