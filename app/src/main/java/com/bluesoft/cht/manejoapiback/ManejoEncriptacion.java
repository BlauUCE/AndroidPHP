package com.bluesoft.cht.manejoapiback;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class ManejoEncriptacion {

    String secretKey = "dfkhsdf#%&/kuh345987435%&/7(/86yfsj39834hsf";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ManejoEncriptacion() {

    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encriptar(String txt) {
        return EncAES.encrypt(txt, secretKey);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String desencriptar(String txt) {
        return EncAES.decrypt(txt, secretKey) ;

    }
}
