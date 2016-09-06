package com.gan.lenka.ratingbardemo;

import android.app.Activity;
import android.os.Bundle;

import com.celery.celeryratingbar.CeleryRatingBar;

/**
 * Created by ganwenjun on 16/9/1.
 */
public class MainActivity extends Activity {

    private CeleryRatingBar celeryRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celeryRatingBar = (CeleryRatingBar) findViewById(R.id.rating);
        celeryRatingBar.setOnRatingChangeListener(new CeleryRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float before, float after, boolean isUser) {

            }
        });
    }
}
