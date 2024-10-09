import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goergesgraceapp.R
import com.squareup.picasso.Picasso

class BookingsCardAdapter(private var bookingsList: List<Bookings>) : RecyclerView.Adapter<BookingsCardAdapter.BookingsViewHolder>() {

    inner class BookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.UnitImage)
        val unitNumber: TextView = itemView.findViewById(R.id.UnitNumber)
        val unitPrice: TextView = itemView.findViewById(R.id.UnitPrice)
        val unitSleepers: TextView = itemView.findViewById(R.id.UnitSleepers)
        val unitAvailability: TextView = itemView.findViewById(R.id.UnitAvailability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_cards, parent, false)
        return BookingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {
        val currentItem = bookingsList[position]

        // Bind data to the views
        if (!currentItem.unitImages.isNullOrEmpty()) {
            Picasso.get().load(currentItem.unitImages).into(holder.unitImage)
        } else {
            Picasso.get().load(R.drawable.placeholderimage).into(holder.unitImage)
        }

        holder.unitNumber.text = "Unit ${currentItem.unitNumber}"
        holder.unitPrice.text = "R${currentItem.price} (off peak)"
        holder.unitSleepers.text = "${currentItem.sleeper} Sleepers"
        holder.unitAvailability.text = currentItem.status
    }

    override fun getItemCount(): Int {
        return bookingsList.size
    }

    // This function will update the list and notify the adapter
    fun updateData(newBookingsList: List<Bookings>) {
        bookingsList = newBookingsList
        notifyDataSetChanged()  // Notify the RecyclerView to refresh
    }
}
