package com.example.ustaadmech.SendNotificationPack;

import android.content.Context;
import android.widget.Toast;

import com.example.ustaadmech.adminapprove;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sendnoti {
     APIService apiService;
     Context context;

    public sendnoti(Context context) {
        this.context = context;
    }


    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
