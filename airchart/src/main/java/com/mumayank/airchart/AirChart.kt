package com.mumayank.airchart

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.BarChart
import com.mumayank.airchart.charts.bar.AirChartBar

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

}