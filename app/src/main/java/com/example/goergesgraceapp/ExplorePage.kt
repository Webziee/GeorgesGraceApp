package com.example.goergesgraceapp

import Bookings
import BookingsCardAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class ExplorePage : AppCompatActivity(), OnMapReadyCallback {

    // Google Maps
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    // Room bookings
    private lateinit var availablerooms: Button
    private lateinit var allrooms: Button
    private lateinit var bookingsrecyclerView: RecyclerView
    private lateinit var bookingsAdapter: BookingsCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explore_page)

        // Find the Google map view
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Set up RecyclerView
        bookingsrecyclerView = findViewById(R.id.BookingsRecyclerView)
        bookingsrecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize BookingsAdapter
        bookingsAdapter = BookingsCardAdapter(emptyList()) // Start with an empty list
        bookingsrecyclerView.adapter = bookingsAdapter

        // Fetch bookings data from Supabase
        fetchBookings()

        availablerooms = findViewById(R.id.availableRooms)
        allrooms = findViewById(R.id.allRooms)

        availablerooms.setOnClickListener {
            Toast.makeText(this, "Available rooms Button Clicked", Toast.LENGTH_SHORT).show()
        }

        allrooms.setOnClickListener {
            Toast.makeText(this, "All rooms Button Clicked", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Fetch bookings from Supabase
    private fun fetchBookings() {
        SupabaseClient.api.getBookings(
            apiKey = SupabaseClient.getApiKey(),
            authToken = "Bearer ${SupabaseClient.getApiKey()}"
        ).enqueue(object : Callback<List<Bookings>> {
            override fun onResponse(call: Call<List<Bookings>>, response: Response<List<Bookings>>) {
                if (response.isSuccessful) {
                    val bookingsList = response.body() ?: emptyList()
                    Log.d("SupabaseResponse", "Bookings List: $bookingsList")
                    bookingsAdapter.updateData(bookingsList) // Update the RecyclerView with the data
                } else {
                    Log.e("SupabaseError", "API call failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Bookings>>, t: Throwable) {
                Log.e("SupabaseError", "API call failed: ${t.message}")
            }
        })
    }

    fun showBottomSheet(booking: Bookings) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_slide_up, null)

        // Find views inside the bottom sheet layout
        val bottomSheetunit = bottomSheetView.findViewById<TextView>(R.id.BottomSheetUnit)
        val bottomSheetprice = bottomSheetView.findViewById<TextView>(R.id.BottomSheetPrice)
        val bottomSheetsleeper = bottomSheetView.findViewById<TextView>(R.id.BottomSheetSleeper)
        val bottomSheetImage = bottomSheetView.findViewById<ImageView>(R.id.BottomSheetImage)
        val bookNowButton = bottomSheetView.findViewById<Button>(R.id.bookingButton)

        // Set data for the specific booking item
        bottomSheetunit.text = "Unit ${booking.unitNumber}"
        bottomSheetprice.text = "R${booking.price} (off peak)\nper night"
        bottomSheetsleeper.text = "${booking.sleeper} Sleeper"

        // Load the image (if there is one) using Picasso
        if (!booking.unitImages.isNullOrEmpty()) {
            Picasso.get().load(booking.unitImages).into(bottomSheetImage)
        } else {
            bottomSheetImage.setImageResource(R.drawable.placeholderimage)
        }

        bookNowButton.setOnClickListener {
            Log.d("BookNowButton", "Book Now button clicked")

            // Start the next activity
            val intent = Intent(this@ExplorePage, Selected_Card::class.java).apply {
                putExtra("unitNumber", booking.unitNumber)
                putExtra("price", booking.price)
                putExtra("sleeper", booking.sleeper)
                putExtra("imageUrl", booking.unitImages)
            }
            startActivity(intent)
        }
            bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    // Google Maps
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set the location of the marker
        val belaBela = LatLng(-24.8850, 28.2950) // Coordinates of Wambaths, Bela-Bela
        googleMap.addMarker(MarkerOptions().position(belaBela).title("Wambaths, Bela-Bela"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belaBela, 10f)) // Zoom level
    }

    // Lifecycle methods for mapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
