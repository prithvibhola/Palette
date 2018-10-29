########################## ANDROID ##########################
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep class javax.annotation.** { *; }
-keep public class android.util.FloatMath
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-dontwarn javax.annotation.**
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
-dontwarn sun.misc.**

########################## KOTLIN ##########################
-keep class kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck { *; }
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-dontwarn java.lang.reflect.**
-dontwarn kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck

########################## RX_JAVA ##########################
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontwarn com.tbruyelle.rxpermissions.**
-dontnote rx.internal.util.PlatformDependent

########################## DAGGER ##########################
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-dontwarn dagger.internal.codegen.**

########################## OHTTP3 ##########################
-dontwarn okhttp3.**
-dontwarn okio.**

########################## RETROFIT ##########################
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-dontwarn retrofit2.Platform$Java8

########################## GLIDE ##########################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

########################## PERMISSIONS ##########################
-dontwarn permissions.dispatcher.**

########################## GOOGLE ##########################
-dontwarn com.google.errorprone.annotations.*

########################## MOSHI ##########################
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-dontwarn org.jetbrains.annotations.**

########################## PROJECT ##########################
-keepnames @kotlin.Metadata class io.palette.data.models.**
-keep class io.palette.data.models.** { *; }
-keepclassmembers class io.palette.data.models.** { *; }

##########################FIREBASE ##########################
-keep class com.google.firebase.** { *; }
-keep class io.grpc.** {*;}
-keep class com.firebase.** { *; }
-keep class org.shaded.apache.** { *; }
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

##########################DEEP LINK ##########################
-keep @interface com.airbnb.deeplinkdispatch.DeepLink
-keepclasseswithmembers class * {
    @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}
-keep class com.airbnb.deeplinkdispatch.** { *; }
-keep @interface io.palette.utility.deeplink.** { *; }
-keepclasseswithmembers class * {
    @io.palette.utility.deeplink.* <methods>;
}
