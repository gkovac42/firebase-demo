package com.example.goran.firebasedemo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    @BindView(R.id.img_profile) ImageView imgProfile;
    @BindView(R.id.txt_display_name) TextView txtDisplayName;
    @BindView(R.id.txt_email) TextView txtEmail;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(getContext())
                        .load(currentUser.getPhotoUrl())
                        .into(imgProfile);
            }

            if (currentUser.getDisplayName() != null) {
                txtDisplayName.setText(currentUser.getDisplayName());
            }

            if (currentUser.getEmail() != null) {
                txtEmail.setText(currentUser.getEmail());
            }
        }
    }

    @OnClick(R.id.btn_sign_out)
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
