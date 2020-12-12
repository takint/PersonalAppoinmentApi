package specialtopic.ngoctinle.personalappointment

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceInterface {

    @GET("/appointments")
    suspend fun getAppointmentData(): Response<List<Appointment>>

    @GET("/appointments/{appId}")
    suspend fun getAppointmentById(@Path("appId") appId: Int): Response<Appointment>

    @Headers("Content-Type: application/json")
    @POST("/appointments/add")
    fun addAppointment(@Body rip: Appointment): Call<Appointment>

    @Headers("Content-Type: application/json")
    @PUT("/appointments/edit")
    fun updateAppointment(@Body rip: Appointment): Call<Appointment>

    @DELETE("/appointments/delete/{appId}")
    fun deleteAppointment(@Path("appId") appId: Int): Call<Void?>

    @Headers("Content-Type: application/json")
    @POST("/appointments/search")
    suspend fun searchAppointment(@Body searchTerm: SearchRequest): Response<List<Appointment>>
}