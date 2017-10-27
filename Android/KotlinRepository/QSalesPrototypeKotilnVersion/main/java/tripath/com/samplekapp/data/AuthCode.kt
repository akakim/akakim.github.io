package tripath.com.samplekapp.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

/**
 * Created by SSLAB on 2017-08-11.
 */

class AuthCode(
        @JsonProperty("advisorSeq") val advisorSeq : String,
        @JsonProperty ("data") val dataList : ArrayList<AdvisorItem>
) : CommonData("","","selectAuthCodeList") ,Parcelable{


    constructor() :this ("",ArrayList<AdvisorItem>()){
        Log.d(TAG , "maybe Parcelable")
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(AdvisorItem.CREATOR)) {
        Log.d(TAG , "Pracelable Constructo")
        Log.d(TAG, this.advisorSeq)
        var i = 0
        for( item in dataList ){

            Log.d(TAG, i++.toString() +" : "+item.toString())
        }

    }

    override fun writeToParcel(parcel: Parcel?, flag: Int) {

        parcel?.writeString( advisorSeq )
        parcel?.writeTypedList( dataList )
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<AuthCode> {
        val TAG : String ="AuthCode"
        override fun createFromParcel(parcel: Parcel): AuthCode {
            return AuthCode(parcel)
        }

        override fun newArray(size: Int): Array<AuthCode?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return super<CommonData>.toString()
    }
}