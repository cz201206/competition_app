package site.lool.android.competition.pojo;

import java.util.Date;

public class BloodPressurePojo {
    public int ID;
    public float value_low;
    public float value_high;
    public Date date_insert;

    public BloodPressurePojo(float value_low, float value_high) {
        this.value_low = value_low;
        this.value_high = value_high;
    }
}
