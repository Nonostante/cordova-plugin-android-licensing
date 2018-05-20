
interface CordovaAndroidLicensing {
    check(success?: () => void, fail?: (error: number) => void): void
}

interface CordovaPlugins {
    readonly licensing: CordovaAndroidLicensing
}

interface Window {
    readonly plugins: CordovaPlugins
}
