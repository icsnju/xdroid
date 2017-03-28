package com.nata.xdroid.ui;

/**
 * Created by Calvin on 2016/12/4.
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by MarioStudio on 2016/5/24.
 */

public class FloatWindow {

    private WindowManager.LayoutParams mLayoutParams;
    private DisplayMetrics mDisplayMetrics;
    private WindowManager mWindowManager;
    private Context mContext;

    private View mContentView;

    private static final int WHAT_HIDE = 0x275;
    private final float DISTANCE = 15.0f;
    private float offsetX, offsetY;

    private long lastTouchTimeMillis;
    private long downTimeMillis;

    private boolean mIsShowing;
    private float downX, downY;
    private float oldX, oldY;
    private boolean isOpen;

    private View mFloatView, mPlayerView;

    public FloatWindow(Context context) {
        this(context, null, null);
    }

    public FloatWindow(Context context, View PlayerView, View floatView) {
        this.mContext = context;
        setFloatView(floatView);
        setPlayerView(PlayerView);
        initWindowManager();
        initLayoutParams();
    }

    public void setPlayerView(View PlayerView) {
        if (PlayerView != null) {
            BackgroundView backgroundView = new BackgroundView(getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            PlayerView.setOnTouchListener(new TouchIntercept());
            PlayerView.setLayoutParams(layoutParams);
            backgroundView.addView(PlayerView);
            this.mPlayerView = backgroundView;
        }
    }

    public void setFloatView(View floatView) {
        if (floatView != null) {
            this.mFloatView = floatView;
            setContentView(mFloatView);
        }
    }

    private void setContentView(View contentView) {
        if (contentView != null) {
            if (isShowing()) {
                getWindowManager().removeView(mContentView);
                createContentView(contentView);
                getWindowManager().addView(mContentView, getLayoutParams());
                updateLocation(getDisplayMetrics().widthPixels / 2, getDisplayMetrics().heightPixels / 2, true);
            } else {
                createContentView(contentView);
            }
        }
    }

    private void createContentView(View contentView) {
        this.mContentView = contentView;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); // 主动计算视图View的宽高信息
        offsetY = getStatusBarHeight(getContext()) + contentView.getMeasuredHeight() / 2;
        offsetX = contentView.getMeasuredWidth() / 2;
        contentView.setOnTouchListener(new WindowTouchListener());
    }

    public Context getContext() {
        return this.mContext;
    }

    public WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            initLayoutParams();
        }
        return mLayoutParams;
    }

    public DisplayMetrics getDisplayMetrics() {
        if (mDisplayMetrics == null) {
            mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        }
        return mDisplayMetrics;
    }

    public View getContentView() {
        return mContentView;
    }

    private void initWindowManager() {
        mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    private void initLayoutParams() {
        getLayoutParams().flags = getLayoutParams().flags
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        getLayoutParams().dimAmount = 0.2f;
        getLayoutParams().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
        getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
        getLayoutParams().gravity = Gravity.LEFT | Gravity.TOP;
        getLayoutParams().format = PixelFormat.RGBA_8888;
        getLayoutParams().alpha = 1.0f;  //  设置整个窗口的透明度
        offsetX = 0;
        offsetY = getStatusBarHeight(getContext());
        getLayoutParams().x = (int) (mDisplayMetrics.widthPixels - offsetX);
        getLayoutParams().y = (int) (mDisplayMetrics.heightPixels * 1 / 4 - offsetY);
    }

    public int getStatusBarHeight(Context context) {
        int height = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = context.getResources().getDimensionPixelSize(resId);
        }
        return height;
    }

    private void updateLocation(float x, float y, boolean offset) {
        if (getContentView() != null) {
            if (offset) {
                getLayoutParams().x = (int) (x - offsetX);
                getLayoutParams().y = (int) (y - offsetY);
            } else {
                getLayoutParams().x = (int) x;
                getLayoutParams().y = (int) y;
            }
            getWindowManager().updateViewLayout(mContentView, getLayoutParams());
        }
    }

    public void show() {
        if (getContentView() != null && !isShowing()) {
            getWindowManager().addView(getContentView(), getLayoutParams());
            mIsShowing = true;
            if (!isOpen) {
                handler.sendEmptyMessage(WHAT_HIDE);
            }
        }
    }

    public void dismiss() {
        if (getContentView() != null && isShowing()) {
            handler.removeMessages(WHAT_HIDE);
            getWindowManager().removeView(getContentView());
            mIsShowing = false;
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    private ValueAnimator alignAnimator(float x, float y) {
        ValueAnimator animator = null;
        if (x <= getDisplayMetrics().widthPixels / 2) {
            animator = ValueAnimator.ofObject(new PointEvaluator(), new Point((int) x, (int) y), new Point(0, (int) y));
        } else {
            animator = ValueAnimator.ofObject(new PointEvaluator(), new Point((int) x, (int) y), new Point(getDisplayMetrics().widthPixels, (int) y));
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                updateLocation(point.x, point.y, true);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lastTouchTimeMillis = System.currentTimeMillis();
                handler.sendEmptyMessage(WHAT_HIDE);
            }
        });
        animator.setDuration(160);
        return animator;
    }

    public class PointEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object from, Object to) {
            Point startPoint = (Point) from;
            Point endPoint = (Point) to;
            float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
            float y = startPoint.y + fraction * (endPoint.y - startPoint.y);
            Point point = new Point((int) x, (int) y);
            return point;
        }
    }

    public void showPlayer() {
        if (isOpen) {
            return;
        }
        getLayoutParams().flags &= ~(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);// 取消WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE属性
        getLayoutParams().height = WindowManager.LayoutParams.MATCH_PARENT;  //
        getLayoutParams().width = WindowManager.LayoutParams.MATCH_PARENT;  //
        oldX = getLayoutParams().x;
        oldY = getLayoutParams().y;
        setContentView(mPlayerView);
        handler.removeMessages(WHAT_HIDE);
        isOpen = true;
    }

    public void turnMini() {
        if (!isOpen) {
            return;
        }
        isOpen = false;
        getLayoutParams().flags &= ~(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);// 取消WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE属性
        getLayoutParams().flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//重新设置WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE属性
        getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
        getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
        setContentView(mFloatView);
        getLayoutParams().alpha = 1.0f;
        updateLocation(oldX, oldY, false);
        lastTouchTimeMillis = System.currentTimeMillis();
        handler.sendEmptyMessage(WHAT_HIDE);
    }

    class BackgroundView extends RelativeLayout {

        public BackgroundView(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null && state.isTracking(event) && !event.isCanceled()) {
                        turnMini();
                        return true;
                    }
                }
                return super.dispatchKeyEvent(event);
            } else {
                return super.dispatchKeyEvent(event);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    turnMini();
                    return true;
                case MotionEvent.ACTION_OUTSIDE:
                    turnMini();
                    return true;
            }
            return super.onTouchEvent(event);
        }
    }

    class TouchIntercept implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    lastTouchTimeMillis = System.currentTimeMillis();
                    break;
            }
            return true;
        }
    }

    class WindowTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isOpen) {
                        down(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isOpen) {
                        move(event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isOpen) {
                        up(event);
                    }
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    if (isOpen) {
                        turnMini();
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    private void down(MotionEvent event) {
        downX = event.getRawX();
        downY = event.getRawY();
        getLayoutParams().alpha = 1.0f;
        downTimeMillis = System.currentTimeMillis();
        lastTouchTimeMillis = System.currentTimeMillis();
        getWindowManager().updateViewLayout(getContentView(), getLayoutParams());
//        updateLocation(event.getRawX(), event.getRawY(), true);
    }

    private void move(MotionEvent event) {
        lastTouchTimeMillis = System.currentTimeMillis();
        updateLocation(event.getRawX(), event.getRawY(), true);
    }

    private void up(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        if (x >= downX - DISTANCE && x <= downX + DISTANCE && y >= downY - DISTANCE && y <= downY + DISTANCE) {
            if (System.currentTimeMillis() - downTimeMillis > 1200) {
                //  Long Click
            } else {
                showPlayer();  //Click
            }
        } else {
            ValueAnimator animator = alignAnimator(x, y);
            animator.start();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_HIDE:
                    if (System.currentTimeMillis() - lastTouchTimeMillis >= 3500) {
                        if (!isOpen) {
                            getLayoutParams().alpha = 0.4f;
                            getWindowManager().updateViewLayout(getContentView(), getLayoutParams());
                        }
                    } else {
                        if (isOpen) {
                            lastTouchTimeMillis = System.currentTimeMillis() + 3500;
                        }
                        handler.sendEmptyMessageDelayed(WHAT_HIDE, 200);
                    }
                    break;
            }
        }
    };
}

