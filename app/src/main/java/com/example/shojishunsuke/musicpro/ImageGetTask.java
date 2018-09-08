package com.example.shojishunsuke.musicpro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.IslamicCalendar;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageGetTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView image;
    private  String     tag;

    public ImageGetTask(ImageView _image){
        super();
        image= _image;
        tag = image.getTag().toString();
    }
    @Override
    protected Bitmap doInBackground(String... params){
        Bitmap bitmap = ImageCache.getImage(params[0]);
        if (bitmap==null){
            bitmap = decodeBitmap(params[0],72,72);
            ImageCache.setImage(params[0],bitmap);
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result){
        if (tag.equals(image.getTag()))image.setImageBitmap(result);
    }

    public static Bitmap decodeBitmap(String path, int width, int height){
        final BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeFile(path,options);
          options.inSampleSize = calculateInSampleSize(options,width,height);
          options.inJustDecodeBounds = false;

          return BitmapFactory.decodeFile(path,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height>reqHeight || width>reqWidth){
            if (width>height){
                inSampleSize = Math.round((float)height/(float)reqHeight);
            }else {
                inSampleSize = Math.round((float)width/(float)reqWidth);
            }
        }
        return inSampleSize;
    }

}
