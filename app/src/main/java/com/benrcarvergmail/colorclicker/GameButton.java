package com.benrcarvergmail.colorclicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Custom implementation of button to allow for custom states. We want custom
 * states because that makes it much easier to use different drawables for the button.
 * We can change the drawable based on its state instead of manually doing it always.
 */
public class GameButton extends Button {

    /**
     * Represents the incorrect state. This state is used when the button was pressed incorrectly.
     */
    private static final int[] STATE_INCORRECT = {R.attr.state_incorrect};

    /*private static final int[] STATE_RED = {R.attr.state_red};
    private static final int[] STATE_BLUE = {R.attr.state_blue};
    private static final int[] STATE_GREEN = {R.attr.state_green};
    private static final int[] STATE_YELLOW = {R.attr.state_yellow};
    private static final int[] STATE_ORANGE = {R.attr.state_orange};
    private static final int[] STATE_PINK = {R.attr.state_pink};
    private static final int[] STATE_PURPLE = {R.attr.state_purple};
    private static final int[] STATE_WHITE = {R.attr.state_white};*/

    // State flags.
    private boolean mIsIncorrect = false;
    /*private boolean mIsRed = false;
    private boolean mIsBlue = false;
    private boolean mIsGreen = false;
    private boolean mIsYellow = false;
    private boolean mIsOrange = false;
    private boolean mIsPink = false;
    private boolean mIsPurple = false;
    private boolean mIsWhite = false;*/

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIncorrect(boolean isIncorrect) {
        mIsIncorrect = isIncorrect;
        // Make sure to call refreshDrawableState() or else
        // the button won't update visually (probably).
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (mIsIncorrect) {
            mergeDrawableStates(drawableState, STATE_INCORRECT);
        }
        /*if (mIsRed) {
            mergeDrawableStates(drawableState, STATE_RED);
        }
        if (mIsBlue) {
            mergeDrawableStates(drawableState, STATE_BLUE);
        }
        if (mIsGreen) {
            mergeDrawableStates(drawableState, STATE_GREEN);
        }
        if (mIsYellow) {
            mergeDrawableStates(drawableState, STATE_YELLOW);
        }
        if (mIsOrange) {
            mergeDrawableStates(drawableState, STATE_ORANGE);
        }
        if (mIsPink) {
            mergeDrawableStates(drawableState, STATE_PINK);
        }
        if (mIsPurple) {
            mergeDrawableStates(drawableState, STATE_PURPLE);
        }
        if (mIsWhite) {
            mergeDrawableStates(drawableState, STATE_WHITE);
        }*/
        return drawableState;
    }

}
