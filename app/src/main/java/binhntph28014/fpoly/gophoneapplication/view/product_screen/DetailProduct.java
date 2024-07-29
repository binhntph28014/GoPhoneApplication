package binhntph28014.fpoly.gophoneapplication.view.product_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import binhntph28014.fpoly.gophoneapplication.PayActivity;
import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.adapter.OptionAdapter;
import binhntph28014.fpoly.gophoneapplication.adapter.ProductAdapter;
import binhntph28014.fpoly.gophoneapplication.adapter.ReviewShowAdapter;
import binhntph28014.fpoly.gophoneapplication.adapter.VoucherAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityDetailProductBinding;
import binhntph28014.fpoly.gophoneapplication.databinding.LayoutDialigOptionProductBinding;
import binhntph28014.fpoly.gophoneapplication.fragment.homescreen.MainActivity;
import binhntph28014.fpoly.gophoneapplication.model.Comment;
import binhntph28014.fpoly.gophoneapplication.model.OptionProduct;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.model.ProductDetail;
import binhntph28014.fpoly.gophoneapplication.model.Voucher;
import binhntph28014.fpoly.gophoneapplication.model.response.DetailProductResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.ListCommentResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.ProductResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.ServerResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.YeuthichRequestBody;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.ApiUtil;
import binhntph28014.fpoly.gophoneapplication.untill.CartUtil;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import binhntph28014.fpoly.gophoneapplication.view.cart.CartActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProduct extends AppCompatActivity implements ObjectUtil {
    public interface YeuthichCallback {
        void onSuccess(String data);
        void onFailure(Throwable t);
    }
    private ActivityDetailProductBinding binding;
    private List<Product> productList;
    private List<Voucher> voucherList;
    private ProductAdapter productAdapter;
    private VoucherAdapter voucherAdapter;
    private ProgressLoadingDialog dialog;
    private AlertDialog dialogCmt;
    private ProductDetail productDetail;
    private List<OptionProduct> optionProductList;
    private OptionAdapter optionAdapter;
    private int quantityProduct = 1;
    private OptionProduct optionProduct;
    private LayoutDialigOptionProductBinding bindingOption;
    private final int totalPrice = 0;
    private String strDetailProduct = "";
    private boolean isShowDetail  = false;
    List<YeuthichRequestBody> listYeuthich ;
    DecimalFormat df = new DecimalFormat("###,###,###");
    String ratingstar;
    String sold_quantity;
    String review_count ;

    Double minPrice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ratingstar = intent.getStringExtra("rating_start");
        sold_quantity = intent.getStringExtra("sold_quantity");
        review_count = intent.getStringExtra("review_count");
        minPrice = intent.getDoubleExtra("minPrice",1000);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initController();
        getVoucher();
        callApiDetailProduct();
        setDataSimilarProduct();
        setNumberCart();


    }

    private void setNumberCart() {
        // Lấy danh sách cart
        binding.tvQuantityCart.setText(CartUtil.listCart.size() + "");
    }

    private void getVoucher() {
        voucherList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            voucherList.add(new Voucher("Giảm" + i + "% đối với đơn hàng trên 100k", "Giảm" + i + "%", "1234", ""));
        }
        binding.count.setText(voucherList.size() + " mã giảm giá");
        productAdapter = new ProductAdapter(this, productList, this);
        voucherAdapter = new VoucherAdapter(this, voucherList);
        binding.recyProductSimilar.setAdapter(productAdapter);
        binding.recyVoucher.setAdapter(voucherAdapter);
    }
    public void  clickReview(){
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        Intent intent = getIntent();
        String id_product = intent.getStringExtra("id_product");
        BaseApi.API.getListComment(token,id_product).enqueue(new Callback<ListCommentResponse>() {
            @Override
            public void onResponse(Call<ListCommentResponse> call, Response<ListCommentResponse> response) {
                if(response.isSuccessful()){
                    Log.d("checkkreviewwww", "onResponse-getListComment: da vao day");
                    ListCommentResponse listCommentResponse = response.body();
                    List<Comment> listComment = listCommentResponse.getData();
                    if(listComment.size()>0){
                        showReviewDialog(listComment);

                    }else{
                        Toast.makeText(DetailProduct.this, "Chưa có lượt đánh giá nào", Toast.LENGTH_SHORT).show();
                    }



                }

            }


            @Override
            public void onFailure(Call<ListCommentResponse> call, Throwable t) {
                Log.d("checkkreviewwww", "onFailure: "+t);

            }
        });
    }
    private void showReviewDialog(List<Comment> listComment) {
        Spinner spinnerSelectStar;
        RecyclerView recyclerViewReview;
        ReviewShowAdapter reviewShowAdapter;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);       LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_review_show, null);
        spinnerSelectStar = dialogView.findViewById(R.id.spinnerselectstar);
        ImageView btn_back  = dialogView.findViewById(R.id.btn_back_cmt);
        recyclerViewReview = dialogView.findViewById(R.id.ryc_review);
        reviewShowAdapter = new ReviewShowAdapter(listComment,this);
        recyclerViewReview.setAdapter(reviewShowAdapter);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        builder.setView(dialogView);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCmt.dismiss(); // Đóng dialog
            }
        });
        spinnerSelectStar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==0){
                    reviewShowAdapter.setReviewList(listComment);
                    reviewShowAdapter.notifyDataSetChanged();
                }
                if(position==1){
                    int targetRating = 5; // Điều chỉnh số cần lọc tại đây
                    List<Comment> filteredComments = listComment.stream()
                            .filter(comment -> comment.getRate() == targetRating)
                            .collect(Collectors.toList());
                    reviewShowAdapter.setReviewList(filteredComments);
                    reviewShowAdapter.notifyDataSetChanged();
                }
                if(position==2){
                    int targetRating = 4; // Điều chỉnh số cần lọc tại đây
                    List<Comment> filteredComments = listComment.stream()
                            .filter(comment -> comment.getRate() == targetRating)
                            .collect(Collectors.toList());
                    reviewShowAdapter.setReviewList(filteredComments);
                    reviewShowAdapter.notifyDataSetChanged();
                }
                if(position==3){
                    int targetRating = 3; // Điều chỉnh số cần lọc tại đây
                    List<Comment> filteredComments = listComment.stream()
                            .filter(comment -> comment.getRate() == targetRating)
                            .collect(Collectors.toList());
                    reviewShowAdapter.setReviewList(filteredComments);
                    reviewShowAdapter.notifyDataSetChanged();
                }
                if(position==4){
                    int targetRating = 2; // Điều chỉnh số cần lọc tại đây
                    List<Comment> filteredComments = listComment.stream()
                            .filter(comment -> comment.getRate() == targetRating)
                            .collect(Collectors.toList());
                    reviewShowAdapter.setReviewList(filteredComments);
                    reviewShowAdapter.notifyDataSetChanged();
                }
                if(position==5){
                    int targetRating = 1; // Điều chỉnh số cần lọc tại đây
                    List<Comment> filteredComments = listComment.stream()
                            .filter(comment -> comment.getRate() == targetRating)
                            .collect(Collectors.toList());
                    reviewShowAdapter.setReviewList(filteredComments);
                    reviewShowAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Xử lý khi không có gì được chọn (nếu cần)
            }
        });




        // Tạo và hiển thị dialog
        dialogCmt = builder.create();
        dialogCmt.show();

    }
    public void callApiDetailProduct() {
        dialog.show();
        Intent intent = getIntent();
        String id_product = intent.getStringExtra("id_product");
        BaseApi.API.getDetailProduct(id_product).enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call, Response<DetailProductResponse> response) {
                if (response.isSuccessful()) {
                    DetailProductResponse detailProductResponse = response.body();
                    assert detailProductResponse != null;
                    if (detailProductResponse.getCode() == 200) {
                        productDetail = detailProductResponse.getResult();
                        Log.d("gggg", "onResponse: " + detailProductResponse.getResult());
                        setDataUi(detailProductResponse);
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        // Parse and display the error message
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Toast.makeText(DetailProduct.this, "Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    boolean isRed = false; // Biến cờ để theo dõi trạng thái màu của ImageView
    public double findMinDiscountPrice(List<OptionProduct> options) {
        double minDiscountPrice = Double.POSITIVE_INFINITY; // Gán giá trị ban đầu là vô cùng lớn
        String name = null;
        for (OptionProduct option : options) {
            Log.d("checkkkk", "findMinDiscountPrice: " + option.getNameColor());
            Log.d("checkkkk", "findMinDiscountPrice: " + option.getPrice());
            Log.d("checkkkk", "findMinDiscountPrice: " + option.getDiscountValue());

            float discountPrice = (float) (option.getPrice() * (100 - option.getDiscountValue()) / 100.0);
            Log.d("checkkkk", "findMinDiscountPrice: " + discountPrice);

            if (discountPrice < minDiscountPrice) {
                minDiscountPrice = discountPrice;
                name = option.getNameColor();
            }
        }

        return minDiscountPrice;
    }
    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void setDataUi(DetailProductResponse detailProductResponse) {

        Log.d("checkgiaaaa", "setDataUi: "+minPrice);

        if (detailProductResponse != null) {
            List<SlideModel> listImg = new ArrayList<>();
            for (int i = 0; i < detailProductResponse.getResult().getImage().size(); i++) {
                String linkImg = detailProductResponse.getResult().getImage().get(i);
                listImg.add(new SlideModel(linkImg, ScaleTypes.FIT));
            }
            Log.d(TAG.toString, "setDataUi: " + detailProductResponse.getResult().getOption());
            if (detailProductResponse.getResult().getOption().size() != 0) {
                DecimalFormat df = new DecimalFormat("###,###,###");
                String price = df.format(minPrice) + " đ";
                binding.tvPrice.setText(price);
            } else {
                binding.tvPrice.setText("Không có dữ liệu trả về");
                Toast.makeText(this, detailProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (detailProductResponse.getResult().getImage().size() != 0) {
                binding.imgProduct.setImageList(listImg, ScaleTypes.FIT);
            } else {
                Glide.with(DetailProduct.this).load(R.drawable.error);
            }


            if (detailProductResponse.getResult().getName().length() != 0) {
                binding.tvName.setText(detailProductResponse.getResult().getName());
                binding.ratingBar.setRating(Float.parseFloat(ratingstar));
                binding.soldQuantity.setText("đã bán  "+sold_quantity);

                binding.txtratingbarrr.setText(review_count);
            } else {
                binding.tvName.setText("Không có dữ liệu trả về");
            }

//            if (detailProductResponse.getResult().getStore_id().getName().length() != 0 || detailProductResponse.getResult().getStore_id().getName() != null) {
//                binding.tvShop.setText(detailProductResponse.getResult().getStore_id().getName());
//            } else {
//                binding.tvShop.setText("Không có dữ liệu trả về");
//            }

//            if (detailProductResponse.getResult().getStore_id().getAddress().length() != 0) {
//                binding.diachiShop.setText(detailProductResponse.getResult().getStore_id().getAddress());
//            } else {
//                binding.diachiShop.setText("Không có dữ liệu trả về");
//            }

//            if (detailProductResponse.getResult().getStore_id().getAvatar().length() != 0) {
//                Glide.with(this)
//                        .load(detailProductResponse.getResult().getStore_id().getAvatar())
//                        .placeholder(R.drawable.loading)
//                        .error(R.drawable.error)
//                        .into(binding.imgShop);
//            } else {
//                Glide.with(this).load(R.drawable.error);
//            }


            strDetailProduct += productDetail.getDescription() + "\n";
            strDetailProduct += "...";
            binding.tvProductDetail.setText(strDetailProduct);
            YeuthichRequestBody yeuthichids = new YeuthichRequestBody(AccountUltil.USER.getId(),detailProductResponse.getResult().getId());
            binding.progressBarFavourite.setVisibility(View.VISIBLE);

            BaseApi.API.getFavorites(AccountUltil.USER.getId()).enqueue(new Callback<List<YeuthichRequestBody>>() {

                @Override
                public void onResponse(Call<List<YeuthichRequestBody>> call, Response<List<YeuthichRequestBody>> response) {
                    if (response.isSuccessful()) {
                        List<YeuthichRequestBody> listYeuthich = response.body();
                        for (int i = 0; i < listYeuthich.size(); i++) {
                            if (detailProductResponse.getResult().getId().equals(listYeuthich.get(i).getProduct_id())) {
                                isRed = true;
                                binding.imgLove.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                                break; // Tìm thấy sản phẩm trong danh sách, thoát khỏi vòng lặp
                            }
                        }
                    } else {
                        Log.d("checktraiim tim", "onResponse: loi roi");
                    }
                    binding.progressBarFavourite.setVisibility(View.GONE);

                    // ...
                }

                @Override
                public void onFailure(Call<List<YeuthichRequestBody>> call, Throwable t) {
                    binding.progressBarFavourite.setVisibility(View.GONE
                    );

                }
            });
            binding.imgLove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("checkannutttlove", "onClick: "+isRed);
                    if (isRed) {
                        binding.progressBarFavourite.setVisibility(View.VISIBLE);

                        binding.imgLove.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // Chuyển sang màu đỏ
                        BaseApi.API.removeFavorite(yeuthichids).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("checkcacnuthoatdong", "onResponse: davao");
                                binding.imgLove.setColorFilter(null); // Chuyển về màu gốc
                                isRed = false; // Đặt lại trạng thái của nút
                                binding.progressBarFavourite.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("checkcacnuthoatdong", "onFailure: "+t);
                                binding.progressBarFavourite.setVisibility(View.GONE);

                            }

                        });

                    } else {
                        binding.imgLove.setColorFilter(null); // Chuyển về màu gốc
                        binding.progressBarFavourite.setVisibility(View.VISIBLE);

                        BaseApi.API.addFavorite(yeuthichids).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // Thêm thành công, cập nhật giao diện
                                    binding.imgLove.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN); // Chuyển sang màu đỏ
                                    isRed = true; // Đặt lại trạng thái của nút
                                }
                                binding.progressBarFavourite.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                binding.progressBarFavourite.setVisibility(View.GONE);

                                // Xử lý lỗi nếu cần
                            }
                        });
                    }
                }
            });

        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
        }


    }

    private void setDetailProduct() {
        if (isShowDetail) {
            strDetailProduct = "";
            strDetailProduct += productDetail.getDescription() + "\n";
            strDetailProduct += "...";
            binding.tvProductDetail.setText(strDetailProduct);
            binding.btnShowDetailProduct.setText("Xem thêm");
            isShowDetail = false;
        } else {
            if (productDetail.getDescription() != null) {
                strDetailProduct += productDetail.getDescription() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getScreen() != null) {
                strDetailProduct += "Screen: " + productDetail.getScreen() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
            }
            if (productDetail.getCamera() != null) {
                strDetailProduct += "Camera: " + productDetail.getCamera() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getChipset() != null) {
                strDetailProduct += "Chipset: " + productDetail.getChipset() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }

            if (productDetail.getRam() != 0) {
                strDetailProduct += "Ram: " + productDetail.getRam() + "GB" + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getRom() != 0) {
                strDetailProduct += "Rom: " + productDetail.getRom() + "GB" + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getOperatingSystem() != null) {
                strDetailProduct += "OperatingSystem: " + productDetail.getOperatingSystem() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getBattery() != null) {
                strDetailProduct += "Battery: " + productDetail.getBattery() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getWeight() != 0) {
                strDetailProduct += "Weight: " + productDetail.getWeight() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            if (productDetail.getManufacturer() != null) {
                strDetailProduct += "Manufacturer: " + productDetail.getManufacturer() + "\n" + "\n";
                isShowDetail = false;
            } else {
                strDetailProduct = "";
                isShowDetail = true;
            }
            binding.tvProductDetail.setText(strDetailProduct);
            binding.btnShowDetailProduct.setText("Thu gọn");
            isShowDetail = true;
        }
    }
    public void setDataSimilarProduct() {
        dialog.show();
        Intent intent = getIntent();
        String id_product = intent.getStringExtra("id_product");
        binding.progressBarFavourite.setVisibility(View.VISIBLE);

        BaseApi.API.getDataSimilarlProduct(id_product).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    assert productResponse != null;
                    Log.d(TAG.toString, "onResponse-setDataSimilarProduct: " + productResponse.getResult().toString());
                    productAdapter.setProductList(productResponse.getResult());
                    binding.recyProductSimilar.setAdapter(productAdapter);
                    binding.progressBarFavourite.setVisibility(View.GONE);

                } else {            binding.progressBarFavourite.setVisibility(View.GONE);


                    Toast.makeText(DetailProduct.this, "Get Data Product Similar Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                binding.progressBarFavourite.setVisibility(View.GONE);

                Toast.makeText(DetailProduct.this, "call api err", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void initController() {
        binding.viewRatingstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickReview();
            }
        });
        binding.ratingBar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickReview();

            }
        });
//        binding.txtratingbarrr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickReview();
//            }
//        });
//        binding.ratingBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickReview();
//
//            }
//        });

        binding.backDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackActivity();
            }
        });
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(false);
            }
        });


        binding.imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProduct.this, CartActivity.class);
                mActivityResultLauncher.launch(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
//        binding.btnDetailComment.setOnClickListener(view -> {
//            Intent intent = new Intent(this, CommentActivity.class);
//            intent.putExtra("id_product", productDetail.getId());
//            intent.putExtra("name", productDetail.getName());
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
//        });
        binding.btnShowDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDetailProduct();
            }
        });
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;
                        int cartSize = intent.getIntExtra("data_cart_size", 0);
                        binding.tvQuantityCart.setText(cartSize + "");
                    }
                }
            });

    private void initView() {
        dialog = new ProgressLoadingDialog(this);
//        binding.textsale.setPaintFlags(binding.textsale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this);
    }

    // --------------------------------- BottomSheetDialog -------------------------------------------- //
    private void showBottomSheetDialog(Boolean isCheck) {
        if (productDetail == null) {
            return;
        }

        BottomSheetDialog dialog1 = new BottomSheetDialog(DetailProduct.this);
        bindingOption = LayoutDialigOptionProductBinding.inflate(getLayoutInflater());
        dialog1.setContentView(bindingOption.getRoot());
        Window window = dialog1.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setDataBottomSheetDialog(isCheck, bindingOption); // Set data cho bottom sheet
        optionProductList = new ArrayList<>();
        optionAdapter = new OptionAdapter(DetailProduct.this, optionProductList);
        optionAdapter.setObjectUtil(this);
        optionAdapter.setDataListOptionProduct(productDetail.getOption());
        bindingOption.rcvOptionProduct.setAdapter(optionAdapter);
        dialog1.show();

        setOnclickBottomDialog(isCheck, bindingOption);
    }

    private void setOnclickBottomDialog(Boolean isCheck, LayoutDialigOptionProductBinding bindingOption) {
        bindingOption.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionProduct != null) {
                    if (isCheck) {

                        Intent intent = new Intent(DetailProduct.this, PayActivity.class);
                        intent.putExtra("totalPrice", totalPrice);
                        startActivity(intent);
//                    } else if (quantityProduct > optionProduct.getSoldQuantity()) {
//                        Toast.makeText(DetailProduct.this, "Không thể thêm quá số lượng sản phẩm trong kho", Toast.LENGTH_SHORT).show();
//
                    } else {
                        urlCartAdd(bindingOption);
                        // Lưu trữ optionProduct.getSoldQuantity() vào Shared Preferences
//                        int soldQuantity = optionProduct.getSoldQuantity();
//                        SharedPreferences sharedPreferences = getSharedPreferences("option", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt("SoldQuantity", soldQuantity);
//                        editor.apply();
                    }
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    Toast.makeText(DetailProduct.this, "Mời chọn sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bindingOption.btnPlus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (quantityProduct < 20) {
                    quantityProduct += 1;
                    bindingOption.tvQuantity.setText(quantityProduct + "");
                }
            }
        });

        bindingOption.btnMinus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (quantityProduct > 1) {
                    quantityProduct -= 1;
                    bindingOption.tvQuantity.setText(quantityProduct + "");
                }
            }
        });
    }

    private void urlCartAdd(LayoutDialigOptionProductBinding bindingOption) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        String optionId = optionProduct.getId();
        quantityProduct = Integer.parseInt(bindingOption.tvQuantity.getText().toString());
        dialog.show();
        binding.progressBarFavourite.setVisibility(View.VISIBLE);
        if(        optionProduct.getQuantity()<quantityProduct){
            Toast.makeText(this, "Sản Phẩm Vượt Quá Số Lượng Cho Phép", Toast.LENGTH_SHORT).show();
            binding.progressBarFavourite.setVisibility(View.GONE);
            dialog.dismiss();

        }else{
            BaseApi.API.createCartItem(token,optionId,quantityProduct).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()) { // chỉ nhận đầu status 200
                        ServerResponse serverResponse = response.body();
                        assert serverResponse != null;
                        Log.d(TAG.toString, "onResponse-cartAdd: " + serverResponse.toString());
                        if (serverResponse.getCode() == 200 || serverResponse.getCode() == 201) {
                            Toast.makeText(DetailProduct.this, "Thêm sản phẩm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                            ApiUtil.setTitleQuantityCart(DetailProduct.this, binding.tvQuantityCart);
                        }
                        binding.progressBarFavourite.setVisibility(View.GONE);

                    } else { // nhận các đầu status #200
                        try {
                            assert response.errorBody() != null;
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");
                            Log.d(TAG.toString, "onResponse-cartAdd: " + errorMessage);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        binding.progressBarFavourite.setVisibility(View.GONE);

                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Toast.makeText(DetailProduct.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG.toString, "onFailure-cartAdd: " + t.toString());
                    dialog.dismiss();
                }
            });

        }

    }
    public static String formatNumber(String numberString) {
        try {
            long number = Long.parseLong(numberString);

            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return numberString;
        }
    }


    @SuppressLint("SetTextI18n")
    private void setDataBottomSheetDialog(Boolean isCheck, LayoutDialigOptionProductBinding bindingoption) {
        if (isCheck) {
            bindingoption.btnSave.setText("Mua ngay");
        } else {
            bindingoption.btnSave.setText("Thêm vào giỏ hàng");
        }
        if (productDetail.getOption().size() != 0) {
            Glide.with(DetailProduct.this).load(productDetail.getOption().get(0).getImage()).into(bindingoption.imgProduct);
        } else {
            Glide.with(DetailProduct.this).load(R.drawable.error).into(bindingoption.imgProduct);
        }
        if (productDetail.getOption().size() != 0) {
            DecimalFormat df = new DecimalFormat("###,###,###");
            bindingoption.tvPrice.setText( df.format(minPrice)+ " đ");
        } else {
            bindingoption.tvPrice.setText("No data");
        }
        if (productDetail.getOption().size() != 0) {
            bindingoption.tvWarehouseQuantity.setText("Kho: " + productDetail.getOption().get(0).getSoldQuantity());
        } else {
            bindingoption.tvWarehouseQuantity.setText("No data");
        }
    }


    @Override
    public void onclickObject(Object object) {
        // Do dùng trung 1 hàm lên phải kiểm tra xem giá trị trả về là của OptionProduct Hay Product
        if (object instanceof OptionProduct) {
            DecimalFormat df = new DecimalFormat("###,###,###");
            Log.d("OBjectDetail", "onclickObject: " + object.toString());
            optionProduct = (OptionProduct) object;
            Log.d("phan tram giam gia", "onclickObject: "+optionProduct.getDiscountValue());
            double phantramgiamgia = (double) (100 - optionProduct.getDiscountValue()) /100;
            Log.d("phan tram giam gia", "onclickObject: "+phantramgiamgia);
            Log.d("price", "onclickObject: "+optionProduct.getPrice());
            Double price =  (optionProduct.getPrice()*phantramgiamgia);
            bindingOption.tvPrice.setText(df.format(price) +" đ");
            if (optionProduct.getQuantity() > 0) {
                optionProduct = (OptionProduct) object;
                Glide.with(this)
                        .load(optionProduct.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(bindingOption.imgProduct);

                bindingOption.tvWarehouseQuantity.setText("Kho: " + optionProduct.getQuantity());
            } else {
                bindingOption.tvWarehouseQuantity.setText("Kho: " + 0);
                Toast.makeText(this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
            }

        } else if (object instanceof Product) {
            Product product = (Product) object;
            String id = product.getId();
            String averageRate = String.valueOf(product.getAverageRate());
            String sellproduct = String.valueOf(product.getSoldQuantity());
            String reviewcount = String.valueOf(product.getReviewCount());
            Double minPrice = (product.getMinPrice());
            Intent intent = new Intent(this, DetailProduct.class);
            intent.putExtra("id_product", id);
            intent.putExtra("sold_quantity",sellproduct);
            intent.putExtra("rating_start",averageRate);
            intent.putExtra("review_count",reviewcount);
            intent.putExtra("minPrice",minPrice);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackActivity();
    }
    private void onBackActivity() {
        // Đoạn này chưa hiểu lắm nhưng kể cả truyền cho màn hình nào thì tất cả
        // registerForActivityResult onBack đểu đc chạy
        Intent intent = new Intent(DetailProduct.this, MainActivity.class);
        intent.putExtra("data_cart_size", CartUtil.listCart.size());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}