# GCMNotify
AWS SNS GCM notification

GCM send notification to android device:
https://docs.kii.com/cn/samples/push-notifications/push-notifications-android/
https://www.intertech.com/Blog/push-notifications-tutorial-for-android-using-google-cloud-messaging-gcm/

make a configuration file:
https://pushbots.com/developer/docs/android-configuration
or follow the instruction to use firebase

GCM TESTING:
http://apns-gcm.bryantan.info/

TO register AWS SNS with GCM for testing(In this case, each phone has registered one by one):
Set in aws sns web page

1. ![create](https://cloud.githubusercontent.com/assets/8034605/25904330/37275fac-359f-11e7-9612-ded028a8df06.PNG)
2. ![cr1](https://cloud.githubusercontent.com/assets/8034605/25904362/51a8b51a-359f-11e7-82de-5467cb163d5e.PNG)
3. 
Option 1: ![capture](https://cloud.githubusercontent.com/assets/8034605/25943455/31fe2ac8-3640-11e7-8520-abd54d34695a.PNG)
Option 2: Use Code in SNSTestActivity: 'AWSCreateEndpointTask' End Point can be known through result
reference Link: http://www.mobileaws.com/2015/03/25/amazon-sns-push-notification-tutorial-android-using-gcm/
3. ![createtopic](https://cloud.githubusercontent.com/assets/8034605/25943334/cfafdd8a-363f-11e7-9d82-603079d1ab88.PNG)
4. ![topic](https://cloud.githubusercontent.com/assets/8034605/25943315/c7cccba0-363f-11e7-9e8c-fc5693944816.PNG)
5. ![cs2](https://cloud.githubusercontent.com/assets/8034605/25904366/56f428a6-359f-11e7-9bea-3237443cbbe6.PNG)
6. ![sub](https://cloud.githubusercontent.com/assets/8034605/25904376/5c3a52ae-359f-11e7-876d-19a1560e3887.PNG)




Video reference:
https://www.youtube.com/watch?v=XM3THjZkRQM
