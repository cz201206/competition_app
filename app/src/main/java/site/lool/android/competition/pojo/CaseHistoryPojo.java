package site.lool.android.competition.pojo;

import java.util.Date;

public class CaseHistoryPojo {
    public Date time_insert;
    public Date time_update;
    public String title;
    public String image_name;

    public CaseHistoryPojo(Date time_insert, Date time_update, String title, String image_name) {
        this.time_insert = time_insert;
        this.time_update = time_update;
        this.title = title;
        this.image_name = image_name;
    }

    @Override
    public String toString() {
        return "CaseHistoryPojo{" +
                "time_insert=" + time_insert +
                ", time_update=" + time_update +
                ", title='" + title + '\'' +
                ", image_name='" + image_name + '\'' +
                '}';
    }
}
