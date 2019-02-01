package io.nonostante.games.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;

public class CordovaAndroidLicensing extends CordovaPlugin {
    private static final byte[] SALT = new byte[]{
            -115, 24, -27, -25, 0, -60, -7, -8, 12, 25, -6, -66, 89, 12, -11, 95, -76, 17, 11, -23
    };

    private static final int ERROR_RETRY = -1;
    private static final int ERROR_NOT_LICENSED = -2;

    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("check")) {
            this.checkLicense(args.getString(0), callbackContext);
            return true;
        }

        return false;
    }

    private void checkLicense(final String deviceId, final CallbackContext callbackContext) {
        final Context context = cordova.getContext();
        final Activity activity = cordova.getActivity();

        mLicenseCheckerCallback = new MyLicenseCheckerCallback(this, callbackContext);

        mChecker = new LicenseChecker(
                context,
                new ServerManagedPolicy(context,
                        new AESObfuscator(SALT, context.getPackageName(), deviceId)),
                context.getString(context.getResources().getIdentifier("lpk", "string", context.getPackageName()))
        );

        mChecker.checkAccess(mLicenseCheckerCallback);
    }

    private void clear(){
        mChecker.onDestroy();
        mChecker = null;
        mLicenseCheckerCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mChecker != null) {
            mChecker.onDestroy();
        }
    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        private final CallbackContext mCordovaCallbackContext;
        private final CordovaAndroidLicensing mPlugin;

        private MyLicenseCheckerCallback(final CordovaAndroidLicensing plugin, final CallbackContext cordovaCallbackContext) {
            mPlugin = plugin;
            mCordovaCallbackContext = cordovaCallbackContext;
        }

        public void allow(int reason) {
            if (mPlugin.cordova.getActivity().isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            mCordovaCallbackContext.success();
            mPlugin.clear();
        }

        public void dontAllow(int reason) {
            if (mPlugin.cordova.getActivity().isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            if (reason == Policy.RETRY) {
                mCordovaCallbackContext.error(ERROR_RETRY);
            } else if (reason == Policy.NOT_LICENSED){
                mCordovaCallbackContext.error(ERROR_NOT_LICENSED);
            } else {
                mCordovaCallbackContext.error(reason);
            }

            mPlugin.clear();
        }

        @Override
        public void applicationError(int errorCode) {
            dontAllow(errorCode);
        }
    }
}
