package co.inlist.serverutils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import co.inlist.interfaces.AsyncTaskCompleteListener;

public class WebServiceDataCollectorAsyncTaskSplash extends
		AsyncTask<String, Void, JSONObject> {
	private final HttpClient Client = new DefaultHttpClient();
	private String content;
	private JSONObject jObj;
	public AsyncTaskCompleteListener callback;
	String URL;

	public WebServiceDataCollectorAsyncTaskSplash(String URL, Activity act) {
		this.callback = (AsyncTaskCompleteListener) act;
		this.URL = URL;
	}

	@Override
	protected JSONObject doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		try {
			HttpGet httpget = new HttpGet(URL);

			Log.v("", "<<<<< check URL : >>>>> " + URL);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			content = Client.execute(httpget, responseHandler);

			jObj = new JSONObject(content);

		} catch (Exception e) {
			Log.v("WebServiceDataCollectorAsyncTask.java",
					"Exception to call web service : " + e);
		}

		return jObj;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		callback.onTaskComplete(result);
	}

	@Override
	protected void onPreExecute() {

	}
}
