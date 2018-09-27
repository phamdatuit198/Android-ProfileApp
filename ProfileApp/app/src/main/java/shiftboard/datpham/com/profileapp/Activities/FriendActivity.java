package shiftboard.datpham.com.profileapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shiftboard.datpham.com.profileapp.Adapter.FriendAdapter;
import shiftboard.datpham.com.profileapp.AsyncTask.AsyncTaskPerson;
import shiftboard.datpham.com.profileapp.AsyncTask.AsyncTaskRandom;
import shiftboard.datpham.com.profileapp.Interface.OnAsyncTaskPersonListener;
import shiftboard.datpham.com.profileapp.Object.Friend;
import shiftboard.datpham.com.profileapp.R;
import shiftboard.datpham.com.profileapp.Unit.Common;
import shiftboard.datpham.com.profileapp.Unit.Constant;

public class FriendActivity extends AppCompatActivity implements OnAsyncTaskPersonListener, ListView.OnItemClickListener,View.OnClickListener {

    private String mID;
    private AsyncTaskPerson               mAsynTaskPerson;
    private AsyncTaskRandom               mRandomProfile;
    private ImageView                     mImgProfile;
    private TextView                      mtvName;
    private TextView                      mtvContactPhone;
    private TextView                      mtvContactEmail;
    private TextView                      mtvContactLocation;
    private ListView                      mListFriends;
    private ImageView                     mImgArrow;
    private LinearLayout                  mLinearFriend;
    private TextView                      mtvNumFriend;
    private TextView                      mtvCity;
    private JSONArray                     mItems;

    private boolean flgDisplayList =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        mImgProfile          = (ImageView)findViewById(R.id.img_profile);
        mtvName              = (TextView)findViewById(R.id.tv_name);
        mtvNumFriend         = (TextView)findViewById(R.id.tv_num_friend);
        mImgArrow            = (ImageView)findViewById(R.id.img_arrow);
        mtvContactPhone      = (TextView)findViewById(R.id.tvContactPhone);
        mtvContactEmail      = (TextView)findViewById(R.id.tvContactEmail);
        mtvContactLocation   = (TextView)findViewById(R.id.tvContactLocation);
        mtvCity              = (TextView)findViewById(R.id.tv_city);
        mLinearFriend        = (LinearLayout)findViewById(R.id.ln_friend);
        mListFriends         = (ListView)findViewById(R.id.lv_friends);
        mListFriends.setOnItemClickListener(this);
        mImgArrow.setOnClickListener(this);
        //set fall focus for profile start at top
        mListFriends.setFocusable(false);

        //Get data from Screen Activity
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
            mID = bundle.getString(Constant.TAG_ID);

        //Toolbar setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.friend_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Execute AsynTaskPerson
        Log.d("Friend Activity"," ID: "+mID.toString());
        mAsynTaskPerson = new AsyncTaskPerson(this);
        mAsynTaskPerson.onSetListenResponseFromServer(this);
        mAsynTaskPerson.execute(mID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Common.flgCallBack = true;
        onBackPressed();
        return true;
    }

    @Override
    public void responseFromServer(String result) throws JSONException {


        if(result != null){

            //Start parse data of Json
            JSONObject json = new JSONObject(result);
            mItems= json.getJSONArray(Constant.TAG_FRIEND);
            String address   = json.getString(Constant.TAG_ADDRESS);
            String city      = json.getString(Constant.TAG_CITY);
            String state     = json.getString(Constant.TAG_STATE);
            String zipcode   = json.getString(Constant.TAG_ZIPCODE);
            String email     = json.getString(Constant.TAG_EMAIL);
            String phone     = json.getString(Constant.TAG_PHONE_NUMBER);
            String imageUrl  = json.getString(Constant.TAG_IMAGE_URL);
            String firstName = json.getString(Constant.TAG_FIRST_NAME);
            String lastName  = json.getString(Constant.TAG_LAST_NAME);

            //Initialize name and address
            String name = firstName+" "+lastName;
            String fulladdress = address+" "+city+", "+state+" "+zipcode;
            String cityState = city+", "+state;

            if(!json.isNull(Constant.TAG_IMAGE_URL)){
                //Using library Picasso to load image
                Picasso.with(this).load(imageUrl).into(mImgProfile);
            }

            List<Friend>listFriend = new ArrayList<Friend>();

            for(int i=0;i<mItems.length();i++){

                //Initialize friend object
                Friend friend = new Friend();
                if(!mItems.getJSONObject(i).isNull(Constant.TAG_IMAGE_URL))
                    friend.setUrl(mItems.getJSONObject(i).getString(Constant.TAG_IMAGE_URL));
                else
                    friend.setUrl(null);

                //Initialize fullname of friend
                String fullnameFriend = mItems.getJSONObject(i).getString(Constant.TAG_FIRST_NAME)+" "+mItems.getJSONObject(i).getString(Constant.TAG_LAST_NAME);
                friend.setName(fullnameFriend);
                friend.setEmail(mItems.getJSONObject(i).getString(Constant.TAG_EMAIL));
                listFriend.add(friend);

            }


            FriendAdapter friendAdapter = new FriendAdapter(this,R.layout.list_row_item,listFriend);

            mListFriends.setAdapter(friendAdapter);
            Common.justifyListViewHeightBasedOnChildren(mListFriends);

            mtvName.setText(name.toString());
            mtvCity.setText(cityState.toString());
            mtvContactPhone.setText(phone.toString());
            mtvContactEmail.setText(email.toString());
            mtvContactLocation.setText(fulladdress.toString());
            mtvNumFriend.setText(String.valueOf(mItems.length()));

            //Check visiable layout of friend if amount friend over 0
            if(mItems.length()>0)
                mLinearFriend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_arrow:
                if(flgDisplayList) {
                    //Close list friends
                    Common.animationRotateUpArrow(mImgArrow);
                    flgDisplayList=false;
                    mListFriends.setVisibility(View.GONE);
                }else {
                    //Display list friends
                    Common.animationRotateDownArrow(mImgArrow);
                    flgDisplayList=true;
                    mListFriends.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
