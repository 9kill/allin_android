package com.tuiyi.allin.third.gdt;

import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.tuiyi.allin.core.entity.AdEntity;
import com.tuiyi.allin.core.nativead.CustomNativeAd;

import java.util.List;

/**
 * 广点通 原生广告
 *
 * @author liuhuijie
 * @date 12/21/20
 */
public class GdtNativeAd extends CustomNativeAd {


    private NativeExpressAD mNativeExpressAd;

    private NativeExpressADView mNativeExpressADView;

    public GdtNativeAd() {

    }

    @Override
    public void loadAd() {

        mAdConfig.thirdPid="5060295460765937";
        mNativeExpressAd = new NativeExpressAD(mActivity, new ADSize(mAdConfig.width, mAdConfig.height), mAdConfig.thirdPid, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                notifyAdClick();
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                notifyAdClose();
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                notifyAdShow();
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                mNativeExpressADView = list.get(0);
                mNativeExpressADView.render();

            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                mViewContainer.removeAllViews();
                mViewContainer.addView(nativeExpressADView);
                notifyAdReady();
            }

            @Override
            public void onNoAD(AdError adError) {
                notifyAdFail(new com.tuiyi.allin.core.AdError(adError.getErrorCode(), adError.getErrorMsg()));
            }
        });
        mNativeExpressAd.loadAD(1);
    }

    @Override
    public void showAd() {
    }

    @Override
    public void destroyAd() {

    }

    @Override
    public void onPauseAd() {

    }

    @Override
    public void onResumeAd() {

    }

    @Override
    public void customAdMessage(AdEntity adEntity) {

    }
}