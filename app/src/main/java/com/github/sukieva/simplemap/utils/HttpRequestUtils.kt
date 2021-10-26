package com.github.sukieva.simplemap.utils

import java.io.IOException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.HashMap


object HttpRequestUtils {
    /**
     * get请求
     *
     * @param url
     * @param param
     * @return
     */
    operator fun get(url: String, param: Map<String?, Any>): String {
        val builder = StringBuilder()
        try {
            val params = StringBuilder()
            for ((key, value) in param) {
                params.append(key)
                params.append("=")
                params.append(value.toString())
                params.append("&")
            }
            if (params.isNotEmpty()) {
                params.deleteCharAt(params.lastIndexOf("&"))
            }
            val restServiceURL = URL(url + if (params.isNotEmpty()) "?$params" else "")
            val httpConnection = restServiceURL.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "GET"
            httpConnection.setRequestProperty("Accept", "application/json")
            if (httpConnection.responseCode != 200) {
                throw RuntimeException(
                    "HTTP GET Request Failed with Error code : " + httpConnection.responseCode
                )
            }
            val inStrm = httpConnection.inputStream
            val b = ByteArray(1024)
            var length: Int
            while (inStrm.read(b).also { length = it } != -1) {
                builder.append(String(b, 0, length))
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return builder.toString()
    }

    /**
     * post 请求
     *
     * @param url
     * @param param
     * @return
     */
    fun post(url: String, param: Map<String?, Any>): String {
        val builder = StringBuilder()
        try {
            val params = StringBuilder()
            for ((key, value) in param) {
                params.append(key)
                params.append("=")
                params.append(value.toString())
                params.append("&")
            }
            if (params.isNotEmpty()) {
                params.deleteCharAt(params.lastIndexOf("&"))
            }
            val restServiceURL = URL(url + if (params.isNotEmpty()) "?$params" else "")
            val httpConnection = restServiceURL.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.setRequestProperty("Accept", "application/json")
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpConnection.doInput = true
            // Post 请求不能使用缓存
            httpConnection.useCaches = false
            if (httpConnection.responseCode != 200) {
                throw RuntimeException(
                    "HTTP POST Request Failed with Error code : " + httpConnection.responseCode
                )
            }
            val inStrm = httpConnection.inputStream
            val b = ByteArray(1024)
            var length: Int
            while (inStrm.read(b).also { length = it } != -1) {
                builder.append(String(b, 0, length))
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return builder.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val urlString = "http://api.map.baidu.com/geocoder"
        val param: MutableMap<String?, Any> = HashMap()
        param["address"] = "盐城"
        param["output"] = "json"
        param["key"] = "aLL6IMYUCn4Gxyn5N8FS8lUX8CTsQvKg"
        //HttpRequestUtils.get(urlString, param);
        val result = HttpRequestUtils[urlString, param]
        println(result)
    }
}
