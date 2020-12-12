package specialtopic.ngoctinle.personalappointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var listAppointments = ArrayList<Appointment>()
    private lateinit var rvAppointments: RecyclerView
    private lateinit var adapter: AppointmentListAdapter
    private lateinit var appointmentService: ApiServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvAppointments = findViewById(R.id.lstAppointment)
        rvAppointments.layoutManager = LinearLayoutManager(this)

        // Init configuration from start
        NetworkHelper.getConfiguration(applicationContext);
        appointmentService = ApiServices(application)
        appointmentService.getAppointmentData().observe(this) {
            listAppointments = ArrayList(it)
            adapter = AppointmentListAdapter(listAppointments)
            rvAppointments.adapter = adapter
        }

        CoroutineScope(Dispatchers.IO).launch {
            appointmentService.getAppointmentsFromService()
        }
    }

    fun onAddNewClick(view: View) {
        val intent = Intent(this, AppointmentActivity::class.java)
        startActivity(intent)
    }

    fun onSearchClick(view: View) {

    }
}