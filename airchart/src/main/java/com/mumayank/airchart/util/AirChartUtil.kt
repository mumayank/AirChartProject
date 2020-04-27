package com.mumayank.airchart.util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mumayank.airchart.R
import com.mumayank.airchart.data_classes.AdditionalValue
import com.mumayank.airchart.data_classes.Value
import kotlinx.android.synthetic.main.air_chart_view_rv_item_additional_data.view.*
import kotlinx.android.synthetic.main.air_chart_view_rv_item_legends.view.*
import mumayank.com.airrecyclerview.AirRv

class AirChartUtil {

    class AdditionalDataInternal(
        val key1: String,
        val value1: String,
        val key2: String,
        val value2: String
    )

    class AdditionalDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val key1TV: TextView = view.key1TV
        val value1TV: TextView = view.value1TV
        val key2TV: TextView = view.key2TV
        val value2TV: TextView = view.value2TV
    }

    class LegendsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val legendColorLayout: LinearLayout = view.legendColorLayout
        val legendLabelTV: TextView = view.legendLabelTV
    }

    companion object {

        const val ANIMATION_TIME = 500
        const val STANDARD_BAR_WIDTH = 0.5f

        fun <T> getItemFromArrayAtIndexCyclically(arrayList: ArrayList<T>, index: Int): T {
            return if (index > arrayList.size - 1) {
                val newIndex = index - arrayList.size
                getItemFromArrayAtIndexCyclically(arrayList, newIndex)
            } else {
                arrayList[index]
            }
        }

        fun setupAdditionalData(
            activity: Activity,
            rvHolderAdditionalData: LinearLayout?,
            additionalDatumItems: java.util.ArrayList<AdditionalValue>?
        ) {
            if (additionalDatumItems == null) {
                rvHolderAdditionalData?.visibility = View.GONE
            } else {
                val additionalDataInternalList = arrayListOf<AdditionalDataInternal>()
                for (i in 0 until additionalDatumItems.size step 2) {
                    val additionalData1 = additionalDatumItems[i]
                    var additionalDataItem2: AdditionalValue? = null
                    if (i != additionalDatumItems.size - 1) {
                        additionalDataItem2 = additionalDatumItems[i + 1]
                    }
                    if (additionalDataItem2 == null) {
                        additionalDataItem2 =
                            AdditionalValue(
                                "-",
                                ""
                            )
                    }
                    additionalDataInternalList.add(
                        AdditionalDataInternal(
                            additionalData1.key,
                            additionalData1.value,
                            additionalDataItem2.key,
                            additionalDataItem2.value
                        )
                    )
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
                        val additionalDataInternal = additionalDataInternalList[position]
                        val additionalDataViewHolder = viewHolder as AdditionalDataViewHolder
                        additionalDataViewHolder.key1TV.text = additionalDataInternal.key1
                        additionalDataViewHolder.value1TV.text = additionalDataInternal.value1
                        additionalDataViewHolder.key2TV.text = additionalDataInternal.key2
                        additionalDataViewHolder.value2TV.text = additionalDataInternal.value2
                    }

                    override fun getEmptyView(): View? {
                        return null
                    }

                    override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                        return LinearLayoutManager(appContext)
                    }

                    override fun getRvHolderViewGroup(): ViewGroup? {
                        return rvHolderAdditionalData
                    }

                    override fun getSize(): Int? {
                        return additionalDataInternalList.size
                    }

                    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
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
                rvHolderAdditionalData?.visibility =
                    View.VISIBLE

            }

        }

        fun setupLegend(
            activity: Activity,
            colors: ArrayList<Int>,
            rvHolderLegends: LinearLayout?,
            valueItems: java.util.ArrayList<Value>,
            isReverseLayoutRequired: Boolean = false
        ) {
            if (valueItems.size > 1) {

                if (isReverseLayoutRequired) {
                    val airChartValueItemsTemp = java.util.ArrayList<Value>()
                    for (i in valueItems.size - 1 downTo 0) {
                        airChartValueItemsTemp.add(valueItems[i])
                    }
                    valueItems.clear()
                    valueItems.addAll(airChartValueItemsTemp)
                }

                val gridLayoutManager = object :
                    GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }

                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
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
                        val airChartValueItem = valueItems[position]
                        val legendsViewHolder = viewHolder as LegendsViewHolder
                        legendsViewHolder.legendLabelTV.text = airChartValueItem.legendLabel

                        legendsViewHolder.legendColorLayout.setBackgroundColor(
                            if (colors.isNullOrEmpty().not()) {
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
                        return gridLayoutManager
                    }

                    override fun getRvHolderViewGroup(): ViewGroup? {
                        return rvHolderLegends
                    }

                    override fun getSize(): Int? {
                        return valueItems.size
                    }

                    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                        return LegendsViewHolder(view)
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
                rvHolderLegends?.visibility = View.VISIBLE
            } else {
                rvHolderLegends?.visibility = View.GONE
            }

        }

        fun setupCustomView(
            activity: Activity,
            layoutInflater: LayoutInflater?,
            customViewLayoutResId: Int?,
            customView: LinearLayout?
        ) {
            if (customViewLayoutResId == null) {
                customView?.visibility = View.GONE
            } else {
                customView?.removeAllViews()
                customView?.addView(
                    layoutInflater?.inflate(
                        customViewLayoutResId,
                        LinearLayout(activity)
                    ), ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                customView?.visibility = View.VISIBLE
            }
        }

        fun getValuesCount(
            valuesCount: Int,
            valueItems: java.util.ArrayList<Value>
        ): Int {
            var valuesCountTemp = valuesCount
            for (yItem in valueItems) {
                for (value in yItem.values) {
                    valuesCountTemp++
                }
            }
            return valuesCountTemp
        }

        fun drawValuesAccordinglyInBarChart(barChart: BarChart) {
            if (barChart.visibleXRange > 20) {
                barChart.xAxis.setDrawLabels(false)
            } else {
                barChart.xAxis.setDrawLabels(true)
            }
        }

        fun drawValuesAccordinglyInLineChart(lineChart: LineChart) {
            if (lineChart.visibleXRange > 20) {
                lineChart.xAxis.setDrawLabels(false)
            } else {
                lineChart.xAxis.setDrawLabels(true)
            }
        }

        fun getPixelsFromDps(activity: Activity, dps: Int): Int {
            val scale: Float = activity.resources.displayMetrics.density
            val pixels = (dps * scale + 0.5f).toInt()
            return pixels
        }

        fun <T> getObjectFromJson(string: String): T {
            return Gson().fromJson<T>(string, object : TypeToken<T>() {}.type)
        }

    }

}