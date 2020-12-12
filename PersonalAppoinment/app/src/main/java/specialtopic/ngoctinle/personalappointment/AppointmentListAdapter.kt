package specialtopic.ngoctinle.personalappointment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppointmentListAdapter(
    private val appointments: ArrayList<Appointment>,
    private val appService: ApiServices
) :
    RecyclerView.Adapter<AppointmentListAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnEdit: TextView = itemView.findViewById(R.id.btnEdit)
        val btnDelete: TextView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.appointment_item, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appID = appointments[position].id

        holder.tvTitle.text = appointments[position].title
        holder.tvDateTime.text = String.format(
            "%s - %s",
            appointments[position].displayStartTime,
            appointments[position].displayEndTime
        )
        holder.tvDescription.text = appointments[position].description
        holder.tvLocation.text = appointments[position].location

        holder.itemView.setOnClickListener {
            onEditClick(holder, appID)
        }

        holder.btnEdit.setOnClickListener {
            onEditClick(holder, appID)
        }

        holder.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                appService.deleteAppointment(appID)
            }
            appointments.removeAt(position)
            notifyDataSetChanged()
        }
    }

    private fun onEditClick(holder: ViewHolder, appID: Int) {
        val intent = Intent(holder.itemView.context, AppointmentActivity::class.java)
        intent.putExtra(NetworkHelper.APP_ID, appID)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }
}