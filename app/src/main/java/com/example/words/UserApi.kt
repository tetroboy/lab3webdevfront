import retrofit2.Call
import retrofit2.http.GET
import okhttp3.Interceptor
import okhttp3.Response

interface UserApi {
    @GET("/api/user")
    fun getUserDetails(): Call<Map<String, String>>
}

