# AirChart
[![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Setup

Add this line in your root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' } // add this line
  }
}
  ```
Add this line in your app build.gradle:
```gradle
dependencies {
  implementation 'com.github.mumayank:AirChartProject:LATEST_VERSION' // add this line
}
```
where LATEST_VERSION is [![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Usage

```

AirChart.bar(object: AirChart.BarInterface{

    override fun getContext(): Context {
        return this@MainActivity
    }

    override fun getChartHolderViewGroup(): ViewGroup? {
        // return any regular ViewGroup (LinearLayout, FrameLayout, etc)
        // where you want the chart to appear

        return parentLayout
    }

    override fun getTitle(): String {
        return "This is title of the chart"
    }

    override fun getIsTitleVisible(): Boolean {
        // Optionally, in case you have a different view
        // for the chart title, you can hide it

        return true
    }

    override fun getColors(): ArrayList<String> {
        // Hex code of the list of colors, applied cyclically

        return arrayListOf("#ffa726", "#2196f3")
    }

    override fun getDecimalFormatPattern(): String {
        // How should the values above the bars be visible? You can specify the format here

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
        // The Y-Axis values. Note that when supplied more than 1 item in the arraylist,
        // the chart automatically gets converted into a group-bar chart

        return arrayListOf(
            YLeftItem("Legend 1", arrayListOf(5f, 5.5f, 3f, 4f))
        )
    }

    override fun onNoValueSelected() {
        // you can optionally perform some action when the user selects outside any bar
    }

    override fun onValueSelected(e: Entry?) {
        // you can optionally perform some action when the user selects any bar
    }

})

```

Expected JSONObject of a chart item from the server to the client is:

```json
{
  "title": "Student records",
  "decimalFormatPattern": "0.0",
  "xLabel": "Projects",
  "xLabels": [
    "Narendra",
    "Rahul",
    "Prakash",
    "Sonia",
    "Lalu"
  ],
  "yLeftLabel": "Progress Value",
  "yLeftItems": [
    {
      "legendLabel": "Scope",
      "values": [
        24,
        12,
        13,
        12,
        17
      ]
    },
    {
      "legendLabel": "Taken up",
      "values": [
        13,
        5,
        2,
        9,
        5
      ]
    }
  ],
  "colors": [
    "#2196f3",
    "#ff9800"
  ]
}
```

Use ChartItem class (provided by this lib) for creating a chartItem object from JSON.

## Changelog

#### v.0.1.5

+ add extra padding to the left of y-left-label so that it doesn't appear at too much corner of the whole view
+ removed warnings from the project

If you are already using this library, and you decide to upgrade, the change required from your end would be:
+ None

---

#### v.0.1.4

+ change getContext to getActivity to avoid confusion when used in the activity
+ add getIsAnimationRequired interface method to make it easy to animate/ display a chart directly
+ bump up dependency library versions

If you are already using this library, and you decide to upgrade, the change required from your end would be:
+ You will be required to implement getIsAnimationRequired interface method, and return a boolean value
+ You will be required to remove getContext and include getActivity interface method instead, while supplying activity context insted of app context
