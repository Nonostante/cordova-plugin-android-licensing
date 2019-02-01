module.exports = {
    check: function (deviceId, success, fail) {
        cordova.exec(success, fail, "CordovaAndroidLicensing", "check", [deviceId]);
    }
};