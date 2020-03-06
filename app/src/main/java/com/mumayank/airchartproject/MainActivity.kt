package com.mumayank.airchartproject

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.mumayank.airchart.AdditionalData
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.YLeftItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showChart()
    }

    private fun showChart(shouldShowChar2: Boolean = false) {
        AirChart.bar(object : AirChart.BarInterface {

            override fun getActivity(): Activity {
                return this@MainActivity
            }

            override fun getChartHolderViewGroup(): ViewGroup? {
                return parentLayout
            }

            override fun getTitle(): String {
                return "This is title of the chart"
            }

            override fun getXLabel(): String {
                return "This is X label"
            }

            override fun getXLabels(): ArrayList<String> {
                return arrayListOf("A", "B", "C", "D")
            }

            override fun getYLeftLabel(): String {
                return "This is Y label"
            }

            override fun getYLeftItems(): java.util.ArrayList<YLeftItem> {
                if (shouldShowChar2) {
                    return arrayListOf(
                        YLeftItem("Legend 1", arrayListOf(5f, 5.5f, 3f, 4f))
                    )
                } else {
                    return arrayListOf(
                        YLeftItem("Legend 1", arrayListOf(5f, 5.5f, 3f, 4f)),
                        YLeftItem("Legend 2", arrayListOf(5f, 5.5f, 3f, 4f)),
                        YLeftItem("Legend 3", arrayListOf(5f, 5.5f, 3f, 4f))
                    )
                }
            }

            /**
             * Additional functions need to be inflated manually:
             */

            override fun getSubTitle(): String {
                return "This is a sub title"
            }

            override fun getAdditionalDatas(): java.util.ArrayList<AdditionalData>? {
                return arrayListOf(
                    AdditionalData("Total turnout", "4.5"),
                    AdditionalData("Final turnover", "19.5 %"),
                    AdditionalData("Subtracted value", "As discussed")
                )
            }

            override fun getCustomViewLayoutResId(): Int? {
                return R.layout.custom_view
            }

            override fun getColors(): ArrayList<String>? {
                return arrayListOf("#ffa726", "#2196f3")
            }

            override fun getDecimalFormatPattern(): String {
                return "0.00"
            }

            override fun getIsAnimationRequired(): Boolean {
                return true
            }

            override fun onValueSelected(e: Entry?) {
                // later
            }

            override fun onNoValueSelected() {
                // later
            }

        })
    }
}