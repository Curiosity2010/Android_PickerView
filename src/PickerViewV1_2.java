package com.android_final_demo_proj.view.Picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.android_final_demo_proj.R;
import com.android_final_demo_proj.util.ScreenUtil;
import com.android_final_demo_proj.util.TextSizeUtil;

/**
 * Created by ruhu on 2018/9/12.
 */

public class PickerViewV1_2 extends View {

    final String TAG = "PickerViewV1_2";

    private Paint linePaint;
    private Paint txtUnSelectPaint;
    private Paint txtSelectedPaint;

    private int mRectHeight = 120;
    private int mRectNum = 5;

    private Scroller scroller;

    public PickerViewV1_2(Context context) {
        this(context, null);
    }

    public PickerViewV1_2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        scroller = new Scroller(getContext());
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        txtUnSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtUnSelectPaint.setStyle(Paint.Style.FILL);
        txtSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtSelectedPaint.setStyle(Paint.Style.FILL);

        mRectHeight = 120;
        mRectNum = 5;
        int backgroundColor = Color.parseColor("#FFFFFF");

        int lineColor = Color.parseColor("#FA6A46");
        int lineWidth = 4;

        int unSelectTxtColor = Color.parseColor("#282828");
        float unSelectTxtSize = 48;

        int selectTxtColor = Color.parseColor("#01B4F5");
        float selectTxtSize = 50;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PickerViewV1_2Style);

            mRectHeight = typedArray.getDimensionPixelSize(R.styleable.PickerViewV1_2Style_rectHeight, 120);
            if (mRectHeight <= 100) {
                mRectHeight = 120;
            }

            mRectNum = typedArray.getInt(R.styleable.PickerViewV1_2Style_rectNum, 5);
            if (mRectNum <= 0) {
                mRectNum = 1;
            }
            backgroundColor = typedArray.getColor(R.styleable.PickerViewV1_2Style_backgroundColor, Color.parseColor("#FFFFFF"));

            lineColor = typedArray.getColor(R.styleable.PickerViewV1_2Style_lineColor, Color.parseColor("#FA6A46"));
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.PickerViewV1_2Style_lineWidth, 4);
            if (lineWidth > 6) {
                lineWidth = 6;
            }
            if (lineWidth < 0) {
                lineWidth = 1;
            }

            unSelectTxtColor = typedArray.getColor(R.styleable.PickerViewV1_2Style_unSelectTxtColor, Color.parseColor("#282828"));
            unSelectTxtSize = typedArray.getDimensionPixelSize(R.styleable.PickerViewV1_2Style_unSelectTxtSize, 48);
            if (unSelectTxtSize >= mRectHeight) {
                unSelectTxtSize = mRectHeight / 3 + 10;
            }
            selectTxtColor = typedArray.getColor(R.styleable.PickerViewV1_2Style_selectTxtColor, Color.parseColor("#01B4F5"));
            selectTxtSize = typedArray.getDimensionPixelSize(R.styleable.PickerViewV1_2Style_selectTxtSize, 50);
            if (selectTxtSize >= mRectHeight) {
                selectTxtSize = mRectHeight / 3 + 14;
            }
            typedArray.recycle();
        }
        setBackgroundColor(backgroundColor);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        txtUnSelectPaint.setColor(unSelectTxtColor);
        txtUnSelectPaint.setTextSize(unSelectTxtSize);
        txtSelectedPaint.setColor(selectTxtColor);
        txtSelectedPaint.setTextSize(selectTxtSize);
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
        int heightSize = mRectNum * mRectHeight;

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
            int topY = (i + mRectNum / 2) * mRectHeight;
            String text = pickerViewAdapter.data[i];
            if (i == childIndex) {
                int[] txtSize = TextSizeUtil.getTextWidthAndHeight(txtSelectedPaint, text);
                float x = pickerViewWidth / 2 - txtSize[0] / 2;
                float y = topY + mRectHeight / 2 + txtSize[1] / 2;
                canvas.drawText(text, x, y, txtSelectedPaint);
            } else {
                int[] txtSize = TextSizeUtil.getTextWidthAndHeight(txtUnSelectPaint, text);
                float x = pickerViewWidth / 2 - txtSize[0] / 2;
                float y = topY + mRectHeight / 2 + txtSize[1] / 2;
                canvas.drawText(text, x, y, txtUnSelectPaint);
            }
        }

        int stY = pickerViewHeight / mRectNum * (mRectNum / 2) + offset;
        int btY = pickerViewHeight / mRectNum * (mRectNum / 2 + 1) + offset;
        Log.e(TAG, "stY=" + stY + " btY=" + btY);
        canvas.drawLine(0, stY, pickerViewWidth, stY, linePaint);
        canvas.drawLine(0, btY, pickerViewWidth, btY, linePaint);
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

                    childIndex = (scrollY + mRectHeight / 2) / mRectHeight;

                    childIndex = Math.max(0, Math.min(childIndex, pickerViewAdapter.data.length - 1));

                    Log.i(TAG, "childIndex=" + childIndex);
                    Log.e(TAG, "item=" + pickerViewAdapter.data[childIndex]);
                    int deltaY0 = childIndex * mRectHeight - scrollY;
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
