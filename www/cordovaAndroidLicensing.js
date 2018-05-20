module.exports = {
    check: function (success, fail) {
        cordova.exec(success, fail, "CordovaAndroidLicensing", "check", []);
    }
};