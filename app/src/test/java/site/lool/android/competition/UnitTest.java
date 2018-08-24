package site.lool.android.competition;

import android.support.annotation.NonNull;
import android.util.Log;

import org.junit.Test;

import java.util.Date;

import site.lool.android.competition.utils.DateHelper;

public class UnitTest {
    public UnitTest(){};

    @Test
    public void test(){

       int month = 12;
       String month_str = month>9?(month+1)+"":"0"+(month+1);
       System.out.print(month_str);
    }


    @NonNull
    private String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }


}
