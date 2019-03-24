package itzmyfancysauce;

import org.powerbot.script.rt4.ClientContext;

public class WidgetManager {
    private ClientContext ctx;

    public WidgetManager(ClientContext ctx) {
        this.ctx = ctx;
    }

    public boolean isTradeVisible() {
        try {
            return ctx.widgets.widget(324).components()[0].visible();
        } catch(ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean tanHide() {
        return ctx.widgets.widget(324).components()[AlKharidTanner.widgetID].interact("Tan All");
    }
}
