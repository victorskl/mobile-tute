package com.sankholin.customlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;
    ImageView imageView;
    TextView tvUsername;
    TextView tvAddDate;

    public UserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    // getView() get called to generate the view of each item
    public View getView(int position, View convertView, ViewGroup parent) {
        // get one object content
        User user = getItem(position);
        // instantiate a layout XML file into View objects
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        imageView = (ImageView) view.findViewById(R.id.ivMessage);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvAddDate = (TextView) view.findViewById(R.id.tvAddDate);
        tvUsername.setText(user.getUserName());
        tvAddDate.setText(user.getAddDate());

        return view;
    }
}
