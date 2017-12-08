package org.com.classmate.APIS;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.com.classmate.interfaces.SubjectListResponse;
import org.com.classmate.model.students.StudentsList.GetStudentsListPojo;
import org.com.classmate.model.subjectList.SubjectListDetails;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 9/20/2016.
 */
public class APIRequest {

    private static final String TAG = APIRequest.class.getSimpleName();
    Context context;
    View view;
    ProgressDialog progressDialog;

    public APIRequest(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        //  progressDialog.setCancelable(cancelable);
    }

    public APIRequest(View view) {
        this.view = view;
        this.context = view.getContext();
    }

    public APIRequest(View view, boolean cancelable) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(cancelable);
    }

    public APIRequest(View view, String progressBarMessage) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressBarMessage);
    }

    public APIRequest(View view, boolean cancelable, String progressBarMessage) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressBarMessage);
        progressDialog.setCancelable(cancelable);
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        return header;

    }

    public void postAPI(String method, JSONObject jsonObject, final RequestCallBack requestCallBack) {

        try {

            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();

                Log.e(TAG, " Constant.MAIN_URL + method) ---" + Constants.MAIN_URL + method);
                Log.e(TAG, "JSON req.toString() ---" + jsonObject);
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, Constants.MAIN_URL + method, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed(Constants.something_went_wrong);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(error.getMessage());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }

                };

                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // MyDialog.snackBar_Error_Bottom(view, e.getMessage());
            requestCallBack.onFailed(e.getMessage());
        }

    }

    public void postJsonRequestMethod(String subUrl, JSONObject jsonObject, final RequestCallBack requestCallBack) {
        try {
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();
                Log.e("Url and paramas", ApiConstants.BASE_URL + "   " + subUrl + "--------" + jsonObject);
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, ApiConstants.BASE_URL + subUrl, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Log.e("result", "result  " + json);
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed("SOMETHING WENT WRONG");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(jsonRequest);
                //ApplicationController.getInstance().addToRequestQueue(jsonRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   MyDialog.snackBar_Error_Bottom(view, e.getMessage());
            requestCallBack.onFailed(e.getMessage());
        }

    }

    /*public void get(String subURl, JSONObject jsonObject, final RequestCallBack requestCallBack) {
        try {
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.NO_NETWORK);
            } else {
                if (progressDialog != null)
                    progressDialog.show();
                // HttpsTrustManager.allowAllSSL();
                Log.d("Url and paramas", Constants.MAIN_URL + "   " + subURl + "--------" + jsonObject);
                String url = Constants.MAIN_URL + subURl;
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Log.e("result", "result  " + json);
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed(Constantss.SOMETHING_WENT_WRONG);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(TAG + e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(TAG + error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getHeader();
                    }
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(jsonRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestCallBack.onFailed(TAG + e.getMessage());
        }
    }*/
    public void postStringRequest(String subURl, final Map<String, String> params, final RequestCallBack requestCallBack) {
        try {
            Log.e(TAG, "POST PARAMS-->" + params);
            String url = ApiConstants.BASE_URL + subURl;
            Log.e(TAG, "POST URL-->" + url + " - " + params);
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            if (json != null) {
                                // Log.e(TAG,"Response in postStringRequest --"+json);
                                requestCallBack.onResponse(json);
                            } else {
                                requestCallBack.onFailed("SOMETHING_WENT_WRONG");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            requestCallBack.onFailed(e.getMessage());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        error.printStackTrace();
                        requestCallBack.onFailed(error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }

                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params;
                    }
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(stringRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestCallBack.onFailed(e.getMessage());
        }
    }

    public static void callApiToGetStudentList(final Context context, String year, String semester, String branchID, String instID, final StudentListResponse studentListResponse) {
//        {"year":"1","semester":"1","branch_id":"1"}
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            // hashMap.put("name", clzName);
            hashMap.put("year", year);
            hashMap.put("semester", semester);
            hashMap.put("branch_id", branchID);
            hashMap.put("institution_id", instID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Admin User HASHAMP--" + hashMap);
        new APIRequest(context).postStringRequest(ApiConstants.GET_STUDENTS_LIST, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Admin Activity--" + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String message = jsonObj.getString("student_list");
                    if ("No Record Found".equalsIgnoreCase(message)) {
                        ToastUtils.displayToast("No students for this Department", context);
                    } else {
                        Gson gson = new Gson();
                        GetStudentsListPojo getStudentsListPojo = gson.fromJson(response, GetStudentsListPojo.class);

                        studentListResponse.stdList(getStudentsListPojo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, context);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }

    public static void callApiToGetSubjectList(final Context context, String university, String branch, String year, String sem, final SubjectListResponse studentListResponse) {
//        {"year":"1","semester":"1","branch_id":"1"}
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            // hashMap.put("name", clzName);
            hashMap.put("university_id", university);
            hashMap.put("branch_id", branch);
            hashMap.put("year", year);
            hashMap.put("semester", sem);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Admin User HASHAMP--" + hashMap);
        new APIRequest(context).postStringRequest(ApiConstants.SUBJECT_LIST, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Admin Activity--" + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String message = jsonObj.getString("subject_list");
                    if ("No Record Found".equalsIgnoreCase(message)) {
                        ToastUtils.displayToast("No students for this Department", context);
                    } else {
                        Gson gson = new Gson();
                        SubjectListDetails getStudentsListPojo = gson.fromJson(response, SubjectListDetails.class);
                        studentListResponse.subjectListResponse(getStudentsListPojo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, context);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }


}
