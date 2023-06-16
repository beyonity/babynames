package com.bogarsoft.babynames.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.databinding.FragmentRashiCalculatorBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RasiCalculatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RasiCalculatorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
   private lateinit var binding: FragmentRashiCalculatorBinding
    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()
    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var txtSelectedDate: TextView
    private lateinit var txtSelectedTime: TextView
    private lateinit var txtRashi: TextView
    private lateinit var txtNakshatra: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRashiCalculatorBinding.inflate(inflater, container, false)
        btnSelectDate = binding.btnSelectDate
        btnSelectTime = binding.btnSelectTime
        txtSelectedDate = binding.txtSelectedDate
        txtSelectedTime = binding.txtSelectedTime
        txtRashi = binding.txtRashi
        txtNakshatra = binding.txtNakshatra

        btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        btnSelectTime.setOnClickListener {
            showTimePickerDialog()
        }


        binding.btnCalculate.setOnClickListener {
            calculateRashiAndNakshatra()
        }
        return binding.root
    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                selectedDate.set(year, month, day)
                txtSelectedDate.text = "$day/${month + 1}/$year"
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                txtSelectedTime.text = String.format("%02d:%02d", hourOfDay, minute)
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun calculateRashiAndNakshatra() {
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)
        val month = selectedDate.get(Calendar.MONTH) + 1
        val year = selectedDate.get(Calendar.YEAR)
        val hour = selectedTime.get(Calendar.HOUR_OF_DAY)
        val minute = selectedTime.get(Calendar.MINUTE)

        val rashi = getRashi(day, month)
        val nakshatra = getNakshatra(day, month, year, hour, minute)

        txtRashi.text = rashi
        txtNakshatra.text = nakshatra
    }

    private fun getRashi(day: Int, month: Int): String {
        val zodiacSigns = arrayOf(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra",
            "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
        )
        val lastDays = arrayOf(14, 13, 14, 15, 16, 17, 16, 15, 15, 14, 13, 14)

        return if (day > lastDays[month - 1]) {
            zodiacSigns[month % 12]
        } else {
            zodiacSigns[(month - 1) % 12]
        }
    }

    private fun getNakshatra(day: Int, month: Int, year: Int, hour: Int, minute: Int): String {
        val nakshatras = arrayOf(
            "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Ardra",
            "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
            "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha",
            "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada",
            "Uttara Bhadrapada", "Revati"
        )

        val monthArr = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

        val totalDays = monthArr[month - 1] + day
        val leapYearDays = getLeapYearDays(year, month)
        val totalDaysWithLeapYear = totalDays + leapYearDays

        val nakshatraIndex = (totalDaysWithLeapYear * 3.333333333).toInt() % 27

        return nakshatras[nakshatraIndex]
    }

    private fun getLeapYearDays(year: Int, month: Int): Int {
        var leapYears = 0
        for (i in 1900 until year) {
            if (isLeapYear(i)) {
                leapYears++
            }
        }

        if (isLeapYear(year) && month <= 2) {
            leapYears--
        }

        return leapYears
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PageOneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RasiCalculatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}