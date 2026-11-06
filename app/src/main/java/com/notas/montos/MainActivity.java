package com.notas.montos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#0f766e"));

        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);   // guarda las notas en el teléfono
        s.setDatabaseEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.addJavascriptInterface(new Puente(), "Android");
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    /** Funciones que la página puede llamar */
    class Puente {
        @JavascriptInterface
        public void share(String texto) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, texto);
            startActivity(Intent.createChooser(i, "Compartir resumen"));
        }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript("window.onAndroidBack && window.onAndroidBack()", valor -> {
            if (!"true".equals(valor)) MainActivity.super.onBackPressed();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.webkit.CookieManager.getInstance().flush();
    }
}
