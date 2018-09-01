package site.lool.android.competition.pojo;

import java.util.Date;

public class BloodPressurePojo {
    public int ID;
    public int value_low;
    public int value_high;
    public Date date_insert;

    public BloodPressurePojo(int value_low, int value_high) {
        this.value_low = value_low;
        this.value_high = value_high;
    }
}
