package site.lool.android.competition.forjs;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.level2.CaseHistoryActivity;
import site.lool.android.competition.activity.level2.StepCountingActivity;
import site.lool.android.competition.activity.level2.WebViewActivity;

public class Home {
    private Context context;
    public Home(Context context){
        this.context = context;
    }
    //打开病历 activity
    @JavascriptInterface
    public void openURL_caseHistroy(){
        Intent intent = new Intent(context,CaseHistoryActivity.class);
        intent.putExtra("path",context.getString(R.string.path_caseHistroy));
        context.startActivity(intent);
    }
    //打开计步 activity
    @JavascriptInterface
    public void openURL_stepCounting(){
        Intent intent = new Intent(context, StepCountingActivity.class);
        intent.putExtra("path",context.getString(R.string.path_stepCounting));
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
        Intent intent1 = new Intent();
    }
}
