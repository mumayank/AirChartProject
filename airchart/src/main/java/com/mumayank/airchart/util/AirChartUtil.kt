package com.mumayank.airchart.util

class AirChartUtil {

    companion object {

        const val ANIMATION_TIME = 500

        fun <T> getItemFromArrayAtIndexCyclically(arrayList: ArrayList<T>, index: Int): T {
            return if (index > arrayList.size - 1) {
                val newIndex = index - arrayList.size
                getItemFromArrayAtIndexCyclically(arrayList, newIndex)
            } else {
                arrayList[index]
            }
        }

    }

}