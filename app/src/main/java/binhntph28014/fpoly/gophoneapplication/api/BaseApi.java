package binhntph28014.fpoly.gophoneapplication.fragment.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import binhntph28014.fpoly.gophoneapplication.fragment.model.response.LoginResponse;
import binhntph28014.fpoly.gophoneapplication.fragment.model.response.ServerResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface BaseApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
    // 10.0.2.2
    // 10.0.3.2
    // 172.20.10.3
    // 192.168.0.106
//    String LOCALHOT = "103.166.183.57"; // đc cho socket
//    192.168.100.4
    // 192.168.1.109
    //String LOCALHOT = "192.168.1.159"; // đc cho socket


       String LOCALHOT = "192.168.42.106"; // đc cho socket
    BaseApi API = new Retrofit.Builder()
            .baseUrl("http://" + LOCALHOT + ":3000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BaseApi.class);
    //yeuthich
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("login-with-google")
    Call<LoginResponse> loginGoogle(@Field("idToken") String tokenGG);

    @FormUrlEncoded
    @POST("register")
    Call<ServerResponse> register(@Field("email") String email,
                                  @Field("password") String password);
    @GET("logout")
    Call<ServerResponse> logout(@Header("Authorization") String authorization);

    @GET("verify/{idCode}")
    Call<ServerResponse> verify(@Path("idCode") String idCode);

    @FormUrlEncoded
    @PUT("user/change-password/{idUser}")
    Call<ServerResponse> changePassword(@Header("Authorization") String authorization,
                                        @Path("idUser") String idUser,
                                        @Field("oldPassword") String oldPassword,
                                        @Field("newPassword") String newPassword);
    @FormUrlEncoded
    @POST("resend-code")
    Call<ServerResponse> resendCode(@Field("email") String email);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<ServerResponse> forgotPassword(@Field("email") String email);


}
