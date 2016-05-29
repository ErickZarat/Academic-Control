package org.erickzarat.academiccontrol.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.erickzarat.academiccontrol.BuildConfig;

/**
 * Created by erick on 28/05/16.
 */
public class RecyclerFabAnination extends RecyclerView.OnScrollListener {

    /**
     * This interface will give the RecyclerView callbacks after the animation has started.
     */
    public interface OnScrollingListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    public OnScrollingListener mListener;

    public void setOnScrollingListener(OnScrollingListener l) {
        mListener = l;
    }

    /**
     * Constructor
     *
     * @param button The {@link View} you want to animate on and off the screen while scrolling
     */
    public RecyclerFabAnination(View button) {
        if (button == null) {
            Log.w(tag, "View is null, nothing will be animated");
        }
        mButton = button;
    }

    /**
     * Set the duration of the animation
     *
     * @param duration the duration in milliseconds</br>Default is 175
     */
    public void setAnimationDuration(long duration) {
        ANIMATION_DURATION = duration;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mButton == null || dy == 0) {
            return;
        }
        if (dy > 0) { // Scrolling to bottom
            if (mIsScrollDirectionLocked && mScrollingDirection != 0) return;

            if (mButton.getVisibility() == View.GONE || mIsAnimatingOff) {
                return;
            } else {
                mScrollingDirection = SCROLLING_DOWN;
                mIsAnimatingOff = !mIsAnimatingOff;

                ViewCompat.setAlpha(mButton, 1f);
                ViewCompat.setTranslationY(mButton, 0F);

                ViewCompat.animate(mButton)
                        .alpha(0F)
                        .translationY(mButton.getHeight())
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(View view) {
                                mIsAnimatingOff = !mIsAnimatingOff;
                                if (D) Log.d(tag, "Animation off screen Done!!!!");
                                mButton.setVisibility(View.GONE);
                            }
                        }).start();
            }
        } else { // Scrolling to top
            if (mIsScrollDirectionLocked && mScrollingDirection != 0) return;

//            if (mButton.getVisibility()==View.VISIBLE || mIsAnimatingOn) {
//                return;
//            } else {
            if (mButton.getVisibility() != View.VISIBLE && !mIsAnimatingOn) {
                mScrollingDirection = SCROLLING_UP;
                mIsAnimatingOn = !mIsAnimatingOn;
                mButton.setVisibility(View.VISIBLE);

                ViewCompat.setAlpha(mButton, 0F);
                ViewCompat.setTranslationY(mButton, mButton.getHeight());

                ViewCompat.animate(mButton)
                        .alpha(1F)
                        .translationY(0F)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(View view) {
                                mIsAnimatingOn = !mIsAnimatingOn;
                                if (D) Log.d(tag, "Animation onto screen Done!!!!");
                            }
                        }).start();
            }
        }
        if (mListener != null) {
            mListener.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (!mIsScrollDirectionLocked) return;

        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                mScrollingDirection = 0;
                break;

            default:
                break;
        }
        if (mListener != null) {
            mListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    /**
     * Returns the view that is being animated.
     */
    public View getAnimatedView() {
        return mButton;
    }

    /**
     * This will lock the animation to the first direction that is scrolled.
     * That means that when you scroll toward the bottom then scroll to
     * the top without releasing the screen.
     * The view will not be animated back on the screen.</br>
     * You would have to release the screen and scroll the other direction
     * to get the opposite animation.
     */
    public void lockScrollingDirection() {
        mIsScrollDirectionLocked = true;
    }

    /**
     * Release the scroll direction lock so that a scroll direction
     * change will immediately run the appropriate animation.
     */
    public void unlockScrollingDirection() {
        mIsScrollDirectionLocked = false;
    }

    /**
     * Get the status of the current scroll lock.
     */
    public boolean isScrollDirectionLocked() {
        return mIsScrollDirectionLocked;
    }

    private static final int SCROLLING_UP = 1;

    private static final int SCROLLING_DOWN = 2;

    private int mScrollingDirection = 0;

    private boolean mIsScrollDirectionLocked = false;

    private long ANIMATION_DURATION = 175L;

    private boolean mIsAnimatingOff = false;

    private boolean mIsAnimatingOn = false;

    private View mButton;

    private boolean D = BuildConfig.DEBUG;

    private static final String tag = RecyclerFabAnination.class.getSimpleName();
}