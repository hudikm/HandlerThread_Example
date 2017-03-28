package sk.uniza.fri.handlerthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int IdNum = 0;

    /***
     * Handler running on UI thread
     */
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(MainActivity.this, "Timer expired: " + (int) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    /***
     * Create new HandlerThread used as a timer
     */
    HandlerThread handlerThread = new HandlerThread("Timer thread");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start(); // Start handler thread
        Looper looper = handlerThread.getLooper(); // get Looper from handlerthread

        final MyTimerHandler myTimerHandler = new MyTimerHandler(looper); //Create new Custom Handler with looper from handlerthread. This handler runs on new thread

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /***
                 * Obtain and send messge to MyTimerHandler
                 */
                Message message = myTimerHandler.obtainMessage(1, IdNum++);
                myTimerHandler.sendMessage(message);
            }
        });


    }

    /**
     * Inner class used for handling message on HandlerThread
     */
    class MyTimerHandler extends Handler {

        public MyTimerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Thread.sleep(1000); //Sleep for 1s

                /***
                 * Obtain and send messge to MyTimerHandler
                 */
                Message message = myHandler.obtainMessage(msg.what, msg.obj);
                myHandler.sendMessage(message);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
