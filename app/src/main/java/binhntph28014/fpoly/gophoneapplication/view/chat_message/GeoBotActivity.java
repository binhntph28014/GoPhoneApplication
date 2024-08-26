package binhntph28014.fpoly.gophoneapplication.view.chat_message;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.databinding.ActivityGeoBotBinding;

public class GeoBotActivity extends AppCompatActivity {

    ActivityGeoBotBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeoBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initController();
    }
    void initController() {
        binding.sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeminiPro model = new GeminiPro();

                String query = binding.edtQuestion.getText().toString();
                binding.sendProgressBar.setVisibility(View.VISIBLE);

                binding.tvResponse.setText("");
                binding.edtQuestion.setText("");

                model.getResponse(query, new ResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        binding.tvResponse.setText(response);
                        binding.sendProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(GeoBotActivity.this,"Error : "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        binding.sendProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }
}