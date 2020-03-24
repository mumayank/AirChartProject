package com.mumayank.airchart.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class MyBarChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BarDataProvider mChart;

    /**
     * the rect object that is used for drawing the bars
     */
    protected RectF mBarRect = new RectF();

    protected BarBuffer[] mBarBuffers;

    protected Paint mShadowPaint;
    protected Paint mBarBorderPaint;

    private float mRoundedBarRadius = 0f;

    private List<Integer> mBorderColorList;

    public MyBarChartRenderer(BarDataProvider chart, ChartAnimator animator,
                              ViewPortHandler viewPortHandler, float mRoundedBarRadius, List<Integer> mBorderColorList) {
        super(animator, viewPortHandler);
        this.mChart = chart;

        this.mRoundedBarRadius = mRoundedBarRadius;
        this.mBorderColorList = mBorderColorList;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        // set alpha after color
        mHighlightPaint.setAlpha(120);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);

        mBarBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);

        initBuffers();
    }

    @Override
    public void initBuffers() {

        BarData barData = mChart.getBarData();

        if (barData != null) {
            mBarBuffers = new BarBuffer[barData.getDataSetCount()];

            for (int i = 0; i < mBarBuffers.length; i++) {
                IBarDataSet set = barData.getDataSetByIndex(i);
                mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                        barData.getDataSetCount(), set.isStacked());
            }
        }
    }

    @Override
    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {

            IBarDataSet set = barData.getDataSetByIndex(i);

            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }
    }

    private RectF mBarShadowRectBuffer = new RectF();

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();

            final float barWidth = barData.getBarWidth();
            final float barWidthHalf = barWidth / 2.0f;
            float x;

            for (int i = 0, count = Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)),
                    dataSet.getEntryCount());
                 i < count; i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                mBarShadowRectBuffer.left = x - barWidthHalf;
                mBarShadowRectBuffer.right = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left))
                    break;

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop();
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom();

                c.drawRect(mBarShadowRectBuffer, mShadowPaint);
            }
        }

        if (mBarBuffers != null) {
            // initialize the buffer
            BarBuffer buffer = mBarBuffers[index];
            buffer.setPhases(phaseX, phaseY);
            buffer.setDataSet(index);
            buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
            buffer.setBarWidth(mChart.getBarData().getBarWidth());

            buffer.feed(dataSet);

            trans.pointValuesToPixel(buffer.buffer);

            final boolean isSingleColor = dataSet.getColors().size() == 1;

            if (isSingleColor) {
                mRenderPaint.setColor(dataSet.getColor());
            }

            for (int j = 0; j < buffer.size(); j += 4) {

                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                    break;

                if (!isSingleColor) {
                    // Set the color for the currently drawn value. If the index
                    // is out of bounds, reuse colors.
                    mRenderPaint.setColor(dataSet.getColor(j / 4));
                }

                if (dataSet.getGradientColor() != null) {
                    GradientColor gradientColor = dataSet.getGradientColor();
                    mRenderPaint.setShader(
                            new LinearGradient(
                                    buffer.buffer[j],
                                    buffer.buffer[j + 3],
                                    buffer.buffer[j],
                                    buffer.buffer[j + 1],
                                    gradientColor.getStartColor(),
                                    gradientColor.getEndColor(),
                                    android.graphics.Shader.TileMode.MIRROR));
                }

                if (dataSet.getGradientColors() != null) {
                    mRenderPaint.setShader(
                            new LinearGradient(
                                    buffer.buffer[j],
                                    buffer.buffer[j + 3],
                                    buffer.buffer[j],
                                    buffer.buffer[j + 1],
                                    dataSet.getGradientColor(j / 4).getStartColor(),
                                    dataSet.getGradientColor(j / 4).getEndColor(),
                                    android.graphics.Shader.TileMode.MIRROR));
                }


                Path path = roundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3]), mRoundedBarRadius, mRoundedBarRadius, true, true, false, false);
                c.drawPath(path, mRenderPaint);

            /*c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);*/

                if (drawBorder) {

                    Path path2 = roundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                            buffer.buffer[j + 3]), mRoundedBarRadius, mRoundedBarRadius, true, true, false, false);
                    c.drawPath(path2, mBarBorderPaint);

                /*c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mBarBorderPaint);*/
                }

            /*c.drawRoundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3]), 10, 10, mRenderPaint);

            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);*/
            }
        }
    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {

        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public void drawValues(Canvas c) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<IBarDataSet> dataSets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset = 0f;
            float negOffset = 0f;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                IBarDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // calculate the correct offset depending on the draw position of
                // the value
                float valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
                posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
                negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight;
                    negOffset = -negOffset - valueTextHeight;
                }

                if(mBarBuffers != null) {
                    // get the buffer
                    BarBuffer buffer = mBarBuffers[i];

                    final float phaseY = mAnimator.getPhaseY();

                    ValueFormatter formatter = dataSet.getValueFormatter();

                    MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                    // if only single values are drawn (sum)
                    if (!dataSet.isStacked()) {

                        for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {

                            float x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f;

                            if (!mViewPortHandler.isInBoundsRight(x))
                                break;

                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                                    || !mViewPortHandler.isInBoundsLeft(x))
                                continue;

                            BarEntry entry = dataSet.getEntryForIndex(j / 4);
                            float val = entry.getY();

                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(c, formatter.getBarLabel(entry), x, val >= 0 ?
                                                (buffer.buffer[j + 1] + posOffset) :
                                                (buffer.buffer[j + 3] + negOffset),
                                        dataSet.getValueTextColor(j / 4));
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                Drawable icon = entry.getIcon();

                                float px = x;
                                float py = val >= 0 ?
                                        (buffer.buffer[j + 1] + posOffset) :
                                        (buffer.buffer[j + 3] + negOffset);

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        c,
                                        icon,
                                        (int) px,
                                        (int) py,
                                        icon.getIntrinsicWidth(),
                                        icon.getIntrinsicHeight());
                            }
                        }

                        // if we have stacks
                    } else {

                        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                        int bufferIndex = 0;
                        int index = 0;

                        while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {

                            BarEntry entry = dataSet.getEntryForIndex(index);

                            float[] vals = entry.getYVals();
                            float x = (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f;

                            int color = dataSet.getValueTextColor(index);

                            // we still draw stacked bars, but there is one
                            // non-stacked
                            // in between
                            if (vals == null) {

                                if (!mViewPortHandler.isInBoundsRight(x))
                                    break;

                                if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                        || !mViewPortHandler.isInBoundsLeft(x))
                                    continue;

                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(c, formatter.getBarLabel(entry), x, buffer.buffer[bufferIndex + 1] +
                                                    (entry.getY() >= 0 ? posOffset : negOffset),
                                            color);
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                    Drawable icon = entry.getIcon();

                                    float px = x;
                                    float py = buffer.buffer[bufferIndex + 1] +
                                            (entry.getY() >= 0 ? posOffset : negOffset);

                                    px += iconsOffset.x;
                                    py += iconsOffset.y;

                                    Utils.drawImage(
                                            c,
                                            icon,
                                            (int) px,
                                            (int) py,
                                            icon.getIntrinsicWidth(),
                                            icon.getIntrinsicHeight());
                                }

                                // draw stack values
                            } else {

                                float[] transformed = new float[vals.length * 2];

                                float posY = 0f;
                                float negY = -entry.getNegativeSum();

                                for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {

                                    float value = vals[idx];
                                    float y;

                                    if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                                        // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                        y = value;
                                    } else if (value >= 0.0f) {
                                        posY += value;
                                        y = posY;
                                    } else {
                                        y = negY;
                                        negY -= value;
                                    }

                                    transformed[k + 1] = y * phaseY;
                                }

                                trans.pointValuesToPixel(transformed);

                                for (int k = 0; k < transformed.length; k += 2) {

                                    final float val = vals[k / 2];
                                    final boolean drawBelow =
                                            (val == 0.0f && negY == 0.0f && posY > 0.0f) ||
                                                    val < 0.0f;
                                    float y = transformed[k + 1]
                                            + (drawBelow ? negOffset : posOffset);

                                    if (!mViewPortHandler.isInBoundsRight(x))
                                        break;

                                    if (!mViewPortHandler.isInBoundsY(y)
                                            || !mViewPortHandler.isInBoundsLeft(x))
                                        continue;

                                    if (dataSet.isDrawValuesEnabled()) {
                                        drawValue(c, formatter.getBarStackedLabel(val, entry), x, y, color);
                                    }

                                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                        Drawable icon = entry.getIcon();

                                        Utils.drawImage(
                                                c,
                                                icon,
                                                (int) (x + iconsOffset.x),
                                                (int) (y + iconsOffset.y),
                                                icon.getIntrinsicWidth(),
                                                icon.getIntrinsicHeight());
                                    }
                                }
                            }

                            bufferIndex = vals == null ? bufferIndex + 4 : bufferIndex + 4 * vals.length;
                            index++;
                        }
                    }

                    MPPointF.recycleInstance(iconsOffset);
                }
            }
        }
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.save();
        c.rotate(-90f, x+10, y-30);
        c.drawText(valueText, x+10, y-30, mValuePaint);
        c.restore();
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = (high.getStackIndex() >= 0 && e.isStacked()) ? true : false;

            final float y1;
            final float y2;

            if (isStack) {

                if (mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[high.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }

            } else {
                y1 = e.getY();
                y2 = 0.f;
            }

            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2f, trans);

            setHighlightDrawPos(high, mBarRect);

            /*c.drawRect(mBarRect, mHighlightPaint);*/

            c.drawRoundRect(new RectF(mBarRect), mRoundedBarRadius, mRoundedBarRadius, mHighlightPaint);
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     *
     * @param high
     */
    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerX(), bar.top);
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    private Path roundRect(RectF rect, float rx, float ry, boolean tl, boolean tr, boolean br, boolean bl) {
        float top = rect.top;
        float left = rect.left;
        float right = rect.right;
        float bottom = rect.bottom;
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
}
