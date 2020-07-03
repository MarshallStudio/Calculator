package com.marshallstudio.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class DisplayFragment extends Fragment {
    private View mRootView;
    private TextView mExpressionTextView;
    private HorizontalScrollView mExpressionHScrollView;
    private ImageView mClearButton;
    private DisplayDataViewModel mViewModel;
    private TextView mResultTextView;

    public DisplayFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView=inflater.inflate(R.layout.fragment_display, container, false);
        initialize();
        setOnClickListener();

        mViewModel= ViewModelProviders.of(requireActivity()).get(DisplayDataViewModel.class);
        MutableLiveData<String> expressionLiveData=mViewModel.getExpression();
        expressionLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String expression) {
                mExpressionTextView.setText(expression);
                mExpressionHScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mExpressionHScrollView.fullScroll(View.FOCUS_RIGHT);
                    }
                });

            }
        });

        MutableLiveData<String> resultLiveData=mViewModel.getResult();
        resultLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                mResultTextView.setText(result);
            }
        });



        return mRootView;
    }

    private void initialize(){
        mExpressionTextView=mRootView.findViewById(R.id.TV_DisplayFrag_Expression);
        mExpressionHScrollView=mRootView.findViewById(R.id.HSV_display);
        mClearButton=mRootView.findViewById(R.id.btn_clear);
        mResultTextView=mRootView.findViewById(R.id.TV_Result);
    }

    public void setOnClickListener(){
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.removeCharacter();
            }
        });

        mClearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mViewModel.clearExpression();
                return true;
            }
        });
    }
}