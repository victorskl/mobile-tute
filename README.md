# Mobile-Tute

Mobile Computing Systems Programming Tutorials

## Android

- Clone this repo
- Download and install Android Studio
- Open each project in, each week workshop directory

### Turn on USB Debug Mode

- [How to Enable USB Debugging Mode on Android](https://www.kingoapp.com/root-tutorials/how-to-enable-usb-debugging-mode-on-android.htm)

- This is required; to run the target on the real phone/tablet. Not simulation. 

- After USB Debug Mode turning on, do reboot the device. (This works for me.)

- On Mac, it is just plug and get detected. However, you can check this using `adb`.
    ```
    cd /Users/username/Library/Android/sdk/platform-tools
    ps axu|grep adb
    ./adb devices
        List of devices attached
        * daemon not running. starting it now at tcp:5037 *
        * daemon started successfully *
        ab44c566	device
    ps axu|grep adb
    ./adb kill-server
    ./adb help
    ```
    Android Studio will start `adb` service when you hit the `Run` button.

- For more
    - [Run Apps on a Hardware Device](https://developer.android.com/studio/run/device.html)
 
    - TL;DR [Build and Run Your App](https://developer.android.com/studio/run/index.html?utm_source=android-studio)
        
    - TL;DR [Create and Manage Virtual Devices](https://developer.android.com/studio/run/managing-avds.html)