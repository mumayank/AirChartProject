package com.mumayank.airchart

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.mikephil.charting.charts.BarChart
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
import mumayank.com.airrecyclerview.AirRv
import java.text.DecimalFormat
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.android.synthetic.main.air_chart_view_rv_item.view.*

class AirChart {

    interface BarInterface {
        fun getContext(): Context
        fun getChartHolderViewGroup(): ViewGroup?
        fun getTitle(): String
        fun getIsTitleVisible(): Boolean
        fun getColors(): ArrayList<String>
        fun getDecimalFormatPattern(): String
        fun getXLabel(): String
        fun getXLabels(): ArrayList<String>
        fun getYLeftLabel(): String
        fun getYLeftItems(): java.util.ArrayList<YLeftItem>
        fun onValueSelected(e: Entry?)
        fun onNoValueSelected()
    }

    companion object {

        const val TIME = 500
        private const val STANDARD_BAR_WIDTH = 0.5f
        const val BAR_RADIUS = 10f
        private const val MAX_X_RANGE = 6f

        fun bar(barInterface: BarInterface, getBarChart:((barChart: BarChart)->Unit)? = null) {

            // inflate the layout
            val inflater = barInterface.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            barInterface.getChartHolderViewGroup()?.removeAllViews()
            barInterface.getChartHolderViewGroup()?.addView(inflater.inflate(R.layout.air_chart_view, LinearLayout(barInterface.getContext())), ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

            // make chart
            val barChart = BarChart(barInterface.getContext())

            // label count
            var valuesCount = 0
            for (yItem in barInterface.getYLeftItems()) {
                for (value in yItem.values) {
                    valuesCount++
                }
            }

            // transform colors
            val colors = arrayListOf<Int>()
            for (color in barInterface.getColors()) {
                colors.add(Color.parseColor(color))
            }

            // setup data
            val barDataSetList = arrayListOf<BarDataSet>()
            for ((index, value) in barInterface.getYLeftItems().withIndex()) {
                val barEntries = arrayListOf<BarEntry>()
                for ((index2, value2) in value.values.withIndex()) {
                    barEntries.add(BarEntry(index2.toFloat(), value2))
                }
                val barDataSet = BarDataSet(barEntries, value.legendLabel)
                barDataSet.valueTextSize = 12f
                barDataSet.valueTextColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
                barDataSet.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val mFormat = DecimalFormat(barInterface.getDecimalFormatPattern())
                        return mFormat.format(value.toDouble())
                    }
                }
                if (barInterface.getColors().isNotEmpty()) {
                    if (barInterface.getYLeftItems().size > 1) {
                        barDataSet.color = getItemFromArrayAtIndexCyclicly(colors, index)
                    } else {
                        barDataSet.colors = colors
                    }
                } else {
                    barDataSet.color = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
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

            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(barInterface.getXLabels())

            barChart.xAxis?.textColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.xAxis?.setDrawAxisLine(true)
            barChart.xAxis?.setDrawGridLines(false)
            barChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            barChart.xAxis?.isGranularityEnabled = true
            barChart.xAxis?.granularity = 1f
            barChart.xAxis?.labelCount = barInterface.getXLabels().size
            barChart.xAxis?.setDrawLabels(true)
            barChart.xAxis.labelRotationAngle = -90f
            barChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
            barChart.xAxis.gridColor = ContextCompat.getColor(barInterface.getContext(), R.color.colorLightGray)
            barChart.xAxis.textSize = 12f
            barChart.xAxis.textColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.xAxis.axisLineColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.xAxis.setAvoidFirstLastClipping(false)
            barChart.xAxis.labelCount = if (barInterface.getXLabels().size > 20) 20 else barInterface.getXLabels().size

            barChart.axisRight?.textColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.axisRight?.setDrawAxisLine(true)
            barChart.axisRight?.setDrawGridLines(false)
            barChart.axisRight?.isGranularityEnabled = true
            barChart.axisRight?.granularity = 1f
            barChart.axisRight?.setDrawLabels(false)
            barChart.axisRight?.axisMinimum = barData.xMin - 0.5f
            barChart.axisRight?.axisMaximum = barData.xMax + 0.5f
            barChart.axisRight.axisLineColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)

            barChart.axisLeft?.axisMinimum = 0f
            barChart.axisLeft?.textColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.axisLeft.textSize = 12f
            barChart.axisLeft?.setDrawAxisLine(true)
            barChart.axisLeft?.setDrawGridLines(false)
            barChart.axisLeft?.isGranularityEnabled = true
            barChart.axisLeft?.granularity = 1f
            barChart.axisLeft?.setDrawLabels(true)
            barChart.axisLeft.setDrawTopYLabelEntry(true)
            barChart.axisLeft.axisLineColor = ContextCompat.getColor(barInterface.getContext(), android.R.color.black)
            barChart.axisLeft?.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }

            // setup chart
            barChart.description = null
            barChart.setVisibleXRangeMinimum(2f)
            barChart.setNoDataText("No data to display")
            barChart.setGridBackgroundColor(ContextCompat.getColor(barInterface.getContext(), android.R.color.white))
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
            barChart.renderer = MyBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler, BAR_RADIUS, colors)
            barChart.setExtraOffsets(0f, 32f, 0f, 16f)
            barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
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

            // setup views
            barInterface.getChartHolderViewGroup()?.title?.text = barInterface.getTitle()
            barInterface.getChartHolderViewGroup()?.title?.visibility = if (barInterface.getIsTitleVisible()) View.VISIBLE else View.GONE
            barInterface.getChartHolderViewGroup()?.xLabel?.text = barInterface.getXLabel()
            barInterface.getChartHolderViewGroup()?.yLabelLeft?.text = barInterface.getYLeftLabel()
            barInterface.getChartHolderViewGroup()?.yLabelRightLayout?.visibility = View.GONE

            // setup rv for legends
            barInterface.getChartHolderViewGroup()?.rvHolderParent?.visibility = View.GONE

            // check for no data
            barInterface.getChartHolderViewGroup()?.noData?.visibility = if (valuesCount == 0) View.VISIBLE else View.GONE

            // add chart
            barInterface.getChartHolderViewGroup()?.chart?.removeAllViews()
            barInterface.getChartHolderViewGroup()?.chart?.addView(barChart, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

            barChart.isDragEnabled = true
            barChart.setScaleEnabled(true)
            barChart.isScaleYEnabled = false
            if (valuesCount > 15) {
                barChart.zoom(valuesCount/15f, 1f, 0f, 0f)
            } else {
                barChart.zoom(1f, 1f, 0f, 0f)
            }
            drawValuesAccordingly(barChart)

            // set zoom operations
            barChart.onChartGestureListener = object: OnChartGestureListener {
                override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                    // do nothing
                }

                override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {
                    // do nothing
                }

                override fun onChartSingleTapped(me: MotionEvent?) {
                    // do nothing
                }

                override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                    // do nothing
                }

                override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
                    drawValuesAccordingly(barChart)
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

            if (barChart.data.dataSetCount > 1) {
                val nos = barInterface.getXLabels().size.toFloat()
                val barSpace = 0f;
                val groupSpace = 0.3f
                val totalBarWidth = 1f - groupSpace
                val barWidth = totalBarWidth / barChart.data.dataSetCount
                barChart.barData.barWidth = barWidth;
                barChart.groupBars(0f, groupSpace, barSpace);
                barChart.xAxis.setCenterAxisLabels(true);
                barChart.xAxis.granularity = 1f
                barChart.xAxis.isGranularityEnabled = true
                barChart.xAxis?.axisMinimum = barData.xMin
                barChart.xAxis?.axisMaximum = barData.xMax + 1f
            } else {
                barChart.setFitBars(true)
                barChart.xAxis?.axisMinimum = barData.xMin - 0.5f
                barChart.xAxis?.axisMaximum = barData.xMax + 0.5f
            }
            barChart.setMaxVisibleValueCount(20)

            // setup legend
            if (barInterface.getYLeftItems().size > 1) {
                barInterface.getChartHolderViewGroup()?.rvHolderParent?.visibility = View.VISIBLE
                val airRv = AirRv(object: AirRv.Callback {
                    override fun getAppContext(): Context? {
                        return barInterface.getContext()
                    }

                    override fun getBindView(viewHolder: RecyclerView.ViewHolder, viewType: Int, position: Int) {
                        val yVal = barInterface.getYLeftItems()[position]
                        val customViewHolder = viewHolder as CustomViewHolder
                        customViewHolder.legendLabelTV.text = yVal.legendLabel
                        customViewHolder.legendColorLayout.setBackgroundColor(
                                if (barInterface.getColors().isNullOrEmpty().not()) {
                                    getItemFromArrayAtIndexCyclicly(colors, position)
                                } else {
                                    android.R.color.black
                                }
                        )
                    }

                    override fun getEmptyView(): View? {
                        return null
                    }

                    override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    }

                    override fun getRvHolderViewGroup(): ViewGroup? {
                        return barInterface.getChartHolderViewGroup()?.rvHolder
                    }

                    override fun getSize(): Int? {
                        return barInterface.getYLeftItems().size
                    }

                    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                        return CustomViewHolder(view)
                    }

                    override fun getViewLayoutId(viewType: Int): Int? {
                        return R.layout.air_chart_view_rv_item
                    }

                    override fun getViewType(position: Int): Int? {
                        return 0
                    }

                })
                airRv.rv.isNestedScrollingEnabled = false
                airRv.rv.setHasFixedSize(true)
                airRv.rv.setItemViewCacheSize(20)
            } else {
                barInterface.getChartHolderViewGroup()?.rvHolderParent?.visibility = View.GONE
            }

            // barChart.animateXY(TIME, TIME)
            barChart.invalidate()

            getBarChart?.invoke(barChart)

        }

        private fun <T> getItemFromArrayAtIndexCyclicly(arrayList: ArrayList<T>, index: Int): T {
            if (index > arrayList.size - 1) {
                val newIndex = index - arrayList.size
                return getItemFromArrayAtIndexCyclicly(arrayList, newIndex)
            } else {
                return arrayList[index]
            }
        }

        private fun drawValuesAccordingly(barChart: BarChart) {
            if (barChart.visibleXRange > 20) {
                barChart.xAxis.setDrawLabels(false)
            } else {
                barChart.xAxis.setDrawLabels(true)
            }
        }
    }

    class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val legendColorLayout: LinearLayout = view.legendColorLayout
        val legendLabelTV: TextView = view.legendLabelTV
    }

}