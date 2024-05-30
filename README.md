# Slashes
An application for experimenting with transferring files between devices.

---

### todo

- [ ] desktop/macOS/arm64
- [ ] android/phone
- [ ] android/watch

---

## Build

- desktop/macOS/arm64

```
gradle desktop:run
```

- android/phone

```
gradle android:assemblePhoneDebug
```

- android/watch

```
gradle android:assembleWatchDebug
```

---

## Issues

This application cannot run without the MANAGE_EXTERNAL_STORAGE permission.
As of May 30, 2024, in watches on Wear OS this can be done through adb using the command:
```
adb shell appops set --uid org.kepocnhh.slashes.watch MANAGE_EXTERNAL_STORAGE allow
```

[link](https://developer.android.com/training/data-storage/manage-all-files#enable-manage-external-storage-for-testing)

---
