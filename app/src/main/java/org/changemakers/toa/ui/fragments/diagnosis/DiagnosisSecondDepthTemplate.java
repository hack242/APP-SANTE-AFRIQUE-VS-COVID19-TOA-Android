package org.changemakers.toa.ui.fragments.diagnosis;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.changemakers.toa.R;
import org.changemakers.toa.databinding.FragmentDiagnosisSecondDepthDeseaseTemplateBinding;
import org.changemakers.toa.interfaces.ActivityCallbackInterface;
import org.changemakers.toa.ui.fragments.DiagnosisFragment;

import static org.changemakers.toa.ui.fragments.DiagnosisFragment.EXTRA_SECOND_DEPTH_POSITION;

public class DiagnosisSecondDepthTemplate extends BottomSheetDialogFragment implements View.OnClickListener {

    private static String[] sDiagnosisOptionsTitles;
    private static String[] sDiagnosisOptionsDetails;
    ActivityCallbackInterface mCallback;
    int secondDepthPosition = 0;
    private FragmentDiagnosisSecondDepthDeseaseTemplateBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDiagnosisSecondDepthDeseaseTemplateBinding.inflate(getLayoutInflater());

        sDiagnosisOptionsTitles = getResources().getStringArray(R.array.diagnosis_second_options_details_titles);
        sDiagnosisOptionsDetails = getResources().getStringArray(R.array.diagnosis_second_options_details);

        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //EXPANDED;

                    binding.collapsingToolabar.setTitleEnabled(false);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {

                    //COLLAPSED;

                    binding.collapsingToolabar.setTitleEnabled(false);
                } else {

                    //IDDLE
                }
            }
        });

        if (savedInstanceState == null) {
            secondDepthPosition = getArguments().getInt(EXTRA_SECOND_DEPTH_POSITION, 0 /*initial diagnosis_second_options_details_titles list position*/);
            binding.title.setText(sDiagnosisOptionsTitles[secondDepthPosition]);
            binding.details.setText(sDiagnosisOptionsDetails[secondDepthPosition]);
        } else {
            secondDepthPosition = savedInstanceState.getInt(EXTRA_SECOND_DEPTH_POSITION, secondDepthPosition);
            binding.title.setText(sDiagnosisOptionsTitles[secondDepthPosition]);
            binding.details.setText(sDiagnosisOptionsDetails[secondDepthPosition]);
        }
        binding.btnNext.setOnClickListener(this);
        //binding.btnPrevention.setOnClickListener(this);

        return binding.getRoot();
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                if (mCallback != null) {
                    YoYo.with(Techniques.BounceIn).onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {

                            //When ANALYSIS continuation is clicked, we load according to the position in previous adpter
                            Log.i("toa_log", secondDepthPosition + "");
                            mCallback.onDiagnosisOptionSelected(v, DiagnosisFragment.DIAGNOSIS_OPTIONS_THIRD_DEPTH, secondDepthPosition);
                        }
                    }).playOn(v);
                }
                break;

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (mCallback == null)
            mCallback = (ActivityCallbackInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mCallback != null)
            mCallback = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(EXTRA_SECOND_DEPTH_POSITION, secondDepthPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            secondDepthPosition = savedInstanceState.getInt(EXTRA_SECOND_DEPTH_POSITION, secondDepthPosition);
        }
    }
}
