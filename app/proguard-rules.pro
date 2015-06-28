# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files (x86)\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature

-keep class com.happy.samuelalva.bcykari.model.** { *; }

-keep class org.jsoup.** { *; }

-keep class android.support.design.widget.** { *; }

-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn javax.annotation.**
-keep class com.facebook.imagepipeline.gif.** { *; }
-keep class com.facebook.imagepipeline.webp.** { *; }
