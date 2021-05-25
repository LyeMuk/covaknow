package com.mukesh.covaknow.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiInterface {

    static final String BaseURL ="https://corona.lmao.ninja/v2/";
    @GET("countries")
    Call<List<cData>> getcData();

}
