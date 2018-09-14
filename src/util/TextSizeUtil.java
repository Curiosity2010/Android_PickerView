package com.android_final_demo_proj.util;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by ruhu on 2018/6/21.
 */

public class TextSizeUtil {

    /**
     * 测量字体宽高
     * @param paint
     * @param text
     * @return
     */
    public static int[] getTextWidthAndHeight(Paint paint, String text){
        int[] result=new int[2];
        Rect rect=new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        int width=rect.width();
        int height=rect.height();
        result[0]=width;
        result[1]=height;
        return result;
    }

    private TextSizeUtil(){}
}
