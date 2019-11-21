package com.nathan.stonksapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    private final static String ARG_RESPONSE = "response";
    private String mStockResponse = "";

    TextView mResponse;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String mStockResponse) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESPONSE, mStockResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        if(getArguments() != null) {
            mStockResponse = getArguments().getString(ARG_RESPONSE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.result_fragment, container, false);
        // Inflate the layout for this fragment
        mResponse = view.findViewById(R.id.response);
        mResponse.setText(mStockResponse);
        return view;
    }

}
