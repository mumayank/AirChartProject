package com.mumayank.airchart

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.BarChart
import com.google.gson.Gson
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.data_classes.AdditionalValue
import com.mumayank.airchart.data_classes.Bar
import com.mumayank.airchart.data_classes.Value
import java.util.*

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

    fun showBarChart(
        IBar: AirChartBar.IBar,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        AirChartBar.show(activity, layoutInflater, chartHolderViewGroup, IBar, getBarChart)
    }

    fun showBarChart(
        jsonString: String,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        try {
            val bar = Gson().fromJson<Bar>(jsonString, Bar::class.java)
            if (bar == null) {
                throw Exception()
            } else {
                Bar(
                    bar.title,
                    bar.xAxisTitle,
                    bar.xAxisLabels,
                    bar.yLeftAxisTitle,
                    bar.yLeftAxisValues,
                    bar.colors,
                    bar.subTitle,
                    bar.decimalFormatPattern,
                    bar.additionalValues,
                    bar.isHorizontal,
                    bar.isAnimationRequired
                )
                showBarChart(object : AirChartBar.IBar {

                    override fun getTitle(): String? {
                        return bar.title
                    }

                    override fun getXAxisTitle(): String {
                        return bar.xAxisTitle
                    }

                    override fun getXAxisLabels(): ArrayList<String> {
                        return bar.xAxisLabels
                    }

                    override fun getYLeftAxisTitle(): String {
                        return bar.yLeftAxisTitle
                    }

                    override fun getYLeftAxisValues(): ArrayList<Value> {
                        return bar.yLeftAxisValues
                    }

                    override fun getColors(): ArrayList<String>? {
                        return bar.colors
                    }

                    override fun getSubTitle(): String {
                        return bar.subTitle
                    }

                    override fun getDecimalFormatPattern(): String {
                        return bar.decimalFormatPattern
                    }

                    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
                        return bar.additionalValues
                    }

                    override fun getIsHorizontal(): Boolean {
                        return bar.isHorizontal
                    }

                    override fun getIsAnimationRequired(): Boolean {
                        return bar.isAnimationRequired
                    }

                }, getBarChart)
            }
        } catch (e: Exception) {
            Log.e("AirChart", e.message ?: "Some error occurred")
        }
    }

}