package com.tuiyi.allin.core;

import android.app.Activity;
import android.text.TextUtils;

import com.tuiyi.allin.core.entity.AdEntity;
import com.tuiyi.allin.core.entity.AdSourceEntity;
import com.tuiyi.allin.core.net.NetApi;
import com.tuiyi.allin.core.net.NetManager;
import com.tuiyi.allin.utlis.AllInLog;
import com.tuiyi.allin.utlis.SysInfoUtils;

import org.json.JSONObject;

/**
 * @author liuhuijie
 * @date 2020/11/15
 */
public abstract class AbstractAd implements IAd {

    protected Activity mActivity;

    protected AdConfig mAdConfig;

    protected AdEntity mAdEntity;

    protected int mCurrentAdPos;

    protected AdCallback mAdCallback;

    protected OnAdReloadListener mOnAdReloadListener;

    public abstract void customAdMessage(AdEntity adEntity);


    public void setAdConfig(Activity activity, AdConfig adConfig, AdEntity adEntity, AdCallback adCallback, OnAdReloadListener listener) {
        this.mAdConfig = adConfig;
        this.mActivity = activity;
        this.mAdCallback = adCallback;
        this.mAdEntity = adEntity;
        this.mOnAdReloadListener = listener;
        customAdMessage(adEntity);
    }

    public void setCurrentAdPos(int currentAdPos) {
        this.mCurrentAdPos = currentAdPos;
    }


    public void removeAdCallBack() {
        this.mAdCallback = null;
    }

    public final void notifyAdClick() {
        if (mAdCallback != null) {
            netLog(NetApi.CLICK_LOG);
            mAdCallback.onAdClick();
        }
    }

    public final void notifyAdReady() {
        if (mAdCallback != null) {
            netLog(NetApi.REQUEST_LOG);
            mAdCallback.onAdReady();
        }
    }

    public final void notifyAdClose() {
        if (mAdCallback != null) {
            mAdCallback.onAdClose();
        }
    }

    //广告展示失败 加载下一条
    public final void notifyAdFail(AdError adError) {
        if (mAdCallback != null && mOnAdReloadListener != null) {
            netLog(NetApi.REQUEST_FAIL_LOG);
            if (mAdEntity != null && mAdEntity.adsource != null && mAdEntity.adsource.size() > ++mCurrentAdPos) {
                mAdConfig.appId = mAdEntity.adsource.get(mCurrentAdPos).appid;
                mAdConfig.thirdPid = mAdEntity.adsource.get(mCurrentAdPos).placeid;
                mAdConfig.className = mAdEntity.adsource.get(mCurrentAdPos).classname;
                mAdConfig.json=mAdEntity.adsource.get(mCurrentAdPos).json;
                mOnAdReloadListener.onAdReload(mActivity, mAdConfig, mCurrentAdPos, mAdEntity.adsource.get(mCurrentAdPos));
            } else {
                adFailBack(adError);
            }
        } else {
            adFailBack(adError);
        }
    }

    private final void adFailBack(AdError adError) {
        mAdCallback.onAdFailed(adError);
    }

    public final void notifyAdShow() {
        if (mAdCallback != null) {
            netLog(NetApi.SHOW_LOG);
            mAdCallback.onAdShow();
        }
    }

    /**
     * 广告日志
     *
     * @param url 请求地址
     */
    protected void netLog(String url) {
        if (mActivity == null || mAdConfig == null || mAdEntity == null) {
            return;
        }
        //获取日志实体
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("placeid", mAdConfig.placeId);
            jsonObject.put("bid", mAdEntity.bid);
            jsonObject.put("deviceinfo", SysInfoUtils.getDeviceInfo(mActivity));

            if (mAdEntity.adsource != null && mAdEntity.adsource.get(mCurrentAdPos) != null) {
                JSONObject sourceObj = new JSONObject();
                AdSourceEntity adSourceEntity = mAdEntity.adsource.get(mCurrentAdPos);
                if (!TextUtils.isEmpty(mAdEntity.adsource.get(mCurrentAdPos).json)){
                    return;
                }
                sourceObj.put("sourceid", adSourceEntity.sourceid);
                sourceObj.put("appid", adSourceEntity.appid);
                sourceObj.put("placeid", adSourceEntity.placeid);
                sourceObj.put("type", adSourceEntity.type);
                jsonObject.put("adsource", sourceObj);
            }
        } catch (Exception e) {
            AllInLog.i(e.getMessage());
            e.printStackTrace();
        }
        new NetManager().doPost(url, jsonObject.toString());

    }
}
