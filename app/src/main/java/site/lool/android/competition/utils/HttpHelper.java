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
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;

    String  URL_String;
    String params;
    Handler handler;
    public static JSONArray JSONArray;

    public HttpHelper(String URL_String, String params, Handler handler) {
        this.URL_String = URL_String;
        this.params = params;
        this.handler = handler;
    }

    public void jsonFromHttp(String  URL_String, String params){

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
                reader.close();
            }
            // 5. 断开连接
            connection.disconnect();
            try {

                JSONArray = new JSONArray(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
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

    public static List<CaseHistoryPojo> CaseHistoryPojoFromJSONArray(JSONArray JSONArray){
        List<CaseHistoryPojo> list = new ArrayList<CaseHistoryPojo>();
        for(int i=0 ;i<JSONArray.length();i++){
            JSONObject jsonObject = null;
            try {
                //单条记录对象
                jsonObject = new JSONObject(JSONArray.get(i).toString());

                //病历ID
                int ID = Integer.parseInt(jsonObject.get("ID").toString());
                //病历简介
                String title = jsonObject.get("title").toString();
                //病历图片名称
                String image_name = jsonObject.get("image_name").toString();
                //病历上传时间
                String time_insert_str = jsonObject.get("time_insert").toString();
                //病历更新时间
                String time_update_str = jsonObject.get("time_update").toString();
                long timeID = Long.parseLong(jsonObject.get("timeID").toString());

                //时间数据类型转换
                Date time_insert = DateHelper.StringToDate(time_insert_str);
                Date time_update = DateHelper.StringToDate(time_update_str);

                CaseHistoryPojo caseHistroyPojo = new CaseHistoryPojo(ID,title,image_name,time_insert,time_update,timeID);
                list.add(caseHistroyPojo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    @Override
    public void run() {
        jsonFromHttp(URL_String, params);

        Message msg= new Message();
        Bundle bundle = new Bundle();
        bundle.putString("json","readyed");
        msg.setData(bundle);
        handler.sendMessage(msg);

    }
}
