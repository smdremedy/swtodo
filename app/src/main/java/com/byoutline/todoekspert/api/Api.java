package com.byoutline.todoekspert.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    //https://api.parse.com

    @GET("/1/login")
    Call<LoginResponse> getLogin(@Query("username") String username, @Query("password") String password);

    @GET("/1/classes/Todo")
    Call<TodosResponse> getTodos(@Header("X-Parse-Session-Token") String token);

    @POST("/1/classes/Todo")
    Call<ResponseBody> postTodo(@Body TodoFromApi todoFromApi, @Header("X-Parse-Session-Token") String token);
}
