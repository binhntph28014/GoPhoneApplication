package binhntph28014.fpoly.gophoneapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityVerifyBinding;
import binhntph28014.fpoly.gophoneapplication.model.response.ServerResponse;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import binhntph28014.fpoly.gophoneapplication.view.login.RegisterSuccess;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verify extends AppCompatActivity {
    private ActivityVerifyBinding binding;
    private ProgressLoadingDialog loadingDialog;
    private String strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initController();
    }
    private void initController(){
        binding.backVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
        binding.btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCode = binding.pinview.getText().toString();
                sendCode(strCode);
            }
        });
        binding.tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode();
            }
        });
    }

    private void sendCode(String strCode) {
        loadingDialog.show();
        BaseApi.API.verify(strCode).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    if(response.isSuccessful()) {
                        ServerResponse serverResponse = response.body();
                        Log.d(TAG.toString,"OnResponse-Verify:"+serverResponse.toString());
                        if(serverResponse.getCode()== 200) {
                            Intent intent = new Intent(Verify.this, RegisterSuccess.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");
                            Log.d(TAG.toString, "onResponse-verify: " + errorMessage);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    loadingDialog.dismiss();
                } catch (Exception e) {
                    Log.d(TAG.toString,"onResponse:" +e);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(Verify.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-verify: " + t);
                loadingDialog.dismiss();
            }
        });
    }

    private void resendCode() {
        loadingDialog.show();
        Log.d(TAG.toString,"email:" +strEmail);
        BaseApi.API.resendCode(strEmail).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    Log.d(TAG.toString,"onResponse-resendCode:"+serverResponse.toString());
                    if(serverResponse.getCode()==200) {
                        Toast.makeText(Verify.this,serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-resendCode: " + errorMessage);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(Verify.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-verify: " + t);
                loadingDialog.dismiss();
            }
        });
    }

    private void initView() {
        loadingDialog = new ProgressLoadingDialog(this);
        Intent intent = getIntent();
        strEmail = intent.getStringExtra("email");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}