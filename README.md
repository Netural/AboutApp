#AboutApp [![Release](https://img.shields.io/github/tag/Netural/AboutApp.svg?label=JitPack%20Maven)](https://jitpack.io/#Netural/AboutApp)
========


How to add AboutApp
--------

```
repositories { 
  maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.Netural:AboutApp:1.1.1
}
```

How to use AboutApp
--------

1. Generate a gesture file using a tool of you choice, for example https://play.google.com/store/apps/details?id=com.davemac327.gesture.tool
2. Save the gesture file in your projects resource folder (RAW)
3. Depending if you are using it within a Fragment or an Activity:

	*Fragment - onCreateView:*
	
	~~fragment = inflater.inflate(R.layout.fragment, null);~~
	
	```
	fragment = AboutAppOverlay
	        .getOverlayContentView(getActivity(), R.layout.fragment,
	                BuildConfig.class.getPackage().getName(), R.raw.gestures);
	```
	
	*Activity - onCreate:* 
	
	~~setContentView(R.layout.activity);~~
	
	```
	setContentView(AboutAppOverlay
	                .getOverlayContentView(this, R.layout.activity,
	                        BuildConfig.class.getPackage().getName(), R.raw.gestures));
	```

4. Add Proguard exceptions as defined below
5. Start App and enjoy

Supported BuildConfig variables
--------

Supported standard variables

```
BuildConfig.APPLICATION_ID
BuildConfig.VERSION_CODE
BuildConfig.VERSION_NAME
BuildConfig.BUILD_TYPE
BuildConfig.FLAVOR
```

Supported user defined variables

```
BuildConfig.GIT_SHA
BuildConfig.BUILD_TIME
```

Proguard exceptions
--------

```
-keepclassmembers class com.yourpackagename.BuildConfig {
  public static final java.lang.String APPLICATION_ID;
  public static final java.lang.String BUILD_TYPE;
  public static final java.lang.String FLAVOR;
  public static final int VERSION_CODE;
  public static final java.lang.String VERSION_NAME;
  public static final java.lang.String BUILD_TIME;
  public static final java.lang.String GIT_SHA;
 }
```

License
=======

    Copyright 2015 Netural GmbH

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
