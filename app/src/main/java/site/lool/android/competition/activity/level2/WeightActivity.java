package site.lool.android.competition.activity.level2;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class WeightActivity extends AppCompatActivity {

    //region 成员变量
    HttpHelper httpHelper;
    String url_commitData,params_url_commitData;
    String url_datas,params_url_datas;

    int value0;
    EditText editText_weight_toolbar;
    WeightActivity.MyHandler myHandler;
    Toolbar toolbar_weight;
    DatePickerDialog picker;
    String timeID;
    String timeID_today;
    MenuItem item_choose_endTime;

    LineChart mLineChart;
    List<Integer> list_datas;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        initMember();
        initToolBar();
        initLineChart();
        initDatePicker();
    }

    //region 初始化
    private void initMember(){
        url_commitData = url_commitData = getString(R.string.host)+getString(R.string.path_weight_commitData);
        url_datas = getString(R.string.host)+getString(R.string.path_weight_datas);
        myHandler =  new WeightActivity.MyHandler();
        editText_weight_toolbar = (EditText)findViewById(R.id.editText_weight_toolbar);
        httpHelper = new HttpHelper(null,null,myHandler);
        list_datas = new ArrayList<Integer>();
        mLineChart = (LineChart)findViewById(R.id.lineChart_weight);
        initTimeID();
    }
    private void initToolBar() {
        toolbar_weight = (Toolbar) findViewById(R.id.toolbar_weight);
        toolbar_weight.setTitle("血糖");//标题
        setSupportActionBar(toolbar_weight);//启用点击响应
        toolbar_weight.setOnMenuItemClickListener(onMenuItemClickListener());//设置 toolbar 条目监听器
    }
    private void initLineChart(){
        httpHelper.setURL_String(url_datas);
        httpHelper.setParams(params_url_datas = "timeID=&period=");
        new Thread(httpHelper).start();
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
                WeightActivity.this.item_choose_endTime.setTitle(DateHelper.date_show(year,month,day));
                //获取数据 发送 startTime period(day,week,month,year) 两个参数，均为字符串类型
                Toast.makeText(WeightActivity.this,DateHelper.timeID(year,month,day),Toast.LENGTH_SHORT).show();
                timeID = DateHelper.timeID(year,month,day);
            }
        };
        picker = new DatePickerDialog(WeightActivity.this, DatePickerListener, year, month, day);
    }
    private void initTimeID(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH);   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        timeID_today = DateHelper.timeID(year,month,day);
        timeID = timeID_today;
    }
    //endregion

    //region 功能区
    private void reDrawLineChart(){
        //数据处理 - 排序
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list_datas.size(); i++) {
            int data = list_datas.get(i);
            entries.add(new Entry(i, data));
        }
        //数据处理 - 添加标签
        LineDataSet dataSet = new LineDataSet(entries, "体重");
        //数据处理 - 设置每个点之间线的颜色
        dataSet.setColors(Color.BLACK, Color.GRAY, Color.RED, Color.GREEN);
        //数据处理 - 数据显示格式
        dataSet.setValueFormatter(new IValueFormatter() {   // 将值转换为想要显示的形式，比如，某点值为1，变为“1￥”,MP提供了三个默认的转换器，
            // LargeValueFormatter:将大数字变为带单位数字；PercentFormatter：将值转换为百分数；StackedValueFormatter，对于BarChart，是否只显示最大值图还是都显示
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value + "斤";
            }
        });

        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        //重绘图表
        mLineChart.invalidate();
    }
    //endregion

    //region 响应区
    //提交数据
    public void commit(View view){
        value0 = Integer.parseInt(editText_weight_toolbar.getText().toString()) ;

        //向服务器提交记录
        params_url_commitData = "timeID="+timeID_today+"&value0="+value0;

        httpHelper.setURL_String(url_commitData);
        httpHelper.setParams(params_url_commitData);

        Log.e("cz","来自 booldglucose commit: params_url_commitData = "+params_url_commitData);
        new Thread(httpHelper).start();
    }
    //设置 - 控件 - toolbar 菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.period_step_counting,menu);
        item_choose_endTime = menu.findItem(R.id.item_choose_endTime);
        return true;
    }
    //菜单
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
                        new Thread(httpHelper).start();
                        Toast.makeText(WeightActivity.this,"年选择",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_by_month:
                        params_url_datas = "timeID="+timeID+"&period=month";
                        new Thread(httpHelper).start();
                        Toast.makeText(WeightActivity.this,"月选择",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_by_week:
                        params_url_datas = "timeID="+timeID+"&period=week";
                        new Thread(httpHelper).start();
                        Toast.makeText(WeightActivity.this,"周选择",Toast.LENGTH_SHORT).show();
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
    class MyHandler extends Handler {
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
                            Toast.makeText(WeightActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                            break;

                        //插入数据 失败 的提示
                        case "insert_no" :
                            Toast.makeText(WeightActivity.this,"请铢勿重复提交数据",Toast.LENGTH_SHORT).show();
                            break;

                        //更新 成功的提示
                        case "update_ok" :
                            Toast.makeText(WeightActivity.this,"更新数据成功",Toast.LENGTH_SHORT).show();
                            break;

                        //查询到数据后的处理
                        case "select_ok":
                            list_datas.clear();
                            datas = (JSONArray)JSONObject.get("datas");
                            for (int i = 0; i<datas.length();i++){
                                JSONObject jobj = (JSONObject) datas.get(i);
                                int value0 = (int)jobj.get("value0");
                                list_datas.add(value0);
                            }
                            if(!(datas.length()<1))
                                reDrawLineChart();
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

    //region 输出

    //endregion
}
