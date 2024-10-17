package com.example.goergesgraceapp

import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class Selected_Card : AppCompatActivity() {

    private var startDate: Long? = null
    private var endDate: Long? = null
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val pricePerNight = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_card)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val selectedDatesTextView = findViewById<TextView>(R.id.selectedDates)
        val numberOfDaysTextView = findViewById<TextView>(R.id.numberOfDays)
        val totalPriceTextView = findViewById<TextView>(R.id.totalPrice)

        // Set minimum and maximum dates if needed
        calendarView.minDate = System.currentTimeMillis() // current date as min date

        // Listener for date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis

            if (startDate == null) {
                startDate = selectedDate
                Toast.makeText(this, "${dateFormatter.format(selectedDate)}", Toast.LENGTH_SHORT).show()

                // Update the selected dates text
                selectedDatesTextView.text = "${dateFormatter.format(selectedDate)}"
            } else if (endDate == null && selectedDate > startDate!!) {
                // Select end date if it’s after the start date
                endDate = selectedDate
                val numDays = TimeUnit.MILLISECONDS.toDays(endDate!! - startDate!!) - 1

                // Calculate the total price
                val totalPrice = numDays * pricePerNight

                // Display the selected range in the TextView
                selectedDatesTextView.text = "${dateFormatter.format(startDate!!)} to ${dateFormatter.format(endDate!!)}"
                numberOfDaysTextView.text = "$numDays"
                totalPriceTextView.text = "R$totalPrice"

                // Reset selection after picking a range
                startDate = null
                endDate = null
            } else {
                // If user selects invalid end date or reselects start date, reset
                Toast.makeText(this, "Invalid end date selected. Try again.", Toast.LENGTH_SHORT).show()
                startDate = selectedDate
                endDate = null
                selectedDatesTextView.text = "${dateFormatter.format(selectedDate)}"
                numberOfDaysTextView.text = ""
                totalPriceTextView.text = ""
            }
        }


        // Retrieve data passed from the previous activity/fragment
        val unitNumber = intent.getStringExtra("unitNumber")
        val price = intent.getIntExtra("price", 0)
        val sleeper = intent.getIntExtra("sleeper", 0)
        val imageUrl = intent.getStringExtra("imageUrl")
        // Find views in the layout
        val unitNumberTextView = findViewById<TextView>(R.id.SelectedSheetUnit)
        val priceTextView = findViewById<TextView>(R.id.SelectedSheetPrice)
        val sleeperTextView = findViewById<TextView>(R.id.SelectedSheetSleeper)
        val unitImageView = findViewById<ImageView>(R.id.SelectedSheetImage)

        // Set data to the views
        unitNumberTextView.text = "Unit $unitNumber"
        priceTextView.text = "Price R$price\nper night"
        sleeperTextView.text = "Sleeper $sleeper"

        // Debug: Check for nulls and log appropriately
        if (unitNumber != null) {
            Log.d("SelectedCard", "Unit Number received: $unitNumber")
        } else {
            Log.e("SelectedCard", "Unit Number is null")
        }

        // Load image using Picasso, add a placeholder for safety
        Picasso.get().load(imageUrl).placeholder(R.drawable.placeholderimage).into(unitImageView)

        // Apply window insets if necessary
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
