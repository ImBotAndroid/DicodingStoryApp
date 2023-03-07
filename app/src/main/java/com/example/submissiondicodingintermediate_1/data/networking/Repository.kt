package com.example.submissiondicodingintermediate_1.data.networking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissiondicodingintermediate_1.data.local.UserPreference
import com.example.submissiondicodingintermediate_1.data.api.ApiService
import com.example.submissiondicodingintermediate_1.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _setMessage = MutableLiveData<String>()
    val setMessage: LiveData<String> = _setMessage

    fun loginUser(email: String, password: String): LiveData<LoginResponse> {
        val loginResponse = MutableLiveData<LoginResponse>()

        _isLoading.value = true
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    loginResponse.value = response.body()
                    response.body()?.let { responses ->
                        responses.loginResult?.let {
                            userPreference.setUser(it)
                        }
                    }
                } else {
                    val responseError = JSONObject(response.errorBody()?.charStream()?.readText() ?: "")
                    val message = responseError.get("message")
                    _setMessage.value = message.toString()

                    loginResponse.value = LoginResponse(
                        null,
                        true,
                        "error"
                    )
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return loginResponse
    }

    fun registerNewUser(name: String, email: String, password: String): LiveData<SignUpResponse> {
        val signUpResponse = MutableLiveData<SignUpResponse>()

        _isLoading.value = true
        val client = apiService.createUser(name, email, password)
        client.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    signUpResponse.value = response.body()

                    Log.e(TAG, "onSuccess: ${response.message()}" )
                } else {
                    val responseError = JSONObject(response.errorBody()?.charStream()?.readText() ?: "")
                    val message = responseError.get("message")
                    _setMessage.value = message.toString()

                    signUpResponse.value = SignUpResponse(
                        true,
                        "error"
                    )
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return signUpResponse
    }

    fun getUser(): LiveData<LoginResult>{
        val getUserData = MutableLiveData<LoginResult>()
        getUserData.postValue(userPreference.getUser())
        return getUserData
    }

    fun deleteUser(){
        userPreference.deleteUser()
    }

    fun getAllStory(bearer: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
               StoryPagingSource(apiService, bearer)
            }
        ).liveData
    }

    fun uploadStory(bearer: String, file: MultipartBody.Part, description: RequestBody): LiveData<UploadStoriesResponse>{
        val postUploadStory = MutableLiveData<UploadStoriesResponse>()

        _isLoading.value = true
        val client = apiService.uploadStories(bearer, file, description)
        client.enqueue(object : Callback<UploadStoriesResponse>{
            override fun onResponse(
                call: Call<UploadStoriesResponse>,
                response: Response<UploadStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    postUploadStory.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return postUploadStory
    }

    fun storyLocation(bearer: String): LiveData<List<ListStoryItem>>{
        val getStoryLocation = MutableLiveData<List<ListStoryItem>>()

        val client = apiService.storyLocation(bearer)
        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
                    getStoryLocation.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return getStoryLocation
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}