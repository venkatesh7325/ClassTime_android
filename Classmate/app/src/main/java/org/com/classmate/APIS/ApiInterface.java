package org.com.classmate.APIS;

import org.com.classmate.model.BranchListPojo;
import org.com.classmate.model.UniversityListPojo;
import org.com.classmate.utils.ApiConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by drkranga on 9/21/2017.
 */

public interface ApiInterface {

    //@Path – variable substitution for the API endpoint. For example movie id will be swapped for{id} in the URL endpoint.

    // @Query – specifies the query key name with the value of the annotated parameter.

    // @Body – payload for the POST call

    // @Header – specifies the header with the value of the annotated parameter

    @GET(ApiConstants.UNIVERSITY)
    Call<UniversityListPojo> getUniversityListApi(@Header("X-Authorization") String authorization);

    @GET(ApiConstants.BRANCH)
    Call<BranchListPojo> getBranchListApi(@Header("X-Authorization") String authorization);
}
