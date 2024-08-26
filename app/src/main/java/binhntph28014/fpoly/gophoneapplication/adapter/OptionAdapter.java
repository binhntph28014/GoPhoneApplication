package binhntph28014.fpoly.gophoneapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.databinding.LayoutIteamOptionProductBinding;
import binhntph28014.fpoly.gophoneapplication.model.OptionProduct;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;
import binhntph28014.fpoly.gophoneapplication.untill.OptionUtil;


public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder>{
    private final Context context;
    private List<OptionProduct> list;
    private OptionUtil optionUtil;
    private ObjectUtil objectUtil;

    public OptionAdapter(Context context, List<OptionProduct> list) {
        this.context = context;
        this.list = list;
    }

    public void setOptionUtil(OptionUtil optionUtil) {
        this.optionUtil = optionUtil;
        notifyDataSetChanged();
    }
    public void setObjectUtil(ObjectUtil objectUtil) {
        this.objectUtil = objectUtil;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataListOptionProduct(List<OptionProduct> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutIteamOptionProductBinding binding = LayoutIteamOptionProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OptionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        OptionProduct optionProduct = list.get(position);
        holder.binding.tvColorOption.setText(optionProduct.getNameColor());
//        holder.binding.tvHetHang1.setVisibility(View.GONE);
//        holder.binding.tvHetHang2.setVisibility(View.GONE);
        Glide.with(context).load(optionProduct.getImage()).into(holder.binding.imgIteamOption);

        if(objectUtil != null) {
            holder.binding.btnDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionUtil != null) {
                    optionUtil.onclickOption(optionProduct);
                }

                if(objectUtil != null) {
                    objectUtil.onclickObject(optionProduct);
                }
            }
        });

        holder.binding.btnDelete.setOnClickListener(view -> {
            optionUtil.deleteOption(optionProduct);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        private final LayoutIteamOptionProductBinding binding;
        public OptionViewHolder(LayoutIteamOptionProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
