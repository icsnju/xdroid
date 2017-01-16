package com.nata.xdroid.injector;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.nata.xdroid.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Calvin on 2016/12/26.
 */

// Todo
public class FileInjector {

    public static void injectAudios(Context context) {
        Map<String, Integer> audios =  new HashMap<>();
        audios.put("music1", R.raw.music1);
        audios.put("music2", R.raw.music2);
        audios.put("music3", R.raw.music3);
        batchSaveAudios(context, audios);

    }

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

    private static boolean batchSaveAudios(Context context, Map<String, Integer> audios){
        for(Map.Entry<String,Integer> entry : audios.entrySet()){
            String soundName = entry.getKey();
            int ressound =  entry.getValue();


            byte[] buffer=null;
            InputStream fIn = context.getResources().openRawResource(ressound);
            int size=0;

            try {
                size = fIn.available();
                buffer = new byte[size];
                fIn.read(buffer);
                fIn.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                return false;
            }

            File appDir = new File(Environment.getExternalStorageDirectory(), "Xdroid");

            if (!appDir.exists()) {
                appDir.mkdir();
            }

            String fileName= soundName+".mp3";
            File file = new File(appDir, fileName);

            FileOutputStream save;
            try {
                save = new FileOutputStream(file);
                save.write(buffer);
                save.flush();
                save.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                return false;
            }

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));


            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, soundName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);

            //Insert it into the database
            context.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), values);
        }
        return true;
    }
}
