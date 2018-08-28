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

       float a = 40.1f;
       System.out.println((int)a);
    }


    @NonNull
    private String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }


}
