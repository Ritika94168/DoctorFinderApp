package com.anshul.doctorfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MyListAdapterAllBookings extends BaseAdapter implements Filterable {
    Activity context;
    // Bitmap[] bitmaps;
    ArrayList<DisplayListBookingDetails> reportsData;
    ArrayList<DisplayListBookingDetails> reportsDataOriginal;
    HashMap<String, Integer> images = new HashMap<String, Integer>();

    public MyListAdapterAllBookings(Activity context, ArrayList<DisplayListBookingDetails> reportsData) {
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

                reportsData = (ArrayList<DisplayListBookingDetails>) results.values; // has

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
                List<DisplayListBookingDetails> FilteredArrList = new ArrayList<DisplayListBookingDetails>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<DisplayListBookingDetails>(reportsData); // saves

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
                        DisplayListBookingDetails model = reportsDataOriginal.get(i);

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

                reportsData = (ArrayList<DisplayListBookingDetails>) results.values; // has

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
                List<DisplayListBookingDetails> FilteredArrList = new ArrayList<DisplayListBookingDetails>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<DisplayListBookingDetails>(reportsData); // saves

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
                        DisplayListBookingDetails model = reportsDataOriginal.get(i);

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

        TextView name;
        TextView Specialization;
        TextView bookingdate;
        TextView bookingtime;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.patient_booking_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.doctorName);
            holder.Specialization = (TextView) convertView.findViewById(R.id.doctorSpecialization);
            holder.bookingdate = (TextView) convertView.findViewById(R.id.bookingdate);
            holder.bookingtime = (TextView) convertView.findViewById(R.id.bookingtime);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DisplayListBookingDetails medicineListModel = reportsData.get(position);

        holder.name.setText(medicineListModel.getDoctorName());

        holder.Specialization.setText(medicineListModel.getDoctorSpecification());

        holder.bookingdate.setText(medicineListModel.getBookingDate());

        holder.bookingtime.setText(medicineListModel.getBookingTime());


        return convertView;
    }

}
