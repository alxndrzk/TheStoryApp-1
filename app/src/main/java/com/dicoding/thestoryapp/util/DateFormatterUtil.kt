package com.dicoding.thestoryapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun changeFormatDate(date: String): String {
    var orderDate: Date? = null
    var date1 = ""
    var date2 = ""
    var dateString = ""
    val simpleDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val dateFormat1 = SimpleDateFormat("dd MMM yyyy")
    val dateFormat2 = SimpleDateFormat("HH:mm")
    try {
        orderDate = simpleDate.parse(date)
        date1 = dateFormat1.format(orderDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    try {
        orderDate = simpleDate.parse(date)
        date2 = dateFormat2.format(orderDate)
        dateString = "$date1  |  $date2"
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateString
}