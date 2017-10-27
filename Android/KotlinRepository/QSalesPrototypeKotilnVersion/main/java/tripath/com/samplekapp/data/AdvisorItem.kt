package tripath.com.samplekapp.data

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

/**
 * Created by SSLAB on 2017-08-11.
 */
class AdvisorItem : Parcelable{
    @JsonProperty ("roomSeq") lateinit var  roomSeq : String
    @JsonProperty("advisorSeq") lateinit var  advisorSeq : String
    @JsonProperty ("authCode") lateinit var  authCode : String
    @JsonProperty ("roomStatus") lateinit var  roomStatus : String



    constructor() : this("","","","")
    constructor(parcel: Parcel)  {
        roomSeq = parcel.readString()
        advisorSeq = parcel.readString()
        authCode = parcel.readString()
        roomStatus = parcel.readString()
    }

    constructor(roomSeq :String,advisorSeq :String,authCode :String,roomStatus : String ){
        this.roomSeq = roomSeq
        this.advisorSeq = advisorSeq
        this.authCode = authCode
        this.roomStatus = roomStatus
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(roomSeq)
        parcel.writeString(advisorSeq)
        parcel.writeString(authCode)
        parcel.writeString(roomStatus)

    }

    override fun describeContents(): Int {
        return 0
    }


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

    companion object CREATOR : Parcelable.Creator<AdvisorItem> {
        override fun createFromParcel(parcel: Parcel): AdvisorItem {
            return AdvisorItem(parcel)
        }

        override fun newArray(size: Int): Array<AdvisorItem?> {
            return arrayOfNulls(size)
        }
    }



}