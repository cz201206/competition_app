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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import site.lool.android.competition.pojo.CaseHistoryPojo;
import site.lool.android.competition.utils.DateHelper;
import site.lool.android.competition.utils.HttpHelper;

@RunWith(AndroidJUnit4.class)
public class InstrumentTest {
    @Test
    public void testDataFromHttp(){
        String  URL_String = "http://10.233.20.203:8079/project/henghost/competition/model/caseHistory/data_json_index.php";
        String params = "username=test&password=123456";

        new Thread(new HttpHelper(URL_String,params,null)).start();
    }


}
