package site.lool.android.competition.activity.level2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.level3.ImageActivity;
import site.lool.android.competition.pojo.CaseHistoryPojo;
import site.lool.android.competition.utils.DateHelper;
import site.lool.android.competition.utils.HttpHelper;
import site.lool.android.competition.utils.PermissonUtils;

//region discription

//endregion discription

public class CaseHistoryActivity extends AppCompatActivity {

    private final int IMAGE = 1;//打开相册时使用

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting();

        fn();

    }


    //region 结构区
    //设置
    private void setting(){

        //设置 主界面
        setContentView(R.layout.activity_case_history);
        //初始化 ToolBar
        initToolBar();
        //初始化 ListView
        initListView();

    }



    //功能
    private void fn(){

        //申请读取存储权限
        PermissonUtils.verifyStoragePermissions(this);

        //ListVeiw 添加数据
        ListView listView_caseHistory = (ListView)findViewById(R.id.listView_caseHistory);
        //openURL();

    }
    //endregion discription



    //region 实现区
    private void data_listView_FromHttp(String URL_String){

        try {
            // 1. 获取访问地址URL
            URL url = new URL(URL_String);
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setConnectTimeout(3000);
            // 设置是否输出
            connection.setDoOutput(true);
            // 设置是否读入
            connection.setDoInput(true);
            // 设置是否使用缓存
            connection.setUseCaches(false);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            connection.setInstanceFollowRedirects(true);
            // 设置使用标准编码格式编码参数的名-值对
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 连接
            connection.connect();
            /* 4. 处理输入输出 */
            // 写入参数到请求中
            String params = "username=test&password=123456";
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
            String msg = "";
            int code = connection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                reader.close();
            }
            // 5. 断开连接
            connection.disconnect();
            try {

                JSONArray JSONArray = new JSONArray(msg);
                JSONObject jsonObject = new JSONObject(JSONArray.get(0).toString());

                String image_name = jsonObject.get("image_name").toString();
                String title = jsonObject.get("title").toString();
                String time_insert_str = jsonObject.get("time_insert").toString();
                String time_update_str = jsonObject.get("time_update").toString();
                Date time_insert = DateHelper.StringToDate(time_insert_str);
                Date time_update = DateHelper.StringToDate(time_update_str);

                CaseHistoryPojo caseHistroyPojo = new CaseHistoryPojo(time_insert,time_update,title,image_name);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //region 设置
    //设置 ToolBar
    private void initToolBar() {
        Toolbar toolbar_caseHistroy = (Toolbar) findViewById(R.id.toolbar_caseHistroy);
        toolbar_caseHistroy.setTitle("病历查看");//标题
        setSupportActionBar(toolbar_caseHistroy);//启用点击响应
        toolbar_caseHistroy.setOnMenuItemClickListener(onMenuItemClickListener());//设置 toolbar 条目监听器
    }
    //设置 ListView
    private void initListView() {

        List list = new ArrayList<>();
        //控件
        ListView listView = (ListView)findViewById(R.id.listView_caseHistory);

        //为适配器准备参数 上下文，数据，界面，map 中 key 位置，item 中各控件位置
        //数据
        // json 数据获取
        String host = this.getString(R.string.host);
        String path = this.getString(R.string.path_data_json_index_case_history);
        String  URL_String = host+path;
        String params = "username=test&password=123456";
        JSONArray JSONArray = HttpHelper.jsonFromHttp(URL_String,params);
        List<CaseHistoryPojo> list_pojos = HttpHelper.CaseHistoryPojoFromJSONArray(JSONArray);

        for(int i = 0 ;i< 5; i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",R.drawable.case_history_small);
            map.put("title",list_pojos.get(i).title);
            map.put("insertTime",list_pojos.get(i).time_insert);
            list.add(map);
        }
        //数据位置排列
        String[] key_array = new String[]{"img","title","insertTime"};
        //控件排列位置非界面，数据模型
        int[] widght_array = new int[]{R.id.item_imageView_caseHistory,R.id.item_textView_caseHistory_title,R.id.item_textView_caseHistory_time_insert};

        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item_case_history,key_array,widght_array);

        listView.setAdapter(adapter);
    }
    //endregion

    //主视图中的 WebView 打开相应链接
    private void  openURL() {

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
        this.webView = webView;

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

    //设置 - 控件 - toolbar 菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_history,menu);
        return true;
    }

    //region 监听器 - toolbar 菜单项
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener(){
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = ""+menuItem.getItemId();
                switch (menuItem.getItemId()) {
                    case R.id.action_by_year:
                        msg += "Click action_upload";
                        break;
                    case R.id.action_date:
                        msg += "Click action_date";
                        break;
                }

                Toast.makeText(CaseHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();

                return true;
            }
        };
        return onMenuItemClick;
    }

    //endregion

    //region 响应区
    public void upload(View view){

        //打开相册上传照片数据
        Uri uri_openAmblum = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent_openAmblum = new Intent(Intent.ACTION_PICK,uri_openAmblum);
        startActivityForResult(intent_openAmblum, IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }else{
            Toast.makeText(this,"from CaseHistoryActivity:没有图片",Toast.LENGTH_SHORT).show();
        }


    }
    //加载图片
    private void showImage(String imagePath){
        Intent intent_openImageActivity = new Intent(this,ImageActivity.class);
        intent_openImageActivity.putExtra("imagePath",imagePath);
        startActivity(intent_openImageActivity);

    }
    //endregion

}
