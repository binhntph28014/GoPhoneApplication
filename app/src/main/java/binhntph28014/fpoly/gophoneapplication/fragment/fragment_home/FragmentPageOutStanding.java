package binhntph28014.fpoly.gophoneapplication.fragment.fragment_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


import binhntph28014.fpoly.gophoneapplication.databinding.FragmentPageOutStandingBinding;
import binhntph28014.fpoly.gophoneapplication.model.Product;
import binhntph28014.fpoly.gophoneapplication.untill.ObjectUtil;

public class FragmentPageOutStanding extends Fragment implements ObjectUtil {
    private List<Product> list;
    private FragmentPageOutStandingBinding binding;
    String avarage,reviewcount;


    public FragmentPageOutStanding() {

    }

    public static FragmentPageOutStanding newInstance() {
        FragmentPageOutStanding fragment = new FragmentPageOutStanding();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPageOutStandingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
    }


    @Override
    public void onclickObject(Object object) {

    }
}