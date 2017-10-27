package tripath.com.samplekapp.data

import tripath.com.samplekapp.StaticValues

/**
 * Created by SSLAB on 2017-08-11.
 */

public interface RestApiService{



    /**
     * Companion object for the factory
     */
    companion object Factory {
        fun defaultCreateUrl(): tripath.com.samplekapp.data.RestApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl(StaticValues.basicURL)
                    .build()

            return retrofit.create(tripath.com.samplekapp.data.RestApiService::class.java)
        }

        fun create(url :String) : tripath.com.samplekapp.data.RestApiService{
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl(url)
                    .build()

            return retrofit.create(tripath.com.samplekapp.data.RestApiService::class.java)
        }

        fun defaultCreateUrl(url :String) : tripath.com.samplekapp.data.RestApiService{
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl(StaticValues.basicURL + url)
                    .build()
            return retrofit.create(tripath.com.samplekapp.data.RestApiService::class.java)
        }
    }


}