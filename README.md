# AirChart
[![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Setup

Add this line in your `root build.gradle` at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' } // add this line
  }
}
  ```
Add this line in your `app build.gradle`:
```gradle
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
dependencies {
  implementation 'com.github.mumayank:AirChartProject:LATEST_VERSION' // add this line
}
```
where LATEST_VERSION is [![](https://jitpack.io/v/mumayank/AirChartProject.svg)](https://jitpack.io/#mumayank/AirChartProject)

## Usage

In your activity, initialize the library instance before loading data (i.e., before making any API call). This will load the view with beautiful progress UI
```
val airChart = AirChart(this, parentLayout)
```

Lastly, when your data is ready, you can call this method to load the bar chart:
```
airChart.bar(object: AirChart.BarInterface {

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

  override fun getYLefts(): java.util.ArrayList<YLeftItem> {
      return arrayListOf(
          YLeftItem("Legend 1", arrayListOf(5f, 5.5f, 3f, 4f)),
          YLeftItem("Legend 2", arrayListOf(5f, 5.5f, 3f, 4f)),
          YLeftItem("Legend 3", arrayListOf(5f, 5.5f, 3f, 4f))
      )
  }

  /**
   * Additional optional functions that can be overridden on demand:
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

```
#### Note
Note that you can init and use the library in one shot also like:
```
AirChart(this, MainViewModel::class.java, parentLayout).bar(...)
```

Expected JSONObject of a chart item from the server to the client is:

```json
{
  "title": "Student records",
  "subTitle": "Subtitle",
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
  "valueItems": [
    {
      "legendLabel": "Scope",
      "valueItems": [
        24,
        12,
        13,
        12,
        17
      ]
    },
    {
      "legendLabel": "Taken up",
      "valueItems": [
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
  ],
  "additionalDatumItems": [
    {
      "key": "Key1",
      "value": "Value1"
    },
    {
      "key": "Key2",
      "value2": "Value2"
    }
  ],
  "isAnimationRequired": true
}
```

To create Java class object from this JSON, ChartItem, YLeftItem, and AdditionalData classes have been included in this lib.

## Changelog

#### v.0.3
+ updated the underlying library AirCoroutine. This helps in removing the need to manually create your own view model for doing background tasks.

If you are already using this library, and you decide to upgrade, the change required from your end would be:
+ Remove your activity's `ViewModel`
+ Remove activity's view model as a param of AirChart lib's constructor

#### v.0.2.1

Majore update!
+ The chart handles progress bar on its own by splitting initialization of the view and loading of the chart
+ Additional interface methods are not inflated by default now. They can be overridden by the developer on demand
+ Added option to add subtitle text, additional info key-value pairs, and a custom view
+ Uses the power of ViewModels to lazy load the bar to drastically improve performance

If you are already using this library, and you decide to upgrade, the change required from your end would be:
+ Update `app build.gradle`
+ Create your activity's `ViewModel`
+ Change the usage of the library 
---

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
