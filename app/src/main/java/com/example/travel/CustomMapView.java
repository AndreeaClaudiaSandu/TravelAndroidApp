package com.example.travel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class CustomMapView extends MapView {


    public CustomMapView(@NonNull Context context) {
        super(context);
    }

    public CustomMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMapView(@NonNull Context context, @Nullable GoogleMapOptions options) {
        super(context, options);
    }

    public CustomMapView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        // Handle MapView's touch events.
        super.dispatchTouchEvent(ev);
        return true;
    }

}
