package site.lool.android.competition.lib;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import site.lool.android.competition.R;

public class ChartMarkerView extends MarkerView {

    TextView tv_indicator;

    String item;
    String unit;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartMarkerView(Context context, int layoutResource, String item, String unit) {
        super(context, layoutResource);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        this.item = null == item ? "" : item;
        this.unit = null == unit ? "" : unit;
    }

    private boolean isReverse = true;

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        isReverse = !(highlight.getX() > 8);
        String content = "时间：";
        content += e.getX() + "d";

        content += "\n" + item + e.getY() + unit;
        tv_indicator.setText(content);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        MPPointF mpPointF = super.getOffset();
        if (!isReverse ) {
            mpPointF.x = -tv_indicator.getWidth();
        } else {
            mpPointF.x = 0;
        }
        mpPointF.y = -tv_indicator.getHeight();
        return mpPointF;
    }
}