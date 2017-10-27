package tripath.com.samplekapp.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

/**
 * Created by SSLAB on 2017-08-11.
 */

open class CommonData(
        @JsonProperty ("code") val code : String,
        @JsonProperty("message") val message : String,
        @JsonProperty ("type") val type : String
) {
    override fun toString(): String {

        val stringBuilder : StringBuilder = StringBuilder()
        val arrayFields = javaClass.declaredFields

        for( field in arrayFields){

            stringBuilder.append( field.name + "[")
            stringBuilder.append( field.toGenericString())
            stringBuilder.append( field.name + "]")
        }

        return stringBuilder.toString()
    }
}