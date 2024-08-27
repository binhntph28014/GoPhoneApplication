package binhntph28014.fpoly.gophoneapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.adapter.FavouriteProductAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.FragmentFavouriteBinding;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.model.response.ProductResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.YeuthichRequestBody;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;
import binhntph28014.fpoly.gophoneapplication.view.product_screen.DetailProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFavourite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFavourite extends Fragment implements ObjectUtil {
    private List<Product> listProduct = new ArrayList<>();
    private List<Product> listProductFavourite= new ArrayList<>();
    private FavouriteProductAdapter favouriteProductAdapter;
    private FragmentFavouriteBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        initView();
        callApiGetListAllProducts();

    }
    public FragmentFavourite() {
        // Required empty public constructor
    }

    public static FragmentFavourite newInstance(String param1, String param2) {
        FragmentFavourite fragment = new FragmentFavourite();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentProfile profile = new FragmentProfile();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.framelayout, profile)
                        .addToBackStack(null)  // Add this line if you want to add the fragment to the back stack
                        .commit();
            }
        });
    }
    private void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.recycleProductFavourite.setLayoutManager(layoutManager);
        listProductFavourite = new ArrayList<>();
        favouriteProductAdapter = new FavouriteProductAdapter(getContext(), listProductFavourite, this);
        favouriteProductAdapter.setProductList(listProductFavourite);
        binding.recycleProductFavourite.setAdapter(favouriteProductAdapter);

    }
    public void callApiGetListAllProducts() {
        binding.progressBarFavourite.setVisibility(View.VISIBLE);
        BaseApi.API.getListAllProduct(true, AccountUltil.TOKEN).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if(!(listProduct ==null)){
                        listProduct.clear();
                    }
                    listProduct.addAll(productResponse.getResult());
                    Log.d("datttttttttttt", "onResponse: "+listProduct.size());
                    binding.progressBarFavourite.setVisibility(View.VISIBLE);
                    BaseApi.API.getFavorites(AccountUltil.USER.getId()).enqueue(new Callback<List<YeuthichRequestBody>>() {
                        @Override
                        public void onResponse(Call<List<YeuthichRequestBody>> call, Response<List<YeuthichRequestBody>> response) {
                            if (response.isSuccessful()) {
                                List<YeuthichRequestBody>  productFavouriteResponse = response.body();
                                Log.d("dattttttt", "onResponse: "+productFavouriteResponse.size());
                                List<Product> listProductFavourite = new ArrayList<>();
                                for (YeuthichRequestBody favorite : productFavouriteResponse) {
                                    String favoriteProductId = favorite.getProduct_id();

                                    // Sử dụng Stream API để lọc ra các sản phẩm từ listProduct có id trùng khớp với id trong favoriteProductId
                                    List<Product> matchingProducts = listProduct.stream()
                                            .filter(product -> favoriteProductId.equals(product.getId()))
                                            .collect(Collectors.toList());

                                    // Thêm các sản phẩm đã lọc được vào danh sách listProductFavourite
                                    listProductFavourite.addAll(matchingProducts);
                                }

                                Log.d("datttttt", "onResponse: "+listProductFavourite.size());
                                if(listProductFavourite.size()==0){
                                    Toast.makeText(getActivity(), "Chưa Có Sản Phẩm Yêu Thích Nào", Toast.LENGTH_SHORT).show();
                                }

                                favouriteProductAdapter.setProductList(listProductFavourite);
                                binding.recycleProductFavourite.setAdapter(favouriteProductAdapter);
                                favouriteProductAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getActivity(), "call list all products err", Toast.LENGTH_SHORT).show();
                            }
                            binding.progressBarFavourite.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<YeuthichRequestBody>> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "call list all products err", Toast.LENGTH_SHORT).show();
                }
                binding.progressBarFavourite.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Err", Toast.LENGTH_SHORT).show();
                binding.progressBarFavourite.setVisibility(View.GONE);
            }
        });
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
}