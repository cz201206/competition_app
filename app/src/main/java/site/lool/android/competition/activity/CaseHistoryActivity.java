package site.lool.android.competition.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import site.lool.android.competition.R;
import site.lool.android.competition.forjs.Home;

public class CaseHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history);
       

        //获取前一个activity 提供的路径
        String host = this.getString(R.string.host);
        String path = getIntent().getStringExtra("path");
        String url = host+path;
        //打开网址
        WebView webView = (WebView)findViewById(R.id.webView_caseHistroy);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
