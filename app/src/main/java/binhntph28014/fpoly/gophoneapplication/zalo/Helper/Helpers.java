package binhntph28014.fpoly.gophoneapplication.zalo.Helper;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import binhntph28014.fpoly.gophoneapplication.zalo.Helper.HMac.HMacUtil;


public class Helpers {
    private static int transIdDefault = 1;

    @NotNull
    @SuppressLint("DefaultLocale")
     public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMdd_hhmmss");
        String timeString = formatDateTime.format(new Date());
        return String.format("%s%06d", timeString, transIdDefault);
    }

    @NotNull
    public static String getMac(@NotNull String key, @NotNull String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return Objects.requireNonNull(HMacUtil.HMacHexStringEncode(HMacUtil.HMACSHA256, key, data));
     }
}
