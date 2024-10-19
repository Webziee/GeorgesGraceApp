import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.goergesgraceapp.BookedAndPaidRoom
import com.example.goergesgraceapp.ExplorePage
import com.example.goergesgraceapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_menu_navigation)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_explore -> {
                    val intent = Intent(this, ExplorePage::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_bookings -> {
                    val intent = Intent(this, BookedAndPaidRoom::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.navigation_profile -> {
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
                else -> false
            }
        }
    }
}
