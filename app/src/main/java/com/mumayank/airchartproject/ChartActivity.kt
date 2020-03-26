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
                                return arrayListOf("A", "B", "C", "D")
                            }

                            override fun getYLeftLabel(): String {
                                return "This is Y label"
                            }

                            override fun getYLeftItems(): java.util.ArrayList<AirChartValueItem> {
                                if (false) {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartValueItem(
                                            "Legend 2",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartValueItem(
                                            "Legend 3",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        )
                                    )
                                }
                            }

                            /**
                             * Additional functions need to be inflated manually:
                             */

                            override fun getSubTitle(): String {
                                return "This is a sub title"
                            }

                            override fun getAdditionalDatas(): java.util.ArrayList<AirChartAdditionalData>? {
                                return arrayListOf(
                                    AirChartAdditionalData(
                                        "Total turnout",
                                        "4.5"
                                    ),
                                    AirChartAdditionalData(
                                        "Final turnover",
                                        "19.5 %"
                                    ),
                                    AirChartAdditionalData(
                                        "Subtracted value",
                                        "As discussed"
                                    )
                                )
                            }

                            /*override fun getCustomViewLayoutResId(): Int? {
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

                            override fun getYLeftItems(): java.util.ArrayList<AirChartValueItem> {
                                if (false) {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Legend 1",
                                            arrayListOf(1f, 2.5f, 3f, 4f)
                                        )
                                    )
                                } else {
                                    return arrayListOf(
                                        AirChartValueItem(
                                            "Legend 1",
                                            arrayListOf(5f, 4f, 2f, 1f)
                                        ),
                                        AirChartValueItem(
                                            "Legend 2",
                                            arrayListOf(5f, 5.5f, 3f, 4f)
                                        ),
                                        AirChartValueItem(
                                            "Legend 3",
                                            arrayListOf(10f, 9f, 8f, 7f)
                                        )
                                    )
                                }
                            }

                            /**
                             * Additional functions need to be inflated manually:
                             */
                            override fun getSubTitle(): String {
                                return "This is a sub title"
                            }

                            override fun getAdditionalDatas(): java.util.ArrayList<AirChartAdditionalData>? {
                                return arrayListOf(
                                    AirChartAdditionalData(
                                        "A",
                                        "4.5"
                                    ),
                                    AirChartAdditionalData(
                                        "B",
                                        "19.5 %"
                                    ),
                                    AirChartAdditionalData(
                                        "This is a long text sent by the server so that it goes to multiple lines. let us see how this looks",
                                        "As discussed"
                                    ),
                                    AirChartAdditionalData(
                                        "D",
                                        "4.5"
                                    ),
                                    AirChartAdditionalData(
                                        "E",
                                        "19.5 %"
                                    )/*,
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
                                    )*/
                                )
                            }

                            /*override fun getCustomViewLayoutResId(): Int? {
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