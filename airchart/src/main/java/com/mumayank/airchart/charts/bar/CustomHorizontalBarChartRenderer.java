package com.mumayank.airchart.charts.bar;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomHorizontalBarChartRenderer extends HorizontalBarChartRenderer {

    public CustomHorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.save();
        if (Float.parseFloat(valueText) >= 0) {
            c.drawText(valueText, x, y, mValuePaint);
        } else {
            int length = 0;
            for (int i = 0; i < valueText.length(); i++) {
                char character = valueText.charAt(i);
                if (character == '.') {
                    continue;
                }
                length++;
            }
            int space = 18 * length;
            c.drawText(valueText, x + space, y, mValuePaint);
        }
        c.restore();
    }
}
