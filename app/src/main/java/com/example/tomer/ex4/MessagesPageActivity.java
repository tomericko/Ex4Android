package com.example.tomer.ex4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessagesPageActivity extends AppCompatActivity {

    private int a = 0;

    private Button btnSend;
    private EditText inputMsg;

    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;

    private ShakeListener listener;

    private SwipeRefreshLayout swipeToRefreshLayout;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MessagesPageActivity.this, "received", Toast.LENGTH_SHORT).show();

            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Intent in = new Intent(context,MessagesPageActivity.class);
            PendingIntent pending=PendingIntent.getActivity(context, 0, in, 0);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle("Title")
                    .setContentText(
                            "Text").setSmallIcon(R.drawable.email)
                    .setContentIntent(pending).setWhen(System.currentTimeMillis()).setAutoCancel(true);

            Notification notification;
            if (android.os.Build.VERSION.SDK_INT>16)
            {
                notification = builder.getNotification();
            }else
            {
                notification = builder.build();
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notificationManager.notify(5, notification);

        }
    };
    private Intent timerServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listener = new ShakeListener(this);
        listener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                if (a % 2 == 0)
                {
                    appendMessage(new Message("Roi", "hey", true));
                }
                else
                {
                    appendMessage(new Message("Tomer", "hey", false));
                }
                a++;
                //Toast.makeText(MessagesActivity.this, "nananana", Toast.LENGTH_SHORT).show();
            }
        });

        swipeToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MessagesPageActivity.this, "mimimi", Toast.LENGTH_SHORT).show();
                swipeToRefreshLayout.setRefreshing(false);
            }
        });

        timerServiceIntent = new Intent(this, TimerService.class);
        startService(timerServiceIntent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TimerService.BROADCAST_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        stopService(timerServiceIntent);
        super.onDestroy();

    }


    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }


    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        //unregisterReceiver(receiver);
        super.onPause();
        listener.pause();
    }

    @Override
    protected void onResume() {
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(TimerService.BROADCAST_ACTION);
        //registerReceiver(receiver, filter);
        super.onResume();
        listener.resume();
    }
}
