package com.blue.rchina.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.utils.UIUtils;


/**
 * Created by cj on 2018/3/15.
 */

public class GoodsView extends LinearLayout {

    public int width;
    public int height;
    public Paint paint;
    public DefaultViewClicker defaultViewClicker;
    public int balanceFactor;

    public interface onViewClick{
        void onLeftClick(GoodsView view);
        void onRightClick(GoodsView view);
    }

    public class DefaultViewClicker implements onViewClick{

        @Override
        public void onLeftClick(GoodsView view) {
            int mcount = getCount();
            if (mcount>1) {
                setCount(mcount - 1);
            }else {
                UIUtils.showToast("不能再减少了");
            }
        }

        @Override
        public void onRightClick(GoodsView view) {
            setCount(getCount()+1);
        }
    }
    private onViewClick viewClickListener;

    public void setViewClickListener(onViewClick viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public int  getCount(){
        if (center != null) {
            int i = Integer.parseInt(center.getText().toString());
            return i;
        }
        return 0;
    }

    public void setCount(int count) {
        if (center != null) {
            center.setText(count+"");
            invalidate();
        }
    }

    public Context context;
    public TextView left;
    public LayoutParams leftP;
    private TextView right;
    private LayoutParams rightP;
    public TextView center;
    public LayoutParams params;

    public GoodsView(Context context) {
        super(context);
    }

    public GoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public GoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GoodsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs){
        context = getContext();
        defaultViewClicker = new DefaultViewClicker();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoodsView);

        int color = typedArray.getColor(R.styleable.GoodsView_color, getResources().getColor(R.color.price_red));
        float dimension = typedArray.getDimension(R.styleable.GoodsView_size, 20);
        String text = typedArray.getString(R.styleable.GoodsView_text);
        if (text == null) {
            text="1";
        }
        int diviedWidth = (int) typedArray.getDimension(R.styleable.GoodsView_dividsize,2);

        left = new TextView(context);
        left.setBackgroundResource(R.drawable.goods_view_bg);
        left.setTextSize(dimension);
        left.setTextColor(color);
        left.setText("－");
        left.setGravity(Gravity.CENTER);
        leftP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

        leftP.setMargins(diviedWidth, diviedWidth, diviedWidth, diviedWidth);
        this.left.setLayoutParams(leftP);
        leftP.weight= 1;
        leftP.gravity= Gravity.CENTER;
        this.left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClickListener != null) {
                    viewClickListener.onLeftClick(GoodsView.this);
                }else {
                    defaultViewClicker.onLeftClick(GoodsView.this);
                }

            }
        });
        addView(this.left);


        center = new TextView(context);
        center.setTextSize(dimension);
        center.setBackgroundResource(R.color.white);
        /*用于避免三个控件一般大*/
        balanceFactor = 20;
        params = new LayoutParams(balanceFactor, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, diviedWidth,0, diviedWidth);
        params.weight= 1;
        params.gravity=Gravity.CENTER;
        center.setGravity(Gravity.CENTER);
        center.setLayoutParams(params);
        center.setText(""+text);
        addView(center);


        right = new TextView(context);
        right.setBackgroundResource(R.drawable.goods_view_bg);
        right.setTextSize(dimension);
        right.setTextColor(color);
        right.setText("＋");
        right.setGravity(Gravity.CENTER);
        rightP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        rightP.setMargins(diviedWidth, diviedWidth, diviedWidth, diviedWidth);
        right.setLayoutParams(rightP);
        rightP.weight= 1;
        rightP.gravity= Gravity.CENTER;
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClickListener != null) {
                    viewClickListener.onRightClick(GoodsView.this);
                }else {
                    defaultViewClicker.onRightClick(GoodsView.this);
                }

            }
        });
        addView(right);




    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawColor(getResources().getColor(R.color.red));
        /*画分隔线*/
        /*paint = new Paint();
        paint.setColor(getResources().getColor(R.color.red));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawRect(0,0,width,height, paint);
        canvas.drawLine(20f,0f,20f,100f,paint);*/
    }
}
