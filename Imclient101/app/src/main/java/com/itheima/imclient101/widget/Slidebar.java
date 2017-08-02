package com.itheima.imclient101.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.itheima.imclient101.R;
import com.itheima.imclient101.adapter.ContactAdapter;
import com.itheima.imclient101.utils.Utils;

import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class Slidebar extends View {
    private String[] sections = {"搜","A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    private Paint paint;
    private int viewWidth;
    private int viewHeight;
    private TextView tv_float;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<String> contact;

    public Slidebar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDimension(R.dimen.slide_text_size));
        paint.setColor(Color.GRAY);
        //setTextAlign 设置文字的对齐方式 CENTER水平居中
        //再去drawText x坐标就不是最左边的坐标 而是要绘制的文字的中间的坐标
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制所有的文字到界面
        for(int i = 0;i<sections.length;i++){
            canvas.drawText(sections[i],viewWidth/2,viewHeight/sections.length*(i+1),paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(tv_float == null){
            ViewGroup parent = (ViewGroup) getParent();
            tv_float = (TextView) parent.findViewById(R.id.tv_float);
            recyclerView = (RecyclerView) parent.findViewById(R.id.recyclerview);
            adapter = (ContactAdapter) recyclerView.getAdapter();
            contact = adapter.getContact();
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 到数组中找到点中的文字

                //设置到中间的textview上
                tv_float.setVisibility(View.VISIBLE);
                tv_float.setText(sections[getSectionIndex((int) event.getY())]);
                //背景要修改为灰色
                setBackgroundResource(R.color.background_gray);
                smoothScrollRecyclerView(sections[getSectionIndex((int) event.getY())]);
                break;
            case MotionEvent.ACTION_MOVE:
                //// 到数组中找到点中的文字
                //设置到中间的textview上
                tv_float.setText(sections[getSectionIndex((int) event.getY())]);
                //recyclerView跟着一起滚动
                smoothScrollRecyclerView(sections[getSectionIndex((int) event.getY())]);
                break;
            case MotionEvent.ACTION_UP:
                //隐藏中间的textview
                tv_float.setVisibility(View.GONE);
                //修改背景为透明的
                setBackgroundColor(Color.TRANSPARENT);
                break;

        }
        return true;
    }

    private int getSectionIndex(int y){
        //计算每一个文字在View中占的高度
        int height = viewHeight / sections.length;
        //计算当前按下的位置 对应的文字在数组中的索引
        int index = y / height;
        //注意做边界的判断
        return index < 0? 0:index >sections.length-1 ? sections.length-1 :index;
    }

    private void smoothScrollRecyclerView(String startChar){
        for(int i=0;i<contact.size();i++){
         if( Utils.getFirstChar(contact.get(i)).equalsIgnoreCase(startChar)){
             recyclerView.smoothScrollToPosition(i);
             return;
         }
        }
    }
}
