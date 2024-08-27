package binhntph28014.fpoly.gophoneapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import binhntph28014.fpoly.gophoneapplication.adapter.CartPayAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityPayBinding;
import binhntph28014.fpoly.gophoneapplication.model.CreateOrder;
import binhntph28014.fpoly.gophoneapplication.model.Info;
import binhntph28014.fpoly.gophoneapplication.model.OptionAndQuantity;
import binhntph28014.fpoly.gophoneapplication.model.body.PurchaseBody;
import binhntph28014.fpoly.gophoneapplication.model.response.InfoResponse;
import binhntph28014.fpoly.gophoneapplication.model.response.ServerResponse;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.CartUtil;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import binhntph28014.fpoly.gophoneapplication.view.success_screen.OrderSuccessActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {
    private ActivityPayBinding binding;
    private ProgressLoadingDialog loadingDialog;
    private List<Info> infoList;
    private Info info;
    private CartPayAdapter cartPayAdapter;
    private int totalPrice;

    private int paymentMethods;
    private static final int REQUEST_CODE_ORDER_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        for(int i = 0; i< CartUtil.listCartCheck.size(); i++){
            CartUtil.listCartCheck.get(i).setDiscount_value(CartUtil.listCartCheck.get(i).getOptionProduct().getDiscountValue());
        }


        //ZaloPay create
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2555, Environment.SANDBOX);

        //Take paymentMethods
        paymentMethods = getIntent().getIntExtra("paymentMethods", 0);
        if (paymentMethods == 1){
            binding.txtPaymentMethods.setText("Thanh toán khi nhận hàng");

        }
        if (paymentMethods == 2){
            binding.txtPaymentMethods.setText("Thanh toán qua ví ZaloPay");
        }



        initView();
        initController();
        urlGetAllInfo();
    }

    private void urlGetAllInfo() {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        loadingDialog.show();
        BaseApi.API.getInfo(token).enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    InfoResponse infoResponse = response.body();
                    Log.d(TAG.toString, "onResponse-getInfo: " + infoResponse.toString());
                    if(infoResponse.getCode() == 200 || infoResponse.getCode() == 201) {
                        infoList = infoResponse.getResult();
                        // setDataAddress
                        setDataInfo();
                    }
                } else { // nhận các đầu status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-getInfo: " + errorMessage);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-getInfo: " + t.toString());
                loadingDialog.dismiss();
            }
        });
    }
    private void setDataInfo() {
        if(infoList.size() == 0) {
            binding.tvInfoUser.setText("Chưa có địa chỉ mời bạn tạo");
            return;
        }
        info = infoList.get(0);
        for (int i = 0; i < infoList.size(); i++) {
            if(infoList.get(i).getChecked()) {
                info = infoList.get(i);
                break;
            }
        }
        if(info != null) {
            binding.tvInfoUser.setText(info.getName() + " | " + info.getPhoneNumber() + " | " + info.getAddress());
        } else {
            binding.tvInfoUser.setText("Chưa có địa chỉ mời bạn tạo");
        }
    }
    private void initController() {
        binding.layoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this, AddressActivity.class);
                mActivityResultLauncher.launch(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //---------------Chưa chọn phương thức thanh toán---------------------
                if (paymentMethods == 0){
                    Toast.makeText(PayActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }

                //---------------Zalo Pay----------------
                if (paymentMethods == 2){
                    if(CartUtil.listCartCheck.size() > 0) {
                        zaloRequest();
                        Log.d("thanhtoan", "phuong thuc: zalopay ");
                        Log.d(TAG.toString, "onClick: paymentMethods "+paymentMethods);

                    } else {
                        Toast.makeText(PayActivity.this, "Chưa có sản phẩm nào", Toast.LENGTH_SHORT).show();
                    }

                }
                //-----------------Thanh toán khi nhận hàng-----------------
                else if (paymentMethods == 1) {
                    Log.d("thanhtoan", "phuong thuc: nhan hang ");

                    if(CartUtil.listCartCheck.size() > 0) {
                        urlCreateOrder();
                    } else {
                        Toast.makeText(PayActivity.this, "Chưa có sản phẩm nào", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.imgBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        });
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();
                        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        //    info = bundle.getSerializable("obje   ct_info", Info.class);
                        //}
                        // Cách nhận ở mọi phiên bản
                        if (bundle.containsKey("object_info")) {
                            Object objectInfo = bundle.get("object_info");
                            if (objectInfo instanceof Info) {
                                info = (Info) objectInfo;
                            }
                        }
                        binding.tvInfoUser.setText(info.getName() + " | " + info.getPhoneNumber() + " | " + info.getAddress());
                    }
                }
            });
    //----------------Normal payment----------------------

    private void urlCreateOrder() {
        if(validatePurchare()) {
            String token = AccountUltil.BEARER + AccountUltil.TOKEN;
            PurchaseBody purchaseBody = new PurchaseBody();
            purchaseBody.setInfoId(info.getId());
            purchaseBody.setUserId(AccountUltil.USER.getId());
            purchaseBody.setProductsOrder(CartUtil.listCartCheck);

            loadingDialog.show();
            BaseApi.API.createOrder(token, purchaseBody).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if(response.isSuccessful()){ // chỉ nhận đầu status 200
                        ServerResponse serverResponse = response.body();
                        Log.d(TAG.toString, "onResponse-createOrder: " + serverResponse.toString());
                        if(serverResponse.getCode() == 200 || serverResponse.getCode() == 201) {
                            Toast.makeText(PayActivity.this, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            removeDataCart();
//                            CartUtil.listCartCheck.clear();
                        }
                    } else { // nhận các đầu status #200
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");
                            Log.d(TAG.toString, "onResponse-createOrder: " + errorMessage);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG.toString, "onFailure-createOrder: " + t.toString());
                    loadingDialog.dismiss();
                }
            });
        }
    }

    private void removeDataCart() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < CartUtil.listCartCheck.size(); i++) {
            int position = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    deleteCart(CartUtil.listCartCheck.get(position));
                }
            });
        }
    }
    private void deleteCart(OptionAndQuantity cart) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        String cartId = cart.getId();
        loadingDialog.show();
        BaseApi.API.deleteCartItem(token, cartId).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    ServerResponse serverResponse = response.body();
                    Log.d(TAG.toString, "onResponse-deleteCartItem: " + serverResponse.toString());
                    if(serverResponse.getCode() == 200) {
                        Intent intent = new Intent(PayActivity.this, OrderSuccessActivity.class);
                        startActivity(intent);
                        CartUtil.listCart.removeAll(CartUtil.listCartCheck);
                        CartUtil.listCartCheck.clear();
                        finishAffinity();
                    }
                } else { // nhận các đầu status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-deleteCartItem: " + errorMessage);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(PayActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-deleteCartItem: " + t.toString());
                loadingDialog.dismiss();
            }
        });
    }

    //---------------- End normal payment----------------------


    //----------------Zalo----------------------

    private void urlCreateOrderZalo() {
        if(validatePurchare()) {
            String token = AccountUltil.BEARER + AccountUltil.TOKEN;
            PurchaseBody purchaseBody = new PurchaseBody();
            purchaseBody.setInfoId(info.getId());
            purchaseBody.setUserId(AccountUltil.USER.getId());
            purchaseBody.setProductsOrder(CartUtil.listCartCheck);
            purchaseBody.setPayment_status(true);
            loadingDialog.show();
            BaseApi.API.createOrderByZalo(token, purchaseBody).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    loadingDialog.dismiss();
                    if(response.isSuccessful()){ // chỉ nhận đầu status 200
                        ServerResponse serverResponse = response.body();

                        if(serverResponse.getCode() == 200 || serverResponse.getCode() == 201) {
                            Toast.makeText(PayActivity.this, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            //zaloRequest();

//                            String orderId = serverResponse.getOrder().getId(); // Lấy ID của đơn hàng từ serverResponse
//                           Log.d(TAG.toString, "onResponse: "+orderId);
//                                            Intent intent = new Intent(PayActivity.this, OrderSuccessActivity.class);
//                                            startActivity(intent);
//                            CartUtil.listCartCheck.clear();
                            //Order order = serverResponse.getOrder();
//                            Log.d("Don hang vua tao", "onResponse-createOrder: " + order);
                        }

                    } else { // nhận các đầu status #200
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");
                            Log.d(TAG.toString, "onResponse-createOrder: " + errorMessage);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG.toString, "onFailure-createOrder: " + t.toString());
                    loadingDialog.dismiss();
                }
            });
        }
    }
    private void zaloRequest(){
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(String.valueOf(totalPrice));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d(TAG.toString, "onPaymentSucceeded: Thanh toan thanh cong");
                        removeDataCartZalo();
                        urlCreateOrderZalo();
//                        Intent intent = new Intent(PayActivity.this, OrderSuccessActivity.class);
//                        startActivityForResult(intent, REQUEST_CODE_ORDER_SUCCESS);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d(TAG.toString, "onPaymentCanceled: Huy thanh toan");
                        urlCreateOrder();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Log.d(TAG.toString, "onPaymentError: Thanh toan that bai");
                        urlCreateOrder();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ORDER_SUCCESS && resultCode == RESULT_OK) {
            // Xử lý khi quay trở lại từ màn hình OrderSuccessActivity
            // Thanh toán thành công
            Log.d(TAG.toString, "onActivityResult: Thanh toan thanh cong");
        }
    }

    private void removeDataCartZalo() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < CartUtil.listCartCheck.size(); i++) {
            int position = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    deleteCartZalo(CartUtil.listCartCheck.get(position));
                }
            });
        }
    }
    private void deleteCartZalo(final OptionAndQuantity cart) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        String cartId = cart.getId();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.show();
            }
        });
        //loadingDialog.show();

        BaseApi.API.deleteCartItem(token, cartId).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    ServerResponse serverResponse = response.body();
                    Log.d(TAG.toString, "onResponse-deleteCartItem: " + serverResponse.toString());
                    if(serverResponse.getCode() == 200) {
//                        Intent intent = new Intent(PayActivity.this, OrderSuccessActivity.class);
//                        startActivity(intent);
                        Intent intent = new Intent(PayActivity.this, OrderSuccessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_ORDER_SUCCESS);
                        CartUtil.listCart.removeAll(CartUtil.listCartCheck);
                        CartUtil.listCartCheck.clear();
                        finishAffinity();
                    }
                } else { // nhận các đầu status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-deleteCartItem: " + errorMessage);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(PayActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-deleteCartItem: " + t.toString());
                loadingDialog.dismiss();
            }
        });
    }
    //----------------End Zalo----------------------

    private boolean validatePurchare() {
        if(info == null) {
            Toast.makeText(this, "Mời nhập địa chỉ!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void initView() {
        totalPrice = getIntent().getIntExtra("totalPrice", 0);
        DecimalFormat df = new DecimalFormat("###,###,###");
        binding.tvTotalPrice.setText(df.format(totalPrice) + "đ");

        loadingDialog = new ProgressLoadingDialog(this);
        infoList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvProduct.setLayoutManager(linearLayoutManager);
        cartPayAdapter = new CartPayAdapter(this, CartUtil.listCartCheck);
        binding.rcvProduct.setAdapter(cartPayAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}