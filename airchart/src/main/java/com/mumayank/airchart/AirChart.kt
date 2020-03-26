package com.mumayank.airchart

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import com.github.mikephil.charting.charts.BarChart
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.charts.horizontal_bar.AirChartHorizontalBar
import com.mumayank.airchart.charts.horizontal_bar.AirChartHorizontalBar2
import com.mumayank.airchart.util.ScrollViewHelper
import kotlinx.android.synthetic.main.air_chart_view.view.*

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
        barInterface: AirChartBar.BarInterface,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        AirChartBar.show(activity, layoutInflater, chartHolderViewGroup, barInterface, getBarChart)
    }

}