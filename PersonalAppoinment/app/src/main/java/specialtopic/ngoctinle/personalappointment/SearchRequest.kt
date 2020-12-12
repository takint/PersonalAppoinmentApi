package specialtopic.ngoctinle.personalappointment

import com.squareup.moshi.Json

data class SearchRequest(
    @field:Json(name = "startTime") val startTime: String,
    @field:Json(name = "endTime") val endTime: String
) {
    // Additional process
}