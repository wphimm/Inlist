package co.inlist.imageloaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ImageProcess {

	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 50;
		int targetHeight = 50;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth) / 2,
				((float) targetHeight) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
	
	
	public static Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffff0000;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float) 4);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
	    final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    final Canvas canvas = new Canvas(output);
	 
	    final int color = Color.RED;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	 
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawOval(rectF, paint);
	 
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	 
	    bitmap.recycle();
	 
	    return output;
	  }
	 public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
		            .getHeight(), Config.ARGB_8888);
//		    Canvas canvas = new Canvas(output);
//
//		    final int color = 0xff4242DB;
//		    final Paint paint = new Paint();
//		    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//		    final RectF rectF = new RectF(rect);
//		    final float roundPx = bitmap.getWidth()/2;
//
//		    paint.setAntiAlias(true);
//		    canvas.drawARGB(0, 0, 0, 0);
//		    paint.setColor(color);
//		    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//		        //canvas.drawCircle(0, 0, bitmap.getWidth(), paint);
//		    canvas.drawBitmap(bitmap, rect, rect, paint);

		    
		    int w = bitmap.getWidth();                                          
		    int h = bitmap.getHeight();                                         

		    int radius = Math.min(h / 2, w / 2);                                
//		    Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		    Paint p = new Paint();                                              
		    p.setAntiAlias(true);                                               

		    Canvas c = new Canvas(output);                                      
		    c.drawARGB(0, 0, 0, 0);                                             
		    p.setStyle(Style.FILL);                                             

		    c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  

		    p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));                 

		    c.drawBitmap(bitmap, 4, 4, p);                                      
		    p.setXfermode(null);                                                
		    p.setStyle(Style.STROKE);                                           
		    p.setColor(Color.BLACK);                                            
		    p.setStrokeWidth(3);                                                
		    c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                  

		    return output;
		}
}
