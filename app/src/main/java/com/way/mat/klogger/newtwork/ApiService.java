package com.way.mat.klogger.newtwork;

import com.way.mat.klogger.newtwork.response.TestResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface ApiService {

    public static final String ROUTE = "/api/people/";

    /**
     * Just test request to free API
     */

    @Headers("Content-Type: application/json")
    @GET(ROUTE)
    Observable<TestResponse> sendRequest();

}
