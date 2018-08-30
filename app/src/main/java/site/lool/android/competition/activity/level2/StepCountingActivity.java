package site.lool.android.competition.activity.level2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.service.CaseHistroyService;
import site.lool.android.competition.pojo.CaseHistoryPojo;
import site.lool.android.competition.utils.DateHelper;
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
    LineChart mLineChart;
    Toolbar toolbar_stepCounting;
    DatePickerDialog picker;
    String timeID;
    MenuItem item_choose_endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_counting);
        initMember();
        initWebView();
        out();
        initLineChart();
        initToolBar();
        initDatePicker();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册传感器
        sensorManager.unregisterListener(this);
        Log.e("cz","销毁程序，取消传感器注册");
    }

    //设置 - 控件 - toolbar 菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.period_step_counting,menu);
        item_choose_endTime = menu.findItem(R.id.item_choose_endTime);
        return true;
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
        mLineChart = (LineChart)findViewById(R.id.lineChart_stepCounting);
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

    //初始化图表
    private void initLineChart(){
        //数据
        float[] dataObjects = {1, 2, 3, 4, 5, 6, 2, 6, 5, 8, 7, 9, 100};
        //数据处理 - 排序
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataObjects.length; i++) {
            float data = dataObjects[i];
            entries.add(new Entry(i, data));
        }
        //数据处理 - 添加标签
        LineDataSet dataSet = new LineDataSet(entries, "Label1");
        //数据处理 - 设置每个点之间线的颜色
        dataSet.setColors(Color.BLACK, Color.GRAY, Color.RED, Color.GREEN);
        //数据处理 - 数据显示格式
        dataSet.setValueFormatter(new IValueFormatter() {   // 将值转换为想要显示的形式，比如，某点值为1，变为“1￥”,MP提供了三个默认的转换器，
            // LargeValueFormatter:将大数字变为带单位数字；PercentFormatter：将值转换为百分数；StackedValueFormatter，对于BarChart，是否只显示最大值图还是都显示
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value + "步";
            }
        });

        LineData lineData = new LineData(dataSet);
        /*List<ILineDataSet> sets = new ArrayList<>();  // 多条线
        sets.add(dataSet);
        sets.add(dataSet1);
        sets.add(dataSet2);
        LineData lineData = new LineData(sets);
        */
        mLineChart.setData(lineData);
    }
    private void initToolBar() {
        toolbar_stepCounting = (Toolbar) findViewById(R.id.toolbar_stepCounting);
        toolbar_stepCounting.setTitle("计步");//标题
        setSupportActionBar(toolbar_stepCounting);//启用点击响应
        toolbar_stepCounting.setOnMenuItemClickListener(onMenuItemClickListener());//设置 toolbar 条目监听器
    }
    private void initDatePicker(){
        //初始化 DatePicker
        Map<String, Integer> date_year_month_day = DateHelper.date_year_month_day();
        int year= date_year_month_day.get("year");
        int month= date_year_month_day.get("month");
        int day = date_year_month_day.get("day");
        DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //更新界面
                StepCountingActivity.this.item_choose_endTime.setTitle(DateHelper.date_show(year,month,day));
                //获取数据 发送 startTime period(day,week,month,year) 两个参数，均为字符串类型
                Toast.makeText(StepCountingActivity.this,DateHelper.date_start(year,month,day),Toast.LENGTH_SHORT).show();
                timeID = DateHelper.date_start(year,month,day);
            }
        };
        picker = new DatePickerDialog(StepCountingActivity.this, DatePickerListener, year, month, day);
    }
    //endregion

    //region 响应区
    //toolbar 菜单项
        private Toolbar.OnMenuItemClickListener onMenuItemClickListener(){
            Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    String msg = ""+menuItem.getItemId();
                    switch (menuItem.getItemId()) {
                        case R.id.item_by_year:
                            /*
                            params = "timeID="+timeID+"&period=year";
                            HttpHelper =  new HttpHelper(URL_String,params,MyHandler);
                            new Thread(HttpHelper).start();//子线程执行数据填充任务
                            */
                            Toast.makeText(StepCountingActivity.this,"年选择",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_by_month:
                            Toast.makeText(StepCountingActivity.this,"月选择",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_by_week:
                            Toast.makeText(StepCountingActivity.this,"日选择",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_choose_endTime:
                            picker.show();
                            break;
                    }
                    return true;
                }
            };
            return onMenuItemClick;
        }
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

    private void out(){
        Object value = shredPres.getSharedPreference("stepCount",1);
        Log.e("cz",value+";");
    }

    class MyHandler extends Handler{
        //数据格式 [{result:"insert_ok",data:[]}]
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.get("json").equals("readied")){
                JSONArray JSONArray = (JSONArray)msg.obj;
                try {
                    JSONObject JSONObject = (JSONObject)JSONArray.get(0);
                    if(JSONObject.get("result").equals("insert_ok")){
                        Toast.makeText(StepCountingActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
