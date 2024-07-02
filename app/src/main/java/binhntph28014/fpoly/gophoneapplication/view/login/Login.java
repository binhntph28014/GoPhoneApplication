package binhntph28014.fpoly.gophoneapplication.view.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityLoginBinding;
import binhntph28014.fpoly.gophoneapplication.fragment.homescreen.MainActivity;
import binhntph28014.fpoly.gophoneapplication.model.response.LoginResponse;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.ApiUtil;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.untill.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressLoadingDialog loadingDialog;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;

    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initController();
        initLoginGoogle();
    }

    private void initController() {
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        binding.txtfogotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPass.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edtEmail.getText().toString().trim();
                String pass = binding.edtPass.getText().toString().trim();
                loginAccount(email, pass);
            }
        });

        binding.btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void loginAccount(String email, String pass) {
        if (validateLogin(email, pass)) {
            loadingDialog.show();
            BaseApi.API.login(email, pass).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.getCode() == 200) {
                            AccountUltil.TOKEN = loginResponse.getToken();
                            Log.d(TAG, "onResponse-Token: " + AccountUltil.TOKEN);
                            // Đăng nhập thành công, lấy chi tiết người dùng và danh sách giỏ hàng
                            ApiUtil.getDetailUser(Login.this, loadingDialog);
                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            screenSwitch(Login.this, MainActivity.class);
                            finishAffinity();
                        }
                    } else {
                        handleErrorResponse(response);
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }

    private void handleErrorResponse(Response<?> response) {
        try {
            String errorBody = response.errorBody().string();
            JSONObject errorJson = new JSONObject(errorBody);
            String errorMessage = errorJson.getString("message");
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void screenSwitch(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    private void initView() {
        loadingDialog = new ProgressLoadingDialog(this);
    }

    private boolean validateLogin(String email, String pass) {
        if (areEditTextsEmpty(binding.edtEmail, binding.edtPass)) {
            Toast.makeText(Login.this, "Vui lòng nhập đủ thông tin !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Validator.isValidEmail(email)) {
            Toast.makeText(Login.this, "Nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean areEditTextsEmpty(EditText editText1, EditText editText2) {
        String text1 = editText1.getText().toString().trim();
        String text2 = editText2.getText().toString().trim();
        return TextUtils.isEmpty(text1) || TextUtils.isEmpty(text2);
    }

    // ----------------------- LOGIN GOOGLE ---------------------------- //

    private void initLoginGoogle() {
        sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completeTask) {
        try {
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);

            String idToken = account.getIdToken();
            String email = account.getEmail();

            if (idToken != null) {
                loadingDialog.show();
                BaseApi.API.loginGoogle(idToken).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getCode() == 200) {
                                AccountUltil.TOKEN = loginResponse.getToken();
                                Log.d(TAG, "Token: " + AccountUltil.TOKEN);
                                // Đăng nhập thành công, lấy chi tiết người dùng và danh sách giỏ hàng
                                ApiUtil.getDetailUser(Login.this,loadingDialog);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("TOKENGG",idToken);
                                editor.commit();
                                Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                screenSwitch(Login.this, MainActivity.class);
                                finishAffinity();
                            }
                        } else {
                            handleErrorResponse(response);
                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
            } else {
                Log.e("Token", "Token is null");
            }
        } catch (ApiException e) {
            Log.w("ApiException", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Đăng nhập bằng Google thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
