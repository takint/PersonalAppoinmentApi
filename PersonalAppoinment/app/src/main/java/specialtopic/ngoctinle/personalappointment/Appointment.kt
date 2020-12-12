package specialtopic.ngoctinle.personalappointment

import com.squareup.moshi.Json

data class Appointment(
    @field:Json(name = "_id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "isAllDay") val isAllDay: Boolean,
    @field:Json(name = "location") val location: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "displayStartTime") val displayStartTime: String,
    @field:Json(name = "displayEndTime") val displayEndTime: String
) {
    constructor() : this(0, "Not Found", false, "N/A", "N/A", "N/A", "N/A") {
    }
}