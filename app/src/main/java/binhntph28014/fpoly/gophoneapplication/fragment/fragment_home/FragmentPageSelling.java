package binhntph28014.fpoly.gophoneapplication.fragment.fragment_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.adapter.ProductByCategoryAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.FragmentPageSellingBinding;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.model.ProductByCategory;
import binhntph28014.fpoly.gophoneapplication.model.response.ProductByCategoryReponse;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import binhntph28014.fpoly.gophoneapplication.view.product_screen.DetailProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPageSelling extends Fragment implements ObjectUtil {
    private ProductByCategoryAdapter productAdapter;
    private List<ProductByCategory> productList;
    private FragmentPageSellingBinding binding;

    public FragmentPageSelling() {

    }


    public static FragmentPageSelling newInstance() {
        FragmentPageSelling fragment = new FragmentPageSelling();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPageSellingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initController();
        callApiProductByCategory();
    }
    private void initController() {
    }

    private void initView() {
        productList = new ArrayList<>();
        productAdapter = new ProductByCategoryAdapter(getActivity(), productList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recycleProductMain.setLayoutManager(linearLayoutManager);
        binding.recycleProductMain.setAdapter(productAdapter);
    }

    private void callApiProductByCategory() {
        Log.d("checkkbuggggg", "davaoday");
        binding.progressBar.setVisibility(View.VISIBLE);
        BaseApi.API.getListProductByCategory(AccountUltil.TOKEN).enqueue(new Callback<ProductByCategoryReponse>() {
            @Override
            public void onResponse(Call<ProductByCategoryReponse> call, Response<ProductByCategoryReponse> response) {
                Log.d("checkkbuggggg", "davaoday1");

                if (response.isSuccessful()) { // chỉ nhận đầu status 200
                    ProductByCategoryReponse reponse = response.body();
                    Log.d(TAG.toString, "onResponse-ListProductByCategory: " + reponse.toString());
                    if (reponse.getCode() == 200) {
                        for (ProductByCategory productByCategory : reponse.getResult()) {
                            if (productByCategory.getProduct().size() > 0) {
                                productList.add(productByCategory);
                            }
                        }
                        productAdapter.setListProductType(productList);
                    }
                } else { // nhận các đầu status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-register: " + errorMessage);
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductByCategoryReponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG.toString, "onResume: ");
    }

    @Override
    public void onclickObject(Object object) {
        Product product = (Product) object;
        String id = product.getId();
        String averageRate = String.valueOf(product.getAverageRate());
        String sellproduct = String.valueOf(product.getSoldQuantity());
        String reviewcount = String.valueOf(product.getReviewCount());
        Double minPrice = (product.getMinPrice());
        Intent intent = new Intent(getActivity(), DetailProduct.class);
        intent.putExtra("id_product", id);
        intent.putExtra("sold_quantity",sellproduct);
        intent.putExtra("rating_start",averageRate);
        intent.putExtra("review_count",reviewcount);
        intent.putExtra("minPrice",minPrice);
        Log.d("checkkgiaaa", "checkkkigaaaaa: "+minPrice);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG.toString, "onStart: ");
    }
}