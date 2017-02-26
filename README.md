# Point Manager

Draft system to add, update and delete points related to kid activities (transactions). Created for self-learning purpose

## PointTracker 

Backend service to manage points of users. Based on Spring boot <br />
Controller to process REST API requests from clients <br />
Implements JPA (Java Persistence API) <br />
Embedded hibernate database, storing data in files <br />
Build in docker container <br />
Declare Entity classes for user and transaction <br />

## InterestCalc

Service in backend that calculates interest points every month. Based on Spring boot <br />
REST client to PointTracker server that update points. Supports schedule and retry mechanism <br />
Mongo DB to persist own data <br />
AMQP message broker (RabbitMQ) consuming notifications of users deleted in PointTracker DB <br />
Build in docker container <br />

## PointManager

Web client implementing REST client via Javascript (Ajax) <br />

## PointReader

Android client. There are four different versions: <br /> 

#### PointReader1

Use intent service to retrieve data from the server in a background thread <br />
Broadcast receiver to receive data in UI thread from worker thread asynchronously <br /> 
Use Retrofit 1.9 for REST API in synchronous way <br />
Popup menu to present list of users <br />
RecycleView to show transaction list <br />
saveInstanceState to save Views states in orientation changes <br />
Serializable to transfer ArrayList of Objects between activities and service <br />
Settings persisted with SharedPreferences and managed with a PreferenceFragment <br />
Unit testing with JUnit with mockito <br />

#### PointReader2

Use Retrofit 2.1 for REST API in asynchronous way. <br />
No intent service and broadcast receiver since Retrofit 2.1 handle Callbacks in UI thread <br />
Parcelable to transfer ArrayList of Objects between activities and service, instead of Serializable <br />
Add mocking to Unit testing <br />

#### PointReader3

Implements RxJava for ReactiveX programming for REST API callbacks <br />

#### PointReader4

Implements Dagger2 (Dependency Injection) for Presenter <br />
Cache memory for offline access using custom interceptor in okhttp client <br />

 

