package binhntph28014.fpoly.gophoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.model.Voucher;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder>{
    Context context;
    List<Voucher> list;

    public VoucherAdapter(Context context, List<Voucher> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_voucher,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher voucher = list.get(position);
        holder.textSale.setText(voucher.getNameVoucher());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textSale;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSale = itemView.findViewById(R.id.textVoucher);
        }
    }
}
