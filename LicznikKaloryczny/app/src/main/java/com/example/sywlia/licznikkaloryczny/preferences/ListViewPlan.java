package com.example.sywlia.licznikkaloryczny.preferences;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewPlan extends ListView {
    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public ListViewPlan(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * height;
            setLayoutParams(params);

            int width = getChildAt(0).getWidth() + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.width = getCount() * width;
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }
}