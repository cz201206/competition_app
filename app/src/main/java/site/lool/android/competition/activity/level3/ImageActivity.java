package site.lool.android.competition.activity.level3;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import site.lool.android.competition.R;
import site.lool.android.competition.lib.ZoomImageView;
import site.lool.android.competition.utils.HttpHelper;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageActivity extends AppCompatActivity {


    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;

    ImageView imageView;
    String URL_String,params;
    MyHandler MyHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);

        memeberInit();
        new ImageThread().start();
    }



    //region初始化
    private void memeberInit(){

        imageView = findViewById(R.id.imageView_image);
        String host = this.getString(R.string.host);
        String path = this.getString(R.string.path_caseHistroy_photo_upload);
        URL_String = host+path+"/";
        params = "username=test&password=123456";

        MyHandler = new MyHandler();
    }

    //endregion

    //region 响应
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            /*if(isOK_image.equals("OK")){
                ImageActivity.this.imageView.setImageBitmap(ImageActivity.this.Bitmap);
            }*/
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap Bitmap = (Bitmap) msg.obj;
                    ImageActivity.this.imageView.setImageBitmap(Bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(ImageActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(ImageActivity.this,"服务器发生错误", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
    //endregion

    //region 子线程

    class ImageThread extends Thread{
        @Override
        public void run() {
            String image_name = (String)ImageActivity.this.getIntent().getExtras().get("image_name");
            Bitmap bitmap = HttpHelper.bitmapFromHttp(URL_String+image_name,params,ImageActivity.this.MyHandler);

            Message msg = Message.obtain();
            msg.obj = bitmap;
            msg.what = GET_DATA_SUCCESS;
            ImageActivity.this.MyHandler.sendMessage(msg);

        }
    }
    //endregion

}
