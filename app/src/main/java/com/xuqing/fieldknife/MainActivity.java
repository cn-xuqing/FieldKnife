package com.xuqing.fieldknife;

import android.app.Activity;
import android.os.Bundle;

import test.Test;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(">>>>>>>>>>"+Test.TEST);
    }
}
