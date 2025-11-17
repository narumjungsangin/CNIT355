package com.example.cnit355_lab9m1_junsu_yoon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class MyReceiver extends BroadcastReceiver {

    public static final String ACTION_APP_SMS = "MY_SMS_FOR_APP";
    public static final String EXTRA_MSG = "sms_msg";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) return;

        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (msgs == null || msgs.length == 0) return;

        StringBuilder body = new StringBuilder();
        for (SmsMessage m : msgs) {
            if (m != null && m.getMessageBody() != null) {
                body.append(m.getMessageBody());
            }
        }

        Intent forward = new Intent(ACTION_APP_SMS);
        forward.putExtra(EXTRA_MSG, body.toString());
        context.sendBroadcast(forward);
    }
}
