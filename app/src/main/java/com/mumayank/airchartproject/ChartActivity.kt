package com.mumayank.airchartproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.mumayank.airchart.data_classes.AirChartAdditionalData
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.data_classes.AirChartValueItem
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.chart_activity.*

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
                                return arrayListOf("Row 1", "Row 2", "Row 3")
                            }

                            override fun getYLeftLabel(): String {
                                return "This is Y label"
                            }

                            override fun getYLeftItems(): java.util.ArrayList<AirChartValueItem> {
                                if (false) {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Series A",
                                            arrayListOf(1f, 1f, 1f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Series B",
                                            arrayListOf(3f, 3f, 3f)
                                        ),
                                        AirChartValueItem(
                                            "Series C",
                                            arrayListOf(6f, 6f, 6f)
                                        ),
                                        AirChartValueItem(
                                            "Series D",
                                            arrayListOf(10f, 10f,10f)
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

                            /*override fun getAdditionalDatas(): java.util.ArrayList<AirChartAdditionalData>? {
                                return arrayListOf(
                                    AirChartAdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AirChartAdditionalData(
                                        "Final turnover and suppose this field has a longer text then what?",
                                        "19.5 %"
                                    ),
                                    AirChartAdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    )
                                )
                            }*/

                            /*override fun getCustomViewLayoutResId(): Int? {
                                return R.layout.custom_view
                            }*/

                            override fun getColors(): ArrayList<String>? {
                                return arrayListOf("#E57373", "#7986CB", "#FFD54F", "#AED581")
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
                                return "Horizontal Bar Chart"
                            }

                            override fun getXLabel(): String {
                                return "This is X label"
                            }

                            override fun getXLabels(): ArrayList<String> {
                                return arrayListOf("Row 1", "Row 2", "Row 3", "Row 4", "Row 5", "Row 6")
                            }

                            override fun getYLeftLabel(): String {
                                return "This is Y label"
                            }

                            override fun getYLeftItems(): java.util.ArrayList<AirChartValueItem> {
                                if (true) {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Series A",
                                            arrayListOf(8f, -16f, 24f, 11f, 4f, 5f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Series A",
                                            arrayListOf(1f, 2f, 3f)
                                        ),
                                        AirChartValueItem(
                                            "Series B",
                                            arrayListOf(3f, 4f, 5f)
                                        ),
                                        AirChartValueItem(
                                            "Series C",
                                            arrayListOf(5f, 6f, 7f)
                                        ),
                                        AirChartValueItem(
                                            "Series D",
                                            arrayListOf(7f, 8f, 9f)
                                        )
                                    )
                                }
                            }

                            /**
                             * Additional functions need to be inflated manually:
                             */

                            /*override fun getSubTitle(): String {
                                return "Examples"
                            }*/

                            override fun getAdditionalDatas(): java.util.ArrayList<AirChartAdditionalData>? {
                                return arrayListOf(
                                    AirChartAdditionalData(
                                        "In this example",
                                        "When values are far apart"
                                    )
                                )
                            }

                            /*override fun getCustomViewLayoutResId(): Int? {
                                return R.layout.custom_view
                            }*/

                            override fun getColors(): ArrayList<String>? {
                                return arrayListOf("#E57373", "#7986CB", "#FFD54F", "#AED581")
                            }

                            override fun getDecimalFormatPattern(): String {
                                return "0.#"
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