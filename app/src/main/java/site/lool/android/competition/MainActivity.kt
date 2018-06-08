package site.lool.android.competition

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import site.lool.android.competition.lib.bottombar.BottomBar
import site.lool.android.competition.lib.bottombar.fragment.Fragment1
import site.lool.android.competition.lib.bottombar.fragment.Fragment2
import site.lool.android.competition.lib.bottombar.fragment.Fragment3


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //设置 bottom 组件
        val bottomBar = findViewById<BottomBar>(R.id.bottom_bar)
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#ff5d5e")
                .addItem(Fragment1::class.java,
                        "首页",
                        R.drawable.item1_before,
                        R.drawable.item1_after)
                .addItem(Fragment2::class.java,
                        "优惠活动",
                        R.drawable.item2_before,
                        R.drawable.item2_after)
                .addItem(Fragment3::class.java,
                        "我的",
                        R.drawable.item3_before,
                        R.drawable.item3_after)
                .build()
        // Example of a call to a native method
        //sample_text.text = stringFromJNI()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
