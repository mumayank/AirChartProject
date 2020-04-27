package com.mumayank.airchart.charts.bar

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
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
import com.mumayank.airchart.util.AirChartUtil.Companion.STANDARD_BAR_WIDTH
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class AirChartBar {

    interface IBar {
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

        fun getIsHorizontal(): Boolean {
            return false
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

    }

    companion object {

        fun show(
            activity: Activity,
            layoutInflater: LayoutInflater?,
            chartHolderViewGroup: ViewGroup?,
            iBar: IBar,
            getBarChart: ((barChart: BarChart) -> Unit)? = null
        ) {
            // make chart
            val barChart =
                if (iBar.getIsHorizontal() == false) BarChart(activity) else HorizontalBarChart(
                    activity
                )
            val airChartValueItems = arrayListOf<Value>()

            // do in BG
            AirCoroutine.execute(activity, object : AirCoroutine.Callback {

                var valuesCount = 0
                val colors = arrayListOf<Int>()
                val xLabels = arrayListOf<String>()

                override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {

                    if (iBar.getIsHorizontal() == false) {
                        // add data
                        for (i in iBar.getYLeftAxisValues()) {
                            airChartValueItems.add(i)
                        }

                        // add x labels
                        for (i in iBar.getXAxisLabels()) {
                            xLabels.add(i)
                        }

                        // transform colors
                        if (iBar.getColors() == null) {
                            colors.add(activity.resources.getColor(android.R.color.holo_green_light))
                        } else {
                            for (color in (iBar.getColors() as ArrayList<String>)) {
                                colors.add(Color.parseColor(color))
                            }
                        }
                    } else {
                        // reverse data
                        for (i in iBar.getYLeftAxisValues().size - 1 downTo 0) {
                            val airChartValueItem = iBar.getYLeftAxisValues()[i]
                            val values = arrayListOf<Double>()
                            for (j in airChartValueItem.values.size - 1 downTo 0) {
                                values.add(airChartValueItem.values[j])
                            }
                            airChartValueItems.add(
                                Value(
                                    airChartValueItem.legendLabel,
                                    values
                                )
                            )
                        }

                        // reverse x labels
                        for (i in iBar.getXAxisLabels().size - 1 downTo 0) {
                            xLabels.add(iBar.getXAxisLabels()[i])
                        }

                        // transform colors - reversed
                        if (iBar.getColors() == null) {
                            colors.add(activity.resources.getColor(android.R.color.holo_green_light))
                        } else {
                            for (i in (iBar.getColors() as ArrayList<String>).size - 1 downTo 0) {
                                colors.add(Color.parseColor((iBar.getColors() as ArrayList<String>)[i]))
                            }
                        }
                    }

                    // label count
                    valuesCount = AirChartUtil.getValuesCount(valuesCount, airChartValueItems)

                    // setup data
                    val barDataSetList = arrayListOf<BarDataSet>()
                    for ((index, value) in airChartValueItems.withIndex()) {
                        val barEntries = arrayListOf<BarEntry>()
                        for ((index2, value2) in value.values.withIndex()) {
                            barEntries.add(BarEntry(index2.toFloat(), value2.toFloat()))
                        }
                        val barDataSet = BarDataSet(barEntries, value.legendLabel)
                        barDataSet.valueTextSize = 12f
                        barDataSet.valueTextColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barDataSet.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val decimalFormatPattern = iBar.getDecimalFormatPattern() ?: "0.#"
                                val mFormat = DecimalFormat(decimalFormatPattern)
                                return mFormat.format(value.toDouble())
                            }
                        }
                        if (iBar.getColors().isNullOrEmpty().not()) {
                            if (airChartValueItems.size > 1) {
                                barDataSet.color =
                                    AirChartUtil.getItemFromArrayAtIndexCyclically(colors, index)
                            } else {
                                barDataSet.colors = colors
                            }
                        } else {
                            barDataSet.color =
                                ContextCompat.getColor(
                                    activity,
                                    android.R.color.black
                                )
                        }
                        barDataSet.setDrawValues(true)
                        val barData = BarData(barDataSet)
                        when (valuesCount) {
                            1 -> {
                                barData.barWidth = 0.4f
                            }
                            2 -> {
                                barData.barWidth = 0.4f
                            }
                            3 -> {
                                barData.barWidth = 0.4f
                            }
                            else -> {
                                barData.barWidth = STANDARD_BAR_WIDTH
                            }
                        }
                        barDataSetList.add(barDataSet)
                    }
                    val barData = BarData(barDataSetList.toList())
                    barChart.data = barData

                    if (iBar.getIsHorizontal() == false) {
                        barChart.xAxis.valueFormatter =
                            IndexAxisValueFormatter(xLabels)
                        barChart.xAxis?.textColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.xAxis?.setDrawAxisLine(true)
                        barChart.xAxis?.setDrawGridLines(false)
                        barChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
                        barChart.xAxis?.isGranularityEnabled = true
                        barChart.xAxis?.granularity = 1f
                        barChart.xAxis?.setDrawLabels(true)
                        barChart.xAxis.labelRotationAngle = -90f
                        barChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                        barChart.xAxis.gridColor =
                            ContextCompat.getColor(
                                activity,
                                R.color.colorLightGray
                            )
                        barChart.xAxis.textSize = 12f
                        barChart.xAxis.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.xAxis.setAvoidFirstLastClipping(false)
                        barChart.xAxis.labelCount =
                            if (xLabels.size > 20) 20 else xLabels.size

                        barChart.axisRight?.setDrawAxisLine(true)
                        barChart.axisRight?.setDrawGridLines(false)
                        barChart.axisRight?.setDrawLabels(false)
                        barChart.axisRight.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )

                        barChart.axisLeft?.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        barChart.axisLeft?.textColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.axisLeft?.setDrawAxisLine(true)
                        barChart.axisLeft?.setDrawGridLines(false)
                        barChart.axisLeft?.isGranularityEnabled = true
                        barChart.axisLeft?.granularity = 1f
                        barChart.axisLeft?.setDrawLabels(true)
                        barChart.axisLeft.textSize = 12f
                        barChart.axisLeft.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.axisLeft?.axisMinimum = 0f
                        barChart.axisLeft.setDrawTopYLabelEntry(true)

                        barChart.renderer =
                            CustomBarChartRenderer(
                                barChart,
                                barChart.animator,
                                barChart.viewPortHandler
                            )

                        // set zoom operations
                        barChart.onChartGestureListener = object : OnChartGestureListener {
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
                                AirChartUtil.drawValuesAccordinglyInBarChart(barChart)
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
                        barChart.setExtraOffsets(0f, 16f, 0f, 16f)
                    } else {
                        barChart.setExtraOffsets(0f, 32f, 32f, 16f)
                        barChart.xAxis.valueFormatter =
                            IndexAxisValueFormatter(xLabels)
                        barChart.xAxis?.textColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.xAxis?.setDrawAxisLine(true)
                        barChart.xAxis?.setDrawGridLines(false)
                        barChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
                        barChart.xAxis?.isGranularityEnabled = true
                        barChart.xAxis?.granularity = 1f
                        barChart.xAxis?.setDrawLabels(true)
                        barChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                        barChart.xAxis.gridColor =
                            ContextCompat.getColor(
                                activity,
                                R.color.colorLightGray
                            )
                        barChart.xAxis.textSize = 12f
                        barChart.xAxis.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.xAxis.setAvoidFirstLastClipping(false)

                        barChart.axisRight?.setDrawAxisLine(true)
                        barChart.axisRight?.setDrawGridLines(false)
                        barChart.axisRight?.setDrawLabels(false)
                        barChart.axisRight.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )

                        barChart.axisLeft?.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        barChart.axisLeft?.textColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.axisLeft?.setDrawAxisLine(true)
                        barChart.axisLeft?.setDrawGridLines(false)
                        barChart.axisLeft?.isGranularityEnabled = true
                        barChart.axisLeft?.granularity = 1f
                        barChart.axisLeft?.setDrawLabels(true)
                        barChart.axisLeft?.labelCount = 4
                        barChart.axisLeft.textSize = 12f
                        barChart.axisLeft.axisLineColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        barChart.axisLeft.setDrawTopYLabelEntry(true)

                        barChart.renderer =
                            CustomHorizontalBarChartRenderer(
                                barChart,
                                barChart.animator,
                                barChart.viewPortHandler
                            )
                    }

                    // setup chart
                    barChart.description = null
                    barChart.setVisibleXRangeMinimum(2f)
                    barChart.setNoDataText("No data to display")
                    barChart.setGridBackgroundColor(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.white
                        )
                    )
                    barChart.setPinchZoom(false)
                    barChart.isDoubleTapToZoomEnabled = false
                    barChart.setDrawBorders(false)
                    barChart.setBorderWidth(.4f)
                    barChart.dragDecelerationFrictionCoef = 1f
                    barChart.isAutoScaleMinMaxEnabled = false
                    barChart.setHardwareAccelerationEnabled(false)
                    barChart.isFocusableInTouchMode = false
                    barChart.isFocusable = false
                    barChart.isLongClickable = false
                    barChart.isHighlightPerDragEnabled = false
                    barChart.extraBottomOffset = 8f
                    barChart.legend?.isEnabled = false
                    barChart.setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            iBar.onNoValueSelected()
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) {
                                return
                            }
                            barChart.highlightValues(null)
                            iBar.onValueSelected(e)
                        }
                    })

                    // setup spacing
                    val times = if (iBar.getIsHorizontal() == false) 0.5f else 0.05f
                    val min = if (barData.yMin < 0) barData.yMin else 0f
                    val max = if (barData.yMax > 0) barData.yMax else 0f
                    barChart.axisLeft.axisMaximum = max + (max - min).absoluteValue * times
                    if (min < 0) {
                        val limitLine = LimitLine(0f, "")
                        limitLine.lineColor = Color.BLACK
                        limitLine.lineWidth = 0.5f
                        barChart.axisLeft.addLimitLine(limitLine)
                        barChart.axisLeft.axisMinimum = min + (min - max) * times
                    } else {
                        barChart.axisLeft.axisMinimum = 0f
                    }

                    if (barChart.data.dataSetCount > 1) {
                        val barSpace = 0f
                        val groupSpace = 0.3f
                        val totalBarWidth = 1f - groupSpace
                        val barWidth = totalBarWidth / barChart.data.dataSetCount
                        barChart.barData.barWidth = barWidth
                        barChart.groupBars(0f, groupSpace, barSpace)
                        barChart.xAxis.setCenterAxisLabels(true)
                        barChart.xAxis.granularity = 1f
                        barChart.xAxis.isGranularityEnabled = true

                        barChart.xAxis?.axisMinimum = barData.xMin
                        barChart.xAxis?.axisMaximum = barData.xMax + 1f
                    } else {
                        barChart.xAxis?.axisMinimum = barData.xMin - 0.5f
                        barChart.xAxis?.axisMaximum = barData.xMax + 0.5f
                        barChart.setFitBars(true)
                    }
                    barChart.setMaxVisibleValueCount(20)

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
                            iBar.getTitle() ?: ""
                        chartHolderViewGroup?.subTitle?.text =
                            iBar.getSubTitle()
                        chartHolderViewGroup?.title?.visibility =
                            if (iBar.getTitle().isNullOrEmpty().not()) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.subTitle?.visibility =
                            if (iBar.getSubTitle()?.isEmpty() == false) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.yLabelRightLayout?.visibility =
                            View.GONE

                        // add chart
                        if (iBar.getIsHorizontal() == false) {
                            chartHolderViewGroup?.xLabel?.text = iBar.getXAxisTitle()
                            val yLabelLeft =
                                chartHolderViewGroup?.yLabelLeft as TextView?
                            yLabelLeft?.text = iBar.getYLeftAxisTitle()

                            barChart.setScaleEnabled(true)
                            barChart.isScaleYEnabled = false
                            if (valuesCount > 15) {
                                barChart.zoom(valuesCount / 15f, 1f, 0f, 0f)
                            } else {
                                barChart.zoom(1f, 1f, 0f, 0f)
                            }
                            AirChartUtil.drawValuesAccordinglyInBarChart(barChart)
                        } else {
                            chartHolderViewGroup?.topTitleTextLayout?.visibility = View.VISIBLE
                            chartHolderViewGroup?.bottomTitleTextLayout?.visibility = View.GONE
                            chartHolderViewGroup?.xLabelTop?.text = iBar.getYLeftAxisTitle()
                            val yLabelLeft =
                                chartHolderViewGroup?.yLabelLeft as TextView?
                            yLabelLeft?.text = iBar.getXAxisTitle()
                            chartHolderViewGroup?.arrowTop?.visibility = View.GONE
                            chartHolderViewGroup?.arrowBottom?.visibility = View.VISIBLE
                            val layoutParamsTemp =
                                chartHolderViewGroup?.yLabelLeft?.layoutParams as LinearLayout.LayoutParams
                            layoutParamsTemp.topMargin = AirChartUtil.getPixelsFromDps(activity, 8)
                            chartHolderViewGroup.yLabelLeft?.layoutParams = layoutParamsTemp
                            chartHolderViewGroup.yLabelLeft?.requestLayout()
                            barChart.setScaleEnabled(false)
                            barChart.setDrawValueAboveBar(true)
                            barChart.xAxis.labelCount = xLabels.size
                        }
                        chartHolderViewGroup?.chart?.removeAllViews()
                        chartHolderViewGroup?.chart?.addView(
                            barChart,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )

                        // check for no data
                        chartHolderViewGroup?.noData?.visibility =
                            if (valuesCount == 0) View.VISIBLE else View.GONE

                        barChart.isDragEnabled = true

                        // setup legend
                        val colorsTemp = arrayListOf<Int>()
                        for (i in colors.size - 1 downTo 0) {
                            colorsTemp.add(colors[i])
                        }
                        AirChartUtil.setupLegend(
                            activity,
                            if (iBar.getIsHorizontal() == false) colors else colorsTemp,
                            chartHolderViewGroup?.rvHolderLegends,
                            airChartValueItems,
                            iBar.getIsHorizontal() ?: false
                        )

                        // setup additional data
                        AirChartUtil.setupAdditionalData(
                            activity,
                            chartHolderViewGroup?.rvHolderAdditionalData,
                            iBar.getAdditionalValues()
                        )

                        // setup custom view
                        AirChartUtil.setupCustomView(
                            activity,
                            layoutInflater,
                            iBar.getCustomViewLayoutResId(),
                            chartHolderViewGroup?.customView
                        )

                        chartHolderViewGroup?.progressView?.visibility = View.GONE

                        if (iBar.getIsAnimationRequired() == true) {
                            barChart.animateXY(
                                AirChartUtil.ANIMATION_TIME,
                                AirChartUtil.ANIMATION_TIME
                            )
                        } else {
                            barChart.invalidate()
                        }

                        if (iBar.getIsHorizontal() == true) {
                            val layoutParams =
                                chartHolderViewGroup?.chart?.layoutParams as LinearLayout.LayoutParams
                            layoutParams.topMargin =
                                AirChartUtil.getPixelsFromDps(activity, 22) * -1
                            chartHolderViewGroup.chart?.layoutParams = layoutParams
                            chartHolderViewGroup.chart?.requestLayout()
                        }

                        getBarChart?.invoke(barChart)
                    }

                }

            })
        }

    }

}