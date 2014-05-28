Why are you reading thisi? - I said not to!

== Installation From Hydro Sources on Trusty ==

This is only temporary until rosjava upgrades to indigo...but hydro sources work fine.

We will create a series of underlays:

* indigo
* rosjava
* rocon_rosjava
* android
* rocon_android

```
> mkdir -p ~/android
> cd ~/android
> yujin_init_workspace -j5 rosjava rosjava-release
> cd rosjava
> yujin_init_build .
> yujin_make --install-rosdeps
> yujin_make

```

```
> cd ~/android
> yujin_init_workspace -j5 rocon_rosjava rocon-rosjava
> cd rocon_rosjava
> yujin_init_build -u ~/android/rosjava/devel .
> yujin_make

```

```
> cd ~/android
> yujin_init_workspace -j5 android android
> cd android
> yujin_init_build -u "~/android/rocon_rosjava/devel;~/android/rosjava/devel" .
> yujin_make

```


```
> cd ~/android
> yujin_init_workspace -j5 interactions android
> cd interactions
> yujin_init_build -u "~/android/android/devel;~/android/rocon_rosjava/devel;~/android/rosjava/devel" .
> yujin_make

```
