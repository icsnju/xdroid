package com.nata.xdroid.injector;

/**
 * Created by Calvin on 2016/12/26.
 */

public class TextInjector {
    public static String mock(int inputType) {
        String textType =  TextTypeDetector.detectByInputType(inputType);
        return TextValueDictionary.getInstance().getRandomValidValue(textType);
    }
}
