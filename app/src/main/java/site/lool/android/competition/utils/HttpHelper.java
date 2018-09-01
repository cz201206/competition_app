package site.lool.android.competition.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Handler;

import site.lool.android.competition.pojo.CaseHistoryPojo;

public class HttpHelper implements Runnable{
//region 成员变量
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;

    public String  URL_String;
    public String params;
    public Handler handler;

    public HttpHelper(String URL_String, String params, Handler handler) {
        this.URL_String = URL_String;
        this.params = params;
        this.handler = handler;
    }
//endregion 成员变量

    //接收来自服务器 json 格式的数据，格式为：json 数组
    public JSONArray jsonFromHttp(String  URL_String, String params){
        Log.e("cz","来自 httphelper 请求参数："+params);
        try {
            // 1. 获取访问地址URL
            URL url = new URL(URL_String);
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            //connection.setConnectTimeout(3000);
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
                Log.e("cz","from httpheler response(msg)："+msg);
                reader.close();
            }
            // 5. 断开连接
            connection.disconnect();
            try {

                return new JSONArray(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;

    }

    //获取网络图片
    public static Bitmap bitmapFromHttp(String  URL_String, String params,Handler handler){
        Bitmap bitmap = null;
        try {
            // 1. 获取访问地址URL
            URL url = new URL(URL_String);
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            //connection.setConnectTimeout(3000);
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
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
            String msg = "";
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }else{
                //服务启发生错误
                handler.sendEmptyMessage(SERVER_ERROR);
            }
            // 5. 断开连接
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //网络连接错误
            handler.sendEmptyMessage(NETWORK_ERROR);
        }
        return bitmap;
    }


//region run
    @Override
    public void run() {
        //服务器数据
        JSONArray JSONArray = jsonFromHttp(URL_String, params);

        Message msg= Message.obtain();
        msg.obj = JSONArray;

        //判断是否取得网络数据
        if(null!=JSONArray)
        msg.what = GET_DATA_SUCCESS;
        else msg.what = NETWORK_ERROR;

        //通知 handler 处理数据
        handler.sendMessage(msg);
        Log.e("cz","httpHelper 请求数据完成");
    }
//endregion run

//region getter setter
    public void setURL_String(String URL_String) {
        this.URL_String = URL_String;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
//endregion
}
