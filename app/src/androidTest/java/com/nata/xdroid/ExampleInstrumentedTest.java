package com.nata.xdroid;


import android.test.ActivityInstrumentationTestCase2;

import com.nata.xdroid.ui.SettinigsActivity;

public class ExampleInstrumentedTest extends ActivityInstrumentationTestCase2 {

    public ExampleInstrumentedTest() {
        super(SettinigsActivity.class);
        System.out.println("come");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testStartClose() throws Exception {
        Thread.sleep(1000);
    }
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }



}