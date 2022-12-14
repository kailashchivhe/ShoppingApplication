package com.kai.ninerauth.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kai.ninerauth.Model.User;
import com.kai.ninerauth.R;
import com.kai.ninerauth.databinding.FragmentProfileBinding;
import com.kai.ninerauth.listener.ProfileRetrivalListener;
import com.kai.ninerauth.listener.ProfileUpdateListener;
import com.kai.ninerauth.ui.login.LoginViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    ProfileViewModel profileViewModel;
    String jwtToken;
    String email;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getProfileLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user !=null){
                    setProfileDetails(user);
                }
            }
        });
    }

    private void setProfileDetails(User user) {
        binding.editTextProfileFirstName.setText(user.getFirstName());
        binding.editTextProfileLastName.setText(user.getLastName());
        binding.editTextTextProfieEmail.setText(user.getEmail());
        binding.editTextProfilePassword.setText(user.getPassword());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        email = sharedPreferences.getString("email", "");
        profileViewModel.profileRetrival(email,jwtToken);
//        binding.editTextProfileFirstName.setText(jwtToken);

        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateClicked();
            }
        });

        binding.buttonProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClicked();
            }
        });
    }

    void onLogoutClicked() {
        //TODO: logout user and delete jwt token
        sharedPreferences.edit().remove("jwtToken").commit();
        NavHostFragment.findNavController(ProfileFragment.this)
                .navigate(R.id.action_ProfileFragment_to_LoginFragment);
    }

    void onUpdateClicked() {
        //TODO: update profile
        String firstName = binding.editTextProfileFirstName.getText().toString();
        String lastName = binding.editTextProfileLastName.getText().toString();
        String email = binding.editTextTextProfieEmail.getText().toString();
        String password = binding.editTextProfilePassword.getText().toString();
        User user = new User(firstName,lastName,email,password,jwtToken);
        profileViewModel.profileUpdate(user);
    }

}