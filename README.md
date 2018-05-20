This plugin allow to check the app licensing status via Google play LVL.

More info: [Google Play Application Licensing](https://developer.android.com/google/play/licensing/index).

**N.B.**
Android only

# Installation
You need your app public key to install the plugin:
```
cordova plugin add https://github.com/nonostante/cordova-plugin-android-licensing.git --variable PUBLIC_KEY=$app_public_key$
```

# Usage
The plugin is available on the `window.plugins.licensing` object.

## Check
Allow to check asynchronically the licensing via the google play servers.
```js
check(success: Function, fail: Function): void
```

### Example
```js
window.plugins.licensing.check(
    function () { // success callback

    },
    function (errCode) { // fail callback

    }
);
```

### Error codes
|Code |Description    |
|----:|:--------------|
|-1   |Retry |
|-2   |License not valid |

# License
Apache License 2.0
