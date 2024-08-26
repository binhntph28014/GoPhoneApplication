package binhntph28014.fpoly.gophoneapplication.view.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityRegisterBinding;
import binhntph28014.fpoly.gophoneapplication.model.response.ServerResponse;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import binhntph28014.fpoly.gophoneapplication.untill.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ProgressLoadingDialog loadingDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initController();
        initView();
    }
    private void initController() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edtEmail.getText().toString().trim();
                String pass = binding.edtPass.getText().toString().trim();
                String repass = binding.edtRepass.getText().toString().trim();
                registerAccount(email,pass,repass);
            }
        });
        binding.backRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
        binding.tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }

    private void registerAccount(String email,String pass,String repass) {
        if(checkRegister(email,pass,repass)) {
            loadingDialog.show();
            BaseApi.API.register(email,pass).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if(response.isSuccessful()){ // chỉ nhận đầu status 200
                        ServerResponse serverResponse = response.body();
                        Log.d(TAG.toString, "onResponse-register: " + serverResponse.toString());
                        if(serverResponse.getCode() == 200) {
                            Intent intent = new Intent(Register.this, Verify.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                    } else { // nhận các đầu status #200
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");
                            Log.d(TAG.toString, "onResponse-register: " + errorMessage);
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
                    Toast.makeText(Register.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG.toString, "onFailure-register: " + t.toString());
                    loadingDialog.dismiss();
                }
            });
        }
    }
    private boolean checkRegister(String email,String pass,String repass) {
        setTextUI();
        if(!Validator.isValidEmail(email)) {
            binding.tvErrorEmail.setText("Không đúng định dạng email!");
            binding.lineEmail.setVisibility(View.GONE);
            binding.tvErrorEmail.setVisibility(View.VISIBLE);
            return false;
        } else if(!pass.equals(repass)) {
            binding.tvErrorPass.setText("Pass và repass không khớp nhau!");
            binding.linePass.setVisibility(View.GONE);
            binding.tvErrorPass.setVisibility(View.VISIBLE);
            return false;
        } else if(pass.length() < 6) {
            binding.tvErrorPass.setText("Vui lòng đặt mật khẩu từ 6 kí tự!");
            binding.linePass.setVisibility(View.GONE);
            binding.tvErrorPass.setVisibility(View.VISIBLE);
            return false;
        }
        return true;

    }
    private void setTextUI() {
        binding.tvErrorEmail.setText("");
        binding.tvErrorPass.setText("");
        binding.lineEmail.setVisibility(View.VISIBLE);
        binding.tvErrorEmail.setVisibility(View.GONE);
        binding.linePass.setVisibility(View.VISIBLE);
        binding.tvErrorPass.setVisibility(View.GONE);
    }
    private void initView() {
        loadingDialog = new ProgressLoadingDialog(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}