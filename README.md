# MVVM-Dagger-Livedata
The main purpose of this app is to show MVVM sample implementation using the new Google Architectural components LiveData and ViewModel.

The whole application is built based on the MVVM architectural pattern.

##### Model 
This is responsible for handling the data in the application. Model
cannot directly interact with Views, but it interacts with ViewModels
and then Views with the help of observables. (Sounds confusing right?
Don’t worry in the course we will cover every step in detail).


##### View 
This is the User Interface of our Application. It should not
contain any application logic.


##### ViewModel 
It is basically a link between Model and View.

# Application Architecture
![alt text](https://cdn-images-1.medium.com/max/1600/1*OqeNRtyjgWZzeUifrQT-NA.png)

The main advatage of using MVVM, there is no two way dependency between ViewModel and Model unlike MVP. Here the view can observe the datachanges in the viewmodel as we are using LiveData which is lifecycle aware. The viewmodel to view communication is achieved through observer pattern (basically observing the state changes of the data in the viewmodel).

## Environment
* Kotlin V 1.3 +
* Java 8 Compatibility
* Min SDk Support 21
* Target SDK 29
* Android Studio 3.5 +

### Libraries uses
- Android Architecture Component (View model, Live data, Lifecycle
  aware)
- Retrofit with OkHTTP3
- Stetho for debugging
- EasyRecyclerView
- Glide
- RxKotlin
- Google gson


#### TODO
- [ ] Integrate CI/CD

# How to build ?

Open terminal and type the below command to generate debug build <br/>

``` ./gradlew assembleDebug ```

Open terminal and type the below command to generate release build <br/>

``` ./gradlew assembleRelease ```

# How to generate code coverage report ?

Open terminal and type the following command

```./gradlew clean jacocoTestReport```

The coverage report will be generated on the following path.

``` app/build/reports ```

### License
```
   Copyright (C) 2019-2020 Thinkwik india online services LLP

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
