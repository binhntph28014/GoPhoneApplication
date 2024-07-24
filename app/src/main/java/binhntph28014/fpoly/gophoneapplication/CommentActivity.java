package binhntph28014.fpoly.gophoneapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import binhntph28014.fpoly.gophoneapplication.adapter.ProductAdapter;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.untill.ProgressLoadingDialog;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }
}