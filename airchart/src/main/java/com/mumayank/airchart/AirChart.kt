package com.mumayank.airchart

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.android.synthetic.main.air_chart_view_rv_item_additional_data.view.*
import kotlinx.android.synthetic.main.air_chart_view_rv_item_legends.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mumayank.com.airrecyclerview.AirRv
import java.text.DecimalFormat

class AirChart(
    val activity: Activity,
    val chartHolderViewGroup: ViewGroup?
) {

    var layoutInflater: LayoutInflater? = null
    
    init {
        // inflate view so that progress starts showing instantly
        layoutInflater = activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        chartHolderViewGroup?.removeAllViews()
        chartHolderViewGroup?.addView(
            layoutInflater?.inflate(
                R.layout.air_chart_view,
                LinearLayout(activity)
            ),
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }
    
    fun bar(
        barInterface: BarInterface,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        // make chart
        val barChart = BarChart(activity)

        // do in BG
        AirCoroutine.execute(activity, object: AirCoroutine.Callback {

            var valuesCount = 0
            val colors = arrayListOf<Int>()

            override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {

                // label count
                for (yItem in barInterface.getYLeftItems()) {
                    for (value in yItem.values) {
                        valuesCount++
                    }
                }

                // transform colors
                if (barInterface.getColors() == null) {
                    colors.add(activity.resources.getColor(android.R.color.holo_green_light))
                } else {
                    for (color in (barInterface.getColors() as ArrayList<String>)) {
                        colors.add(Color.parseColor(color))
                    }
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
                        if (barInterface.getYLeftItems().size > 1) {
                            barDataSet.color =
                                getItemFromArrayAtIndexCyclically(colors, index)
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
                    IndexAxisValueFormatter(barInterface.getXLabels())

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
                barChart.xAxis?.labelCount = barInterface.getXLabels().size
                barChart.xAxis?.setDrawLabels(true)
                barChart.xAxis.labelRotationAngle = -90f
                barChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                barChart.xAxis.gridColor =
                    ContextCompat.getColor(
                        activity,
                        R.color.colorLightGray
                    )
                barChart.xAxis.textSize = 12f
                barChart.xAxis.textColor =
                    ContextCompat.getColor(
                        activity,
                        android.R.color.black
                    )
                barChart.xAxis.axisLineColor =
                    ContextCompat.getColor(
                        activity,
                        android.R.color.black
                    )
                barChart.xAxis.setAvoidFirstLastClipping(false)
                barChart.xAxis.labelCount =
                    if (barInterface.getXLabels().size > 20) 20 else barInterface.getXLabels().size

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
                barChart.axisRight?.axisMinimum = barData.xMin - 0.5f
                barChart.axisRight?.axisMaximum = barData.xMax + 0.5f
                barChart.axisRight.axisLineColor =
                    ContextCompat.getColor(
                        activity,
                        android.R.color.black
                    )

                barChart.axisLeft?.axisMinimum = 0f
                barChart.axisLeft?.textColor =
                    ContextCompat.getColor(
                        activity,
                        android.R.color.black
                    )
                barChart.axisLeft.textSize = 12f
                barChart.axisLeft?.setDrawAxisLine(true)
                barChart.axisLeft?.setDrawGridLines(false)
                barChart.axisLeft?.isGranularityEnabled = true
                barChart.axisLeft?.granularity = 1f
                barChart.axisLeft?.setDrawLabels(true)
                barChart.axisLeft.setDrawTopYLabelEntry(true)
                barChart.axisLeft.axisLineColor =
                    ContextCompat.getColor(
                        activity,
                        android.R.color.black
                    )
                barChart.axisLeft?.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
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
                barChart.renderer = MyBarChartRenderer(
                    barChart,
                    barChart.animator,
                    barChart.viewPortHandler,
                    BAR_RADIUS,
                    colors
                )
                barChart.setExtraOffsets(0f, 32f, 0f, 16f)
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

                // setup spacing
                if (barChart.data.dataSetCount > 1) {
                    // val nos = barInterface.getXLabels().size.toFloat()
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
                    barChart.setFitBars(true)
                    barChart.xAxis?.axisMinimum = barData.xMin - 0.5f
                    barChart.xAxis?.axisMaximum = barData.xMax + 0.5f
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
                        barInterface.getTitle() ?: ""
                    chartHolderViewGroup?.subTitle?.text =
                        barInterface.getSubTitle()
                    chartHolderViewGroup?.title?.visibility =
                        if (barInterface.getTitle() != null) View.VISIBLE else View.GONE
                    chartHolderViewGroup?.xLabel?.text =
                        barInterface.getXLabel()
                    val yLabelLeft =
                        chartHolderViewGroup?.yLabelLeft as TextView?
                    yLabelLeft?.text = barInterface.getYLeftLabel()
                    chartHolderViewGroup?.yLabelRightLayout?.visibility =
                        View.GONE

                    // setup custom view
                    if (barInterface.getCustomViewLayoutResId() == null) {
                        chartHolderViewGroup?.customView?.visibility =
                            View.GONE
                    } else {
                        chartHolderViewGroup?.customView?.removeAllViews()
                        chartHolderViewGroup?.customView?.addView(
                            layoutInflater?.inflate(
                                barInterface.getCustomViewLayoutResId() as Int,
                                LinearLayout(activity)
                            ), ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )
                        chartHolderViewGroup?.customView?.visibility =
                            View.VISIBLE
                    }

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
                    barChart.setScaleEnabled(true)
                    barChart.isScaleYEnabled = false
                    if (valuesCount > 15) {
                        barChart.zoom(valuesCount / 15f, 1f, 0f, 0f)
                    } else {
                        barChart.zoom(1f, 1f, 0f, 0f)
                    }
                    drawValuesAccordingly(barChart)

                    // setup legend
                    if (barInterface.getYLeftItems().size > 1) {
                        chartHolderViewGroup?.rvHolderLegends?.visibility =
                            View.VISIBLE
                        val airRv = AirRv(object : AirRv.Callback {
                            override fun getAppContext(): Context? {
                                return activity
                            }

                            override fun getBindView(
                                viewHolder: RecyclerView.ViewHolder,
                                viewType: Int,
                                position: Int
                            ) {
                                val yVal = barInterface.getYLeftItems()[position]
                                val customViewHolder = viewHolder as LegedsViewHolder
                                customViewHolder.legendLabelTV.text = yVal.legendLabel
                                customViewHolder.legendColorLayout.setBackgroundColor(
                                    if (barInterface.getColors().isNullOrEmpty().not()) {
                                        getItemFromArrayAtIndexCyclically(colors, position)
                                    } else {
                                        android.R.color.black
                                    }
                                )
                            }

                            override fun getEmptyView(): View? {
                                return null
                            }

                            override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                                return object :
                                    StaggeredGridLayoutManager(
                                        2,
                                        StaggeredGridLayoutManager.VERTICAL
                                    ) {
                                    override fun canScrollVertically(): Boolean {
                                        return false
                                    }

                                    override fun canScrollHorizontally(): Boolean {
                                        return false
                                    }
                                }
                            }

                            override fun getRvHolderViewGroup(): ViewGroup? {
                                return chartHolderViewGroup?.rvHolderLegends
                            }

                            override fun getSize(): Int? {
                                return barInterface.getYLeftItems().size
                            }

                            override fun getViewHolder(
                                view: View,
                                viewType: Int
                            ): RecyclerView.ViewHolder {
                                return LegedsViewHolder(view)
                            }

                            override fun getViewLayoutId(viewType: Int): Int? {
                                return R.layout.air_chart_view_rv_item_legends
                            }

                            override fun getViewType(position: Int): Int? {
                                return 0
                            }

                        })
                        airRv.rv.isNestedScrollingEnabled = false
                        airRv.rv.setHasFixedSize(true)
                    } else {
                        chartHolderViewGroup?.rvHolderLegends?.visibility =
                            View.GONE
                    }

                    // setup additional data
                    if (barInterface.getAdditionalDatas() == null) {
                        chartHolderViewGroup?.rvHolderAdditionalData?.visibility =
                            View.GONE
                    } else {
                        val additionalDatas = arrayListOf<AdditionalData>()
                        additionalDatas.addAll(barInterface.getAdditionalDatas() as ArrayList<AdditionalData>)
                        if (additionalDatas.size % 2 != 0) {
                            additionalDatas.add(AdditionalData("-", "-"))
                        }

                        val airRv = AirRv(object : AirRv.Callback {
                            override fun getAppContext(): Context? {
                                return activity
                            }

                            override fun getBindView(
                                viewHolder: RecyclerView.ViewHolder,
                                viewType: Int,
                                position: Int
                            ) {
                                val additionalData = additionalDatas[position]
                                val additionalDataViewHolder =
                                    viewHolder as AdditionalDataViewHolder
                                additionalDataViewHolder.keyTV.text = additionalData.key
                                additionalDataViewHolder.valueTV.text = additionalData.value
                                additionalDataViewHolder.keyTV.gravity =
                                    if (position % 2 != 0) Gravity.END else Gravity.START
                                additionalDataViewHolder.valueTV.gravity =
                                    if (position % 2 != 0) Gravity.END else Gravity.START
                            }

                            override fun getEmptyView(): View? {
                                return null
                            }

                            override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                                return object :
                                    StaggeredGridLayoutManager(
                                        2,
                                        StaggeredGridLayoutManager.VERTICAL
                                    ) {
                                    override fun canScrollVertically(): Boolean {
                                        return false
                                    }

                                    override fun canScrollHorizontally(): Boolean {
                                        return false
                                    }
                                }
                            }

                            override fun getRvHolderViewGroup(): ViewGroup? {
                                return chartHolderViewGroup?.rvHolderAdditionalData
                            }

                            override fun getSize(): Int? {
                                return additionalDatas.size
                            }

                            override fun getViewHolder(
                                view: View,
                                viewType: Int
                            ): RecyclerView.ViewHolder {
                                return AdditionalDataViewHolder(view)
                            }

                            override fun getViewLayoutId(viewType: Int): Int? {
                                return R.layout.air_chart_view_rv_item_additional_data
                            }

                            override fun getViewType(position: Int): Int? {
                                return 0
                            }

                        })
                        airRv.rv.isNestedScrollingEnabled = false
                        airRv.rv.setHasFixedSize(true)
                        chartHolderViewGroup?.rvHolderAdditionalData?.visibility =
                            View.VISIBLE
                    }

                    chartHolderViewGroup?.progressView?.visibility = View.GONE

                    if (barInterface.getIsAnimationRequired()) {
                        barChart.animateXY(TIME, TIME)
                    } else {
                        barChart.invalidate()
                    }

                    getBarChart?.invoke(barChart)
                }

            }

        })
    }

    interface BarInterface {
        fun getTitle(): String?
        fun getXLabel(): String
        fun getXLabels(): ArrayList<String>
        fun getYLeftLabel(): String
        fun getYLeftItems(): java.util.ArrayList<YLeftItem>

        fun getSubTitle(): String {
            return ""
        }

        fun getAdditionalDatas(): java.util.ArrayList<AdditionalData>? {
            return null
        }

        fun getCustomViewLayoutResId(): Int? {
            return null
        }

        fun getColors(): ArrayList<String>? {
            return null
        }

        fun getDecimalFormatPattern(): String {
            return "0.#"
        }

        fun getIsAnimationRequired(): Boolean {
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

        private const val TIME = 500
        private const val STANDARD_BAR_WIDTH = 0.5f
        private const val BAR_RADIUS = 10f
        // private const val MAX_X_RANGE = 6f

        private fun <T> getItemFromArrayAtIndexCyclically(arrayList: ArrayList<T>, index: Int): T {
            if (index > arrayList.size - 1) {
                val newIndex = index - arrayList.size
                return getItemFromArrayAtIndexCyclically(arrayList, newIndex)
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

    class LegedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val legendColorLayout: LinearLayout = view.legendColorLayout
        val legendLabelTV: TextView = view.legendLabelTV
    }

    class AdditionalDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val keyTV: TextView = view.keyTV
        val valueTV: TextView = view.valueTV
    }

}