#AboutApp [![Release](https://img.shields.io/github/tag/Netural/AboutApp.svg?label=JitPack%20Maven)](https://jitpack.io/#Netural/AboutApp)


*How to add AboutApp to your project*

```gradle
   repositories { 
        maven { url "https://jitpack.io" }
   }
   dependencies {
         compile 'com.github.Netural:AboutApp:1.0.3'
   }
```

*Proguard exceptions*

```
-keepclassmembers class com.yourpackage.BuildConfig {
  public static final java.lang.String APPLICATION_ID;
  public static final java.lang.String BUILD_TYPE;
  public static final java.lang.String FLAVOR;
  public static final int VERSION_CODE;
  public static final java.lang.String VERSION_NAME;
  public static final java.lang.String BUILD_TIME;
  public static final java.lang.String GIT_SHA;
 }
```
