# Blokky - stop doomscrolling

Do you often catch yourself looking at Reels / Shorts way to long?
This is why I created Blokky. Blokky uses the Accessibility Service to block access to Reels / Shorts for you.

Easyly downloaded and activated in just a minute, it can save you hours every day.

## Installation
Install Blokky from the [PlayStore](https://play.google.com/store/apps/details?id=com.robingebert.blokky) or from the [Releases](https://github.com/Ronjar/Blokky/releases) here on GitHub.

#### Why do you need Accessibility Service, that's a pretty dangerous permission?
Well thank you for asking this well phrased question. In order to detect if you've been a bad boy:girl and clicked on the Reels / Shorts tab although you promised youself to really work today and to navigate you friendly but firmly back to your feed tab.

#### I can't activate Accessibility Service in settings?
Some Vendors like Xiaomi restrict permissions based on installation source. Use this ADB command or ask your tech savy nerd friend to do it for you:
```Bash
$ adb shell settings put secure enabled_accessibility_services com.robingebert.blokky/.feature_accessibility.ReelsBlockAccessibilityService
```

