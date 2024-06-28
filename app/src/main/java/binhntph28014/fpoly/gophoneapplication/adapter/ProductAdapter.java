package binhntph28014.fpoly.gophoneapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.LayoutItemProductBinding;
import binhntph28014.fpoly.gophoneapplication.model.OptionProduct;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.model.response.DetailProductResponse;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context context;
    private List<Product> productList;
    private List<Product> filteredItems;
    private ObjectUtil objectUtil;

    public ProductAdapter(Context context, List<Product> productList, ObjectUtil objectUtil) {
        this.context = context;
        this.productList = productList;
        this.objectUtil = objectUtil;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        this.filteredItems = new ArrayList<>(productList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemProductBinding binding = LayoutItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.binding.tvName.setText(product.getName());
        DecimalFormat df = new DecimalFormat("###,###,###");
        holder.binding.tvPrice.setText(df.format(product.getMinPrice()) + " đ");
        BaseApi.API.getDetailProduct(product.getId()).enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call, Response<DetailProductResponse> response) {
                if(response.isSuccessful()){
                    DetailProductResponse detailProductResponse = response.body();
                    List<OptionProduct> listoption =    detailProductResponse.getResult().getOption();
                    double maxDiscount = 0;
                    for(OptionProduct optionProduct: listoption)
                    {
                        if (optionProduct.getDiscountValue() > maxDiscount) {
                            maxDiscount = optionProduct.getDiscountValue();
                        }
                    }
                    if(maxDiscount>0){
                        holder.binding.txtDiscount.setVisibility(View.VISIBLE);
                        holder.binding.txtDiscount.setText("-"+(maxDiscount)+"%");
                        holder.binding.imageMall.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(context, "Không có dữ liệu detail", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Log.d("checkloiiiiiiii", "onFailure: "+t);

            }
        });
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.binding.imgProduct);
        holder.binding.ratingBar.setRating((float) product.getAverageRate());
        try {
            holder.binding.tvReview.setText("Đã bán "+product.getSoldQuantity());
        } catch (Exception ex) {
            holder.binding.tvReview.setText("Đã bán "+0);
        }

        holder.binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkfind ", "onClick: "+product.getMinPrice());
                objectUtil.onclickObject(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private LayoutItemProductBinding binding;
        public ProductViewHolder(LayoutItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterItem(String query) {
        productList.clear();
        if(query.isEmpty()) {
            productList.addAll(filteredItems);
        } else {
            for (Product item : filteredItems) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())){
                    productList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}