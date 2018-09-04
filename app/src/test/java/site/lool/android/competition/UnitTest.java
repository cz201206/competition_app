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

       float a = 1f;
       System.out.println(a);
    }


    @NonNull
    private String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }


}
