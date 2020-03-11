package com.jinesh.mpokket.network

import com.jinesh.mpokket.BuildConfig
import com.jinesh.mpokket.MyApplication
import com.jinesh.mpokket.extension.hasNetwork
import com.google.gson.*
import com.google.gson.internal.bind.TypeAdapters
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Cache


interface RetrofitAPI {

    companion object {
        const val search = "search/repositories"
        const val repos = "repos"
        const val contributors = "contributors"
        const val login = "login"
        const val repo = "repo"
        const val users = "users"
    }


    @GET("$search")
    fun searchRepositories(@Query("q")  query:String,@Query("sort")  sort:String,@Query("order")  order:String): Call<JsonObject>

    @GET("$repos/{$login}/{$repo}/$contributors")
    fun getRepoDetail(@Path("login")  login:String,@Path("repo")  repo:String): Call<JsonArray>

    @GET("$users/{$login}/$repos")
    fun getRepos(@Path("login")  login:String): Call<JsonArray>

    @GET("$users/{$login}")
    fun getUserDetail(@Path("login")  login:String): Call<JsonObject>

}




object RetrofitClient {

    private val cacheSize = (5.times(1024).times(1024)).toLong()
    private val myCache = Cache(MyApplication.instance.cacheDir, cacheSize)

    fun getClient(
    ): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->


            var request = chain.request().newBuilder().build()
            val response = chain.proceed(request)
            response
        })
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(MyApplication.instance)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 2).build()
                chain.proceed(request)
            }
            .cache(myCache)
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()



        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }
}

object HMap {
    const val q = "q"
}


private val UNRELIABLE_INTEGER: TypeAdapter<Number> = object : TypeAdapter<Number>() {
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Number? {
        val jsonToken = `in`.peek()
        when (jsonToken) {
            TypeAdapters.NUMBER, TypeAdapters.STRING, TypeAdapters.DOUBLE, JsonToken.STRING, JsonToken.NUMBER -> {
                val s = `in`.nextString()
                try {
                    return Integer.parseInt(s)
                } catch (ignored: NumberFormatException) {
                }

                try {
                    return java.lang.Double.parseDouble(s).toInt()
                } catch (ignored: NumberFormatException) {
                }

                return null
            }
            JSONObject.NULL -> {
                `in`.nextNull()
                return null
            }
            TypeAdapters.BOOLEAN -> {
                `in`.nextBoolean()
                return null
            }
            else -> throw JsonSyntaxException("Expecting number, got: $jsonToken")
        }
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Number) {
        out.value(value)
    }
}

private val UNRELIABLE_INTEGER_FACTORY =
    TypeAdapters.newFactory(Int::class.javaPrimitiveType, Int::class.java, UNRELIABLE_INTEGER)


fun myGson(): Gson {
    return GsonBuilder().registerTypeAdapterFactory(UNRELIABLE_INTEGER_FACTORY).create()!!
}


