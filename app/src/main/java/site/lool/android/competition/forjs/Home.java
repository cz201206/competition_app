package site.lool.android.competition.forjs;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.CaseHistoryActivity;
import site.lool.android.competition.activity.WebViewActivity;

public class Home {
    private Context context;
    public Home(Context context){
        this.context = context;
    }
    @JavascriptInterface
    public void openURL_caseHistroy(){
        Intent intent = new Intent(context,CaseHistoryActivity.class);
        intent.putExtra("path",context.getString(R.string.path_caseHistroy));
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openURL_article(){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("path",context.getString(R.string.article));
        context.startActivity(intent);
    }
    @JavascriptInterface
    public void openURL_specialOffers(){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("path",context.getString(R.string.specialOffers));
        context.startActivity(intent);
    }
}
