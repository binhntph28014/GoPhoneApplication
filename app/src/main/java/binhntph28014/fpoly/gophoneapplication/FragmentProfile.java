package binhntph28014.fpoly.gophoneapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import binhntph28014.fpoly.gophoneapplication.api.BaseApi;
import binhntph28014.fpoly.gophoneapplication.databinding.FragmentProfileBinding;
import binhntph28014.fpoly.gophoneapplication.databinding.LayoutDialogLogoutBinding;
import binhntph28014.fpoly.gophoneapplication.databinding.RateUsDialogLayoutBinding;
import binhntph28014.fpoly.gophoneapplication.login.Login;
import binhntph28014.fpoly.gophoneapplication.model.response.ServerResponse;
import binhntph28014.fpoly.gophoneapplication.profile_screen.OrderProductScreen;
import binhntph28014.fpoly.gophoneapplication.profile_screen.ProfileUserScreen;
import binhntph28014.fpoly.gophoneapplication.ultil.AccountUltil;
import binhntph28014.fpoly.gophoneapplication.ultil.ProgressLoadingDialog;
import binhntph28014.fpoly.gophoneapplication.ultil.TAG;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {

    private FragmentProfileBinding binding;
    private ProgressLoadingDialog loadingDialog;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;

    private String phoneNumber = "099999999";
    private String facebookLink = "https://www.facebook.com/quyen2k386/";

    private float userRate = 0;

    public FragmentProfile() {
        // Required empty public constructor
    }


    public static FragmentProfile newInstance() {
        FragmentProfile fragment = new FragmentProfile();
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
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoogleSignInClient = createGoogleSignInClient();
        initView();
        initController();
        setData();

        binding.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + phoneNumber);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        binding.ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(facebookLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private GoogleSignInClient createGoogleSignInClient() {
        // Initialize GoogleSignInOptions with the appropriate configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Add any additional options as needed
                .build();

        // Initialize the GoogleSignInClient
        return GoogleSignIn.getClient(requireContext(), gso);
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == getActivity().RESULT_OK) {
                        setData();
                    }
                }
            });

    private void setData() {
        binding.tvUserName.setText(AccountUltil.USER.getEmail());
        Glide.with(requireActivity())
                .load(AccountUltil.USER.getAvatar())
                .placeholder(R.drawable.loading)
                .error(R.drawable.avatar1)
                .into(binding.imgAvartar);
        if(TextUtils.isEmpty(AccountUltil.USER.getPhone())) {
            binding.tvPhone.setText("099.999.999");
        } else {
            binding.tvPhone.setText(AccountUltil.USER.getPhone());
        }
    }

    private void initController() {
        HistoryDon();//theo dõi đơn hàng
        phanHoiKhieuNai();//phan hoi khieu nai
        resetPass();
        profile();
//        myStore();
        yeuThich();
        logOut();//đăng xuất

    }
    private void profile() {
        binding.layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileUserScreen.class);
                mActivityResultLauncher.launch(intent);
                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }
    private void resetPass() {
        binding.layoutResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ResetPass.class));
                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }

    private void HistoryDon() {
        binding.layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderProductScreen.class));
                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }
    private void yeuThich() {
        binding.layoutFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentFavourite favourite = new FragmentFavourite();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.framelayout, favourite)
                        .addToBackStack(null)  // Add this line if you want to add the fragment to the back stack
                        .commit();
            }
        });
    }

    private void phanHoiKhieuNai() {
        binding.layoutPhanhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateUsDialogLayoutBinding bindingLogout = RateUsDialogLayoutBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(bindingLogout.getRoot());
                Window window = dialog.getWindow();
                assert window != null;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                bindingLogout.rateNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Cảm ơn bạn đã đánh giá và phản hồi.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                bindingLogout.laterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bindingLogout.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if(rating <=1) {
                            bindingLogout.ratingImage.setImageResource(R.drawable.one);
                        } else if(rating <=2) {
                            bindingLogout.ratingImage.setImageResource(R.drawable.two);
                        }else if(rating <=3) {
                            bindingLogout.ratingImage.setImageResource(R.drawable.three);
                        }else if(rating <=4) {
                            bindingLogout.ratingImage.setImageResource(R.drawable.four);
                        } else if(rating<= 5) {
                            bindingLogout.ratingImage.setImageResource(R.drawable.five);
                        }
                        animateImage(bindingLogout.ratingImage);

                        userRate = rating;
                    }
                });
                dialog.show();
            }
        });

//        binding.layoutPhanhoi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), DanhGiaActivity.class));
//                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
//            }
//        });
    }
    private void animateImage(ImageView ratingImage)  {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }
    private void logOut() {
        sharedPreferences = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String tokenGG = sharedPreferences.getString("TOKENGG",null);

        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutDialogLogoutBinding bindingLogout = LayoutDialogLogoutBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(bindingLogout.getRoot());
                Window window = dialog.getWindow();
                assert window != null;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bindingLogout.btnCancelLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bindingLogout.btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutAccount();
                        if(tokenGG != null){
                            mGoogleSignInClient.signOut();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    private void logoutAccount() {
        loadingDialog.show();
        BaseApi.API.logout(AccountUltil.BEARER + AccountUltil.TOKEN).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if(response.isSuccessful()){ // chỉ nhận đầu status 200
                    ServerResponse serverResponse = response.body();
                    assert serverResponse != null;
                    Log.d(TAG.toString, "onResponse-logout: " + serverResponse.toString());
                    if(serverResponse.getCode() == 200) {
                        startActivity(new Intent(getActivity(), Login.class));
                        requireActivity().finishAffinity();
                    }
                } else { // nhận các đầu status #200
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Log.d(TAG.toString, "onResponse-logout: " + errorMessage);
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG.toString, "onFailure-logout: " + t.toString());
                loadingDialog.dismiss();
            }
        });
    }
    private void initView() {
        loadingDialog = new ProgressLoadingDialog(requireContext());
    }
}