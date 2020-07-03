package com.marshallstudio.calculator;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;


public class ButtonsSimpleFragment extends Fragment {
    private View mRootView;

    public ButtonsSimpleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView=inflater.inflate(R.layout.fragment_buttons_simple, container, false);

        final DisplayDataViewModel viewModel= ViewModelProviders.of(requireActivity()).get(DisplayDataViewModel.class);

        GridLayout rootGridLayout=mRootView.findViewById(R.id.GL_fragment_buttons_Simple_root);
        for (int i=0;i<rootGridLayout.getChildCount();i++){
            TextView textView= (TextView) rootGridLayout.getChildAt(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.btn_equal) {
                        viewModel.solveExpression();
                    }
                    else if (view.getId()==R.id.btn_percentage){
                        Snackbar snackbar=Snackbar.make(view,"Percentage is not currently available.",Snackbar.LENGTH_SHORT);
                        View snackBarView=snackbar.getView();
                        snackBarView.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                    else {
                        TextView button = (TextView) view;
                        viewModel.appendToExpression(button.getText().toString());
                    }

                }
            });
        }



        return mRootView;
    }


}