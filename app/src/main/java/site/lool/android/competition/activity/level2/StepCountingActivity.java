package site.lool.android.competition.activity.level2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.service.CaseHistroyService;
import site.lool.android.competition.pojo.CaseHistoryPojo;
import site.lool.android.competition.utils.HttpHelper;
import site.lool.android.competition.utils.SharedPreferencesHelper;

public class StepCountingActivity extends AppCompatActivity implements SensorEventListener {
    WebView webView;
    String url_webView;
    String url_commitData;
    String params;
    SensorManager sensorManager;
    TextView textView_stepCoution_toolbar;
    SharedPreferencesHelper shredPres;
    EditText editText_stepCoution_toolbar;
    MyHandler myHandler;
    int stepCount_sensor;
    int stepCount;
    int stepCount_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_counting);
        initMember();
        initWebView();
        initOut();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册传感器
        sensorManager.unregisterListener(this);
        Log.e("cz","销毁程序，取消传感器注册");
    }


    //region 初始化

    //初始化成员变量
    private void initMember() {
        textView_stepCoution_toolbar = (TextView)findViewById(R.id.textView_stepCoution_toolbar);
       editText_stepCoution_toolbar = (EditText)findViewById(R.id.editText_stepCoution_toolbar);
        url_webView = getString(R.string.host)+getString(R.string.path_stepCounting);
        url_commitData = getString(R.string.host)+getString(R.string.path_stepCounting_commitData);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        boolean isSuccess = sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),SensorManager.SENSOR_DELAY_GAME);
        if(!isSuccess){
            textView_stepCoution_toolbar.setText("传感器缺失请手动输入");
        }
        shredPres = new SharedPreferencesHelper(this,"stepCounting");
        //上次记录
        stepCount_record = Integer.parseInt(shredPres.getSharedPreference("stepCount",stepCount).toString());
        myHandler = new MyHandler();
    }


    //初始化 WebView
    private void initWebView(){
        webView = (WebView)findViewById(R.id.webView_stepCounting);
        //设置
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
        webView.loadUrl(url_webView);
    }

    //endregion

    //region 响应区

    //响应界面中的提交按钮
    public void commit(View view){

        //数字框中数字
        int count_byHand = -1;
        if(editText_stepCoution_toolbar.getText().toString().equals("")){
            count_byHand = 0;
        }else {
            count_byHand = Integer.parseInt(editText_stepCoution_toolbar.getText().toString());
        }
        //最终步数确定
        //手动提交优先
        if(count_byHand>0){
            stepCount = count_byHand;
            //如果当前计步数据小于记录数则认为当前计步数为当天有效值
        } else if(stepCount<stepCount_record){
            stepCount = stepCount;
            //常规计算步数：计步器值 - 记录值
        }else{
            stepCount = stepCount_sensor - stepCount_record;
        }
        
        //更新本地记录
        shredPres.put("stepCount",stepCount+stepCount_record);

        //向服务器提交记录
        params = "stepCount="+stepCount;
        HttpHelper httpHelper= new HttpHelper(url_commitData,params,myHandler);
        new Thread(httpHelper).start();
    }


    //传感器数据变化
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        stepCount_sensor = (int)values[0];
        if(stepCount_sensor>=stepCount_record){
            stepCount = stepCount_sensor - stepCount_record;
        }else{
            stepCount = stepCount_sensor;
        }
        this.textView_stepCoution_toolbar.setText("今日步数："+stepCount);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //endregion

    private void initOut(){
        Object value = shredPres.getSharedPreference("stepCount",1);
        Log.e("cz",value+";");
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.get("json").equals("readied")){
                JSONArray JSONArray = (JSONArray)msg.obj;
                try {
                    String response = (String)JSONArray.get(0);
                    if(response.equals("insert_ok")){
                        Toast.makeText(StepCountingActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
