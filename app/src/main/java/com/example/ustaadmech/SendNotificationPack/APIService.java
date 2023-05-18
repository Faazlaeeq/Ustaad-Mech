package com.example.ustaadmech.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANp9eBwo:APA91bGN7ijKTb3gCwFvLy6mD-tAS0eX64TJE4NugbkGfGvCCyNIzhPqeh7HXq02DHvjqHGi2QQVbSJ3J3cEXel0TPFEOWZ5Onf6D8OatJnh0T9RWKrfepY6gRoN2U_aX81rkW1s-trB" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

