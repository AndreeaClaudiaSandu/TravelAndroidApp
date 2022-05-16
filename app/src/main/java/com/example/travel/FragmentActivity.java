package com.example.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentActivity extends Fragment {
    private Activity activity;

    public FragmentActivity() {
        // Required empty public constructor
    }

    public static FragmentActivity newInstance(Activity activity) {
        FragmentActivity fragment = new FragmentActivity();
        fragment.setActivity(activity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        ((TextView) root.findViewById(R.id.specificActivityTitle)).setText(activity.getName());
        ((TextView) root.findViewById(R.id.specificActivityDescription)).setText(activity.getDescription());
        ((TextView) root.findViewById(R.id.specificActivityLocation)).setText(activity.getCountry() + ", " + activity.getCity());
        ((TextView) root.findViewById(R.id.specificActiivityPrice)).setText("Starting from: " + activity.getPrice());
        ((ImageView) root.findViewById(R.id.specificActivityImage)).setImageResource(activity.getImage());
        return root;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}