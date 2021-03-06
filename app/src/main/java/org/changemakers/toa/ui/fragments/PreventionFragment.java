package org.changemakers.toa.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.changemakers.toa.R;
import org.changemakers.toa.databinding.FragmentPreventionBinding;
import org.changemakers.toa.databinding.ItemPreventionFragmentBinding;
import org.changemakers.toa.databinding.ItemPreventionFragmentRightBinding;
import org.changemakers.toa.interfaces.ActivityCallbackInterface;
import org.changemakers.toa.ui.FragmentActivity;

public class PreventionFragment extends BottomSheetDialogFragment {

    private static final int ITEM_TYPE_LEFT = 1;
    private static final int ITEM_TYPE_RIGHT = 2;
    private static String[] sPreventionOptions;
    private static String[] sPreventionIcons;
    ActivityCallbackInterface mCallback;
    private FragmentPreventionBinding binding;

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

        binding = FragmentPreventionBinding.inflate(getLayoutInflater());

        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sPreventionOptions = getResources().getStringArray(R.array.prevention_options);
        sPreventionIcons = getResources().getStringArray(R.array.prevention_icons);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(new PreventionRecyclerViewAdapter());
        binding.recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);


        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {

                    ViewCompat.animate(binding.lottieSelectedOption).setDuration(400).alpha(1f).start();
                    //EXPANDED;

                    binding.collapsingToolabar.setTitleEnabled(false);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {

                    //ViewCompat.animate(binding.lottieSelectedOption).setDuration(200).alpha(0f).start();
                    //COLLAPSED;

                    binding.collapsingToolabar.setTitleEnabled(false);
                } else {
                    if (binding.lottieSelectedOption.getAlpha() == 1)
                        ViewCompat.animate(binding.lottieSelectedOption).setDuration(200).alpha(0f).start();

                    //IDDLE
                }
            }
        });


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((FragmentActivity) getActivity()).getDelegate().setSupportActionBar(binding.toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void onItemSelected(View v, int position) {

        if (mCallback != null)
            mCallback.onPreventionOptionSelected(v, position);
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

    private class PreventionRecyclerViewAdapter extends RecyclerView.Adapter<PreventionRecyclerViewAdapter.PreventionViewHolder> {

        public PreventionRecyclerViewAdapter() {

        }

        @NonNull
        @Override
        public PreventionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == ITEM_TYPE_LEFT) {
                ItemPreventionFragmentBinding itemPreventionFragmentBinding = ItemPreventionFragmentBinding.inflate(getLayoutInflater(), parent, false);
                return new PreventionViewHolder(itemPreventionFragmentBinding);
            } else {
                ItemPreventionFragmentRightBinding itemPreventionFragmentRightBinding = ItemPreventionFragmentRightBinding.inflate(getLayoutInflater(), parent, false);
                return new PreventionViewHolder(itemPreventionFragmentRightBinding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull PreventionViewHolder holder, int position) {

            if (getItemViewType(position) == ITEM_TYPE_LEFT) {
                holder.itemviewBinding.optionTitle.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_bg_gradient_tbox_blue));

                holder.itemviewBinding.optionTitle.setText(sPreventionOptions[position]);
                holder.itemviewBinding.lottieAnnimIcon
                        .setAnimation(getResources()
                                .getIdentifier(sPreventionIcons[position], "raw", getActivity().getPackageName()));

            } else {

                holder.itemviewRightBinding.optionTitle.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_bg_gradient_tbox_green_light));

                holder.itemviewRightBinding.optionTitle.setText(sPreventionOptions[position]);
                holder.itemviewRightBinding.lottieAnnimIcon
                        .setAnimation(getResources()
                                .getIdentifier(sPreventionIcons[position], "raw", getActivity().getPackageName()));

            }

        }

        @Override
        public int getItemViewType(int position) {

            return position % 2 != 0 ? ITEM_TYPE_LEFT : ITEM_TYPE_RIGHT;
        }

        @Override
        public int getItemCount() {
            return sPreventionOptions.length;
        }

        class PreventionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ItemPreventionFragmentBinding itemviewBinding;
            ItemPreventionFragmentRightBinding itemviewRightBinding;

            public PreventionViewHolder(ItemPreventionFragmentBinding binding) {
                super(binding.getRoot());
                this.itemviewBinding = binding;
                itemviewBinding.getRoot().setOnClickListener(this);
            }

            public PreventionViewHolder(ItemPreventionFragmentRightBinding binding) {
                super(binding.getRoot());
                this.itemviewRightBinding = binding;
                itemviewRightBinding.getRoot().setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onItemSelected(v, getAdapterPosition());
            }
        }

    }
}
