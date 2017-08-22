## Week 3 - Tute 1

- Exercise
  - [week3](week3)

- Android runtime
  - Dalvik vs ART (>v5.0+)
  - https://android.jlelse.eu/closer-look-at-android-runtime-dvm-vs-art-1dc5240c3924 
  - http://www.anandtech.com/show/8231/a-closer-look-at-android-runtime-art-in-android-l
  
- 4 Components for App
  - Activity - foreground program
  - Service - background task
  - Receiver - (broadcast receiver) respond to events
  - Provider - (content provider) read/write data on phone storage
  
- Activity
  - managing the lifecycle of an Activity
  - passing data across Activities
  - https://stackoverflow.com/questions/8515936/android-activity-life-cycle-what-are-all-these-methods-for
  

  
## Week 4 - Tute 2

- Exercise
  - [week4](week4)



## Week 5 - Tute 3

- Exercise
  - [week5](week5)
  
### Process and Thread

- UI thread (aka main thread)
  - https://developer.android.com/guide/components/processes-and-threads.html
  - note that even service component is running on main thread

- 2 important principles
  - do not block the UI thread
  - do not access UI from outside of the UI thread
  
- a better design: UI thread and worker thread

- Ways of multithreading
  - https://developer.android.com/training/multiple-threads/index.html
  - techniques
    1. Thread and Runnable
    2. HandlerThread
    3. AsyncTask 
    4. IntentService
    5. ThreadPoolExecutor

- [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html)
  - allows you to perform background operations and publish results on the UI thread 
  - a helper class around Thread and Handler 
  - used for short operations (a few seconds at the most.)
  - don’t need to manipulate a new thread, directly

- [IntentService](https://developer.android.com/reference/android/app/IntentService.html)
  - "work queue processor" pattern
  - commonly used to offload tasks from an application's main thread
  - All requests are handled on a single worker thread - they may take as long as necessary 
  - and will not block the application's main loop
  - but only one request will be processed at a time
  - don’t need to manipulate a new thread, directly
  - also [experimented in week4 tute](week4/UIAndServices/app/src/main/java/com/sankholin/uiandservices/MyService.java)

### Sensors
- Motion sensors
- Environmental sensors
- Position sensors
- Location service
