package shiftboard.datpham.com.profileapp.Interface;

import org.json.JSONException;

/**
 * Created by admin on 9/26/2018.
 */

public interface OnAsyncTaskPersonListener {
    public void responseFromServer(String result) throws JSONException;
}
