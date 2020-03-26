package com.mumayank.airchartproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.mumayank.airchart.data_classes.AdditionalData
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.charts.bar.AirChartBarValueItem
import com.mumayank.airchart.charts.horizontal_bar.AirChartHorizontalBar
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.chart_activity.*
import kotlinx.coroutines.delay

class ChartActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_CHART_TYPE = "INTENT_EXTRA_CHART_TYPE"
    }

    enum class ChartType {
        BAR,
        HORIZONTAL_BAR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)

        showChart()
    }

    private fun showChart() {

        val airChart = AirChart(this, parentLayout)

        AirCoroutine.execute(this, object: AirCoroutine.Callback {

            override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {
                // delay(2000)
                return true
            }

            override fun isTaskTypeCalculation(): Boolean {
                return true
            }

            override fun onFailure(viewModel: ViewModel) {
                // do nothing
            }

            override fun onSuccess(viewModel: ViewModel) {
                val string = intent.getStringExtra(INTENT_EXTRA_CHART_TYPE) ?: ""

                when (ChartType.valueOf(string)) {

                    ChartType.BAR -> {

                        airChart.showBarChart(object: AirChartBar.BarInterface {

                            override fun getTitle(): String? {
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

                            override fun getYLeftItems(): java.util.ArrayList<AirChartBarValueItem> {
                                if (false) {
                                    return arrayListOf(
                                        AirChartBarValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartBarValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartBarValueItem(
                                            "Legend 2",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartBarValueItem(
                                            "Legend 3",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        )
                                    )
                                }
                            }

                            /**
                             * Additional functions need to be inflated manually:
                             */

                            /*override fun getSubTitle(): String {
                                return "This is a sub title"
                            }*/

                            /*override fun getAdditionalDatas(): java.util.ArrayList<AdditionalData>? {
                                return arrayListOf(
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    )
                                )
                            }

                            override fun getCustomViewLayoutResId(): Int? {
                                return R.layout.custom_view
                            }*/

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

                    ChartType.HORIZONTAL_BAR -> {

                        airChart.showHorizontalBarChart(object: AirChartBar.BarInterface {

                            override fun getTitle(): String? {
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

                            override fun getYLeftItems(): java.util.ArrayList<AirChartBarValueItem> {
                                if (false) {
                                    return arrayListOf(
                                        AirChartBarValueItem(
                                            "Legend 1",
                                            arrayListOf(1f, 2.5f, 3f, 4f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartBarValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 4f, 2f, 1f)
                                        ),
                                        AirChartBarValueItem(
                                            "Legend 2",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartBarValueItem(
                                            "Legend 3",
                                            arrayListOf(10f, 9f, 8f, 7f)
                                        )
                                    )
                                }
                            }

                            /**
                             * Additional functions need to be inflated manually:
                             */
/*
                            override fun getSubTitle(): String {
                                return "This is a sub title"
                            }

                            override fun getAdditionalDatas(): java.util.ArrayList<AdditionalData>? {
                                return arrayListOf(
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    ),
                                    AdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    )
                                )
                            }

                            override fun getCustomViewLayoutResId(): Int? {
                                return R.layout.custom_view
                            }*/

                            override fun getColors(): ArrayList<String>? {
                                return arrayListOf("#ffa726", "#2196f3", "#6200EE")
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

            }

        })

    }
}