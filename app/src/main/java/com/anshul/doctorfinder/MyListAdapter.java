package com.anshul.doctorfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by root on 4/8/17.
 */

public class MyListAdapter extends BaseAdapter implements Filterable {
    Activity context;
    // Bitmap[] bitmaps;
    ArrayList<DisplayList> reportsData;
    ArrayList<DisplayList> reportsDataOriginal;
    HashMap<String, Integer> images = new HashMap<String, Integer>();

    public MyListAdapter(Activity context, ArrayList<DisplayList> reportsData) {
        super();
        this.context = context;
        this.reportsData = reportsData;


    }


    public int getCount() {
        // TODO Auto-generated method stub
        return reportsData.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                reportsData = (ArrayList<DisplayList>) results.values; // has

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                // results of a
                // filtering
                // operation in
                // values
                // List<String> FilteredArrList = new ArrayList<String>();
                List<DisplayList> FilteredArrList = new ArrayList<DisplayList>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<DisplayList>(reportsData); // saves

                }

                /********
                 *
                 * If constraint(CharSequence that is received) is null returns
                 * the mOriginalValues(Original) values else does the Filtering
                 * and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = reportsDataOriginal.size();
                    results.values = reportsDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < reportsDataOriginal.size(); i++) {
                        DisplayList model = reportsDataOriginal.get(i);

                        String data = model.getDoctorName();
                        if (data.toLowerCase(locale).contains(constraint.toString())) {
                            FilteredArrList.add(model);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    public Filter getFilter1() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                reportsData = (ArrayList<DisplayList>) results.values; // has

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                // results of a
                // filtering
                // operation in
                // values
                // List<String> FilteredArrList = new ArrayList<String>();
                List<DisplayList> FilteredArrList = new ArrayList<DisplayList>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<DisplayList>(reportsData); // saves

                }

                /********
                 *
                 * If constraint(CharSequence that is received) is null returns
                 * the mOriginalValues(Original) values else does the Filtering
                 * and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = reportsDataOriginal.size();
                    results.values = reportsDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < reportsDataOriginal.size(); i++) {
                        DisplayList model = reportsDataOriginal.get(i);

                        String data = model.getDoctorSpecification();
                        if (data.toLowerCase(locale).contains(constraint.toString())) {
                            FilteredArrList.add(model);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }

    private class ViewHolder {
        ImageView image;
        TextView name;
        TextView Specialization;
        TextView description;
        TextView address;
        TextView rating;
        ImageButton locationBt;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_list1, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.doctorImage);
//            holder.creadteddate = (TextView) convertView.findViewById(R.id.created_date);
            holder.name = (TextView) convertView.findViewById(R.id.doctorName);

            holder.Specialization = (TextView) convertView.findViewById(R.id.doctorSpecialization);
            holder.description = (TextView) convertView.findViewById(R.id.doctorDescription);
            holder.address = (TextView) convertView.findViewById(R.id.doctorAddress);
            holder.rating = (TextView) convertView.findViewById(R.id.doctorRating);
            holder.locationBt = (ImageButton) convertView.findViewById(R.id.locationButton);

            holder.locationBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to google Map for current Address
                    String strUri = "http://maps.google.com/maps?q=Location:-" + "20/21 Industrial Area,Ambala Cantt";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);


                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DisplayList medicineListModel = reportsData.get(position);

        holder.image.setImageResource(R.drawable.hospital);
        //holder.image.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], 100, 50, false));
        //holder.image.setImageBitmap(b);
        holder.locationBt.setImageResource(R.drawable.aaaa);
        holder.name.setText(medicineListModel.getDoctorName());

        holder.Specialization.setText(medicineListModel.getDoctorSpecification());

        holder.description.setText(medicineListModel.getDoctorDescription());

        holder.address.setText(medicineListModel.getDoctorAddress());


        return convertView;
    }

}
