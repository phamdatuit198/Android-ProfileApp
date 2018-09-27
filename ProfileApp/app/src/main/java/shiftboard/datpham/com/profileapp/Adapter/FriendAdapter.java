package shiftboard.datpham.com.profileapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shiftboard.datpham.com.profileapp.Object.Friend;
import shiftboard.datpham.com.profileapp.R;

/**
 * Created by admin on 9/26/2018.
 */

public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context mContext;
    private int layoutId;
    private List<Friend> list = new ArrayList<Friend>();

    public FriendAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Friend> objects){
        super(context,resource,objects);

        this.mContext = context;
        this.layoutId = resource;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater in = ((Activity)mContext).getLayoutInflater();
        convertView = in.inflate(layoutId,null);

        ImageView imgProfile = (ImageView)convertView.findViewById(R.id.img_profile_friend);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvNameFriend);
        TextView tvEmail = (TextView)convertView.findViewById(R.id.tvEmailFriend);

        tvName.setText(list.get(position).getName());
        tvEmail.setText(list.get(position).getEmail());
        String imageUrl = list.get(position).getUrl();
        if(imageUrl != null){
            Picasso.with(mContext).load(imageUrl).into(imgProfile);
        }
        return convertView;


    }

    @Override
    public int getPosition(@Nullable Friend item) {
        return super.getPosition(item);
    }

    @Nullable
    @Override
    public Friend getItem(int position) {
        return super.getItem(position);
    }
}
