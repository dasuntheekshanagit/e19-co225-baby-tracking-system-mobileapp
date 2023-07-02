package com.example.babyone;/*
package com.example.babyone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

*/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *//*

public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     *//*

    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}*/

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.babyone.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class homeFragment extends Fragment {
    // Initialize variables
    /*private ImageView ivImage;
    private TextView tvName;*/
    /*private Button btLogout;*/
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private FragmentHomeBinding binding;

    private TextView txtvHeight;
    private TextView txtvWeight;
    private TextView txtvBMI;
    private String email;
    ArrayList<Long> heightList;
    ArrayList<Long> weightList;

//    public homeFragment (String email) {
////        homeFragment fragment = new homeFragment();
////        Bundle args = new Bundle();
////        args.putString("email", email);
////        fragment.setArguments(args);
////        return fragment;
//            this.email = email;
//            System.out.println(email);
//    }
    public homeFragment() {
        // Required empty public constructor
    }

    //Implement method to set email from MainLanding
    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Binding Elements
        txtvHeight = binding.txtvHeight;
        txtvWeight = binding.txtvWeight;
        txtvBMI = binding.txtvBMI;
        System.out.println("Bundle Email before null" +email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionName = "guardians/";
        System.out.println("Bundle Email not null" +email);

        FirestoreHelper.readFromCollection(db, collectionName, email, new FirestoreHelper.FirestoreDataCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Map<String, Object>> dataMap) {
                long weight = 0;
                long height = 0;

                // Handle the retrieved data here
                for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
                    Map<String, Object> data = entry.getValue();
                    for (Map.Entry<String, Object> fieldEntry : data.entrySet()) {
                        String fieldName = fieldEntry.getKey();
                        Object fieldValue = fieldEntry.getValue();
                        //Retrive baby height
                        if (fieldName.equals("baby_height")) {
                            heightList = (ArrayList<Long>) fieldValue;
                            height = heightList.get(heightList.size() - 1);
                            txtvHeight.setText(height + "cm");
                        }
                        //Retrive baby weight
                        if (fieldName.equals("baby_weight")) {
                            weightList = (ArrayList<Long>)fieldValue;
                            weight = weightList.get(weightList.size() - 1);
                            txtvWeight.setText(weight + "kg");
                        }
                    }
                }

                // Calculate BMI
                double heightInMeter = height / 100.0; // Convert height to meters
                double bmi = weight / Math.pow(heightInMeter, 2);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedBMI = decimalFormat.format(bmi);

                // Set BMI value to TextView
                txtvBMI.setText(formattedBMI);
            }
        });

        // Initialize sign-in client
        //googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        /*btLogout.setOnClickListener(v -> {
            // Sign out from Google
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Check condition
                    if (task.isSuccessful()) {
                        // When task is successful, sign out from Firebase
                        firebaseAuth.signOut();
                        // Display Toast
                        Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                        // Finish activity
                        requireActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

