package com.benrcarvergmail.colorclicker;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Benjamin on 4/12/2016.
 */
public class ResizeAnimation extends Animation {
    private View view;
    private final int startWidth;
    private final int targetWidth;

    /**
     * Constructor
     * @param v target view
     * @param t target width
     */
    public ResizeAnimation(View v, int t) {
        view = v;
        targetWidth = t;
        startWidth = view.getWidth();
    }

    /**
     * Applies the transformation
     * @param interpolatedTime length of animation
     * @param t the transformation in question
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().width = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
        view.requestLayout();
    }

    // Initialize the animation
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    /**
     * Returns true because this animation changes bounds
     * @return boolean indicating whether or not this animation changes the bounds of the target
     */
    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
