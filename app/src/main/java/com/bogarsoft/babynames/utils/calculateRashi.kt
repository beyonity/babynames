package com.bogarsoft.babynames.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.*

// Function to calculate the Rashi (Zodiac Sign)

class Astrology {


    fun calculateRashi(nirayanaLongitude: Double): String {
        val rashiDegrees =
            arrayOf(0.0, 30.0, 60.0, 90.0, 120.0, 150.0, 180.0, 210.0, 240.0, 270.0, 300.0, 330.0)
        val rashiNames = arrayOf(
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Virgo",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces"
        )

        var rashi = ""
        for (i in rashiDegrees.indices) {
            if (nirayanaLongitude < rashiDegrees[i]) {
                rashi = rashiNames[i]
                break
            }
        }
        return rashi
    }

    // Function to calculate the Nakshatra (Lunar Mansion)
    fun calculateNakshatra(nirayanaLongitude: Double): String {
        val nakshatraDegrees = arrayOf(
            0.0,
            13.333,
            26.667,
            40.0,
            53.333,
            66.667,
            80.0,
            93.333,
            106.667,
            120.0,
            133.333,
            146.667,
            160.0
        )
        val nakshatraNames = arrayOf(
            "Ashwini",
            "Bharani",
            "Krittika",
            "Rohini",
            "Mrigashirsha",
            "Ardra",
            "Punarvasu",
            "Pushya",
            "Ashlesha",
            "Magha",
            "Purva Phalguni",
            "Uttara Phalguni",
            "Hasta"
        )

        var nakshatra = ""
        for (i in nakshatraDegrees.indices) {
            if (nirayanaLongitude < nakshatraDegrees[i]) {
                nakshatra = nakshatraNames[i]
                break
            }
        }
        return nakshatra
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculate(
        days: Int,
        month: Int,
        year: Int,
        hour: Int,
        minute: Int,
        second: Int
    ): String {
        val birthDate = LocalDate.of(1990, 3, 11)
        val birthTime = LocalTime.of(14, 30)
        val birthLocation = ZoneId.of("Asia/Kolkata")
        val longitude = 77.5948 // Longitude of the birth location in degrees

        // Convert birth date and time to ZonedDateTime with the birth location's time zone
        val birthDateTime = ZonedDateTime.of(birthDate, birthTime, birthLocation)

        // Get the Julian Day Number (JDN) for the birth date
        val jdn = ChronoUnit.DAYS.between(LocalDate.of(-4712, 1, 1), birthDate).toDouble()

        // Calculate the Sidereal Time at local noon adjusted by the longitude of the birth location
        val siderealTime = 280.46 + 0.9856474 * jdn + birthDateTime.offset.totalSeconds / 86400.0 * 360.0

        // Get the Ayanamsa value for the birth date
        val ayanamsa = 23.0 // Example value (needs to be determined using appropriate methods)

        // Calculate the Nirayana Longitude of the Moon
        val moonLongitude = (siderealTime - ayanamsa + longitude + 360) % 360

        // Calculate the Rashi and Nakshatra
        val rashi = calculateRashi(moonLongitude)
        val nakshatra = calculateNakshatra(moonLongitude)

        println(rashi + " " + nakshatra)
        return rashi + " " + nakshatra
    }

}
