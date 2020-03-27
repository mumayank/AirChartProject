package com.mumayank.airchart.charts.horizontal_bar

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
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.data_classes.AirChartValueItem
import com.mumayank.airchart.util.AirChartUtil
import com.mumayank.airchart.util.CustomBarChartRenderer
import com.mumayank.airchart.util.CustomHorizontalBarChartRenderer
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.collections.ArrayList

/*
todo
Does not support zooming
Does not support color array different than data array proper cyclic filling
Does not support single value
 */

class AirChartHorizontalBar {

    companion object {

        const val STANDARD_BAR_WIDTH = 0.5f
        const val BAR_RADIUS = 10f

        fun show(
            activity: Activity,
            layoutInflater: LayoutInflater?,
            chartHolderViewGroup: ViewGroup?,
            barInterface: AirChartBar.BarInterface,
            getBarChart: ((barChart: BarChart) -> Unit)? = null
        ) {
            // make chart
            val barChart = HorizontalBarChart(activity)
            val airChartValueItems = arrayListOf<AirChartValueItem>()

            // do in BG
            AirCoroutine.execute(activity, object: AirCoroutine.Callback {

                var valuesCount = 0
                val colors = arrayListOf<Int>()
                val xLabels = arrayListOf<String>()

                override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {

                    // reverse data
                    for (i in barInterface.getYLeftItems().size-1 downTo 0) {
                        val airChartValueItem = barInterface.getYLeftItems()[i]
                        val values = arrayListOf<Float>()
                        for (j in airChartValueItem.values.size-1 downTo 0) {
                            values.add(airChartValueItem.values[j])
                        }
                        airChartValueItems.add(AirChartValueItem(airChartValueItem.legendLabel, values))
                    }

                    // reverse x labels
                    for (i in barInterface.getXLabels().size-1 downTo 0) {
                        xLabels.add(barInterface.getXLabels()[i])
                    }

                    // label count
                    valuesCount = AirChartUtil.getValuesCount(valuesCount, airChartValueItems)

                    // transform colors - reversed
                    if (barInterface.getColors() == null) {
                        colors.add(activity.resources.getColor(android.R.color.holo_green_light))
                    } else {
                        for (i in (barInterface.getColors() as ArrayList<String>).size-1 downTo 0) {
                            colors.add(Color.parseColor((barInterface.getColors() as ArrayList<String>)[i]))
                        }
                    }

                    // setup data
                    val barDataSetList = arrayListOf<BarDataSet>()
                    for ((index, value) in airChartValueItems.withIndex()) {
                        val barEntries = arrayListOf<BarEntry>()
                        for ((index2, value2) in value.values.withIndex()) {
                            barEntries.add(BarEntry(index2.toFloat(), value2))
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
                                val mFormat =
                                    DecimalFormat(barInterface.getDecimalFormatPattern())
                                return mFormat.format(value.toDouble())
                            }
                        }
                        if (barInterface.getColors().isNullOrEmpty().not()) {
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
                                barData.barWidth = 0.2f
                            }
                            2 -> {
                                barData.barWidth = 0.3f
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
                    barChart.xAxis.labelCount =
                        if (xLabels.size > 20) 20 else xLabels.size

                    barChart.axisLeft?.setDrawAxisLine(true)
                    barChart.axisLeft?.setDrawGridLines(false)
                    barChart.axisLeft?.setDrawLabels(false)
                    barChart.axisLeft.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )

                    barChart.axisRight?.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }
                    barChart.axisRight?.textColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    barChart.axisRight?.setDrawAxisLine(true)
                    barChart.axisRight?.setDrawGridLines(false)
                    barChart.axisRight?.isGranularityEnabled = true
                    barChart.axisRight?.granularity = 1f
                    barChart.axisRight?.setDrawLabels(false)
                    barChart.axisRight.textSize = 12f
                    barChart.axisRight.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    barChart.axisRight?.axisMinimum = 0f
                    barChart.axisRight.setDrawTopYLabelEntry(true)

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
                    barChart.renderer =
                        CustomHorizontalBarChartRenderer(
                            barChart,
                            barChart.animator,
                            barChart.viewPortHandler
                        )
                    /*barChart.renderer =
                        MyBarChartRenderer(
                            barChart,
                            barChart.animator,
                            barChart.viewPortHandler,
                            BAR_RADIUS,
                            colors
                        )*/
                    barChart.setExtraOffsets(0f, 32f, 50f, 16f)
                    barChart.setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            barInterface.onNoValueSelected()
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) {
                                return
                            }
                            barChart.highlightValues(null)
                            barInterface.onValueSelected(e)
                        }
                    })

                    // set zoom operations - not in scope for now
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

                    // setup spacing
                    val min = barData.yMin
                    val max = barData.yMax
                    barChart.axisLeft.axisMaximum = max
                    barChart.axisLeft.axisMinimum = min
                    if (min < 0) {
                        val limitLine = LimitLine(0f, "")
                        limitLine.lineColor = Color.BLACK
                        limitLine.lineWidth = 0.5f
                        barChart.axisLeft.addLimitLine(limitLine)
                    } else {
                        if (barChart.data.dataSetCount > 1) {
                            barChart.xAxis.axisMinimum = barData.xMin
                            barChart.xAxis.axisMaximum = barData.xMax + 1f
                        } else {
                            barChart.xAxis.axisMinimum = barData.xMin - 0.5f
                            barChart.xAxis.axisMaximum = barData.xMax + 0.5f
                        }
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
                    } else {
                        barChart.setFitBars(true)
                    }

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
                            barInterface.getTitle() ?: ""
                        chartHolderViewGroup?.subTitle?.text =
                            barInterface.getSubTitle()
                        chartHolderViewGroup?.title?.visibility =
                            if (barInterface.getTitle() != null) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.subTitle?.visibility =
                            if (barInterface.getSubTitle().isEmpty().not()) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.xLabel?.text =
                            barInterface.getYLeftLabel()
                        val yLabelLeft =
                            chartHolderViewGroup?.yLabelLeft as TextView?
                        yLabelLeft?.text = barInterface.getXLabel()
                        chartHolderViewGroup?.yLabelRightLayout?.visibility =
                            View.GONE
                        chartHolderViewGroup?.arrowTop?.visibility = View.GONE
                        chartHolderViewGroup?.arrowBottom?.visibility = View.VISIBLE
                        val layoutParamsTemp = chartHolderViewGroup?.yLabelLeft?.layoutParams as LinearLayout.LayoutParams
                        layoutParamsTemp.topMargin = AirChartUtil.getPixelsFromDps(activity, 8)
                        chartHolderViewGroup.yLabelLeft?.layoutParams = layoutParamsTemp
                        chartHolderViewGroup.yLabelLeft?.requestLayout()

                        // check for no data
                        chartHolderViewGroup?.noData?.visibility =
                            if (valuesCount == 0) View.VISIBLE else View.GONE

                        // add chart
                        chartHolderViewGroup?.chart?.removeAllViews()
                        chartHolderViewGroup?.chart?.addView(
                            barChart,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )

                        barChart.isDragEnabled = true
                        barChart.setScaleEnabled(false)
                        barChart.setDrawValueAboveBar(true)
                        //barChart.isScaleXEnabled = false
                        /*if (valuesCount > 15) {
                            barChart.zoom(valuesCount / 15f, 1f, 0f, 0f)
                        } else {
                            barChart.zoom(1f, 1f, 0f, 0f)
                        }
                        AirChartUtil.drawValuesAccordinglyInBarChart(barChart)*/

                        // setup legend
                        val colorsTemp = arrayListOf<Int>()
                        for (i in colors.size-1 downTo 0) {
                            colorsTemp.add(colors[i])
                        }
                        AirChartUtil.setupLegend(activity, colorsTemp, chartHolderViewGroup?.rvHolderLegends, airChartValueItems, true)

                        // setup additional data
                        AirChartUtil.setupAdditionalData(activity, chartHolderViewGroup?.rvHolderAdditionalData, barInterface.getAdditionalDatas())

                        // setup custom view
                        AirChartUtil.setupCustomView(activity, layoutInflater, barInterface.getCustomViewLayoutResId(), chartHolderViewGroup?.customView)

                        chartHolderViewGroup?.progressView?.visibility = View.GONE

                        if (barInterface.getIsAnimationRequired()) {
                            barChart.animateXY(AirChartUtil.ANIMATION_TIME, AirChartUtil.ANIMATION_TIME)
                        } else {
                            barChart.invalidate()
                        }

                        val layoutParams = chartHolderViewGroup?.chart?.layoutParams as LinearLayout.LayoutParams
                        layoutParams.topMargin = AirChartUtil.getPixelsFromDps(activity, 22) * -1
                        chartHolderViewGroup.chart?.layoutParams = layoutParams
                        chartHolderViewGroup.chart?.requestLayout()

                        getBarChart?.invoke(barChart)
                    }

                }

            })
        }

    }

}