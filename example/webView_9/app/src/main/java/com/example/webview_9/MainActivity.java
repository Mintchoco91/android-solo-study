package com.example.webview_9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//안드로이드 앱 만들기 #9 (WebView)
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "https://www.naver.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        //WebviewClientClass 에서 alt+enter로 클래스만들기 가능
        webView.setWebViewClient(new WebviewClientClass());
    }
    //Ctrl + O


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //안드로이드에서 뒤로가기시 webview back 처리
        if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebviewClientClass extends WebViewClient {

        @Override
        //shouldOverrideUrlLoading : 현재 페이지 url 읽어오는 method
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}