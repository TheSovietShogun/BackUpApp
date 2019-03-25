package app.dx.dx_deas.app_dx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BR_Reinicio extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



                Intent i = new Intent(context, splash.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          /*      i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
               i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                i.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);*/
                context.startActivity(i);



    }
}