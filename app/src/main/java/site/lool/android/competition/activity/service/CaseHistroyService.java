package site.lool.android.competition.activity.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import site.lool.android.competition.pojo.CaseHistoryPojo;
import site.lool.android.competition.utils.DateHelper;

public class CaseHistroyService {
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
}
