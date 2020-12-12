package specialtopic.ngoctinle.personalappointment

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class ApiServices(private val app: Application) {
    private val appointmentData = MutableLiveData<List<Appointment>>()
    private val appointment = MutableLiveData<Appointment>()

    fun getAppointmentData(): MutableLiveData<List<Appointment>> {
        return appointmentData
    }

    fun getAppointment(): MutableLiveData<Appointment> {
        return appointment
    }

    @WorkerThread
    suspend fun getAppointmentsFromService() {
        try {
            if (NetworkHelper.isNetworkConnected(app)) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                val service = retrofit.create(ApiServiceInterface::class.java)
                val serviceData = service.getAppointmentData().body() ?: emptyList()

                appointmentData.postValue(serviceData)
            }
        } catch (ex: Exception) {
            Log.d("error", ex.message.toString())
        }
    }

    @WorkerThread
    suspend fun getAppointmentById(appId: Int) {
        try {
            if (NetworkHelper.isNetworkConnected(app)) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                val service = retrofit.create(ApiServiceInterface::class.java)
                val serviceData = service.getAppointmentById(appId).body() ?: Appointment()

                appointment.postValue(serviceData)
            }
        } catch (ex: Exception) {
            Log.d("error", ex.message.toString())
        }
    }

    @WorkerThread
    fun searchAppointment(searchTerm: SearchRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (NetworkHelper.isNetworkConnected(app)) {
                    val moshi = Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .build()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .build()
                    val service = retrofit.create(ApiServiceInterface::class.java)
                    val appResults = service.searchAppointment(searchTerm).body() ?: emptyList()

                    appointmentData.postValue(appResults)
                }
            } catch (ex: Exception) {
                Log.d("error", ex.message.toString())
            }
        }
    }

    @WorkerThread
    fun createAppointment(newApp: Appointment) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (NetworkHelper.isNetworkConnected(app)) {
                    val moshi = Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .build()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .build()
                    val service = retrofit.create(ApiServiceInterface::class.java)
                    val newAppointmentCall = service.addAppointment(newApp)

                    newAppointmentCall.await()
                }
            } catch (ex: Exception) {
                Log.d("error", ex.message.toString())
            }
        }
    }

    @WorkerThread
    fun updateAppointment(updatedAppointment: Appointment) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (NetworkHelper.isNetworkConnected(app)) {
                    val moshi = Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .build()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .build()
                    val service = retrofit.create(ApiServiceInterface::class.java)
                    val updateAppointmentCall = service.updateAppointment(updatedAppointment)

                    updateAppointmentCall.await()
                }
            } catch (ex: Exception) {
                Log.d("error", ex.message.toString())
            }
        }
    }

    @WorkerThread
    fun deleteAppointment(appId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (NetworkHelper.isNetworkConnected(app)) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(NetworkHelper.API_ENDPOINT_URL)
                        .build()
                    val service = retrofit.create(ApiServiceInterface::class.java)
                    val deleteAppointmentCall = service.deleteAppointment(appId)

                    deleteAppointmentCall.await()
                }
            } catch (ex: Exception) {
                Log.d("error", ex.message.toString())
            }

        }
    }
}