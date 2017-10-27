package tripath.com.samplekapp.restclient

import android.content.Context
import com.google.gson.Gson

import com.android.volley.*
import com.android.volley.toolbox.Volley
import tripath.com.samplekapp.data.AuthCode
import tripath.com.samplekapp.data.User

/**
 * Created by SSLAB on 2017-08-11.
 */
class RestApiClient{

    companion object {
        const val basicURL = "http://172.19.132.194:8080"
    }

    val gson : Gson
    val context  : Context


    constructor(context :Context ){
        this.context = context
        gson = Gson()
    }

    fun getAuthCodeList(authCode : AuthCode){

    }

    fun loginCheck(user : User, listener : Response.Listener<User> ,error : Response.ErrorListener  ){
        val params : String = gson.toJson( user )

        /**
         *  listener: ((response: T) -> Unit)?,
        errorListener: ((error: VolleyError) -> Unit)?
         */
        val request : Request<User> = ApiRequest<User>(
                Request.Method.POST,
                basicURL+"/external",
                params,
                listener,
                error,
                User::class.java
        )

        Volley.newRequestQueue(context).add(request)
    }


}