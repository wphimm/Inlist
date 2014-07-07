
package co.inlist.interfaces;

import org.json.JSONObject;


public interface AsyncTaskCompleteListener {
    // it will call when background process finish
    public void onTaskComplete(JSONObject result);
}
