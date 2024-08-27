package binhntph28014.fpoly.gophoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ItemNotificationBinding;
import binhntph28014.fpoly.gophoneapplication.model.Notifi;
import binhntph28014.fpoly.gophoneapplication.model.Order;
import binhntph28014.fpoly.gophoneapplication.model.response.store.DetailBills;
import binhntph28014.fpoly.gophoneapplication.ultil.AccountUltil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;

    private List<Notifi> notifiList;

    public NotificationAdapter(Context context, List<Notifi> notifiList) {
        this.context = context;
        this.notifiList = notifiList;
    }

    public void setNotifiList(List<Notifi> notifiList) {
        this.notifiList = notifiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifi notification = notifiList.get(position);

        switch (notification.getType()){
            case "msg":
                holder.binding.tvTitle.setText("Bạn có tin nhắn mới từ " + notification.getSender().getUsername());
                break;
            case "delivere":
                holder.binding.tvTitle.setText("Đơn hàng đang giao");
                break;
            case "wfc":
                holder.binding.tvTitle.setText("Đơn hàng đang chờ xác nhận");
                break;
            case "wfd":
                holder.binding.tvTitle.setText("Đơn hàng đang chờ giao");
                break;
            case "delivered":
                holder.binding.tvTitle.setText("Giao hàng thành công");
                break;
            case "canceled":
                holder.binding.tvTitle.setText("Đã hủy đơn hàng");
                break;
            default:holder.binding.tvTitle.setText("Bạn có thông báo mới");
                break;
        }
        // image
        Glide.with(context)
                .load(R.drawable.avatar1)
                .placeholder(R.drawable.loading)
                .into(holder.binding.imgAvartar);

        // date
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
        Date date;
        Date dateOrder;
        try {
            date = inputFormat.parse(notification.getCreatedAt());
            dateOrder = inputFormat.parse((notification.getContent()));
        } catch (Exception e) {
            date = new Date();
            dateOrder = new Date();
        }
        holder.binding.tvDate.setText(outputFormat.format(date));
        holder.binding.tvContent.setText("thời gian đặt đơn:"+outputFormat.format(dateOrder));

        if(position == notifiList.size() -1) {
            holder.binding.line.setVisibility(View.GONE);
        } else {
            holder.binding.line.setVisibility(View.VISIBLE);
        }
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApi.API.getDetailBill(token,notification.getOrder_id()).enqueue(new Callback<DetailBills>() {
                    @Override
                    public void onResponse(Call<DetailBills> call, Response<DetailBills> response) {
                        if(response.isSuccessful()){
                            DetailBills detailBills = response.body();
                            Order resultBuil = detailBills.getResult();
                            showCustomDialog(context,resultBuil);

                        }
                    }

                    @Override
                    public void onFailure(Call<DetailBills> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notifiList != null) {
            return notifiList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding binding;
        public ViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static void showCustomDialog(Context context,Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.layout_notifi_dialog, null);
        builder.setView(dialogView);
        RecyclerView rcvOrderDetail = dialogView.findViewById(R.id.rcvOrderDetail);
        TextView tvOrderId = dialogView.findViewById(R.id.tvOrderId);
        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);
        TextView tvTotalPrice = dialogView.findViewById(R.id.tvTotalPrice);
        TextView btnItem = dialogView.findViewById(R.id.btnItem);

        DecimalFormat df = new DecimalFormat("###,###,###");
        tvTotalPrice.setText(df.format(order.getTotalPrice()));
        tvOrderId.setText("Đơn hàng: " + order.getId());
        tvStatus.setText(order.getStatus());
        OrderProductAdapter   orderProductAdapter = new OrderProductAdapter(context, order.getProductsOrder());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(layoutManager);
        rcvOrderDetail.setAdapter(orderProductAdapter);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}


