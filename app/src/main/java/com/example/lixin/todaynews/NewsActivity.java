package com.example.lixin.todaynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by hua on 2017/8/12.
 */

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        WebView webView = (WebView) findViewById(R.id.webView);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ImageView imageView = (ImageView) findViewById(R.id.news_iv);
        imageView.setOnClickListener(this);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        //要加载的网页
        webView.loadUrl(url);
        webView.addJavascriptInterface("", "test");
        //加载方式
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.news_iv:
                this.finish();
                break;

        }
    }
}
