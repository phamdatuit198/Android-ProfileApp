package shiftboard.datpham.com.profileapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import shiftboard.datpham.com.profileapp.Interface.OnAsyncTaskPersonListener;
import shiftboard.datpham.com.profileapp.Interface.OnAsyncTaskRandomListener;
import shiftboard.datpham.com.profileapp.Unit.Constant;

/**
 * Created by admin on 9/26/2018.
 */

public class AsyncTaskPerson extends AsyncTask<String, Void, String> {


    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String UrlPerson = Constant.URL_PROFILE_PERSON;
    private URL url = null;
    private HttpURLConnection conn;
    private OnAsyncTaskPersonListener mListner;
    public static final int CONNECTION_TIMEOUT= 10000;
    public static final int READ_TIMEOUT = 15000;

    public AsyncTaskPerson(Context context){
        this.mContext = context;

    }

    public void onSetListenResponseFromServer(OnAsyncTaskPersonListener listener){
        this.mListner = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext,"Loading Profile","Please Wait",false,false);
    }

    @Override
    protected String doInBackground(String... params) {
        String id = params[0];

        //Initialize to retrieve a list of friends for a person
        UrlPerson = UrlPerson+id.toString();
        try{
            url = new URL(UrlPerson);
        }catch (IOException e){
            Log.d("AsyncTask Random","URL is not invalid");
        }

        //Connect to Server
        try{
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
        }catch (IOException e){
            e.printStackTrace();

        }

        //Server response
        try{
            int response_code = conn.getResponseCode();

            if(response_code == HttpURLConnection.HTTP_OK){

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    result.append(line);
                }
                Log.d("Return Server: "," "+result.toString());
                return result.toString();
            }
        }catch (IOException e){
            e.printStackTrace();

        }finally {
            conn.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mProgressDialog.dismiss();
        if(s!=null) {
            try {
                mListner.responseFromServer(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
