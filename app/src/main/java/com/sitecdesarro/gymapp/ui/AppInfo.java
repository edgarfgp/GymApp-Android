package com.sitecdesarro.gymapp.ui;

import android.graphics.drawable.Drawable;

import com.sitecdesarro.gymapp.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class AppInfo extends WelcomeActivity{

    @Override
    protected WelcomeConfiguration configuration() {
        return  new WelcomeConfiguration.Builder(this).defaultBackgroundColor
                (com.stephentuso.welcome.R.color.primary_material_light)
                .page(new BasicPage
                (R.drawable.bike,
                "Bikes",
                "You can select your favorite bike.")
                .background(R.color.colorPrimary)
        )
                .page(new BasicPage(R.drawable.functional,
                        "Funtional",
                        "Reserve your place in your functional class.")
                        .background(R.color
                        .colorPrimary)
                ).swipeToDismiss(true)
                .build();
    }
}
