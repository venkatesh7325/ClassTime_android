package org.com.classmate.ui.activities.common;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.com.classmate.APIS.ApiClient;
import org.com.classmate.APIS.ApiInterface;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.BranchListPojo;
import org.com.classmate.model.UniversityList;
import org.com.classmate.model.UniversityListPojo;
import org.com.classmate.ui.activities.admin.AdminDashBoardActivity;
import org.com.classmate.ui.activities.admin.AdminFormActivity;
import org.com.classmate.ui.activities.hod.HodFormActivity;
import org.com.classmate.ui.activities.students.StudentFormActivity;
import org.com.classmate.ui.activities.teacher.TeachersFormActivity;
import org.com.classmate.ui.fragments.welcomeFragments.FragmentFour;
import org.com.classmate.ui.fragments.welcomeFragments.ThreeFragment;
import org.com.classmate.ui.fragments.welcomeFragments.TwoFragment;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.ButtonAnimation;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.DynamicCustomButtons;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

;import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppCompatActivity {
    int currentPage = 0;
    int numPages = 0;
    private String TAG = "IntroductionActivity";
    @Bind(R.id.btn_signup)
    Button signUp;
    @Bind(R.id.btn_signin)
    LinearLayout signIn;
    String logOrReg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_screen);
        ButterKnife.bind(IntroductionActivity.this);
        callUnvAndBranchApis(); // get Unv and Branch list
        DynamicCustomButtons.setbuttonFontSemiBold(signUp, this);
        init();
        ButtonAnimation buttonAnimation = new ButtonAnimation();
        buttonAnimation.startAnimation(this, signUp);
    }

    void callUnvAndBranchApis() {
        if (!Utility.isConnectingToInternet(this)) {
            ToastUtils.displayToast(Constants.no_internet_connection, this);
            return;
        }
        Log.d(TAG, "Unvi list--" + Utility.getUniversityList(this));
        if (Utility.getUniversityList(this).isEmpty())
            callUniversityListApi();
        if (Utility.getBranchListList(this).isEmpty())
            callBranchListApi();
    }


    void init() {
        // Instantiate a ViewPager and a PagerAdapter
        ViewPager vPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(vPager);
        setUpIndicator(vPager);
    }

    @OnClick(R.id.btn_signup)
    public void onClickSignUp() {
        logOrReg = "Sign up";
        popupToShowModeOfLogin();
    }

    @OnClick(R.id.btn_signin)
    public void onClickSignIn() {
        logOrReg = getResources().getString(R.string.sign_in);
        popupToShowModeOfLogin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    void popupToShowModeOfLogin() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_reg_mode_popup);
        dialogFullWidth(dialog);
        dialog.show();
        TextView okayTv = (TextView) dialog.findViewById(R.id.okay);
        TextView cancelTv = (TextView) dialog.findViewById(R.id.cancel);
        CustomTextViewRegular title = (CustomTextViewRegular) dialog.findViewById(R.id.tv_popup_title);
        title.setText(logOrReg.concat(" as"));
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg_login_modes);
        okayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(IntroductionActivity.this, "Choose your Role for login or registration ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (logOrReg.equalsIgnoreCase(getResources().getString(R.string.sign_in))) {
                    Log.d(TAG, "Selected mode--" + returnSelection(radioGroup));
                    Bundle b = new Bundle();
                    b.putString("mode_of_login", returnSelection(radioGroup));
                    Intent i = new Intent(IntroductionActivity.this, LoginActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                } else {
                    getSelectedLoginMode(radioGroup, selectedId);
                }
                dialog.dismiss();
            }
        });


        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    void dialogFullWidth(Dialog dialog) {
        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    void getSelectedLoginMode(RadioGroup radioGroup, int checkedId) {
        if (checkedId == -1) {
            Toast.makeText(this, "Choose your Role for login or registration ", Toast.LENGTH_SHORT).show();
        } else {
            navigateToNextScreen(checkedId, returnSelection(radioGroup));
        }
    }

    private String returnSelection(RadioGroup radioGroup) {
        String selection = "";
        try {
            int id = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(id);
            int radioId = radioGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
            if (btn != null && btn.getText() != null) {
                selection = (String) btn.getText();
                Log.d(TAG, "Selected mode--" + selection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selection;
    }

    void navigateToNextScreen(int id, String selectedRb) {
        Intent i = null;
        Log.d(TAG, "Selected mode--" + selectedRb);
        Bundle b = new Bundle();
        b.putString("mode_of_login", selectedRb);
        switch (id) {
            case R.id.rb_admin:
                i = new Intent(this, AdminFormActivity.class);
                i.putExtras(b);
                break;
            case R.id.rb_hod:
                i = new Intent(this, HodFormActivity.class);
                i.putExtras(b);
                break;
            case R.id.rb_faculty:
                i = new Intent(this, HodFormActivity.class);
                i.putExtras(b);
                break;
            case R.id.rb_student:
                i = new Intent(this, StudentFormActivity.class);
                i.putExtras(b);
                break;
        }
        if (null != i)
            startActivity(i);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(false, new FadePageTransformer());
        adapter.addFragment(new TwoFragment());
        adapter.addFragment(new ThreeFragment());
        adapter.addFragment(new FragmentFour());
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    private void setUpIndicator(ViewPager viewPager) {
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setVisibility(View.VISIBLE);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        numPages = 3;
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                ButtonAnimation buttonAnimation = new ButtonAnimation();
                buttonAnimation.startAnimation(IntroductionActivity.this, signUp);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                /*
                Empty
                 */
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                /*
                Empty
                 */
            }
        });


    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            view.setAlpha(1 - Math.abs(position));
            if (position < 0) {
                view.setScrollX((int) ((float) (view.getWidth()) * position));
            } else if (position > 0) {
                view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
            } else {
                view.setScrollX(0);
            }
        }

    }

    public void callUniversityListApi() {
        try {
            Log.v(TAG, "Unv API list--" + "University");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UniversityListPojo> universityListCall = apiService.getUniversityListApi(ApiConstants.Authorization);
            universityListCall.enqueue(new Callback<UniversityListPojo>() {
                @Override
                public void onResponse(Call<UniversityListPojo> call, Response<UniversityListPojo> response) {
                    int statusCode = response.code();
                    Log.v(TAG, "statusCode--" + statusCode + "--response--" + response);
                    List<UniversityList> universityLists = response.body().getUniversityList();
                    Log.v(TAG, "Unv API list--" + universityLists.size());
                    Gson gson = new Gson();
                    String jsonUnv = gson.toJson(universityLists);
                    Utility.saveUniversityList(IntroductionActivity.this, jsonUnv);
                }

                @Override
                public void onFailure(Call<UniversityListPojo> call, Throwable t) {
                    Log.e(TAG, "Error---" + t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callBranchListApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BranchListPojo> branchListApi = apiService.getBranchListApi(ApiConstants.Authorization);
        branchListApi.enqueue(new Callback<BranchListPojo>() {
            @Override
            public void onResponse(Call<BranchListPojo> call, Response<BranchListPojo> response) {
                int statusCode = response.code();
                Log.v(TAG, "statusCode--" + statusCode + "--response--" + response);
                List<BranchList> branchListPojos = response.body().getBranchList();
                Log.v(TAG, "Unv list--" + branchListPojos.size());
                Gson gson = new Gson();
                String bcnListString = gson.toJson(branchListPojos);
                Utility.saveBranchList(IntroductionActivity.this, bcnListString);
            }

            @Override
            public void onFailure(Call<BranchListPojo> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}