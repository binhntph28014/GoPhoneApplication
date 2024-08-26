package binhntph28014.fpoly.gophoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.model.OptionAndQuantity;
import binhntph28014.fpoly.gophoneapplication.untill.CartInterface;
import binhntph28014.fpoly.gophoneapplication.untill.CartUtil;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<OptionAndQuantity> listCart;
    private CartInterface cartInterface;

    public CartAdapter(Context context, List<OptionAndQuantity> listCart, CartInterface cartInterface) {
        this.context = context;
        this.listCart = listCart;
        this.cartInterface = cartInterface;
    }

    public void setListCart(List<OptionAndQuantity> listCart) {
        this.listCart = listCart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_cart,parent,false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OptionAndQuantity cart = listCart.get(position);
        if(cart == null) {
            return;
        }
        holder.tvName.setText(cart.getOptionProduct().getProduct().getName());
        DecimalFormat df = new DecimalFormat("###,###,###");
        Double checkgia = (double)(100-cart.getOptionProduct().getDiscountValue())/100;
        Double gia = cart.getOptionProduct().getPrice() *checkgia;
        holder.tvPrice.setText(df.format(gia) + "đ");
        holder.tvQuantity.setText(cart.getQuantity() + "");
        holder.tvColorOption.setText("Phân loại: " + cart.getOptionProduct().getNameColor());
        Glide.with(context)
                .load(cart.getOptionProduct().getImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.imgProduct);

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartInterface.onclickMinus(cart,position);
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartInterface.onclickPlus(cart,position);
            }
        });
        holder.chkPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    CartUtil.listCartCheck.add(cart);
                    cartInterface.setTotalPrice();
                } else {
                    CartUtil.listCartCheck.remove(cart);
                    cartInterface.setTotalPrice();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listCart!= null) {
            return listCart.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CheckBox chkPurchase;
        private ImageView imgProduct;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvQuantity;
        private TextView btnMinus;
        private TextView btnPlus;
        private TextView tvColorOption;

        public LinearLayout layoutForeground;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            chkPurchase = itemView.findViewById(R.id.chkPurchase);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            layoutForeground = itemView.findViewById(R.id.layoutForeground);
            tvColorOption = itemView.findViewById(R.id.tvColorOption);
        }
    }
    public void removeItem(int index) {
        listCart.remove(index);
        notifyItemRemoved(index);
    }
    public void undoItem(OptionAndQuantity cart, int index) {
        listCart.add(index, cart);
        notifyItemInserted(index);
    }
}
