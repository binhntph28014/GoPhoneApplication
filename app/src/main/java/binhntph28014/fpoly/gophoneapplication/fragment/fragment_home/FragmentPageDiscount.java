package binhntph28014.fpoly.gophoneapplication.fragment.fragment_home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.databinding.FragmentPageDiscountBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPageDiscount extends Fragment {





        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Thực hiện hành động khi người dùng kéo xuống để làm mới
                    // Ví dụ: tải lại dữ liệu từ máy chủ, cập nhật giao diện, vv.

                    // Sau khi hoàn thành, bạn cần gọi setRefreshing(false) để dừng quá trình làm mới
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }


}