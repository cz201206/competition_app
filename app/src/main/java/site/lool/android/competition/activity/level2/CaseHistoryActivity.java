package site.lool.android.competition.activity.level2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import site.lool.android.competition.R;
import site.lool.android.competition.activity.level3.ImageActivity;
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

        //设置 ToolBar
        Toolbar toolbar_caseHistroy = (Toolbar) findViewById(R.id.toolbar_caseHistroy);
        toolbar_caseHistroy.setTitle("病历查看");//标题
        setSupportActionBar(toolbar_caseHistroy);//启用点击响应
        toolbar_caseHistroy.setOnMenuItemClickListener(onMenuItemClickListener());//设置 toolbar 条目监听器
        // onCreateOptionsMenu(Menu menu) //设置 初始化菜单 被调函数
    }

    //功能
    private void fn(){

        //申请读取存储权限
        PermissonUtils.verifyStoragePermissions(this);
        openURL();

    }
    //endregion discription

    //region 实现区

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
