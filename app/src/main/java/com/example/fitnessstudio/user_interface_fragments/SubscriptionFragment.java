package com.example.fitnessstudio.user_interface_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.heart_rate.HeartRateActivity;
import com.example.fitnessstudio.user_interface.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SubscriptionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_subscription, container, false);
        AppCompatButton buySubscription=view.findViewById(R.id.buy_subscription);
        ListView listView=view.findViewById(R.id.recycler_view_subscription);
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(requireContext(),R.layout.subscription_item,arrayList);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
             public void handleOnBackPressed() {
                 setEnabled(false);
                FragmentManager fragmentManager=getParentFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_user_interface,new MainUserInterface());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}