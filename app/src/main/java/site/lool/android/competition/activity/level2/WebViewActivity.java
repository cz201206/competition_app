package site.lool.android.competition.activity.level2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import site.lool.android.competition.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //获取前一个activity 提供的路径
        String host = this.getString(R.string.host);
        String path = getIntent().getStringExtra("path");
        String url = host+path;
        //打开网址
        WebView webView = (WebView)findViewById(R.id.webView_);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient(){//本activity中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        //启用js
        settings.setJavaScriptEnabled(true);

        //载入
        webView.loadUrl(url);
    }
}
