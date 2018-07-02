package site.lool.android.competition.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import site.lool.android.competition.R;
import site.lool.android.competition.lib.bottombar.BottomBar;
import site.lool.android.competition.lib.bottombar.fragment.Fragment1;
import site.lool.android.competition.lib.bottombar.fragment.Fragment2;
import site.lool.android.competition.lib.bottombar.fragment.Fragment3;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomBar bottomBar = (BottomBar)findViewById(R.id.bottom_bar);
        bottomBar
                .setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#ff5d5e")

                .addItem(Fragment1.class,"首页",R.drawable.item1_before,R.drawable.item1_after)
                .addItem(Fragment2.class,"优惠活动",R.drawable.item2_before,R.drawable.item2_after)
                .addItem(Fragment3.class,"我的",R.drawable.item3_before,R.drawable.item3_after)

                .build();

    }
}
