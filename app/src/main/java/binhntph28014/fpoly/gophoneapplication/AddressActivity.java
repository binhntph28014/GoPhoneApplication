package binhntph28014.fpoly.gophoneapplication;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

import binhntph28014.fpoly.gophoneapplication.adapter.InfoAdapter;
import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityAddressBinding;
import binhntph28014.fpoly.gophoneapplication.model.Info;
import binhntph28014.fpoly.gophoneapplication.model.response.InfoResponse;
import binhntph28014.fpoly.gophoneapplication.untill.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.untill.InfoInterface;
import binhntph28014.fpoly.gophoneapplication.untill.TAG;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity implements InfoInterface {
    private ActivityAddressBinding binding;
    private List<Info> infoList;
    private InfoAdapter infoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initController();
        urlGetAllInfo();
    }
    private void initView() {
        infoList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvInfo.setLayoutManager(layoutManager);
        infoAdapter = new InfoAdapter(this, infoList, this);
        binding.rcvInfo.setAdapter(infoAdapter);
    }

    private void initController() {
        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                mActivityResultLauncher.launch(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onclickObject(Object object) {
        // Bắt sự kiện check
        Info info = (Info) object;
        Intent intent = new Intent(AddressActivity.this, PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_info", info);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void updateObject(Object object) {
        Info info = (Info) object;
        Log.d(TAG.toString, "updateObject: " + info.toString());
        Intent intent = new Intent(this, UpdateAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        urlGetAllInfo();

                    }
                }
            });

    private void urlGetAllInfo() {
        String token = AccountUltil.BEARER + AccountUltil.TOKEN;
        binding.progressBar.setVisibility(View.VISIBLE);
        BaseApi.API.getInfo(token).enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    InfoResponse infoResponse = response.body();
                    Log.d(TAG.toString, "onResponse-getInfo: " + infoResponse.toString());
                    if(infoResponse.getCode() == 200 || infoResponse.getCode() == 201) {
                        infoList = infoResponse.getResult();
                        infoAdapter.setInfoList(infoList);
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
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-getInfo: " + t.toString());
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}