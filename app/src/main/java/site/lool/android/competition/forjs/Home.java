package site.lool.android.competition.forjs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.level2.BloodGlucoseActivity;
import site.lool.android.competition.activity.level2.BloodPressureActivity;
import site.lool.android.competition.activity.level2.CaseHistoryActivity;
import site.lool.android.competition.activity.level2.StepCountingActivity;
import site.lool.android.competition.activity.level2.WebViewActivity;
import site.lool.android.competition.activity.level2.WeightActivity;

public class Home {
    private Context context;
    public Home(Context context){
        this.context = context;
    }

    //打开病历 activity
    @JavascriptInterface
    public void openActivity_caseHistroy(){
        Intent intent = new Intent(context,CaseHistoryActivity.class);
        intent.putExtra("path",context.getString(R.string.path_caseHistroy));
        context.startActivity(intent);
    }

    //打开计步 activity
    @JavascriptInterface
    public void openActivity_stepCounting(){
        Intent intent = new Intent(context, StepCountingActivity.class);
        intent.putExtra("path",context.getString(R.string.path_stepCounting));
        context.startActivity(intent);
    }

    //打开血糖 activity
    @JavascriptInterface
    public void openActivity_bloodGlucose(){
        Intent intent = new Intent(context, BloodGlucoseActivity.class);
        context.startActivity(intent);
    }
    //打开血压 activity
    @JavascriptInterface
    public void openActivity_bloodPressure(){
        Intent intent = new Intent(context, BloodPressureActivity.class);
        context.startActivity(intent);
    }
    //打开体重 activity
    @JavascriptInterface
    public void openActivity_weight(){
        Intent intent = new Intent(context, WeightActivity.class);
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
    @JavascriptInterface
    public void openURL_sos(){
        String host = context.getString(R.string.host);
        String path = context.getString(R.string.path_sos);
        Uri uri = Uri.parse(host+path);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
