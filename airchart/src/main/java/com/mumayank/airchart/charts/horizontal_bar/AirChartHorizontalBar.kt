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
            getBarChart: ((horizontalBarChart: HorizontalBarChart) -> Unit)? = null
        ) {
            // make chart
            val horizontalBarChart = HorizontalBarChart(activity)
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
                    horizontalBarChart.data = barData

                    horizontalBarChart.xAxis.valueFormatter =
                        IndexAxisValueFormatter(xLabels)
                    horizontalBarChart.xAxis?.textColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    horizontalBarChart.xAxis?.setDrawAxisLine(true)
                    horizontalBarChart.xAxis?.setDrawGridLines(false)
                    horizontalBarChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
                    horizontalBarChart.xAxis?.isGranularityEnabled = true
                    horizontalBarChart.xAxis?.granularity = 1f
                    horizontalBarChart.xAxis?.setDrawLabels(true)
                    horizontalBarChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                    horizontalBarChart.xAxis.gridColor =
                        ContextCompat.getColor(
                            activity,
                            R.color.colorLightGray
                        )
                    horizontalBarChart.xAxis.textSize = 12f
                    horizontalBarChart.xAxis.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    horizontalBarChart.xAxis.setAvoidFirstLastClipping(false)
                    horizontalBarChart.xAxis.labelCount =
                        if (xLabels.size > 20) 20 else xLabels.size

                    horizontalBarChart.axisLeft?.setDrawAxisLine(true)
                    horizontalBarChart.axisLeft?.setDrawGridLines(false)
                    horizontalBarChart.axisLeft?.setDrawLabels(false)
                    horizontalBarChart.axisLeft.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )

                    horizontalBarChart.axisRight?.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }
                    horizontalBarChart.axisRight?.textColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    horizontalBarChart.axisRight?.setDrawAxisLine(true)
                    horizontalBarChart.axisRight?.setDrawGridLines(false)
                    horizontalBarChart.axisRight?.isGranularityEnabled = true
                    horizontalBarChart.axisRight?.granularity = 1f
                    horizontalBarChart.axisRight?.setDrawLabels(false)
                    horizontalBarChart.axisRight.textSize = 12f
                    horizontalBarChart.axisRight.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )
                    horizontalBarChart.axisRight?.axisMinimum = 0f
                    horizontalBarChart.axisRight.setDrawTopYLabelEntry(true)

                    // setup chart
                    horizontalBarChart.description = null
                    horizontalBarChart.setVisibleXRangeMinimum(2f)
                    horizontalBarChart.setNoDataText("No data to display")
                    horizontalBarChart.setGridBackgroundColor(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.white
                        )
                    )
                    horizontalBarChart.setPinchZoom(false)
                    horizontalBarChart.isDoubleTapToZoomEnabled = false
                    horizontalBarChart.setDrawBorders(false)
                    horizontalBarChart.setBorderWidth(.4f)
                    horizontalBarChart.dragDecelerationFrictionCoef = 1f
                    horizontalBarChart.isAutoScaleMinMaxEnabled = false
                    horizontalBarChart.setHardwareAccelerationEnabled(false)
                    horizontalBarChart.isFocusableInTouchMode = false
                    horizontalBarChart.isFocusable = false
                    horizontalBarChart.isLongClickable = false
                    horizontalBarChart.isHighlightPerDragEnabled = false
                    horizontalBarChart.extraBottomOffset = 8f
                    horizontalBarChart.legend?.isEnabled = false
                    horizontalBarChart.renderer =
                        CustomHorizontalBarChartRenderer(
                            horizontalBarChart,
                            horizontalBarChart.animator,
                            horizontalBarChart.viewPortHandler
                        )
                    /*barChart.renderer =
                        MyBarChartRenderer(
                            barChart,
                            barChart.animator,
                            barChart.viewPortHandler,
                            BAR_RADIUS,
                            colors
                        )*/
                    horizontalBarChart.setExtraOffsets(0f, 32f, 50f, 16f)
                    horizontalBarChart.setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            barInterface.onNoValueSelected()
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) {
                                return
                            }
                            horizontalBarChart.highlightValues(null)
                            barInterface.onValueSelected(e)
                        }
                    })

                    // set zoom operations - not in scope for now
                    horizontalBarChart.onChartGestureListener = object : OnChartGestureListener {
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
                            AirChartUtil.drawValuesAccordinglyInBarChart(horizontalBarChart)
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
                    horizontalBarChart.axisLeft.axisMaximum = max
                    horizontalBarChart.axisLeft.axisMinimum = min
                    if (min < 0) {
                        val limitLine = LimitLine(0f, "")
                        limitLine.lineColor = Color.BLACK
                        limitLine.lineWidth = 0.5f
                        horizontalBarChart.axisLeft.addLimitLine(limitLine)
                    } else {
                        if (horizontalBarChart.data.dataSetCount > 1) {
                            horizontalBarChart.xAxis.axisMinimum = barData.xMin
                            horizontalBarChart.xAxis.axisMaximum = barData.xMax + 1f
                        } else {
                            horizontalBarChart.xAxis.axisMinimum = barData.xMin - 0.5f
                            horizontalBarChart.xAxis.axisMaximum = barData.xMax + 0.5f
                        }
                    }

                    if (horizontalBarChart.data.dataSetCount > 1) {
                        val barSpace = 0f
                        val groupSpace = 0.3f
                        val totalBarWidth = 1f - groupSpace
                        val barWidth = totalBarWidth / horizontalBarChart.data.dataSetCount
                        horizontalBarChart.barData.barWidth = barWidth
                        horizontalBarChart.groupBars(0f, groupSpace, barSpace)
                        horizontalBarChart.xAxis.setCenterAxisLabels(true)
                        horizontalBarChart.xAxis.granularity = 1f
                        horizontalBarChart.xAxis.isGranularityEnabled = true
                    } else {
                        horizontalBarChart.setFitBars(true)
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
                            horizontalBarChart,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )

                        horizontalBarChart.isDragEnabled = true
                        horizontalBarChart.setScaleEnabled(false)
                        horizontalBarChart.setDrawValueAboveBar(true)
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
                            horizontalBarChart.animateXY(AirChartUtil.ANIMATION_TIME, AirChartUtil.ANIMATION_TIME)
                        } else {
                            horizontalBarChart.invalidate()
                        }

                        val layoutParams = chartHolderViewGroup?.chart?.layoutParams as LinearLayout.LayoutParams
                        layoutParams.topMargin = AirChartUtil.getPixelsFromDps(activity, 22) * -1
                        chartHolderViewGroup.chart?.layoutParams = layoutParams
                        chartHolderViewGroup.chart?.requestLayout()

                        getBarChart?.invoke(horizontalBarChart)
                    }

                }

            })
        }

    }

}