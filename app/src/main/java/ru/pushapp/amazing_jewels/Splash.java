package ru.pushapp.amazing_jewels;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.Navigation;

public class Splash extends Fragment {

    ImageView animImage;
    AnimatedVectorDrawableCompat avd;
    Animatable animatable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment, container, false);


        animImage = view.findViewById(R.id.splash_anim);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        avd = AnimatedVectorDrawableCompat.create(getContext(),R.drawable.avd_anim);
        animImage.setImageDrawable(avd);

        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                Log.i("TESTanim","end");
                Navigation.findNavController(animImage).navigate(R.id.action_splash_to_startFragment);
            }
        });

        animatable = (Animatable) animImage.getDrawable();
        animatable.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TESTanim", "pause");
        avd.clearAnimationCallbacks();
        animatable.stop();
    }
}
