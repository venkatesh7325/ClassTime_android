package org.com.classmate.APIS;

import android.util.Log;

import org.com.classmate.model.BranchList;
import org.com.classmate.model.BranchListPojo;
import org.com.classmate.model.UniversityList;
import org.com.classmate.model.UniversityListPojo;
import org.com.classmate.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by drkranga on 9/21/2017.
 */

public class CallApisInActivity {
    public static String TAG = "CallApisInActivity";

    public static void callUniversityListApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UniversityListPojo> universityListCall = apiService.getUniversityListApi(ApiConstants.Authorization);
        universityListCall.enqueue(new Callback<UniversityListPojo>() {
            @Override
            public void onResponse(Call<UniversityListPojo> call, Response<UniversityListPojo> response) {
                int statusCode = response.code();
                Log.v(TAG, "statusCode--" + statusCode + "--response--" + response);
                List<UniversityList> universityLists = response.body().getUniversityList();
                Log.v(TAG, "Unv list--" + universityLists.size());
            }

            @Override
            public void onFailure(Call<UniversityListPojo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public static void callBranchListApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BranchListPojo> branchListApi = apiService.getBranchListApi(ApiConstants.Authorization);
        branchListApi.enqueue(new Callback<BranchListPojo>() {
            @Override
            public void onResponse(Call<BranchListPojo> call, Response<BranchListPojo> response) {
                int statusCode = response.code();
                Log.v(TAG, "statusCode--" + statusCode + "--response--" + response);
                List<BranchList> branchListPojos = response.body().getBranchList();
                Log.v(TAG, "Unv list--" + branchListPojos.size());
            }
            @Override
            public void onFailure(Call<BranchListPojo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        /*universityListCall.enqueue(new Callback<UniversityListPojo>() {
            @Override
            public void onResponse(Call<UniversityListPojo> call, Response<UniversityListPojo> response) {
                int statusCode = response.code();
                Log.v(TAG, "statusCode--" + statusCode + "--response--" + response);
                List<UniversityList> universityLists = response.body().getUniversityList();
                Log.v(TAG, "Unv list--" + universityLists.size());
            }

            @Override
            public void onFailure(Call<UniversityListPojo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });*/
    }
}
