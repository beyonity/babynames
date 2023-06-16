package com.bogarsoft.babynames.utils;

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

class AstrologyPage {
    private var birthDate: LocalDate? = null
    private var birthTime: LocalTime? = null
    private var location: String? = null
    private var rasi: String? = null
    private var nakshatra: String? = null
    private var lagnam: String? = null
    private var startingLetter: String? = null

    private val rasiNames = arrayOf(
        "Mesha (Aries)",
        "Vrishabha (Taurus)",
        "Mithuna (Gemini)",
        "Karka (Cancer)",
        "Simha (Leo)",
        "Kanya (Virgo)",
        "Tula (Libra)",
        "Vrishchika (Scorpio)",
        "Dhanu (Sagittarius)",
        "Makara (Capricorn)",
        "Kumbha (Aquarius)",
        "Meena (Pisces)"
    )

    private val nakshatraNames = arrayOf(
        "Ashwini",
        "Bharani",
        "Krittika",
        "Rohini",
        "Mrigashira",
        "Ardra",
        "Punarvasu",
        "Pushya",
        "Ashlesha",
        "Magha",
        "Purva Phalguni",
        "Uttara Phalguni",
        "Hasta",
        "Chitra",
        "Swati",
        "Vishakha",
        "Anuradha",
        "Jyeshtha",
        "Mula",
        "Purva Ashadha",
        "Uttara Ashadha",
        "Shravana",
        "Dhanishta",
        "Shatabhisha",
        "Purva Bhadrapada",
        "Uttara Bhadrapada",
        "Revati"
    )

    private val nakshatraLetters = arrayOf(
        arrayOf("Chu", "Che", "Cho", "La"),       // Ashwini
        arrayOf("Lee", "Lu", "Le", "Lo"),         // Bharani
        arrayOf("A", "Ee", "U", "Ea"),            // Krittika
        arrayOf("O", "Va", "Vee", "Vu"),          // Rohini
        arrayOf("Ve", "Vo", "Ka", "Ke"),          // Mrigashira
        arrayOf("Ku", "Gha", "Ng", "Na"),         // Ardra
        arrayOf("Ke", "Ko", "Ha", "Hi"),          // Punarvasu
        arrayOf("Hu", "He", "Ho", "Da"),          // Pushya
        arrayOf("De", "Du", "De", "Do"),          // Ashlesha
        arrayOf("Ma", "Me", "Mu", "Me"),          // Magha
        arrayOf("Mo", "Ta", "Tee", "Too"),        // Purva Phalguni
        arrayOf("Te", "To", "Pa", "Pee"),         // Uttara Phalguni
        arrayOf("Pu", "Sha", "Na", "Tha"),        // Hasta
        arrayOf("Pe", "Po", "Ra", "Ree"),         // Chitra
        arrayOf("Ru", "Re", "Ro", "Taa"),         // Swati
        arrayOf("Tee", "Too", "Te", "To"),        // Vishakha
        arrayOf("Na", "Ni", "Nu", "Ne"),          // Anuradha
        arrayOf("No", "Ya", "Yi", "U"),           // Jyeshtha
        arrayOf("Ye", "Yo", "Ba", "Be"),          // Mula
        arrayOf("Bhu", "Dha", "Pha", "Da"),       // Purva Ashadha
        arrayOf("Be", "Bo", "Ja", "Ji"),          // Uttara Ashadha
        arrayOf("Ju", "Je", "Jo", "Gha"),         // Shravana
        arrayOf("Ga", "Gi", "Gu", "Ge"),          // Dhanishta
        arrayOf("Go", "Sa", "Si", "Su"),          // Shatabhisha
        arrayOf("Se", "So", "Dha", "Di"),         // Purva Bhadrapada
        arrayOf("Du", "Tha", "Jha", "Da"),        // Uttara Bhadrapada
        arrayOf("De", "Do", "Cha", "Chi")         // Revati
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateMoonLongitude(date: LocalDate): Double {
        // Calculate the Julian Day (JD) for the given date
        val jd = date.toEpochDay() + 2440587.5

        // Calculate the Julian Century
        val julianCentury = (jd - 2451545) / 36525.0

        // Calculate the Moon's Longitude
        var moonLongitude = 13.1763966 * julianCentury + 133.1734233

        // Adjust the longitude to be within the range of 0 to 360 degrees
        if (moonLongitude < 0) {
            moonLongitude += 360
        } else if (moonLongitude >= 360) {
            moonLongitude -= 360
        }

        return moonLongitude
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleSubmit() {
        // Perform calculations based on birth date, time, and location
        // Replace the below code with your actual calculations
        // Example calculations for Rasi and Nakshatra
        val currentDate = LocalDate.now()
        val moonLongitude = calculateMoonLongitude(birthDate!!)
        val rasi = (moonLongitude / 30).toInt() + 1
        val nakshatra = (moonLongitude / 13.333).toInt() + 1

        // Example code to determine the starting letter based on Nakshatra
        val startingLetter: String = nakshatraLetters[nakshatra - 1].joinToString(", ")

        this.rasi = rasiNames[rasi - 1]
        this.nakshatra = nakshatraNames[nakshatra - 1]
        this.startingLetter = startingLetter
    }

    fun handleChartClick() {
        // Handle the click event for the detailed chart button
        // Add your code here to navigate to the detailed chart page or perform any other action
    }

    fun render() {
        // Render the AstrologyPage component
        println("Birth Date: $birthDate")
        println("Birth Time: $birthTime")
        println("Location: $location")

        if (rasi != null && nakshatra != null) {
            println("Rasi: $rasi")
            println("Nakshatra: $nakshatra")
            println("Lagnam: $lagnam")
            println("Names Starting Letter: $startingLetter")
        }
    }
}
/*fun main() {
    val page = AstrologyPage()
    page.birthDate = LocalDate.of(1990, 1, 1)
    page.birthTime = LocalTime.of(12, 0).toString()
    page.location = "Sample Location"
    page.handleSubmit()
    page.render()
}*/
