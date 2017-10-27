package tripath.com.samplekapp.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

/**
 * Created by SSLAB on 2017-08-10.
 */


data class User(
        @JsonProperty ("advisorId") val advisorId : String,
        @JsonProperty ("password") val password : String,
        @JsonProperty ("data") val data : String,
        @JsonProperty ("advisorDvcKey") val advisorDvcKey : String
) : CommonData("","","login"){
    override fun toString(): String {
        return super.toString()
    }
}
