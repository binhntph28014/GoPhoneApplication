package binhntph28014.fpoly.gophoneapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.databinding.LayoutItemAddressBinding;
import binhntph28014.fpoly.gophoneapplication.model.Info;
import binhntph28014.fpoly.gophoneapplication.untill.InfoInterface;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder>{
    private final Context context;
    private List<Info> infoList;
    private final InfoInterface infoInterface;

    public InfoAdapter(Context context, List<Info> infoList, InfoInterface infoInterface) {
        this.context = context;
        this.infoList = infoList;
        this.infoInterface = infoInterface;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemAddressBinding binding = LayoutItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new InfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        Info info = infoList.get(position);
        if(info == null) {
            return;
        }
        holder.binding.tvName.setText(info.getName()  + " | ");
        holder.binding.tvAddress.setText(info.getAddress());
        holder.binding.tvPhoneNumber.setText(info.getPhoneNumber());
        if(info.getChecked()) {
            holder.binding.tvDefault.setVisibility(View.VISIBLE);
            holder.binding.chkChooseInfo.setChecked(true);
        } else {
            holder.binding.tvDefault.setVisibility(View.GONE);
            holder.binding.chkChooseInfo.setChecked(false);
        }
        holder.binding.chkChooseInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    infoInterface.onclickObject(info);
                }
            }
        });

        holder.binding.tvUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoInterface.updateObject(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(infoList != null) {
            return infoList.size();
        }
        return 0;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemAddressBinding binding;
        public InfoViewHolder(LayoutItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
