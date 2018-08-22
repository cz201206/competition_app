package site.lool.android.competition.utils;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

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
import java.util.List;
import android.os.Handler;

import site.lool.android.competition.pojo.CaseHistoryPojo;

public class HttpHelper implements Runnable{
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
    public static List<CaseHistoryPojo> CaseHistoryPojoFromJSONArray(JSONArray JSONArray){
        List<CaseHistoryPojo> list = new ArrayList<CaseHistoryPojo>();
        for(int i=0 ;i<JSONArray.length();i++){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(JSONArray.get(i).toString());
                String image_name = jsonObject.get("image_name").toString();
                String title = jsonObject.get("title").toString();
                String time_insert_str = jsonObject.get("time_insert").toString();
                String time_update_str = jsonObject.get("time_update").toString();
                Date time_insert = DateHelper.StringToDate(time_insert_str);
                Date time_update = DateHelper.StringToDate(time_update_str);

                CaseHistoryPojo caseHistroyPojo = new CaseHistoryPojo(time_insert,time_update,title,image_name);
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
