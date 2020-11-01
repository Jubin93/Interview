package com.test.employeeapp.data.retrofit;

import com.test.employeeapp.data.model.EmployeeListResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/v2/5d565297300000680030a986")
    Call<ArrayList<EmployeeListResponseModel>> getEmployeeList();

}