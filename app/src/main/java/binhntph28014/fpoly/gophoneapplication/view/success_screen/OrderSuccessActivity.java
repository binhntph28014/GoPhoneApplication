package binhntph28014.fpoly.gophoneapplication.view.success_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import binhntph28014.fpoly.gophoneapplication.databinding.ActivityOrderSuccessMainBinding;
import binhntph28014.fpoly.gophoneapplication.fragment.homescreen.MainActivity;


public class OrderSuccessActivity extends AppCompatActivity {
    ActivityOrderSuccessMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initController();
    }
    private void initController() {
        binding.btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void initView() {
    }
}