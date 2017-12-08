package org.com.classmate.ui.fragments.welcomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.com.classmate.R;


/**
 * create an instance of this fragment.
 */
public class TwoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View twoView = inflater.inflate(R.layout.fragment_two, container, false);
        return twoView;
    }

}
