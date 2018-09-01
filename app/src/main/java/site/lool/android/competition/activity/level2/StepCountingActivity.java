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
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import site.lool.android.competition.R;
import site.lool.android.competition.utils.DateHelper;
import site.lool.android.competition.utils.HttpHelper;
import site.lool.android.competition.utils.SharedPreferencesHelper;

public class StepCountingActivity extends AppCompatActivity implements SensorEventListener {
    //region 成员变量
    WebView webView;
    String url_webView;

    HttpHelper httpHelper;
    String url_commitData,params_url_commitData;
    String url_datas,params_url_datas;

    SensorManager sensorManager;
    TextView textView_stepCoution_toolbar;
    SharedPreferencesHelper shredPres;
    EditText editText_stepCoution_toolbar;
    MyHandler myHandler;
    int stepCount_sensor;
    int stepCount;
    int stepCount_record;
    Toolbar toolbar_stepCounting;
    DatePickerDialog picker;
    String timeID;
    String timeID_today;
    MenuItem item_choose_endTime;

    LineChart mLineChart;
    List<Integer> list_datas;
    //endregion 成员变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_counting);
        initMember();
//        initWebView();
        initOut();
        initToolBar();
        initDatePicker();
        initLineChart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册传感器
        sensorManager.unregisterListener(this);
        Log.e("cz","销毁程序，取消传感器注册");
    }



    //region 功能区

    private void reDrawLineChart(int size){
        if(size<=0){
            LineData lineData = new LineData();
            mLineChart.setData(lineData);
        }else {
            //数据处理 - 排序
            List<Entry> entries = new ArrayList<>();
            for (int i = 0; i < list_datas.size(); i++) {
                int data = list_datas.get(i);
                entries.add(new Entry(i, data));
            }
            //数据处理 - 添加标签
            LineDataSet dataSet = new LineDataSet(entries, "步数");
            //数据处理 - 设置每个点之间线的颜色
            dataSet.setColors(Color.BLACK, Color.GRAY, Color.RED, Color.GREEN);
            //数据处理 - 数据显示格式
            dataSet.setValueFormatter(new IValueFormatter() {   // 将值转换为想要显示的形式，比如，某点值为1，变为“1￥”,MP提供了三个默认的转换器，
                // LargeValueFormatter:将大数字变为带单位数字；PercentFormatter：将值转换为百分数；StackedValueFormatter，对于BarChart，是否只显示最大值图还是都显示
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return (int) value + "步";
                }
            });

            LineData lineData = new LineData(dataSet);
            mLineChart.setData(lineData);
        }
        //重绘图表
        mLineChart.invalidate();
    }

    //endregion 初始化
    //region 初始化

    //初始化成员变量
    private void initMember() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH);   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        timeID_today = DateHelper.timeID(year,month,day);
        timeID = timeID_today;

        textView_stepCoution_toolbar = (TextView)findViewById(R.id.textView_stepCoution_toolbar);
        editText_stepCoution_toolbar = (EditText)findViewById(R.id.editText_stepCoution_toolbar);
        url_webView = getString(R.string.host)+getString(R.string.path_stepCounting);
        url_commitData = getString(R.string.host)+getString(R.string.path_stepCounting_commitData);
        url_datas = getString(R.string.host)+getString(R.string.path_stepCounting_datas);
        params_url_datas = "timeID=&period=";
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
        //数据
        list_datas = new ArrayList<Integer>();

        httpHelper = new HttpHelper(url_datas,params_url_datas,myHandler);
        new Thread(httpHelper).start();
    }


    //初始化 WebView
    private void initWebView(){
        /*
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
        */
    }

    //初始化图表
    private void initLineChart(){

        mLineChart.setData(new LineData());
        Log.e("cz","初始化 chart 完成");

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
                Toast.makeText(StepCountingActivity.this,DateHelper.timeID(year,month,day),Toast.LENGTH_SHORT).show();
                timeID = DateHelper.timeID(year,month,day);
            }
        };
        picker = new DatePickerDialog(StepCountingActivity.this, DatePickerListener, year, month, day);
    }
    private void initOut(){
        Object value = shredPres.getSharedPreference("stepCount",1);
        Log.e("cz",value+";");
    }
    //endregion 初始化

    //region 响应区
    //设置 - 控件 - toolbar 菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.period_step_counting,menu);
        item_choose_endTime = menu.findItem(R.id.item_choose_endTime);
        return true;
    }
    //toolbar 菜单项
        private Toolbar.OnMenuItemClickListener onMenuItemClickListener(){
            Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    httpHelper.setURL_String(url_datas);
                    httpHelper.setParams(params_url_datas);

                    int itemId = menuItem.getItemId();
                    switch (itemId) {
                        case R.id.item_by_year:

                            params_url_datas = "timeID="+timeID+"&period=year";
                            httpHelper.setParams(params_url_datas);
                            new Thread(httpHelper).start();
                            Toast.makeText(StepCountingActivity.this,"年选择",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_by_month:
                            params_url_datas = "timeID="+timeID+"&period=month";
                            httpHelper.setParams(params_url_datas);
                            new Thread(httpHelper).start();
                            Toast.makeText(StepCountingActivity.this,"月选择",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.item_by_week:
                            params_url_datas = "timeID="+timeID+"&period=week";
                            httpHelper.setParams(params_url_datas);
                            new Thread(httpHelper).start();
                            Toast.makeText(StepCountingActivity.this,"周选择",Toast.LENGTH_SHORT).show();
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
        params_url_commitData = "timeID="+timeID_today+"&stepCount="+stepCount;

        httpHelper.setURL_String(url_commitData);
        httpHelper.setParams(params_url_commitData);
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
    class MyHandler extends Handler{
        //数据格式 [{result:"insert_ok",data:[]}]
        @Override
        public void handleMessage(Message msg) {
            JSONArray JSONArray = null;
            JSONObject JSONObject = null;
            String result = null;
            JSONArray datas = null;

            //处理来自服务器的数据
            if(HttpHelper.GET_DATA_SUCCESS == msg.what){
               JSONArray = (JSONArray)msg.obj;
                try {
                    //获取结果对象中的业务类型识别码[{result:"识别码",datas:[]}]
                    JSONObject = (JSONObject)JSONArray.get(0);
                    //处理结果 - 插入数据成功
                    result = (String)JSONObject.get("result");
                    switch (result){

                        //插入数据成功后的提示
                        case "insert_ok" :
                            Toast.makeText(StepCountingActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                            StepCountingActivity.this.finish();
                            break;

                        //插入数据 失败 的提示
                        case "insert_no" :
                            Toast.makeText(StepCountingActivity.this,"请铢勿重复提交数据",Toast.LENGTH_SHORT).show();
                            break;

                        //更新 成功的提示
                        case "update_ok" :
                            Toast.makeText(StepCountingActivity.this,"更新数据成功",Toast.LENGTH_SHORT).show();
                            StepCountingActivity.this.finish();
                            break;

                        //查询到数据后的处理
                        case "select_ok":
                            list_datas.clear();
                            datas = (JSONArray)JSONObject.get("datas");
                            for (int i = 0; i<datas.length();i++){
                                JSONObject jobj = (JSONObject) datas.get(i);
                                int stepCount = (int)jobj.get("stepCount");
                                list_datas.add(stepCount);
                            }
                            reDrawLineChart(datas.length());
                            break;

                    }
                    Log.e("cz","数据结果："+result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion




}
