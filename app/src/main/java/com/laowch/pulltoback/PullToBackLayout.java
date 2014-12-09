package com.laowch.pulltoback;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


public class PullToBackLayout extends ViewGroup {


    public static int BACK_FACTOR = 3;

    private View target;

    private View scrollChild;

    private final ViewDragHelper dragHelper;

    private OnPullToBackListener onPullToBackListener;
    private OnDraggingBorderChangeListener onDraggingBorderChangeListener;

    boolean isBacking;

    private int verticalRange;

    private int draggingBorder;

    private int draggingState = 0;

    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;


    public PullToBackLayout(Context context) {
        this(context, null);
    }


    public PullToBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        dragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());

        onPullToBackListener = new DefaultPullToBackListener();

        onDraggingBorderChangeListener = new DefaultDraggingListener();

        setWillNotDraw(false);

        setBackgroundColor(0xCC303030);
    }


    public boolean pullToBack() {
        ensureTarget();
        isBacking = true;
        if (dragHelper.smoothSlideViewTo(target, 0, verticalRange)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    public void setScrollChild(View scrollChild) {
        this.scrollChild = scrollChild;
    }


    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid out yet.
        if (target == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException(
                        "SwipeRefreshLayout can host only one direct child");
            }
            target = getChildAt(0);
            //  mOriginalOffsetTop = target.getTop() + getPaddingTop();

            if (scrollChild == null) {
                scrollChild = target;
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (getChildCount() > 0) {
            getChildAt(0).measure(
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                            MeasureSpec.EXACTLY));
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (scrollChild instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) scrollChild;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return scrollChild.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(scrollChild, -1);
        }
    }


    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            // Does ViewCompat.canScrollVertically support api < 14 ?
            return ViewCompat.canScrollVertically(scrollChild, 1);
        } else {
            return ViewCompat.canScrollVertically(scrollChild, 1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        boolean handled = false;
        if (isEnabled() && !canChildScrollDown() || !canChildScrollUp()) {
            handled = dragHelper.shouldInterceptTouchEvent(ev);
        } else {
            dragHelper.cancel();
        }
        return !handled ? super.onInterceptTouchEvent(ev) : handled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    public void setOnPullToBackListener(OnPullToBackListener onPullToBackListener) {
        this.onPullToBackListener = onPullToBackListener;
    }

    public void setOnDraggingListener(OnDraggingBorderChangeListener onDraggingBorderChangeListener) {
        this.onDraggingBorderChangeListener = onDraggingBorderChangeListener;
    }

    public interface OnPullToBackListener {
        public void onBack();

        public void onShowTips();

        public void onDismissTips();
    }

    public interface OnDraggingBorderChangeListener {
        public void onDraggingTopChange(int top, int range);
    }


    public class DragHelperCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == target;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (state == draggingState) { // no change
                return;
            }
            if ((draggingState == ViewDragHelper.STATE_DRAGGING || draggingState == ViewDragHelper.STATE_SETTLING) &&
                    state == ViewDragHelper.STATE_IDLE) {
                // the view stopped from moving.

                if (draggingBorder == 0) {
                    onStopDraggingToClosed();
                } else if (draggingBorder == verticalRange) {

                    if (onPullToBackListener != null) {
                        onPullToBackListener.onBack();
                    }
                }
            }
            if (state == ViewDragHelper.STATE_DRAGGING) {
                onStartDragging();
            }
            draggingState = state;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return verticalRange;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            draggingBorder = Math.abs(top);

            if (onPullToBackListener != null) {
                if (draggingBorder >= verticalRange / BACK_FACTOR) {
                    onPullToBackListener.onShowTips();
                } else {
                    onPullToBackListener.onDismissTips();
                }
            }

            if (onDraggingBorderChangeListener != null) {
                onDraggingBorderChangeListener.onDraggingTopChange(top, verticalRange);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int returnValue;

            if (!canChildScrollUp()) {
                final int topBound = getPaddingTop();
                final int bottomBound = verticalRange;
                returnValue = Math.min(Math.max(top, topBound), bottomBound);
            } else {
                final int topBound = -verticalRange;
                final int bottomBound = getPaddingTop();
                returnValue = Math.min(Math.max(top, topBound), bottomBound);
            }
            return returnValue;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final float rangeToCheck = verticalRange;
            if (draggingBorder == 0) {
                return;
            }
            if (draggingBorder == rangeToCheck) {
                return;
            }
            boolean settleToOpen = false;
            if (yvel > AUTO_OPEN_SPEED_LIMIT) { // speed has priority over position
                settleToOpen = !canChildScrollUp();
            } else if (yvel < -AUTO_OPEN_SPEED_LIMIT) {
                if (!canChildScrollUp()) {
                    settleToOpen = false;
                }
            } else if (draggingBorder >= rangeToCheck / BACK_FACTOR) {
                settleToOpen = true;
            } else if (draggingBorder < rangeToCheck / BACK_FACTOR) {
                settleToOpen = false;
            }

            int dest = !canChildScrollUp() ? verticalRange : -verticalRange;

            final int settleDestY = settleToOpen ? dest : 0;

            if (dragHelper.settleCapturedViewAt(0, settleDestY)) {
                ViewCompat.postInvalidateOnAnimation(PullToBackLayout.this);
            }
        }
    }


    private void onStartDragging() {

    }

    private void onStopDraggingToClosed() {

    }

    @Override
    public void computeScroll() { // needed for automatic settling.
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        verticalRange = h;
    }

    // default pull to back listener to control tips and activity exit animation.
    class DefaultPullToBackListener implements OnPullToBackListener {

        Activity activity;

        public DefaultPullToBackListener() {
            activity = (Activity) getContext();
        }

        @Override
        public void onBack() {
            activity.finish();
            activity.overridePendingTransition(0, R.anim.fade_out);
        }

        @Override
        public void onShowTips() {
            if (!isBacking) {
                activity.findViewById(R.id.back_tips).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onDismissTips() {
            activity.findViewById(R.id.back_tips).setVisibility(View.GONE);
        }
    }

    // default dragging listener for background animation.
    class DefaultDraggingListener implements OnDraggingBorderChangeListener {

        @Override
        public void onDraggingTopChange(int top, int range) {
            float ratio = 1 - clamp(top * 2 / (float) range, 0f, 0.9f);
            PullToBackLayout.this.getBackground().setAlpha((int) (255 * ratio));
        }
    }


    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

}