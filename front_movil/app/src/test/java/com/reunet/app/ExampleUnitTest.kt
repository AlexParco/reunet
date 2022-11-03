package com.reunet.app

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_format_date(){
        val string_date = "12-12-2012"
        val format = SimpleDateFormat("dd-MM-yyyy")
        val date:Date = format.parse(string_date) as Date
        val millis: Long = date.time
        val d = Date(millis)
        val f: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'")
        val dateClose = f.format(d)
        println(dateClose)
        println(d)
    }

    @Test
    fun test_format_date_2() {
        val formatter = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault())
        val dateClose = formatter.parse("2022-11-10") as Date
        println(dateClose)
    }
}