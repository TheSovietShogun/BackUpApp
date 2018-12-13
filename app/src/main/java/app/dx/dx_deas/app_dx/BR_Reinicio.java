package app.dx.dx_deas.app_dx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BR_Reinicio extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, log.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);

    }
}