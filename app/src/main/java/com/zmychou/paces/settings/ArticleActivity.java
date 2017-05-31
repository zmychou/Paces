package com.zmychou.paces.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zmychou.paces.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_article_toolbar);
        WebView webView = (WebView) findViewById(R.id.wv_article_webview);
        WebViewClient client = new WebViewClient();
        webView.loadUrl("http://www.bing.com/");
        setSupportActionBar(toolbar);
    }
}
