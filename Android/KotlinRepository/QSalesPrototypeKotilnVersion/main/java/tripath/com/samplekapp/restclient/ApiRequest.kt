package tripath.com.samplekapp.restclient

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import tripath.com.samplekapp.data.User
import java.nio.charset.Charset

/**
 * Created by SSLAB on 2017-08-11.
 */

open class ApiRequest <T> : JsonRequest<T> {

    val methodApiRequest : Int
    val clazz : Class<T>

    constructor(
            method: Int,
            url: String?,
            requestBody: String?,
            listener: Response.Listener< T >,
            errorListener: Response.ErrorListener,
            clazz: Class<T>

    ) : super(method,url,requestBody, listener, errorListener ){
        this.methodApiRequest = method
        this.clazz = clazz
    }


    override fun getHeaders(): MutableMap<String, String> {

        val headers = HashMap<String,String>()

        headers.put("Accept", "application/json" )
        return headers
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T>  {
        var jsonString : String?


            if (method == Method.GET || method == Method.POST) {

                // TODO : byte 에서 String 으로 변경하는데 스피드 이슈가 있다던데 조사해보기
                val charsetStr: String = HttpHeaderParser.parseCharset(response?.headers, PROTOCOL_CHARSET)

                val charset = Charset.forName(charsetStr)


                val data: ByteArray? = response?.data


                jsonString = data?.toString(charset)


//            String( response?.data , HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET) )


//            response?.data.
//            jsonString =  response?.data.toString() HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET))
            } else {
                val resultObj: JSONObject = JSONObject()
                resultObj.put("statusCode", response?.statusCode)

                val charsetStr: String = HttpHeaderParser.parseCharset(response?.headers, PROTOCOL_CHARSET)

                val charset = Charset.forName(charsetStr)


                val data: ByteArray? = response?.data


                jsonString = data?.toString(charset)

            }


            val mapper = ObjectMapper()
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

            return Response.success(jsonString as T  , HttpHeaderParser.parseCacheHeaders(response))

    }

}