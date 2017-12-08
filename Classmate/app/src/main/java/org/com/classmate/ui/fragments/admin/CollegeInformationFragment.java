package org.com.classmate.ui.fragments.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.GetClz.GetCollegeResponsePojo;
import org.com.classmate.model.UserDetails.GetUserDetailsResponsePojo;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollegeInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollegeInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollegeInformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CLZ INF FRG";
    TextView tvClzName, tvClzEmail, tvClzWebsite, tvClzNumber, tvClzAddress, tvClzDate, tvClzVerificationCode;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CollegeInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollegeInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollegeInformationFragment newInstance(String param1, String param2) {
        CollegeInformationFragment fragment = new CollegeInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_college_information, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        tvClzName = (TextView) view.findViewById(R.id.tv_clz_name);
        tvClzAddress = (TextView) view.findViewById(R.id.tv_clz_address);
        tvClzEmail = (TextView) view.findViewById(R.id.tv_clz_email);
        tvClzNumber = (TextView) view.findViewById(R.id.tv_clz_mobile);
        tvClzWebsite = (TextView) view.findViewById(R.id.tv_clz_website);
        tvClzDate = (TextView) view.findViewById(R.id.tv_clz_date);
        tvClzVerificationCode = (TextView) view.findViewById(R.id.tv_clz_verification_code);
        if (Utility.isConnectingToInternet(getActivity()))
            callApiToGetUserDetails(getActivity());
        else
            ToastUtils.displayToast(Constants.no_internet_connection, getActivity());
    }

    void callApiToGetUserDetails(final Context context) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            // hashMap.put("name", clzName);
            hashMap.put("id", String.valueOf(Utility.getUserID(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Admin User HASHAMP--" + hashMap);
        new APIRequest(context).postStringRequest(ApiConstants.GET_USER_DETAILS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Admin Activity--" + response);
                Gson gson = new Gson();
                GetCollegeResponsePojo successResponsePojo = gson.fromJson(response, GetCollegeResponsePojo.class);
                if (successResponsePojo.getMessage().equalsIgnoreCase("success")) {
                    tvClzName.setText(successResponsePojo.getUserDetails().getName() + "(" + successResponsePojo.getUserDetails().getCollegeCode() + ")");
                    tvClzEmail.setText(successResponsePojo.getUserDetails().getEmail());
                    tvClzNumber.setText(successResponsePojo.getUserDetails().getMobile());
                    tvClzAddress.setText(successResponsePojo.getUserDetails().getAddress());
                    tvClzWebsite.setText(successResponsePojo.getUserDetails().getName());
                    tvClzVerificationCode.setText("College verification Code : ".concat(successResponsePojo.getUserDetails().getCode()));
                } else {
                    ToastUtils.displayToast(Constants.something_went_wrong, context);
                }

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, context);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
