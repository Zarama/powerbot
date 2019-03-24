package sheel.scripts.OSRS.JugFiller;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import sheel.scripts.OSRS.JugFiller.tasks.Bank;
import sheel.scripts.OSRS.JugFiller.tasks.Fill;

import java.util.ArrayList;
import java.util.List;

@Script.Manifest(
        name = "SJugFiller",
        description = "Fills jugs and banks them!",
        properties = "author=sheel;client=4"
)
public class JugFiller extends PollingScript<ClientContext>
{
    private Bank bankTask = new Bank(ctx);
    private Fill fillTask = new Fill(ctx);

    private List<Task> taskList;

    @Override
    public void start()
    {
        taskList = new ArrayList<>();
        taskList.add(bankTask);
        taskList.add(fillTask);
    }


    @Override
    public void poll()
    {
        for (Task task : taskList)
        {
            if(task.validate())
            {
                task.execute();
            }
        }
    }
}
