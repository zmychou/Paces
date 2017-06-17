package com.zmychou.paces.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.zmychou.paces.R;

import java.util.HashMap;

public class ArticleActivity extends AppCompatActivity {
    private static String EXTRA_URL = "com.zmychou.paces.article.URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);

        setContentView(R.layout.activity_article);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_article_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleActivity.this.finish();
            }
        });

        final RelativeLayout loading = (RelativeLayout) findViewById(R.id.pb_article_activity_loading);

        WebView webView = (WebView) findViewById(R.id.wv_article_webview);
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.INVISIBLE);
                if (url != null) {
                    toolbar.setTitle(view.getTitle());
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent i = new Intent(ArticleActivity.this, ArticleActivity.class);
                i.putExtra(EXTRA_URL, url);
                startActivity(i);
                return true;
            }
        };
        WebSettings settings = webView.getSettings();
        webView.setWebViewClient(client);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19");

        if (url != null) {
            webView.loadUrl(url);
            return;
        }
        webView.loadUrl("http://iranshao.com/articles/train");
    }
}
