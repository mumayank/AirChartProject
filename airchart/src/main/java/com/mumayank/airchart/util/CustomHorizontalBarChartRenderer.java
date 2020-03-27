package com.mumayank.airchart.util;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomHorizontalBarChartRenderer extends HorizontalBarChartRenderer {

    public CustomHorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.save();
        if (Float.parseFloat(valueText) > 0) {
            c.drawText(valueText, x, y, mValuePaint);
        } else {
            int multiple = 15 * valueText.length();
            c.drawText(valueText, x + multiple, y, mValuePaint);
        }
        c.restore();
    }
}
