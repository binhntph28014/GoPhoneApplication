package binhntph28014.fpoly.gophoneapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import binhntph28014.fpoly.gophoneapplication.fragment.fragment_home.FragmentPageDiscount;
import binhntph28014.fpoly.gophoneapplication.fragment.fragment_home.FragmentPageOutStanding;
import binhntph28014.fpoly.gophoneapplication.fragment.fragment_home.FragmentPageSelling;

public class ViewPagerHomeAdapter extends FragmentStateAdapter {
    public ViewPagerHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentPageSelling();
            case 1:
                return new FragmentPageDiscount();
            case 2:
                return new FragmentPageOutStanding();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
