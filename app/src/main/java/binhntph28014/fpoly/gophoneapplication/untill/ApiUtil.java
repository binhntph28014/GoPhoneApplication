package binhntph28014.fpoly.gophoneapplication.untill;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import binhntph28014.fpoly.gophoneapplication.adapter.CartAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.model.response.CartReponse;
import binhntph28014.fpoly.gophoneapplication.model.response.DetailUserReponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtil {



    public static void getDetailUser(Context context,ProgressLoadingDialog loadingDialog) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        String idUser = JWTUltil.decoded(AccountUltil.TOKEN).getUserId();
        loadingDialog.show();
        BaseApi.API.detailProfile(token,idUser).enqueue(new Callback<DetailUserReponse>() {
            @Override
            public void onResponse(Call<DetailUserReponse> call, Response<DetailUserReponse> response) {
                if(response.isSuccessful()){ // status 200
                    DetailUserReponse detailUserReponse = response.body();

                    Log.d(TAG.toString,"OnResponse-DetailProfile:"+detailUserReponse.toString());
                    if(detailUserReponse.getCode() ==200) {
                        AccountUltil.USER = detailUserReponse.getData();  // lấy data user
                    }
                } else { // status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString,"OnResponse-detailProfile"+errorMessage);
                        Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw  new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DetailUserReponse> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString,"OnFailured-detailProfile" +t.toString());
                loadingDialog.dismiss();
            }
        });
    }

    // lấy getAllCart
    public static void getAllCart(Context context, CartAdapter cartAdapter) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        BaseApi.API.allCartUser(token).enqueue(new Callback<CartReponse>() {
            @Override
            public void onResponse(Call<CartReponse> call, Response<CartReponse> response) {
                if(response.isSuccessful()) {
                    CartReponse cartReponse = response.body();
                    Log.d(TAG.toString,"OnResponse-AllCartUser:" +cartReponse.toString());
                    if(cartReponse.getCode()== 200) {
                        CartUtil.listCart = cartReponse.getData();
                        if(cartAdapter != null) {
                            cartAdapter.setListCart(CartUtil.listCart);
                        }
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString,"OnResponse-AllCartUser"+errorMessage);
                        Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CartReponse> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString,"OnFailured-AllCartUser"+t.toString());
            }
        });
    }
    public static void setTitleQuantityCart(Context context, TextView tvQuantityCart) {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        BaseApi.API.allCartUser(token).enqueue(new Callback<CartReponse>() {
            @Override
            public void onResponse(Call<CartReponse> call, Response<CartReponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    CartReponse cartReponse = response.body();
                    Log.d(TAG.toString, "onResponse-allCartUser: " + cartReponse.toString());
                    if(cartReponse.getCode() == 200) {
                        CartUtil.listCart = cartReponse.getData();
                        tvQuantityCart.setText(CartUtil.listCart.size() + "");
                    }
                } else { // nhận các đầu status #200
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-allCartUser: " + errorMessage);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CartReponse> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-allCartUser: " + t.toString());
            }
        });
    }

}
