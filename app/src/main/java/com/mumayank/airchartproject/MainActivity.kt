package com.mumayank.airchartproject

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.ChartItem
import com.mumayank.airchart.YLeftItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AirChart.bar(object: AirChart.BarInterface{

            override fun getContext(): Context {
                return this@MainActivity
            }

            override fun getChartHolderViewGroup(): ViewGroup? {
                return parentLayout
            }

            override fun getTitle(): String {
                return "This is title of the chart"
            }

            override fun getIsTitleVisible(): Boolean {
                return true
            }

            override fun getColors(): ArrayList<String> {
                return arrayListOf("#ffa726", "#2196f3")
            }

            override fun getDecimalFormatPattern(): String {
                return "0.00"
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
                return arrayListOf(
                    YLeftItem("Legend 1", arrayListOf(5f, 5.5f, 3f, 4f))
                )
            }

            override fun onNoValueSelected() {
                // do nothing for now
            }

            override fun onValueSelected(e: Entry?) {
                // do nothing for now
            }

        })
    }
}