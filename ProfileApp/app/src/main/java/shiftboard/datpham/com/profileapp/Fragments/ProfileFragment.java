package shiftboard.datpham.com.profileapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shiftboard.datpham.com.profileapp.Activities.FriendActivity;
import shiftboard.datpham.com.profileapp.Adapter.FriendAdapter;
import shiftboard.datpham.com.profileapp.AsyncTask.AsyncTaskRandom;
import shiftboard.datpham.com.profileapp.Interface.OnAsyncTaskRandomListener;
import shiftboard.datpham.com.profileapp.Object.Friend;
import shiftboard.datpham.com.profileapp.R;
import shiftboard.datpham.com.profileapp.Unit.Common;
import shiftboard.datpham.com.profileapp.Unit.Constant;

import static android.R.attr.animationDuration;
import static android.R.attr.debuggable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements OnAsyncTaskRandomListener,ListView.OnItemClickListener,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AsyncTaskRandom               mRandomProfile;
    private ImageView                     mImgProfile;
    private ImageView                     mImgArrow;
    private TextView                      mtvNumFriend;
    private TextView                      mtvName;
    private TextView                      mtvContactPhone;
    private TextView                      mtvContactEmail;
    private TextView                      mtvContactLocation;
    private TextView                      mtvCity;
    private ListView                      mListFriends;

    private LinearLayout                  mLinearFriend;
    private boolean                       flgDisplayList =false;
    private JSONArray                     mItems;
    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!Common.flgCallBack) {
            mRandomProfile = new AsyncTaskRandom(getActivity());
            mRandomProfile.onSetListenResponseFromServer(this);
            mRandomProfile.execute();
        }else {
            Common.flgCallBack = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        mImgProfile          = (ImageView)root.findViewById(R.id.img_profile);
        mImgArrow            = (ImageView)root.findViewById(R.id.img_arrow);
        mtvName              = (TextView)root.findViewById(R.id.tv_name);
        mtvCity              = (TextView)root.findViewById(R.id.tv_city);
        mtvContactPhone      = (TextView)root.findViewById(R.id.tvContactPhone);
        mtvContactEmail      = (TextView)root.findViewById(R.id.tvContactEmail);
        mtvContactLocation   = (TextView)root.findViewById(R.id.tvContactLocation);
        mtvNumFriend         = (TextView)root.findViewById(R.id.tv_num_friend);
        mLinearFriend        = (LinearLayout)root.findViewById(R.id.ln_friend);
        mListFriends         = (ListView)root.findViewById(R.id.lv_friends);
        mListFriends.setOnItemClickListener(this);

        //set fall focus for profile start at top
        mListFriends.setFocusable(false);
        mImgArrow.setOnClickListener(this);
        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void responseFromServer(String result) throws JSONException {
        if(result != null){

            JSONObject json = new JSONObject(result);
            mItems= json.getJSONArray(Constant.TAG_FRIEND);
            String address = json.getString(Constant.TAG_ADDRESS);
            String city    = json.getString(Constant.TAG_CITY);
            String state   = json.getString(Constant.TAG_STATE);
            String zipcode = json.getString(Constant.TAG_ZIPCODE);
            String email   = json.getString(Constant.TAG_EMAIL);
            String phone   = json.getString(Constant.TAG_PHONE_NUMBER);
            String imageUrl = json.getString(Constant.TAG_IMAGE_URL);
            String firstName = json.getString(Constant.TAG_FIRST_NAME);
            String lastName = json.getString(Constant.TAG_LAST_NAME);
            Log.d("Resutrn Server","Image URL: "+imageUrl);

            //Initialize name and address, city
            String name = firstName+" "+lastName;
            String fulladdress = address+" "+city+", "+state+" "+zipcode;
            String cityState = city+", "+state;
            //Load Image from server
            if(!json.isNull(Constant.TAG_IMAGE_URL)){
                //Using library Picasso to load image
                Picasso.with(getActivity()).load(imageUrl).into(mImgProfile);
            }


            if(!json.isNull(Constant.TAG_IMAGE_URL)){
                //Using library Picasso to load image
                Picasso.with(getActivity()).load(imageUrl).into(mImgProfile);
            }


            List<Friend>listFriend = new ArrayList<Friend>();

            for(int i=0;i<mItems.length();i++){

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


            FriendAdapter friendAdapter = new FriendAdapter(getActivity(),R.layout.list_row_item,listFriend);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Log.d("ListView Click","Name: "+mItems.getJSONObject(position).getString(Constant.TAG_FIRST_NAME));
            Intent intent = new Intent(getActivity(), FriendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.TAG_ID,mItems.getJSONObject(position).getString(Constant.TAG_ID));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_arrow:
                if(flgDisplayList) {
                    Common.animationRotateUpArrow(mImgArrow);
                    flgDisplayList=false;
                    mListFriends.setVisibility(View.GONE);
                }else {
                    Common.animationRotateDownArrow(mImgArrow);
                    flgDisplayList=true;
                    mListFriends.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
