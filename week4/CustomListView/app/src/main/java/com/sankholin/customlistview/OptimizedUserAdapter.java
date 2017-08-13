package com.sankholin.customlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OptimizedUserAdapter extends ArrayAdapter<User> {
    private int resourceId;

    public OptimizedUserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // get one object content
        User user = getItem(position);
        System.out.println("@ " + user.getUserName());
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            //create a new ViewHolder
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            // store instance components into the viewHolder
            viewHolder.imageView = (ImageView) view.findViewById(R.id.ivMessage);
            viewHolder.tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            viewHolder.tvAddDate = (TextView) view.findViewById(R.id.tvAddDate);
            // store the viewHolder into the view
            view.setTag(viewHolder);
        } else {
            view = convertView;
            //recover the viewHolder that store the previous instance components again
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvUsername.setText(user.getUserName());
        viewHolder.tvAddDate.setText(user.getAddDate());
        return view;
    }

    // Define a ViewHolder class
    class ViewHolder {
        ImageView imageView;
        TextView tvUsername;
        TextView tvAddDate;
    }

}
