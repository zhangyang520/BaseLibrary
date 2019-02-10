/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zhangyang.baselibrary.ui.progressbar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zhangyang.baselibrary.R;
import com.zhangyang.baselibrary.common.BaseConstants;
import com.suntray.chinapost.baselibrary.ui.progressbar.*;


public class KProgressHUD {

    public enum Style {
        SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE,DOU_DONG
    }

    // To avoid redundant APIs, all HUD functions will be forward to
    // a custom dialog
    private ProgressDialog mProgressDialog;
    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private Context mContext;

    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;
    private Style style;


    public KProgressHUD(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;

        setStyle(Style.SPIN_INDETERMINATE);
    }

    public KProgressHUD(Context context,Style style) {
        mContext = context;
        //System.out.println("show DOU_DONG 333333333 ..........style!=Style.DOU_DONG:"+(style!=Style.DOU_DONG));
        setStyle(style);
        if(style!=Style.DOU_DONG){
            mProgressDialog = new ProgressDialog(context);
        }else{
            mProgressDialog = new ProgressDialog(context,R.style.notIn);
        }
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
    }

    /**
     * Create a new HUD. Have the same effect as the constructor.
     * For convenient only.
     * @param context Activity context that the HUD bound to
     * @return An unique HUD instance
     */
    public static KProgressHUD create(Context context) {
        return new KProgressHUD(context);
    }

  /**
   * Create a new HUD. specify the HUD style (if you use a custom view, you need {@code KProgressHUD.create(Context context)}).
   *
   * @param context Activity context that the HUD bound to
   * @param style One of the KProgressHUD.Style values
   * @return An unique HUD instance
   */
    public static KProgressHUD create(Context context, Style style) {
        return new KProgressHUD(context,style);
    }

    /**
     * Specify the HUD style (not needed if you use a custom view)
     * @param style One of the KProgressHUD.Style values
     * @return Current HUD
     */
    public KProgressHUD setStyle(Style style) {
        View view = null;
        this.style=style;
        //System.out.println("show DOU_DONG 444444444 ..........style!=Style.DOU_DONG:"+(style!=Style.DOU_DONG));
        switch (style) {
            case SPIN_INDETERMINATE:
                view = new SpinView(mContext);
                mProgressDialog.setView(view);
                break;
            case PIE_DETERMINATE:
                view = new PieView(mContext);
                mProgressDialog.setView(view);
                break;
            case ANNULAR_DETERMINATE:
                view = new AnnularView(mContext);
                mProgressDialog.setView(view);
                break;
            case BAR_DETERMINATE:
                view = new BarView(mContext);
                mProgressDialog.setView(view);
                break;
            case DOU_DONG:
                //System.out.println("show DOU_DONG 555555555 ..........style!=Style.DOU_DONG:"+(style!=Style.DOU_DONG));
                break;
            // No custom view style here, because view will be added later
        }
        return this;
    }

    /**
     * Specify the dim area around the HUD, like in Dialog
     * @param dimAmount May take value from 0 to 1.
     *                  0 means no dimming, 1 mean darkness
     * @return Current HUD
     */
    public KProgressHUD setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            mDimAmount = dimAmount;
        }
        return this;
    }

    /**
     * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
     * @param width in dp
     * @param height in dp
     * @return Current HUD
     */
    public KProgressHUD setSize(int width, int height) {
        mProgressDialog.setSize(width, height);
        return this;
    }

    /**
     * Specify the HUD background color
     * @param color ARGB color
     * @return Current HUD
     */
    public KProgressHUD setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify corner radius of the HUD (default is 10)
     * @param radius Corner radius in dp
     * @return Current HUD
     */
    public KProgressHUD setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    /**
     * Change animate speed relative to default. Only have effect when use with indeterminate style
     * @param scale 1 is default, 2 means double speed, 0.5 means half speed..etc.
     * @return Current HUD
     */
    public KProgressHUD setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }

    /**
     * Optional label to be displayed on the HUD
     * @return Current HUD
     */
    public KProgressHUD setLabel(String label) {
        mProgressDialog.setLabel(label);
        return this;
    }

    /**
     * Optional detail description to be displayed on the HUD
     * @return Current HUD
     */
    public KProgressHUD setDetailsLabel(String detailsLabel) {
        mProgressDialog.setDetailsLabel(detailsLabel);
        return this;
    }

    /**
     * Max value for use in one of the determinate styles
     * @return Current HUD
     */
    public KProgressHUD setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    /**
     * Set current progress. Only have effect when use with a determinate style, or a custom
     * view which implements Determinate interface.
     */
    public void setProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }

    /**
     * Provide a custom view to be displayed.
     * @param view Must not be null
     * @return Current HUD
     */
    public KProgressHUD setCustomView(View view) {
        if (view != null) {
            mProgressDialog.setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    /**
     * Specify whether this HUD can be cancelled by using back button (default is false)
     * @return Current HUD
     */
    public KProgressHUD setCancellable(boolean isCancellable) {
        mProgressDialog.setCancelable(isCancellable);
        return this;
    }

    /**
     * Specify whether this HUD closes itself if progress reaches max. Default is true.
     * @return Current HUD
     */
    public KProgressHUD setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public KProgressHUD show() {
        try {
            if (!isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isShowing() {
        try {
            return mProgressDialog != null && mProgressDialog.isShowing();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void dismiss() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CancalListener cancalListener;

    public CancalListener getCancalListener() {
        return cancalListener;
    }

    public void setCancalListener(CancalListener cancalListener) {
        this.cancalListener = cancalListener;
    }

    SuccessCallBack successCallBack;

    public SuccessCallBack getSuccessCallBack() {
        return successCallBack;
    }

    public void setSuccessCallBack(SuccessCallBack successCallBack) {
        this.successCallBack = successCallBack;
    }

    /**
     * 进行启动成功的回调
     */
    public void successCallBack(View view){
        this.mProgressDialog.removeView();
        this.mProgressDialog.addViewToFrame(view);
        this.mProgressDialog.setSuccessBtnVisiable(true);
        this.mProgressDialog.setLabel("上传成功");
    }

    private class ProgressDialog extends Dialog{
        private Determinate mDeterminateView;
        private Indeterminate mIndeterminateView;
        private View mView;
        private TextView mLabelText;
        private TextView mDetailsText;
        private Button btn_cacel;
        private Button btn_success;
        private String mLabel;
        private String mDetailsLabel;
        private FrameLayout mCustomViewContainer;
        private BackgroundLayout mBackgroundLayout;
        private int mWidth, mHeight;
        ImageView imageView;

        public ProgressDialog(Context context) {
            super(context);
        }

        public ProgressDialog(Context context,int themeId) {
            super(context,themeId);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if(style!=Style.DOU_DONG){
                setContentView(R.layout.kprogresshud_hud);
                Window window = getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.dimAmount = mDimAmount;
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);
            }else{
                setContentView(R.layout.dialog_doudong);
                Window window = getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
//                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.dimAmount = mDimAmount;
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);
            }
            setCanceledOnTouchOutside(false);
            initViews();
        }

        private void initViews(){
            //最后 加入 对应的view
            if (style == Style.DOU_DONG) {
                imageView=(ImageView) findViewById(R.id.btn_exit);
                Glide.with(mContext).load(R.drawable.loading).into(imageView);
            }else{
                mCustomViewContainer = (FrameLayout) findViewById(R.id.container);
                mBackgroundLayout = (BackgroundLayout) findViewById(R.id.background);
                mBackgroundLayout.setCornerRadius(mCornerRadius);
                if (mWidth != 0) {
                    updateBackgroundSize();
                }
                mBackgroundLayout.setBaseColor(mWindowColor);
                if (mDeterminateView != null) {
                    mDeterminateView.setMax(mMaxProgress);
                }
                if (mIndeterminateView != null) {
                    mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
                }

                mLabelText = (TextView) findViewById(R.id.label);
                if (mLabel != null) {
                    mLabelText.setText(mLabel);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
                mDetailsText = (TextView) findViewById(R.id.details_label);
                if (mDetailsLabel != null) {
                    mDetailsText.setText(mDetailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }

                //按钮的添加：
                btn_cacel=(Button)findViewById(R.id.btn_cacel);
                btn_cacel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cancalListener!=null){
                            cancalListener.cancel();
                        }
                    }
                });
                if(style==Style.BAR_DETERMINATE){
                    btn_cacel.setVisibility(View.VISIBLE);
                }else{
                    btn_cacel.setVisibility(View.GONE);
                }

                btn_success=(Button)findViewById(R.id.btn_success);
                btn_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(successCallBack!=null){
                            successCallBack.call();
                        }
                        dismiss();
                    }
                });
                addViewToFrame(mView);
            }

        }

        /**
         * 进行删除View
         */
        public void removeView(){
            mCustomViewContainer.removeAllViews();
        }
        private void addViewToFrame(View view) {
            if (view == null) return;
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view, params);
        }

        private void updateBackgroundSize() {
            ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
            params.width = Helper.INSTANCE.dpToPixel(mWidth, getContext());
            params.height = Helper.INSTANCE.dpToPixel(mHeight, getContext());
            mBackgroundLayout.setLayoutParams(params);
        }

        public void setProgress(int progress) {
            if (mDeterminateView != null) {
                mDeterminateView.setProgress(progress);
                if (mIsAutoDismiss && progress >= mMaxProgress) {
                    dismiss();
                }
            }
        }

        public void setView(View view) {
            if (view != null) {
                if (view instanceof Determinate) {
                    mDeterminateView = (Determinate) view;
                }
                if (view instanceof Indeterminate) {
                    mIndeterminateView = (Indeterminate) view;
                }
                mView = view;
                if (isShowing()) {
                    if(style!=Style.DOU_DONG){
                        mCustomViewContainer.removeAllViews();
                        addViewToFrame(view);
                    }else{
                        mCustomViewContainer.removeAllViews();
                        addViewToFrame(view);
                    }
                }
            }
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            mDetailsLabel = detailsLabel;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackgroundLayout != null) {
                updateBackgroundSize();
            }
        }

        /**
         * 获取抖动的imageView
         * @return
         */
        public ImageView getDoudongImageView(){
            return  imageView;
        }
        /**
         * 进行设置取消按钮显示
         */
        public void setCancelVisvible(){
            if(btn_cacel!=null){
                btn_cacel.setVisibility(View.VISIBLE);
            }else{
                ////System.out.println("setCancelVisvible btn_cacel is null");
            }
        }

        public void setSuccessBtnVisiable(boolean flag){
            btn_success.setVisibility(flag?View.VISIBLE:View.INVISIBLE);
        }
    }

    public interface CancalListener{
        void cancel();
    }
}
