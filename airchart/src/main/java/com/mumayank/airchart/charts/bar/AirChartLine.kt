package com.mumayank.airchart.charts.bar

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.mumayank.airchart.R
import com.mumayank.airchart.data_classes.AdditionalValue
import com.mumayank.airchart.data_classes.Value
import com.mumayank.airchart.util.AirChartUtil
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class AirChartLine {

    interface ILine {
        fun getTitle(): String
        fun getXAxisTitle(): String
        fun getXAxisLabels(): java.util.ArrayList<String>
        fun getYLeftAxisTitle(): String
        fun getYLeftAxisValues(): java.util.ArrayList<Value>

        fun getColors(): ArrayList<String>? {
            return null
        }

        fun getSubTitle(): String? {
            return ""
        }

        fun getDecimalFormatPattern(): String? {
            return "0.#"
        }

        fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
            return null
        }

        fun getCustomViewLayoutResId(): Int? {
            return null
        }

        fun getIsAnimationRequired(): Boolean? {
            return false
        }

        fun onValueSelected(e: Entry?) {
            return
        }

        fun onNoValueSelected() {
            return
        }

        fun getYRightAxisTitle(): String? {
            return null
        }

        fun getYRightAxisValues(): java.util.ArrayList<Value>? {
            return null
        }

        fun getIsCurved(): Boolean? {
            return false
        }
    }

    companion object {

        fun show(
            activity: Activity,
            layoutInflater: LayoutInflater?,
            chartHolderViewGroup: ViewGroup?,
            iLine: ILine,
            getLineChart: ((lineChart: LineChart) -> Unit)? = null
        ) {
            // make chart
            val lineChart = LineChart(activity)
            val airChartValueItems = arrayListOf<Value>()

            // do in BG
            AirCoroutine.execute(activity, object : AirCoroutine.Callback {

                var valuesCount = 0
                val colors = arrayListOf<Int>()
                val xLabels = arrayListOf<String>()

                override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {


                    // add data
                    for (i in iLine.getYLeftAxisValues()) {
                        airChartValueItems.add(i)
                    }
                    // add data
                    if (iLine.getYRightAxisValues() != null) {
                        for (i in iLine.getYRightAxisValues()!!) {
                            airChartValueItems.add(i)
                        }
                    }
                    // add x labels
                    for (i in iLine.getXAxisLabels()) {
                        xLabels.add(i)
                    }

                    // transform colors
                    if (iLine.getColors() == null) {
                        colors.add(activity.resources.getColor(android.R.color.holo_orange_light))
                        colors.add(activity.resources.getColor(android.R.color.holo_blue_light))
                    } else {
                        for (color in iLine.getColors()!!) {
                            colors.add(Color.parseColor(color))
                        }
                    }

                    // label count
                    valuesCount = AirChartUtil.getValuesCount(valuesCount, airChartValueItems)

                    // setup data
                    val lineDataSetList = arrayListOf<LineDataSet>()
                    for ((index, value) in airChartValueItems.withIndex()) {
                        val entries = arrayListOf<Entry>()
                        for ((index2, value2) in value.values.withIndex()) {
                            entries.add(Entry(index2.toFloat(), value2.toFloat()))
                        }
                        val lineDataSet = LineDataSet(entries, value.legendLabel)
                        lineDataSet.valueTextSize = 12f
                        lineDataSet.valueTextColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        lineDataSet.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val decimalFormatPattern = iLine.getDecimalFormatPattern() ?: "0.#"
                                val mFormat = DecimalFormat(decimalFormatPattern)
                                return mFormat.format(value.toDouble())
                            }
                        }
                        if (iLine.getColors().isNullOrEmpty().not()) {
                            if (airChartValueItems.size > 1) {
                                lineDataSet.color =
                                    AirChartUtil.getItemFromArrayAtIndexCyclically(colors, index)
                            } else {
                                lineDataSet.colors = colors
                            }
                        } else {
                            lineDataSet.color =
                                ContextCompat.getColor(
                                    activity,
                                    android.R.color.holo_blue_light
                                )
                        }

                        if (iLine.getIsCurved() == true) {
                            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                        }

                        lineDataSet.setDrawCircleHole(false)
                        lineDataSet.lineWidth = 2f
                        lineDataSet.circleRadius = 2f
                        lineDataSet.setCircleColor(
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        )
                        if (airChartValueItems.size > 1) {
                            lineDataSet.setDrawValues(false)
                            if (index == 0) {
                                lineDataSet.lineWidth = 5f
                                lineDataSet.circleRadius = 4f
                                lineDataSet.setCircleColor(
                                    ContextCompat.getColor(
                                        activity,
                                        R.color.blueGrey200
                                    )
                                )
                                if (iLine.getColors() == null)
                                    lineDataSet.color =
                                        ContextCompat.getColor(
                                            activity,
                                            android.R.color.holo_orange_light
                                        )
                            }
                        }

                        lineDataSetList.add(lineDataSet)
                    }
                    val lineData = LineData(lineDataSetList.toList())
                    lineChart.data = lineData
                    lineChart.setDrawMarkers(true)

                    lineChart.xAxis.valueFormatter =
                        IndexAxisValueFormatter(xLabels)
                    lineChart.xAxis?.textColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    lineChart.xAxis?.setDrawAxisLine(true)
                    lineChart.xAxis?.setDrawGridLines(true)
                    lineChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
                    lineChart.xAxis?.isGranularityEnabled = true
                    lineChart.xAxis?.granularity = 1f
                    lineChart.xAxis?.setDrawLabels(true)
                    lineChart.xAxis.labelRotationAngle = -90f
                    lineChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                    lineChart.xAxis.gridColor =
                        ContextCompat.getColor(
                            activity,
                            R.color.colorLightGray
                        )
                    lineChart.xAxis.textSize = 12f
                    lineChart.xAxis.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    lineChart.xAxis.setAvoidFirstLastClipping(false)
                    lineChart.xAxis.labelCount = if (xLabels.size > 20) 20 else xLabels.size


                    lineChart.axisRight.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )

                    lineChart.axisLeft?.setDrawAxisLine(true)
                    lineChart.axisLeft?.setDrawLabels(true)

                    lineChart.axisRight?.setDrawLabels(iLine.getYRightAxisValues() != null)
                    lineChart.axisRight?.setDrawAxisLine(iLine.getYRightAxisValues() != null)

                    applyAxisSettings(lineChart.axisLeft, activity)
                    applyAxisSettings(lineChart.axisRight, activity)

                    lineChart.renderer =
                        CustomLineChartRenderer(
                            lineChart,
                            lineChart.animator,
                            lineChart.viewPortHandler
                        )

                    // set zoom operations
                    lineChart.onChartGestureListener = object : OnChartGestureListener {
                        override fun onChartGestureEnd(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {
                            // do nothing
                        }

                        override fun onChartFling(
                            me1: MotionEvent?,
                            me2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                        ) {
                            // do nothing
                        }

                        override fun onChartSingleTapped(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartGestureStart(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {
                            // do nothing
                        }

                        override fun onChartScale(
                            me: MotionEvent?,
                            scaleX: Float,
                            scaleY: Float
                        ) {
                            AirChartUtil.drawValuesAccordinglyInLineChart(lineChart)
                        }

                        override fun onChartLongPressed(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartDoubleTapped(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                            // do nothing
                        }

                    }
                    lineChart.setExtraOffsets(0f, 16f, 0f, 16f)

                    // setup chart
                    lineChart.description = null
                    lineChart.setVisibleXRangeMinimum(2f)
                    lineChart.setNoDataText("No data to display")
                    lineChart.setGridBackgroundColor(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.white
                        )
                    )
                    lineChart.setPinchZoom(false)
                    lineChart.isDoubleTapToZoomEnabled = false
                    lineChart.setDrawBorders(false)
                    lineChart.setBorderWidth(.4f)
                    lineChart.dragDecelerationFrictionCoef = 1f
                    lineChart.isAutoScaleMinMaxEnabled = false
                    lineChart.setHardwareAccelerationEnabled(false)
                    lineChart.isFocusableInTouchMode = false
                    lineChart.isFocusable = false
                    lineChart.isLongClickable = false
                    lineChart.isHighlightPerDragEnabled = false
                    lineChart.extraBottomOffset = 8f
                    lineChart.legend?.isEnabled = false
                    lineChart.setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            iLine.onNoValueSelected()
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) {
                                return
                            }
                            lineChart.highlightValues(null)
                            iLine.onValueSelected(e)
                        }
                    })

                    // setup spacing
                    val times = 0.5f
                    val min = if (lineData.yMin < 0) lineData.yMin else 0f
                    val max = if (lineData.yMax > 0) lineData.yMax else 0f
                    lineChart.axisLeft.axisMaximum = max + (max - min).absoluteValue * times
                    lineChart.axisRight.axisMaximum = max + (max - min).absoluteValue * times

                    if (min < 0) {
                        val limitLine = LimitLine(0f, "")
                        limitLine.lineColor = Color.BLACK
                        limitLine.lineWidth = 0.5f
                        lineChart.axisLeft.addLimitLine(limitLine)
                        lineChart.axisLeft.axisMinimum = min + (min - max) * times
                        lineChart.axisRight.axisMinimum = min + (min - max) * times
                    } else {
                        lineChart.axisLeft.axisMinimum = 0f
                        lineChart.axisRight.axisMinimum = 0f
                    }

                    lineChart.xAxis?.axisMinimum = lineData.xMin - 0.5f
                    lineChart.xAxis?.axisMaximum = lineData.xMax + 0.5f

                    lineChart.setMaxVisibleValueCount(20)

                    return true
                }

                override fun isTaskTypeCalculation(): Boolean {
                    return true
                }

                override fun onFailure(viewModel: ViewModel) {
                    // do nothing
                }

                override fun onSuccess(viewModel: ViewModel) {

                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                        // setup views
                        chartHolderViewGroup?.title?.text =
                            iLine.getTitle()
                        chartHolderViewGroup?.subTitle?.text =
                            iLine.getSubTitle()
                        chartHolderViewGroup?.title?.visibility =
                            if (iLine.getTitle().isEmpty()) View.GONE else View.VISIBLE
                        chartHolderViewGroup?.subTitle?.visibility =
                            if (iLine.getSubTitle()?.isEmpty() == false) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.yLabelRightLayout?.visibility =
                            if (iLine.getYRightAxisTitle() == null) View.GONE else View.VISIBLE

                        // add chart
                        chartHolderViewGroup?.xLabel?.text = iLine.getXAxisTitle()
                        val yLabelLeft =
                            chartHolderViewGroup?.yLabelLeft as TextView?
                        yLabelLeft?.text = iLine.getYLeftAxisTitle()

                        if (iLine.getYRightAxisTitle() != null) {
                            val yLabelRight = chartHolderViewGroup?.yLabelRight as TextView?
                            yLabelRight?.text = iLine.getYRightAxisTitle()
                        }
                        lineChart.setScaleEnabled(true)
                        lineChart.isScaleYEnabled = false
                        if (valuesCount > 15) {
                            lineChart.zoom(valuesCount / 15f, 1f, 0f, 0f)
                        } else {
                            lineChart.zoom(1f, 1f, 0f, 0f)
                        }
                        AirChartUtil.drawValuesAccordinglyInLineChart(lineChart)

                        chartHolderViewGroup?.chart?.removeAllViews()
                        chartHolderViewGroup?.chart?.addView(
                            lineChart,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )

                        // check for no data
                        chartHolderViewGroup?.noData?.visibility =
                            if (valuesCount == 0) View.VISIBLE else View.GONE

                        lineChart.isDragEnabled = true

                        // setup legend
                        val colorsTemp = arrayListOf<Int>()
                        for (i in colors.size - 1 downTo 0) {
                            colorsTemp.add(colors[i])
                        }
                        AirChartUtil.setupLegend(
                            activity,
                            colors,
                            chartHolderViewGroup?.rvHolderLegends,
                            airChartValueItems,
                            false
                        )

                        // setup additional data
                        AirChartUtil.setupAdditionalData(
                            activity,
                            chartHolderViewGroup?.rvHolderAdditionalData,
                            iLine.getAdditionalValues()
                        )

                        // setup custom view
                        AirChartUtil.setupCustomView(
                            activity,
                            layoutInflater,
                            iLine.getCustomViewLayoutResId(),
                            chartHolderViewGroup?.customView
                        )

                        chartHolderViewGroup?.progressView?.visibility = View.GONE

                        if (iLine.getIsAnimationRequired() == true) {
                            lineChart.animateXY(
                                AirChartUtil.ANIMATION_TIME,
                                AirChartUtil.ANIMATION_TIME
                            )
                        } else {
                            lineChart.invalidate()
                        }
                        getLineChart?.invoke(lineChart)
                    }
                }
            })
        }

        private fun applyAxisSettings(axis: YAxis?, activity: Activity) {

            axis?.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
            axis?.textColor =
                ContextCompat.getColor(
                    activity,
                    android.R.color.black
                )
            axis?.setDrawGridLines(false)
            axis?.isGranularityEnabled = true
            axis?.granularity = 1f
            axis?.textSize = 12f
            axis?.axisLineColor =
                ContextCompat.getColor(
                    activity,
                    android.R.color.black
                )
            axis?.axisMinimum = 0f
            axis?.setDrawTopYLabelEntry(true)
        }
    }
}