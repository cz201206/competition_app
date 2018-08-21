package site.lool.android.competition;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(AndroidJUnit4.class)
public class InstrumentTest {
    @Test
    public void dataFromHttp(){
        Log.d("cz","执行开始");
        String  URL_String = "http://10.233.20.203:8079/project/henghost/competition/model/caseHistory/data_json_index.php";

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
                Log.d("cz","执行解析 json");
                JSONArray JSONArray = new JSONArray(msg);
                JSONObject jsonObject = new JSONObject(JSONArray.get(0).toString());
                String image_name = jsonObject.get("image_name").toString();
                System.out.print("第一个对象的照片名称为："+image_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 处理结果
            System.out.println(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("cz","执行完成");

    }
}
