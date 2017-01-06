package com.nata.xdroid.injector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.nata.xdroid.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Calvin on 2016/12/26.
 */

// Todo
public class FileInjector {
    public static void injectImages(Context context) {
        Map<String, Integer> images =  new HashMap<>();
        images.put("image1", R.drawable.image1);
        images.put("image2", R.drawable.image2);
        images.put("image3", R.drawable.image3);
        images.put("image4", R.drawable.image4);
        images.put("image5", R.drawable.image5);
        images.put("image6", R.drawable.image6);
        batchAddImages(context, images);

    }

    private static void batchAddImages(Context context, Map<String, Integer> images) {
        for(Map.Entry<String,Integer> entry : images.entrySet()){
            Drawable drawable = context.getResources().getDrawable(entry.getValue());
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            MediaStore.Images.Media.insertImage(
                    context.getContentResolver(),
                    bitmap,
                    entry.getKey(),
                    entry.getKey()
            );
        }

    }
}
