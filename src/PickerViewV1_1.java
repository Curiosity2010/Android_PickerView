package com.android_final_demo_proj.view.Picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.android_final_demo_proj.util.ScreenUtil;
import com.android_final_demo_proj.util.TextSizeUtil;

/**
 * Created by ruhu on 2018/9/12.
 */

public class PickerViewV1_1 extends View {

    final String TAG = "PickerViewV1_1";

    private Paint paint;
    private Paint txtPaint;
    private Paint txtSelectedPaint;

    private final int rectHeight = 120;
    private final int rectNum = 5;

    private Scroller scroller;


    public PickerViewV1_1(Context context) {
        this(context, null);
    }

    public PickerViewV1_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setBackgroundColor(Color.parseColor("#FFFFFF"));
        scroller = new Scroller(getContext());

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FA6A46"));
        paint.setTextSize(36);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setColor(Color.parseColor("#282828"));
        txtPaint.setTextSize(48);

        txtSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtSelectedPaint.setStyle(Paint.Style.FILL);
        txtSelectedPaint.setColor(Color.parseColor("#01B4F5"));
        txtSelectedPaint.setTextSize(50);
    }


    private int pickerViewWidth;
    private int pickerViewHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //XML中如果将with==wrap_content ,将强制转成屏幕宽度1/2
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = ScreenUtil.getScreenWidthAndHeight(getContext())[0] / 2;
        }
        int heightSize = rectNum * rectHeight;

        pickerViewWidth = widthSize;
        pickerViewHeight = heightSize;
        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pickerViewAdapter == null) {
            return;
        }

        for (int i = 0; i < pickerViewAdapter.getCount(); i++) {
            int topY = (i + 2) * rectHeight;
            String text = pickerViewAdapter.data[i];
            if (i == childIndex) {
                int[] txtSize = TextSizeUtil.getTextWidthAndHeight(txtSelectedPaint, text);
                float x = pickerViewWidth / 2 - txtSize[0] / 2;
                float y = topY + rectHeight / 2 + txtSize[1] / 2;
                canvas.drawText(text, x, y, txtSelectedPaint);
            } else {
                int[] txtSize = TextSizeUtil.getTextWidthAndHeight(txtPaint, text);
                float x = pickerViewWidth / 2 - txtSize[0] / 2;
                float y = topY + rectHeight / 2 + txtSize[1] / 2;
                canvas.drawText(text, x, y, txtPaint);
            }
        }


        int stY = pickerViewHeight / 5 * 2 + offset;
        int btY = pickerViewHeight / 5 * 3 + offset;
        Log.e(TAG, "stY=" + stY + " btY=" + btY);
        canvas.drawLine(0, stY, pickerViewWidth, stY, paint);
        canvas.drawLine(0, btY, pickerViewWidth, btY, paint);
    }

    private int mLastX;
    private int mLastY;
    private int offset;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                scrollBy(0, -deltaY);
                offset = getScrollY();
                Log.w(TAG, "offset=" + offset);
                break;
            case MotionEvent.ACTION_UP:
                if (pickerViewAdapter != null) {
                    int scrollY = getScrollY();

                    childIndex = (scrollY + rectHeight / 2) / rectHeight;

                    childIndex = Math.max(0, Math.min(childIndex, pickerViewAdapter.data.length - 1));

                    Log.i(TAG, "childIndex=" + childIndex);
                    Log.e(TAG, "item=" + pickerViewAdapter.data[childIndex]);
                    int deltaY0 = childIndex * rectHeight - scrollY;
                    graduallyScrollTo(0, deltaY0);
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private int childIndex = 0;

    private void graduallyScrollTo(int destX, int deltaY) {
        scroller.startScroll(0, getScrollY(), 0, deltaY);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            offset = getScrollY();
            postInvalidate();
        }
    }


    public String getSelectItem() {
        return pickerViewAdapter == null ? "" : pickerViewAdapter.getCurrentItem(childIndex);
    }


    private PickerViewAdapter pickerViewAdapter;

    public void setPickerViewAdapter(PickerViewAdapter adapter) {
        pickerViewAdapter = adapter;
    }


    public static class PickerViewAdapter {

        private String[] data;

        public PickerViewAdapter(String[] data) {
            this.data = data;
        }

        public String[] getData() {
            return data;
        }

        public int getCount() {
            return data == null ? 0 : data.length;
        }

        public String getCurrentItem(int childIndex) {
            if (data.length == 0) {
                return null;
            }
            String child = data[childIndex];
            return child;
        }
    }


}
