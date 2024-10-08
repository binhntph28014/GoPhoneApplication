package binhntph28014.fpoly.gophoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.model.City;

public class CityAdapter extends ArrayAdapter<City> {
    public CityAdapter(@NonNull Context context, int resource, @NonNull List<City> objects) {
        super(context, resource,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_spinner_selected, parent,false);
        TextView tvSeleted = convertView.findViewById(R.id.tvSeleted);
        City city = this.getItem(position);
        if(city != null) {
            tvSeleted.setText(city.getProvinceName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_spinner, parent,false);
        TextView tvSpinner = convertView.findViewById(R.id.tvSpinner);
        City city = this.getItem(position);
        if(city != null) {
            tvSpinner.setText(city.getProvinceName());
        }
        return convertView;
    }
    }

