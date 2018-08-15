package site.lool.android.competition;

import android.support.annotation.NonNull;

import org.junit.Test;

public class UnitTest {
    public UnitTest(){};

    @Test
    public void test(){

        String str = "/storage/emulated/0/DCIM/Camera/IMG_20180810_022848.jpg";

        String name = getNameOfPath(str);

        System.out.println(name);

    }

    @NonNull
    private String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }
}
