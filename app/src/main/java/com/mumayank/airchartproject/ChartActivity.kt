package com.mumayank.airchartproject

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.data_classes.Bar
import com.mumayank.airchart.data_classes.Value
import kotlinx.android.synthetic.main.chart_activity.*
import kotlinx.android.synthetic.main.chart_rv_item.view.*
import mumayank.com.airrecyclerview.AirRv


class ChartActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_CHART_TYPE = "INTENT_EXTRA_CHART_TYPE"
        const val INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA"

        private fun getData(
            noOfLegends: Int,
            noOfDataItems: Int,
            minDataVal: Int,
            maxDataVal: Int
        ): ArrayList<Value> {
            val legendsArrayList = java.util.ArrayList<Value>()
            for (k in 1..noOfLegends) {
                val arrayList = java.util.ArrayList<Double>()
                for (i in 1..noOfDataItems) {
                    val nextRandomFloat =
                        kotlin.random.Random.nextInt(minDataVal, maxDataVal + 1).toDouble()
                    arrayList.add(nextRandomFloat)
                }
                legendsArrayList.add(
                    Value(
                        "lengend$k",
                        arrayList
                    )
                )
            }
            return legendsArrayList
        }

        private fun getLabels(size: Int): ArrayList<String> {
            val arrayList = arrayListOf<String>()
            for (i in 1..size) {
                arrayList.add("val$i")
            }
            return arrayList
        }
    }

    enum class ChartType {
        BAR,
        HORIZONTAL_BAR,
        LINE
    }

    private var chartType = ChartType.BAR

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)

        val intentExtraData = intent.getStringExtra(INTENT_EXTRA_DATA) ?: ""

        if (intentExtraData.isEmpty()) {
            AssetHelper.readFile(
                this,
                "chart_data",
                fun(string: String) {
                    showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                        showChartsInternal(
                            chartLayout,
                            string
                        )
                    })
                }
            )
        } else {
            showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                showChartsInternal(
                    chartLayout,
                    intentExtraData
                )
            })
        }

        val intentExtraString = intent.getStringExtra(INTENT_EXTRA_CHART_TYPE) ?: ""
        chartType = ChartType.valueOf(intentExtraString)

        when (chartType) {
            ChartType.BAR -> {
                val barDatas = arrayListOf(

                    /**
                     * todo: POSITIVE VALUES
                     */

                    BarData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    BarData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30)
                    ),
                    BarData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30)
                    ),
                    BarData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30)
                    ),
                    BarData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30)
                    ),
                    BarData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30)
                    ),
                    BarData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000)
                    ),
                    BarData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30)
                    ),
                    BarData(
                        "32 values",
                        getLabels(32),
                        getData(1, 32, 0, 30)
                    ),
                    BarData(
                        "32 values - far apart",
                        getLabels(32),
                        getData(1, 32, 0, 30000)
                    ),
                    BarData(
                        "50 values",
                        getLabels(50),
                        getData(1, 50, 0, 30)
                    ),
                    BarData(
                        "100 values",
                        getLabels(100),
                        getData(1, 100, 0, 30)
                    ),
                    BarData(
                        "500 values",
                        getLabels(500),
                        getData(1, 500, 0, 30)
                    ),
                    BarData(
                        "1000 values",
                        getLabels(1000),
                        getData(1, 1000, 0, 30)
                    ),

                    /**
                     * todo: NEGATIVE VALUES
                     */
                    BarData(
                        "1 value - nagative",
                        getLabels(1),
                        getData(1, 1, -30, -10)
                    ),
                    BarData(
                        "2 values - nagative",
                        getLabels(2),
                        getData(1, 2, -30, -10)
                    ),
                    BarData(
                        "3 values - nagative",
                        getLabels(3),
                        getData(1, 3, -30, -10)
                    ),
                    BarData(
                        "4 values - nagative",
                        getLabels(4),
                        getData(1, 4, -30, 5)
                    ),
                    BarData(
                        "8 values - nagative",
                        getLabels(8),
                        getData(1, 8, -30, 10)
                    ),
                    BarData(
                        "8 values - nagative - large",
                        getLabels(8),
                        getData(1, 8, -3000, -100)
                    ),
                    BarData(
                        "32 values - far apart - negative",
                        getLabels(32),
                        getData(1, 32, -3000, 3000)
                    ),

                    /**
                     * todo: GROUPED POSITIVE
                     */

                    BarData(
                        "2 grouped",
                        getLabels(2),
                        getData(2, 2, 10, 30)
                    ),
                    BarData(
                        "some grouped",
                        getLabels(4),
                        getData(4, 4, 0, 30)
                    ),
                    BarData(
                        "some grouped - far apart",
                        getLabels(4),
                        getData(4, 4, 0, 3000)
                    ),
                    BarData(
                        "many grouped",
                        getLabels(16),
                        getData(4, 16, 0, 30)
                    ),

                    /**
                     * todo: GROUPED NEGATIVE
                     */

                    BarData(
                        "2 grouped - negative",
                        getLabels(2),
                        getData(2, 2, -30, -10)
                    ),
                    BarData(
                        "some grouped - negative",
                        getLabels(4),
                        getData(4, 4, -30, 10)
                    ),
                    BarData(
                        "some grouped - far apart - negative",
                        getLabels(4),
                        getData(4, 4, -3000, 3000)
                    ),
                    BarData(
                        "many grouped - negative",
                        getLabels(16),
                        getData(4, 16, -30, 30)
                    )
                )

                showRv(barDatas.size, fun(chartLayout: LinearLayout, position: Int) {
                    val barData = barDatas[position]
                    showChartsInternal(
                        chartLayout,
                        Gson().toJson(
                            Bar(
                                barData.title,
                                "x axis",
                                barData.xLabels,
                                "y axis",
                                barData.yLefts,
                                barData.colors,
                                null,
                                null,
                                null,
                                false,
                                null
                            )
                        )
                    )
                })

            }

            ChartType.HORIZONTAL_BAR -> {
                val barDatas = arrayListOf(

                    /**
                     * todo: POSITIVE VALUES
                     */

                    BarData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    BarData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),
                    BarData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - far apart",
                        getLabels(16),
                        getData(1, 16, 0, 30000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "19 values",
                        getLabels(19),
                        getData(1, 19, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),

                    /**
                     * todo: NEGATIVE VALUES
                     */
                    BarData(
                        "2 values - nagative",
                        getLabels(2),
                        getData(1, 2, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "3 values - nagative",
                        getLabels(3),
                        getData(1, 3, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),
                    BarData(
                        "4 values - nagative",
                        getLabels(4),
                        getData(1, 4, -30, 5),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values - nagative",
                        getLabels(8),
                        getData(1, 8, -30, 10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - nagative - large",
                        getLabels(16),
                        getData(1, 16, -3000, -100),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - far apart - negative",
                        getLabels(16),
                        getData(1, 16, -3000, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),

                    /**
                     * todo: GROUPED POSITIVE
                     */

                    BarData(
                        "2 grouped",
                        getLabels(2),
                        getData(2, 2, 10, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "some grouped",
                        getLabels(4),
                        getData(4, 4, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "some grouped - far apart",
                        getLabels(4),
                        getData(4, 4, 0, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400)
                        )
                    ),

                    /**
                     * todo: GROUPED NEGATIVE
                     */

                    BarData(
                        "2 grouped - negative",
                        getLabels(2),
                        getData(2, 2, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "some grouped - negative",
                        getLabels(4),
                        getData(4, 4, -30, 10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "some grouped - far apart - negative",
                        getLabels(4),
                        getData(4, 4, -3000, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    )
                )

                showRv(barDatas.size, fun(chartLayout: LinearLayout, position: Int) {
                    val barData = barDatas[position]
                    /*showBarChartsInternal(
                        chartLayout,
                        Bar(barData.title, "x axis", barData.xLabels, "y axis", barData.yLefts, barData.colors, null, null, null, null, null)
                    )*/
                })

            }

            ChartType.LINE -> {
                val lineDataList = ArrayList<String>()
                val lineData = arrayListOf(

                    LineData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    LineData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30)
                    )
                )

                AssetHelper.readFile(
                    this,
                    "line_chart_data",
                    fun(string: String) {
                        lineDataList.add(string)

                        showRv(lineDataList.size, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, lineDataList[position])
                        })
                    }
                )

                AssetHelper.readFile(
                    this,
                    "single_line_chart_data",
                    fun(string: String) {
                        lineDataList.add(string)

                        showRv(lineDataList.size, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, lineDataList[position])
                        })
                    }
                )

                AssetHelper.readFile(
                    this,
                    "invisible_line_chart_data",
                    fun(string: String) {
                        lineDataList.add(string)

                        showRv(lineDataList.size, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, lineDataList[position])
                        })
                    }
                )

                AssetHelper.readFile(
                    this,
                    "negative_values_line_chart_data",
                    fun(string: String) {
                        lineDataList.add(string)

                        showRv(lineDataList.size, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, lineDataList[position])
                        })
                    }
                )

                /*
                showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                    val data = lineData[position]
                    showChartsInternal(
                        chartLayout, Gson().toJson(
                            Line(
                                data.title,
                                "x axis",
                                data.xLabels,
                                "y axis",
                                data.yLefts,
                                data.colors,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                    )
                })
                */
            }
        }
    }

    private fun showRv(size: Int, getBindView: (chartLayout: LinearLayout, position: Int) -> Unit) {
        val airRv = AirRv(object : AirRv.Callback {

            override fun getAppContext(): Context? {
                return this@ChartActivity
            }

            override fun getBindView(
                viewHolder: RecyclerView.ViewHolder,
                viewType: Int,
                position: Int
            ) {
                val customViewHolder = viewHolder as CustomViewHolder
                getBindView.invoke(customViewHolder.chartHolder, position)
            }

            override fun getEmptyView(): View? {
                return null
            }

            override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                return LinearLayoutManager(appContext)
            }

            override fun getRvHolderViewGroup(): ViewGroup? {
                return rvHolder
            }

            override fun getSize(): Int? {
                return size
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return CustomViewHolder(view)
            }

            override fun getViewLayoutId(viewType: Int): Int? {
                return R.layout.chart_rv_item
            }

            override fun getViewType(position: Int): Int? {
                return 0
            }

        })

        LinearSnapHelper().attachToRecyclerView(airRv.rv)
    }

    private fun showChartsInternal(viewGroup: ViewGroup, jsonString: String) {

        when (chartType) {
            ChartType.BAR, ChartType.HORIZONTAL_BAR -> {
                AirChart(this, viewGroup).showBarChart(jsonString)
            }
            ChartType.LINE -> AirChart(this, viewGroup).showLineChart(jsonString)
        }
    }

    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chartHolder: LinearLayout = view.chartHolder
    }

    class BarData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yLefts: java.util.ArrayList<Value>,
        val colors: ArrayList<String>? = null
    )

    class LineData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yLefts: java.util.ArrayList<Value>,
        val colors: ArrayList<String>? = null
    )
}