# android-test-nur

***Android Client for Trending Github Repositories***

* Android Architecture Components: 
    ViewModel , 
    LiveData, 
    Paging Library, 
    Room Database Library,
    Biometric Library
    
* RxAndroid2: Asynch Operations
* Retrofit: Network Operations
* OkHttp: Network Operations
* RxBus: Communication
* Koin: Dependency Injection
* RxFingerprint: Biometric authentication 
* Firebase: Authenticate using Githup with Firebase web flow
* Github Api: oAuth and get trending reposirories
* Moshi:  JSON Library

## Features
* Github account login
* Showing trending Github libraries with sorting order for  github user
* Web flow for login with Githup and Firebase, 
* Biometric authentication implemented for security process
* Pagination while scrolling
* Encryption and decryption  for sensitive datas with Biometrics
* Room and Sqllite db used for storing sensitive datas for  github user (Encryption and decryption not implemented)
* Seperate UI, business logic and data sources' responsibilities
* Avoids multi-threading problems
* Testable 

## Approach of Clean Architecture for Android
There are 4 layers in the project: Data, Domain, Entity and  UI layer that contains view and viewModels.

## Requirements &amp; configurations
#### Requirements
- JDK 8
- Android SDK API 29
- Kotlin Gradle plugin 1.3.61 *(it will be installed automatically when this project is synced)*

#### Configurations
- minSdkVersion=24
- targetSdkVersion=29

## Language
*   [Kotlin](https://kotlinlang.org/)

## Libraries
*   [AndroidX](https://developer.android.com/jetpack/androidx)
*   [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
*   [RxAndroid](https://github.com/ReactiveX/RxAndroid)
*   [OkHttp](http://square.github.io/okhttp/)
*   [Retrofit](http://square.github.io/retrofit/)
*   [Gson](https://github.com/google/gson)
*   [Koin](https://github.com/InsertKoinIO/koin)
*   [RxFingerprint](https://github.com/Mauin/RxFingerprint)
*   [Moshi](https://github.com/square/moshi)



## More about The Clean Architecture

[The Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)


# Trending Github Client App
# TrendingGithubRepoApp
