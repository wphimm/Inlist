package co.inlist.imageloaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewRoundedConrners extends ImageView {

	public ImageViewRoundedConrners(Context context) {
		super(context);
	}

	public ImageViewRoundedConrners(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageViewRoundedConrners(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		BitmapDrawable drawable = (BitmapDrawable) getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}

		Bitmap fullSizeBitmap = drawable.getBitmap();

		int scaledWidth = getMeasuredWidth();
		int scaledHeight = getMeasuredHeight();

		Bitmap mScaledBitmap;
		if (scaledWidth == fullSizeBitmap.getWidth()
				&& scaledHeight == fullSizeBitmap.getHeight()) {
			mScaledBitmap = fullSizeBitmap;
		} else {
			mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap,
					scaledWidth, scaledHeight, true /* filter */);
		}

		// Bitmap roundBitmap = getRoundedCornerBitmap(mScaledBitmap);

		// Bitmap roundBitmap = getRoundedCornerBitmap(getContext(),
		// mScaledBitmap, 10, scaledWidth, scaledHeight, false, false,
		// false, false);
		// canvas.drawBitmap(roundBitmap, 0, 0, null);

		Bitmap circleBitmap = getRoundedCornerBitmap(mScaledBitmap);

		canvas.drawBitmap(circleBitmap, 0, 0, null);

	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int radius = Math.min(h/2, w/2);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(null);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.parseColor("#c5d2d4"));
		paint.setStrokeWidth(8);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		canvas.drawRect(rect, paint);

		return output;
	}

	Bitmap getCircledBitmap(Bitmap bitmap) {

		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(result);

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int radius = Math.min(h / 2, w / 2);
		int color = Color.BLUE;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getHeight() / 2, paint);

		paint.setXfermode(null);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.parseColor("#c5d2d4"));
		paint.setStrokeWidth(8);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		canvas.drawCircle((w / 2), (h / 2), radius, paint);

		return result;
	}

}