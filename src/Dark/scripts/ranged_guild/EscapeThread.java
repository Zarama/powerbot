package Dark.scripts.ranged_guild;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Widget;

public class EscapeThread extends Thread implements Runnable {
    private ClientContext ctx;
    public EscapeThread(ClientContext ctx){
        this.ctx = ctx;
    }

    private volatile boolean stop = false;

    @Override
    public void run(){
        while(!stop){
            Widget toClose = ctx.widgets.widget(325);
            if(toClose != null){
                ctx.widgets.close(toClose,true);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStop(boolean stop){
        System.out.println("Stopped escape thread.");
        this.stop = stop;
    }
}
