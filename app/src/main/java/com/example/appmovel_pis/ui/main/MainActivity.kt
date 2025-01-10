import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val welcomePageFragment = WelcomePageFragment()
            supportFragmentManager.commit {
                replace(R.id.menuFragmentContainer, welcomePageFragment)
            }
        }

        // Example for fragment communication:
        sharedViewModel.email.observe(this) { email ->
            // Perform actions based on email changes
        }
    }
}