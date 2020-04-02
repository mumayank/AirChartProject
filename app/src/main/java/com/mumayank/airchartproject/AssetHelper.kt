package com.mumayank.airchartproject

import android.app.Activity
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetHelper {
    companion object {

        private fun readFile(fileName: String, activity: Activity, onSuccess:(string: String)->Unit, onFailure:()->Unit) {
            var bufferedReader: BufferedReader? = null
            try {
                bufferedReader = BufferedReader(InputStreamReader(activity.assets.open(fileName)))
                var string = ""
                var line = bufferedReader.readLine()
                while (line != null) {
                    string += line
                    line = bufferedReader.readLine()
                }
                onSuccess.invoke(string)
            } catch (e: Exception) {
                // ignore
                onFailure.invoke()
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }

        private fun readFileWithDelay(fileName: String, activity: Activity, onSuccess:(string: String)->Unit, onFailure:()->Unit) {
            // Thread.sleep(500)
            readFile(
                fileName,
                activity,
                onSuccess,
                onFailure
            )
        }

        fun readFile(activity: Activity?, fileName: String, onContentReceived: (string: String)->Unit) {
            if (activity == null) {
                return
            }

            readFileWithDelay(
                fileName,
                activity,
                fun(string: String) {
                    onContentReceived.invoke(string)
                },
                fun() {

                }
            )
        }

    }
}