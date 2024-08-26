package binhntph28014.fpoly.gophoneapplication.fragment.fragment_home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import binhntph28014.fpoly.gophoneapplication.R;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;


public class FragmentPageDiscount extends Fragment implements ObjectUtil {


    public FragmentPageDiscount() {

    }

    public static FragmentPageDiscount newInstance() {
        FragmentPageDiscount fragment = new FragmentPageDiscount();
        return fragment;
    }

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


    @Override
    public void onclickObject(Object object) {

    }
}