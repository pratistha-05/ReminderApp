Medication Reminder App
=======================

The **Medication Reminder App** is designed to help users set reminders for taking their medications or other important tasks. It allows users to create one-time or recurring reminders, complete with customizable alerts and dosages. With a clean and user-friendly interface, this app ensures that no medication is ever missed!

Features
--------

*   **Create Reminders**: Set reminders for any medication or task, with the option to add dosage and time
    
*   **Repeat Reminders**: Set recurring reminders for regular medication schedules, and set intervals in minutes or hours
    
*   **Audio Alerts**: Get notified with a alarm sound when it's time to take medication
  
*   **Notification Actions**: Respond to reminders directly from notifications with options to mark them as DONE or REJECT them.
    
*   **Reminder Details**: Display essential information such as name, dosage, reminder time, and whether the reminder is recurring or one-time.
    
*   **Manage Reminders**: Edit or delete existing reminders from the list.
    
*   **Responsive Design**: Works seamlessly on various screen sizes.
    

Screenshots
-----------

<p align="center">
<img width="250" alt="Screenshot 2025-01-18 at 3 57 16 PM" src="https://github.com/user-attachments/assets/8b41efbc-2a86-4614-95d7-7ad61ad2f5ae" />
<img width="250" alt="Screenshot 2025-01-19 at 12 12 27 AM" src="https://github.com/user-attachments/assets/746a235b-8cd8-410f-8853-33a34cbf4544" />
<img width="250" alt="Screenshot 2025-01-19 at 12 53 42 AM" src="https://github.com/user-attachments/assets/45aa09f4-6f39-493b-9d49-6c6d29fd0285" />
<img width="250" alt="Screenshot 2025-01-19 at 12 54 42 AM" src="https://github.com/user-attachments/assets/206452a8-5bc5-4c95-aa44-bbf828ad8caf" />
<img width="250" alt="Screenshot 2025-01-19 at 12 55 14 AM" src="https://github.com/user-attachments/assets/0bfba740-2234-43d4-8da8-661ce2e41213" />
<img width="250" alt="Screenshot 2025-01-19 at 12 55 55 AM" src="https://github.com/user-attachments/assets/3b0a3e7f-4596-4a26-a8d8-1da54e90ac46" />
</p>


Getting Started
---------------

Follow the steps below to run this app on your local machine:

### Prerequisites

*   Android Studio (latest version)
    
*   Kotlin (latest stable version)
    
*   Jetpack Compose
    
*   Hilt for dependency injection
    
*   Room Database for local storage
    
*   Retrofit for network communication (if required for extended functionality)
    

### App Structure

The app follows a **Clean MVVM (Model-View-ViewModel)** architecture for easy scalability and maintainability.

#### Main Components

*   **ViewModel**: Handles the UI logic and communicates with the data layer.
    
*   **Repository**: Manages the data (from local or remote sources).

*   **Use-Cases**: Manages the data (from repository to viewmodel).
    
*   **Room Database**: Stores reminders locally on the device.
    
*   **Notifications**: Sends notifications for reminders.
    
*   **AlarmManager**: Schedules and manages alarms.
    

#### Screens

*   **Reminder List Screen**: Displays a list of all reminders.
    
*   **Add/Edit Reminder Screen**: Allows the user to add or edit a reminder with a time picker, dosage, and repeat options.
    
*   **Notification Receiver**: Triggers notifications when it's time for a reminder.
    

### Permissions

The app requires the following permissions:

*   **POST\_NOTIFICATIONS**: To show notifications for reminders.
    
