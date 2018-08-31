package site.lool.android.competition;

import android.support.annotation.NonNull;
import android.util.Log;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import site.lool.android.competition.utils.DateHelper;

public class UnitTest {
    public UnitTest(){};

    @Test
    public void test(){

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH);   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        String timeID_today = DateHelper.timeID(year,month,day);
        System.out.println(timeID_today);
    }


    @NonNull
    private String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }


}
