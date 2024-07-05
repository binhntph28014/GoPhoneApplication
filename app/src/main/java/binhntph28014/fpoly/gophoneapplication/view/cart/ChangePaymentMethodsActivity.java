package binhntph28014.fpoly.gophoneapplication.view.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import binhntph28014.fpoly.gophoneapplication.R;


public class ChangePaymentMethodsActivity extends AppCompatActivity {
    private AppCompatButton zalopay, payNormal;
    private int paymentMethods = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_payment_methods);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        zalopay = findViewById(R.id.btn_zalopay);
        payNormal = findViewById(R.id.btn_pay);

        // Lấy giá trị paymentMethods từ Intent
        paymentMethods = getIntent().getIntExtra("paymentMethods", 0);

        payNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật giá trị paymentMethods
                paymentMethods = 1;

                // Truyền giá trị paymentMethods trở lại CartActivity
                Intent intent = new Intent();
                intent.putExtra("paymentMethods", paymentMethods);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        zalopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật giá trị paymentMethods
                paymentMethods = 2;

                // Truyền giá trị paymentMethods trở lại CartActivity
                Intent intent = new Intent();
                intent.putExtra("paymentMethods", paymentMethods);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}