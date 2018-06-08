package site.lool.android.competition.lib.bottombar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import site.lool.android.competition.R;


public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //渲染界面
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, container, false);

        WebView webVeiw =(WebView)view.findViewById(R.id.webView_fragment1);
        String url = "http://lool.site/competition/view/specialOffers.php";
        //设置
        webVeiw.setWebViewClient(new WebViewClient(){//本activity中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webVeiw.getSettings();
        settings.setJavaScriptEnabled(true);//启用js
        //载入
        webVeiw.loadUrl(url);

        return view;
    }
}
