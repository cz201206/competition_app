package site.lool.android.competition.activity.level3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import site.lool.android.competition.R;
import site.lool.android.competition.utils.PermissonUtils;

public class ImageActivity extends AppCompatActivity {
    public EditText editText;
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        editText = (EditText)this.findViewById(R.id.editText_caseTitle);

        PermissonUtils.verifyStoragePermissions(this);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");

        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ((ImageView)findViewById(R.id.image_activity)).setImageBitmap(bm);
    }

    //region 响应区

    public void upload_ok(View view){

        //上传数据
        StringBuilder sb = new StringBuilder("");
        sb.append(this.getString(R.string.host));
        sb.append(this.getString(R.string.receiver_caseHistroy_photo));
        String urlStr = sb.toString();
        //调试信息
        Log.d("cz","上传位置："+urlStr);
        Log.d("cz","图片位置："+imagePath);

        String picTitle = editText.getText().toString();
        FileInputStream fi = null;
        try{
            fi = new FileInputStream(imagePath);
        }catch (Exception e){
            e.printStackTrace();
        }


        new Uploader(picTitle,fi,urlStr,new ImageActivityHandler()).start();
    }

    public void upload_cancel(View view){
        this.finish();
    }

    //endregion

    class ImageActivityHandler extends Handler {

        public ImageActivityHandler() {
        }

        public ImageActivityHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("cz", "handleMessage......");
            super.handleMessage(msg);
            // 此处可以更新UI
            Bundle b = msg.getData();
            String color = b.getString("color");
            ImageActivity.this.editText.setText("hadler处理");

        }
    }

    class Uploader extends Thread {

        private final String BOUNDARYSTR = "--------aifudao7816510d1hq";
        private final String END = "\r\n";
        private final String LAST = "--";

        private String data;//表单数据
        private FileInputStream fis;//文件输入流
        private Handler handler;
        private String urlStr;

        public Uploader(String picTitle, FileInputStream fis, String urlStr, Handler handler) {
            this.urlStr = urlStr;
            this.data = picTitle;
            this.handler = handler;
            this.fis=fis;
        }

        @Override
        public void run() {
            try {
                URL httpUrl=new URL(urlStr);
                HttpURLConnection connection= (HttpURLConnection) httpUrl.openConnection();
                connection.setRequestMethod("POST");//必须为post
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);//固定格式
                DataOutputStream dos=new DataOutputStream(connection.getOutputStream());
                StringBuffer sb=new StringBuffer();
                /**
                 * 写入文本数据
                 */

                sb.append(LAST+BOUNDARYSTR+END);
                sb.append("Content-Disposition: form-data; name=\"title\""+END+END);
                sb.append(data+END);//内容
                /**
                 * 循环写入文件
                 */
                sb.append(LAST+BOUNDARYSTR+END);
                sb.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"file\";");
                sb.append("filename=\""+"map_image.png"+"\""+END+END);
                dos.write(sb.toString().getBytes("utf-8"));
                if (fis != null) {
                    byte[] b=new byte[1024];
                    int len;
                    while ((len=fis.read(b))!=-1){
                        dos.write(b,0,len);
                    }
                    dos.write(END.getBytes());
                }
                dos.write((LAST+BOUNDARYSTR+LAST+END).getBytes());
                dos.flush();
                sb=new StringBuffer();
                if (connection.getResponseCode()==200) {//请求成功
                    Log.d("cz","请求成功");
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    Message msg= Message.obtain();
                    handler.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("cz","run over");
        }

    }
}
