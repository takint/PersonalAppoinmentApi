package specialtopic.ngoctinle.personalappointment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentActivity : AppCompatActivity() {

    private lateinit var appointmentService: ApiServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val appId = intent.getIntExtra(NetworkHelper.APP_ID, 0)
        appointmentService = ApiServices(application)

        appointmentService.getAppointment().observe(this, {
            if (it != null) {
                etTitle.setText(it.title)
                etDescription.setText(it.description)
                etLocation.setText(it.location)
                chbAllDay.isChecked = it.isAllDay
                etDetailStartDate.setText(it.displayStartTime)
                etDetailEndDate.setText(it.displayEndTime)
            }
        })

        if (appId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                appointmentService.getAppointmentById(appId)
            }
        }
    }

    fun onSaveClick(view: View) {

    }

    fun onCancelClick(view: View) {
        finish()
    }
}