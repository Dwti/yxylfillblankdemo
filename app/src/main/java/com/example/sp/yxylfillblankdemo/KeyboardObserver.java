package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by sp 2017/6/7.
 */
public class KeyboardObserver implements ViewTreeObserver.OnGlobalLayoutListener {
    private View mRootView;
    private int mOriginHeight;
    private int mPreHeight;
    private KeyBoardVisibleChangeListener mKeyBoardVisibleChangeListener;

    public interface KeyBoardVisibleChangeListener {
        /**
         * call back
         * @param isShow true is show else hidden
         * @param keyboardHeight keyboard height
         */
        void onKeyboardVisibleChange(boolean isShow, int keyboardHeight);
    }

    public void setKeyBoardVisibleChangeListener(KeyBoardVisibleChangeListener keyBoardListen) {
        this.mKeyBoardVisibleChangeListener = keyBoardListen;
    }

    public KeyboardObserver(View rootView) {
        if (rootView == null) {
            return;
        }
        mRootView = rootView;
        if (mRootView != null) {
            addContentTreeObserver();
        }
    }

    private void addContentTreeObserver() {
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        int currHeight = mRootView.getHeight();
        if (currHeight == 0) {
            return;
        }
        boolean hasChange = false;
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
            mOriginHeight = currHeight;
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true;
                mPreHeight = currHeight;
            } else {
                hasChange = false;
            }
        }
        if (hasChange) {
            boolean isShow;
            int keyboardHeight = 0;
            if (mOriginHeight == currHeight) {
                //hidden
                isShow = false;
            } else {
                //show
                keyboardHeight = mOriginHeight - currHeight;
                isShow = true;
            }

            if (mKeyBoardVisibleChangeListener != null) {
                mKeyBoardVisibleChangeListener.onKeyboardVisibleChange(isShow, keyboardHeight);
            }
        }
    }

    public void destroy() {
        if (mRootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
}
