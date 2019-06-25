# CaptureClock
Lean and fun Android Alarm Clock App with some TensorFlow lite image recognition

![picture alt](https://lh3.googleusercontent.com/d8itdPXWWaJF9cfE5nBDMOkv-3Av4AKhYf3oXRn9cj0IaeLd-7L0JSRtifaxQG9YILsV=w720-h310-rw)
![picture alt](https://lh3.googleusercontent.com/e1Jz4tJT74jBCgi6Oc8-rmNq7ySBJegvvv4D8kdC0LrqCj3A3n_OksSg0658Ayyptg=w720-h310-rw)
![picture alt](https://lh3.googleusercontent.com/0fG42NVicEfU8Ke8DZsmzoPO-lYAjEGymYhHKeTzYhl4J83_3Uam0NO9q_ejNGFqRm0=w720-h310-rw)
![picture alt](https://lh3.googleusercontent.com/C2JMrRdIMzLUxJrFHTZEEOGAUDx7jFcMqRb0w6dFpuG5tLgnnmpkg1RKzeR2Dbvia04=w720-h310-rw)

### Concept
When the alarm goes off a random object gets chosen and only if you capture it the alarm sound gets silent.<br/>
But if you don't want to wake everybody up while roaming through the house, just shake your device and the volume percentage drops accordingly.<br/>
The app also has a timer function and you can choose the alarm sound, volume and give it a nice name.<br/>
Just try it -> [Google Play](https://bit.ly/2ZDd55z) :thumbsup:

# Used & Learned
 ### TensorFlow (AI :squirrel:)
  * Retrain a TensorFlow model
  * Convert a model in to a TFlite graph (usable on a mobile device)
  
  
 ### Android JetPack (Architecture components)
  * Room (sqLite database framework)
  * Databinding 
  * ViewModel and LiveData
  
  
 ### Camera implementation


 ### Clean project structure
  * packages
    * adapter
    * receiver_services
    * database
    * utils
    * pojos
    * viewModels
  * UI	→ Dao	→ Repository	→ (Back-End)



