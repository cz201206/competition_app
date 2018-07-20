package site.lool.android.competition.activity.level2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.MainActivity;

public class CaseHistoryActivity extends AppCompatActivity {

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_upload:
                    msg += "Click action_upload";
                    break;
                case R.id.action_date:
                    msg += "Click action_date";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(CaseHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history);

        //ToolBar 处理
        Toolbar toolbar_caseHistroy = (Toolbar) findViewById(R.id.toolbar_caseHistroy);
        toolbar_caseHistroy.setTitle("病历查看");//标题
        setSupportActionBar(toolbar_caseHistroy);//启用点击响应
        toolbar_caseHistroy.setOnMenuItemClickListener(onMenuItemClick);

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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_history,menu);
        return true;
    }*/

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
