About:
This is an example app for testing purpose only. This is an app to input Machine data that contains
some information such as Machine name, type, code number and maintenance date.

Feature:
- View machine List
- Add, update, delete machine data
- Multiple add image
- Multiple delete image
- QR code scanner

Design Pattern:
This project used Model View ViewModel (MVVM) as design pattern. I also added repository class to 
handle data collection from local database. The repository was injected by dagger hilt and it will
call from coroutines.

Local Database:
This project used Room as local database.