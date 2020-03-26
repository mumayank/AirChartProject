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
import com.mumayank.airchart.util.ScrollViewHelper

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
        AirChartBar.show(activity, layoutInflater, chartHolderViewGroup, barInterface, fun(barChart: BarChart) {
            /*resetViewIfNoSpaceForChart(chartHolderViewGroup?.findViewById(R.id.scrollView), chartHolderViewGroup?.findViewById(R.id.scrollViewChild), chartHolderViewGroup?.findViewById(R.id.chart))
            getBarChart?.invoke(barChart)*/
        })
    }

    fun showHorizontalBarChart(
        barInterface: AirChartBar.BarInterface,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        AirChartHorizontalBar.show(activity, layoutInflater, chartHolderViewGroup, barInterface, fun(barChart: BarChart) {
            /*resetViewIfNoSpaceForChart(chartHolderViewGroup?.findViewById(R.id.scrollView), chartHolderViewGroup?.findViewById(R.id.scrollViewChild), chartHolderViewGroup?.findViewById(R.id.chart))
            getBarChart?.invoke(barChart)*/
        })
    }

    companion object {

        private fun resetViewIfNoSpaceForChart(scrollView: ScrollView?, scrollViewChild: LinearLayout?, chartView: LinearLayout?) {
            if (scrollView == null || scrollViewChild == null || chartView == null) {
                return
            }

            if (ScrollViewHelper.isScrollViewScrollable(scrollView, scrollViewChild)) {
                val chartLayoutParams = chartView.layoutParams as LinearLayout.LayoutParams
                chartLayoutParams.height = 550
                chartView.layoutParams = chartLayoutParams
                chartView.requestLayout()
            } else {
                val scrollViewChildLayoutParams = scrollViewChild.layoutParams
                scrollViewChildLayoutParams.height = scrollView.height
                scrollViewChild.layoutParams = scrollViewChildLayoutParams
                scrollViewChild.requestLayout()
            }
        }

    }

}