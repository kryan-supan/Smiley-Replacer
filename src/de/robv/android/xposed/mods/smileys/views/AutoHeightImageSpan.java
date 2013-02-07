package de.robv.android.xposed.mods.smileys.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

public class AutoHeightImageSpan extends ReplacementSpan {
    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the bottom of the surrounding text, i.e., at the same level as the
     * lowest descender in the text.
     */
    public static final int ALIGN_BOTTOM = 0;
    
    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 1;
    
    private final int mVerticalAlignment;
    private final Drawable mDrawable;
    private final int mDrawableHeight;
    private final int mDrawableWidth;
	
	public AutoHeightImageSpan(Drawable drawable) {
		this(drawable, ALIGN_BOTTOM);
    }

	public AutoHeightImageSpan(Drawable drawable, int verticalAlignment) {
		mDrawable = drawable;
		mDrawableHeight = drawable.getIntrinsicHeight();
		mDrawableWidth = drawable.getIntrinsicWidth();
	    mVerticalAlignment = verticalAlignment;
    }
	

	@Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
	    canvas.save();
        
        float transY = y - mDrawable.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BOTTOM)
        	transY += paint.getFontMetrics().descent;
        
        canvas.translate(x, transY);
        mDrawable.draw(canvas);
        canvas.restore();
    }
	
	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
		float scale = 1.0f;
    	if (mVerticalAlignment == ALIGN_BASELINE)
    		scale = -paint.getFontMetrics().ascent / mDrawableHeight;
    	else
    		scale = paint.getFontSpacing() / mDrawableHeight;
    	
    	int height = (int)(mDrawableHeight * scale + 0.5f);
    	int width = (int)(mDrawableWidth * scale + 0.5f);
    	mDrawable.setBounds(0, 0, width, height);
    	
        if (fm != null) {
            fm.ascent = -height; 
            fm.descent = 0; 

            fm.top = fm.ascent;
            fm.bottom = 0;
        }
        return width;
	}
}
