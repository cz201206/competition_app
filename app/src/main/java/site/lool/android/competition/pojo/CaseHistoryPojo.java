package site.lool.android.competition.pojo;

import java.util.Date;

public class CaseHistoryPojo {
    public int ID;
    public String title;
    public String image_name;
    public Date time_insert;
    public Date time_update;
    public long timeID;

    public CaseHistoryPojo(int ID, String title, String image_name, Date time_insert, Date time_update, long timeID) {
        this.ID = ID;
        this.title = title;
        this.image_name = image_name;
        this.time_insert = time_insert;
        this.time_update = time_update;
        this.timeID = timeID;
    }
}
