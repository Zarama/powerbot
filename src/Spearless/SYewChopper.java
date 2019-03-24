package Spearless;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


@Script.Manifest(name = "SYewChopper", properties = "author=Spearless; topic=1333332; client=4;", description = "Cuts Yews and banks in East Varrock,DraynorV,GE,SouthFalador,Edgeville")
public class SYewChopper extends PollingScript<ClientContext> implements PaintListener,MessageListener {
    Area EDGEVILLE_AREA= new Area(new Tile(3083,3481),new Tile(3092,3466));
    Tile TileToTreesEDG[]={new Tile(3093, 3489, 0), new Tile(3090, 3486, 0), new Tile(3093, 3483, 0), new Tile(3093, 3479, 0), new Tile(3093, 3475, 0), new Tile(3093, 3471, 0), new Tile(3089, 3470, 0)};
    Tile TILEBETWEEN[]={new Tile(3088,3475)};
    Area EDGEVILLE_BANK_AREA= new Area(new Tile(3090,3499),new Tile(3096,3487));
    Tile EDGEVILLE_TILE_TO_BANK[]={new Tile(3089, 3471, 0), new Tile(3093, 3472, 0), new Tile(3094, 3476, 0), new Tile(3094, 3480, 0), new Tile(3094, 3484, 0), new Tile(3090, 3487, 0), new Tile(3092, 3491, 0)};
Area FALADOR_TREEAREA= new Area(new Tile(2993,3314), new Tile(3001,3308));
    Area FALADOR_BANK= new Area(new Tile(3009,3358), new Tile(3019,3354));
    Area SOUTH_LUMB= new Area(new Tile(3245,3199),new Tile(3250,3205));
    Area STAIR_AREA= new Area(new Tile(3204,3229,0),new Tile(3210,3225,0));
    Tile ToBankFalador[]={new Tile(2999, 3311, 0), new Tile(2999, 3315, 0), new Tile(3002, 3318, 0), new Tile(3005, 3321, 0), new Tile(3006, 3325, 0), new Tile(3006, 3329, 0), new Tile(3006, 3333, 0), new Tile(3006, 3337, 0), new Tile(3006, 3341, 0), new Tile(3006, 3345, 0), new Tile(3006, 3349, 0), new Tile(3006, 3353, 0), new Tile(3006, 3357, 0), new Tile(3010, 3359, 0), new Tile(3013, 3355, 0)};
Tile tileToTreesFalador[]= {new Tile(3013, 3357, 0), new Tile(3009, 3359, 0), new Tile(3008, 3355, 0), new Tile(3008, 3351, 0), new Tile(3008, 3347, 0), new Tile(3008, 3343, 0), new Tile(3007, 3339, 0), new Tile(3007, 3335, 0), new Tile(3007, 3331, 0), new Tile(3007, 3327, 0), new Tile(3007, 3323, 0), new Tile(3007, 3319, 0), new Tile(3007, 3315, 0), new Tile(3004, 3318, 0), new Tile(3001, 3315, 0), new Tile(2999, 3311, 0)};
    Tile TileToTreesLUMB[]={new Tile(3208, 3220, 2), new Tile(3206, 3223, 2), new Tile(3206, 3226, 2), new Tile(3206, 3229, 2), new Tile(3205, 3228, 1), new Tile(3205, 3228, 0), new Tile(3208, 3228, 0), new Tile(3211, 3228, 0), new Tile(3214, 3227, 0), new Tile(3215, 3224, 0), new Tile(3215, 3221, 0), new Tile(3214, 3218, 0), new Tile(3217, 3218, 0), new Tile(3220, 3218, 0), new Tile(3223, 3218, 0), new Tile(3226, 3218, 0), new Tile(3229, 3219, 0), new Tile(3232, 3218, 0), new Tile(3232, 3215, 0), new Tile(3232, 3212, 0), new Tile(3234, 3209, 0), new Tile(3234, 3206, 0), new Tile(3236, 3203, 0), new Tile(3238, 3199, 0), new Tile(3241, 3196, 0), new Tile(3244, 3195, 0), new Tile(3247, 3198, 0)};
    Tile tileToBankLUMB[]={new Tile(3249, 3200, 0), new Tile(3245, 3199, 0), new Tile(3241, 3199, 0), new Tile(3237, 3202, 0), new Tile(3236, 3206, 0), new Tile(3236, 3210, 0), new Tile(3236, 3214, 0), new Tile(3233, 3218, 0), new Tile(3229, 3218, 0), new Tile(3225, 3218, 0), new Tile(3221, 3218, 0), new Tile(3217, 3218, 0), new Tile(3215, 3222, 0), new Tile(3215, 3226, 0), new Tile(3211, 3228, 0), new Tile(3207, 3228, 0), new Tile(3206, 3229, 1), new Tile(3206, 3229, 2), new Tile(3206, 3225, 2), new Tile(3206, 3221, 2)};
    Area LUMB_BANKAREA= new Area(new Tile(3204,3223,2),new Tile(3212,3215,2));
Area VEAST_BANK= new Area(new Tile(3250,3422), new Tile(3258,3419));
    Area VARROCK_EAST= new Area(new Tile(3246,3476), new Tile(3252,3470));
    Area GE_BANKAREA= new Area(new Tile(3162,3486   ), new Tile(3171,3492));
    Area GEYew_Area= new Area(new Tile(3201,3506), new Tile(3226,3498));
    Area Draynor_Yew = new Area(new Tile(3049, 3273, 0), new Tile(3059, 3268));
    Area Draynor_Bank= new Area( new Tile(3091,3247), new Tile(3098,3240));
    Tile TileToYewVEAST[]={new Tile(3254, 3420, 0), new Tile(3254, 3424, 0), new Tile(3251, 3427, 0), new Tile(3248, 3430, 0), new Tile(3248, 3434, 0), new Tile(3245, 3437, 0), new Tile(3243, 3441, 0), new Tile(3243, 3445, 0), new Tile(3244, 3449, 0), new Tile(3245, 3453, 0), new Tile(3248, 3456, 0), new Tile(3248, 3460, 0), new Tile(3250, 3465, 0), new Tile(3250, 3469, 0), new Tile(3247, 3472, 0)};
Tile TileToBankVEAST[]={new Tile(3248, 3471, 0), new Tile(3246, 3467, 0), new Tile(3246, 3463, 0), new Tile(3245, 3459, 0), new Tile(3245, 3455, 0), new Tile(3245, 3451, 0), new Tile(3245, 3447, 0), new Tile(3245, 3443, 0), new Tile(3245, 3439, 0), new Tile(3245, 3435, 0), new Tile(3246, 3431, 0), new Tile(3250, 3429, 0), new Tile(3253, 3426, 0), new Tile(3253, 3422, 0), new Tile(3257, 3420, 0)};
    Tile TileToTreesGE[]= {new Tile(3169, 3488, 0), new Tile(3173, 3488, 0), new Tile(3177, 3488, 0), new Tile(3181, 3488, 0), new Tile(3185, 3488, 0), new Tile(3189, 3489, 0), new Tile(3192, 3493, 0), new Tile(3195, 3496, 0), new Tile(3198, 3499, 0), new Tile(3202, 3501, 0), new Tile(3206, 3501, 0)};
    Tile tileToBankGE[]= {new Tile(3207, 3503, 0), new Tile(3204, 3502, 0), new Tile(3201, 3502, 0), new Tile(3197, 3499, 0), new Tile(3195, 3496, 0), new Tile(3193, 3493, 0), new Tile(3190, 3491, 0), new Tile(3187, 3491, 0), new Tile(3184, 3491, 0), new Tile(3181, 3491, 0), new Tile(3178, 3491, 0), new Tile(3174, 3491, 0), new Tile(3171, 3489, 0), new Tile(3168, 3488, 0)};
    Tile tileToBankDraynor[]={new Tile(3056, 3271, 0), new Tile(3059, 3271, 0), new Tile(3062, 3272, 0), new Tile(3066, 3274, 0), new Tile(3069, 3276, 0), new Tile(3071, 3273, 0), new Tile(3071, 3270, 0), new Tile(3071, 3267, 0), new Tile(3071, 3264, 0), new Tile(3071, 3261, 0), new Tile(3074, 3258, 0), new Tile(3077, 3257, 0), new Tile(3077, 3254, 0), new Tile(3077, 3251, 0), new Tile(3080, 3249, 0), new Tile(3083, 3249, 0), new Tile(3086, 3249, 0), new Tile(3089, 3249, 0), new Tile(3092, 3247, 0), new Tile(3092, 3244, 0)};
    Tile tileToTreesDraynor[]={new Tile(3092, 3245, 0), new Tile(3089, 3247, 0), new Tile(3086, 3247, 0), new Tile(3083, 3247, 0), new Tile(3080, 3250, 0), new Tile(3078, 3253, 0), new Tile(3078, 3256, 0), new Tile(3075, 3257, 0), new Tile(3073, 3260, 0), new Tile(3071, 3263, 0), new Tile(3071, 3266, 0), new Tile(3071, 3269, 0), new Tile(3071, 3272, 0), new Tile(3071, 3275, 0), new Tile(3067, 3276, 0), new Tile(3064, 3274, 0), new Tile(3062, 3271, 0), new Tile(3059, 3270, 0), new Tile(3056, 3273, 0)};
    int  WcInitLevel, hours, seconds, minutes, Wclevel,WcExpInit;
    double runTime;
    long initime;
    int YewInvID=1515;
    int choser=0;
    int logsChopped=0;
    int BankDr[]={6947,6943,18492,18491,24101};
    GameObject bankDr= ctx.objects.select().id(BankDr).poll();
    Item YewsLogsItem=ctx.inventory.select().id(YewInvID).poll();
    GameObject stairs= ctx.objects.select().id(16671).poll();
    Font font = new Font(("Arial"), Font.BOLD, 16);
    void frame(){
        final JFrame frame = new JFrame();
        frame.setSize(300, 400);
        frame.setVisible(true);
        final JCheckBox draynorV= new JCheckBox("DraynorV");
        final JCheckBox GE= new JCheckBox("Grand Exchange");
        final JCheckBox VEAST= new JCheckBox("Varrock East");
        final JCheckBox LUMB= new JCheckBox("Lumbridge South");
        final JCheckBox FALADORS= new JCheckBox("Faladro South");
        final JCheckBox EDGEVILLE= new JCheckBox("Edgeville");
//final JCheckBox AntibanCamera= new JCheckBox("Antiban/rotatingCamera");
        JPanel panel = new JPanel();
        panel.add(VEAST);
        panel.add(GE);
        panel.add(draynorV);
       // panel.add(LUMB);
        panel.add(FALADORS);
        panel.add(EDGEVILLE);
        frame.add(panel);
        draynorV.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(draynorV.isEnabled()){
            choser=1;
        }
    }
});
        GE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(GE.isEnabled()){
                    choser=2;
                }
            }
        });
        VEAST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(VEAST.isEnabled()){
                    choser=3;
                }
            }
        });

        LUMB.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent actionEvent) {
        if(LUMB.isEnabled()){
            choser=4;
        }
    }
});
        FALADORS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(FALADORS.isEnabled()){
                    choser=5;
                }
            }
        });
        EDGEVILLE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(EDGEVILLE.isEnabled()){
                    choser=6;
                }
            }
        });
    }
    public void start() {
        initime=System.currentTimeMillis();
        WcInitLevel =ctx.skills.level(Constants.SKILLS_WOODCUTTING);
        WcExpInit= ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
        frame();

    }

    void chopYew(){
        GameObject YewTree= ctx.objects.select().id(1753).nearest().poll();
        if(ctx.players.local().animation()==-1 && ctx.inventory.select().count()<28&& YewTree.inViewport()&& !ctx.players.local().inMotion()){

            log.info("Chop");
            YewTree.interact("Chop");
            YewTree.click();

        }
    }
    void antiban() {
        Random random = new Random();
      int switcher= random.nextInt(100);
        int x = random.nextInt(1000)-300;
        int y = random.nextInt(1300)-400;

        switch (switcher) {
            case 1:
            ctx.input.move(x, y);

                break;
            case 2:
                ctx.input.move(x,y);
                break;
            case 5:
                ctx.input.move(x,y);
                break;
            case 7:
                ctx.camera.angle('s');
                break;
            case 8:
               ctx.camera.angle('w');
                break;
            case 9:
                ctx.camera.angle('e');
                break;
        }
    }



    public void poll() {
        switch (state()) {

            case CHDRAYNOR:
                antiban();
                ctx.input.speed(100);

                chopYew();
                if (ctx.players.local().animation() == 867||ctx.players.local().animation()==-1) {
                    antiban();
                }
                if (ctx.inventory.select().count() == 28 && !Draynor_Bank.contains(ctx.players.local())) {
                    TilePath path = ctx.movement.newTilePath(tileToBankDraynor);
                    path.randomize(2, 2);
                    path.traverse();
                    log.info("Going to bank");
                }


                if (ctx.inventory.select().count() == 28 && !ctx.players.local().inMotion()) {
                    YewsLogsItem = ctx.inventory.select().id(YewInvID).poll();

                    log.info("Banking");
                    bankDr.interact("Bank");
                    YewsLogsItem.interact("Deposit-All");
                }
                if (ctx.inventory.select().id(YewInvID).count() == 0 && !Draynor_Yew.contains(ctx.players.local())) {

                    log.info("Coming back");
                    TilePath path = ctx.movement.newTilePath(tileToTreesDraynor);
                    path.randomize(2, 2);
                    path.traverse();
                }
                break;
            case CHGE:
                antiban();
                chopYew();
                if (ctx.inventory.select().count() == 28 && !GE_BANKAREA.contains(ctx.players.local())) {

                    TilePath path = ctx.movement.newTilePath(tileToBankGE);
                    path.randomize(2, 2);
                    path.traverse();
                    ctx.camera.angle('n');
                }

                if (ctx.players.local().inMotion() == false && GE_BANKAREA.contains(ctx.players.local()) && ctx.inventory.select().count() == 28) {
                    YewsLogsItem = ctx.inventory.select().id(YewInvID).poll();
                    GameObject bankGE = ctx.objects.select().id(10060, 10059).poll();
                    bankGE.interact("Bank");

                    if (ctx.bank.open()) {
                        YewsLogsItem.interact("Deposit-All");

                    }
                }
                if (ctx.inventory.select().id(YewInvID).count() == 0 && !GEYew_Area.contains(ctx.players.local())) {

                    TilePath path = ctx.movement.newTilePath(TileToTreesGE);
                    path.randomize(2, 2);
                    path.traverse();
                }
                break;
            case CHVEAST:

                antiban();
                chopYew();
                if (ctx.inventory.select().count() == 28 && !VEAST_BANK.contains(ctx.players.local())) {

                    TilePath path = ctx.movement.newTilePath(TileToBankVEAST);
                    path.randomize(2, 2);
                    path.traverse();
                }
                if (VEAST_BANK.contains(ctx.players.local()) && !ctx.players.local().inMotion() && ctx.inventory.select().count() == 28) {
                    GameObject bankers = ctx.objects.select().id(7409).poll();
                    bankers.interact("Bank");
                    YewsLogsItem = ctx.inventory.select().id(YewInvID).poll();

                    if (ctx.bank.open()) {

                        YewsLogsItem.interact("Deposit-All");
                    }
                }
                if (ctx.inventory.select().id(YewInvID).count() == 0 && !VARROCK_EAST.contains(ctx.players.local())) {

                    TilePath path = ctx.movement.newTilePath(TileToYewVEAST);
                    path.randomize(2, 2);
                    path.traverse();
                }
                break;
            case CHOLUMB:
                chopYew();
                if (!STAIR_AREA.contains(ctx.players.local())&&ctx.inventory.select().count() == 28 && !LUMB_BANKAREA.contains(ctx.players.local())) {
                    TilePath path = ctx.movement.newTilePath(tileToBankLUMB);
                    path.randomize(1, 1);
                    path.traverse();
                }
                    if(STAIR_AREA.contains(ctx.players.local())){
                        stairs.click();
                        log.info("Climbing up");
                    }
                    if(LUMB_BANKAREA.contains(ctx.players.local())) {
                        bankDr.interact("Bank");
                    if(ctx.bank.open()){
                        YewsLogsItem.interact("Deposit-All");
                    }
                    if(ctx.inventory.select().id(YewInvID).count()==0&& !SOUTH_LUMB.contains(ctx.players.local())) {

                        TilePath path1 = ctx.movement.newTilePath(TileToTreesLUMB);
                        path1.randomize(1, 1);
                        path1.traverse();
                        GameObject stairs3 = ctx.objects.select().id(16673).poll();
                        if (stairs3.inViewport()) {
                            stairs3.interact("Climb-down");

                        }
                        if (stairs.inViewport()) {
                            stairs.interact("Climb-down");
                        }
                    }

                    }

                break;
            case CHFALADORSOUTH:
                chopYew();
                if(ctx.inventory.select().count()==28&& !FALADOR_BANK.contains(ctx.players.local()))
                {
                    log.info("Walking to bank");
                    TilePath path= ctx.movement.newTilePath(ToBankFalador);
                path.randomize(2,2);
                    path.traverse();
                }
                if(FALADOR_BANK.contains(ctx.players.local())&& ctx.inventory.select().count()==28){
                    bankDr.interact("Bank");
                    log.info("banking");
                    bankDr.click();
                    if(ctx.bank.open()|| ctx.bank.opened()){
                        YewsLogsItem.interact("Deposit-All");

                    }
                }
                if(ctx.inventory.select().id(YewInvID).count()==0&& !FALADOR_TREEAREA.contains(ctx.players.local())){
                    TilePath path1=ctx.movement.newTilePath(tileToTreesFalador);
                    path1.randomize(2,2);
                    path1.traverse();
                }

                break;
            case CHEDGEVILLE:
                antiban();
                chopYew();
                GameObject YewTree= ctx.objects.select().id(1753).nearest().poll();

                if(!YewTree.inViewport()&&ctx.inventory.select().count()<28&&EDGEVILLE_AREA.contains(ctx.players.local())){

                    TilePath path= ctx.movement.newTilePath(TILEBETWEEN);

                    path.traverse();
                    ctx.camera.angle('w');
                    log.info("Looking for a tree");

                }
                if(ctx.inventory.select().count()==28 && !EDGEVILLE_BANK_AREA.contains(ctx.players.local())){
                    TilePath path1= ctx.movement.newTilePath(EDGEVILLE_TILE_TO_BANK);
                    path1.randomize(2,2);
                    path1.traverse();
                    log.info("Walking bank");

                    if(!path1.valid()){
                        GameObject door= ctx.objects.select().id(1543).poll();
                        door.interact("Open");
                    }
                }
                if( ctx.inventory.select().count()==28&&EDGEVILLE_BANK_AREA.contains(ctx.players.local())) {
                    ctx.camera.angle('n');
                    bankDr.interact("Bank");
                    log.info("Banking");
                    if (ctx.inventory.select().count() == 28) {
                        YewsLogsItem.interact("Deposit-All");

                    }
                }
                    if (ctx.inventory.select().id(YewInvID).count() == 0 && !EDGEVILLE_AREA.contains(ctx.players.local())) {
                        TilePath path3 = ctx.movement.newTilePath(TileToTreesEDG);
                        path3.randomize(2,2);
                        path3.traverse();
                    log.info("Walking to trees");
                    }

                break;

        }
    }

    private State state() {
        if ( choser==1)
        {
            return State.CHDRAYNOR;
    }else if(choser==2){
            return State.CHGE;
        }else if(choser==3){
            return State.CHVEAST;

        }else if (choser==4){

return State.CHOLUMB;

        }else if(choser==5){
            return State.CHFALADORSOUTH;
        }else if(choser==6){
            return State.CHEDGEVILLE;
        }else{
            log.info("Nothing");
            return State.NOTHING;
        }
    }
    private enum State{
        CHDRAYNOR,CHOLUMB,NOTHING,CHGE,CHVEAST,CHFALADORSOUTH,CHEDGEVILLE;

    }
    public void repaint(Graphics g1){

        int currentExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
        int currLevel = ctx.skills.level(Constants.SKILLS_WOODCUTTING);
        int logsToNextLevel = (ctx.skills.experienceAt(currLevel + 1) - currentExp) / 175;
        int expGained= currentExp-WcExpInit;
        Wclevel = currLevel -WcInitLevel;
        hours = (int) ((System.currentTimeMillis() - initime) / 3600000);
        minutes = (int) ((System.currentTimeMillis() - initime) / 60000 % 60);
        seconds = (int) ((System.currentTimeMillis() - initime) / 1000) % 60;
        runTime = (double) (System.currentTimeMillis() - initime) / 3600000;

        Graphics2D g2= (Graphics2D) g1;
        int posx= (int) ctx.input.getLocation().getX();
        int posy= (int) ctx.input.getLocation().getY();
        g2.setColor(Color.GREEN);
        g2.drawLine(posx,posy-10,posx,posy+10);
        g2.drawLine(posx-10,posy,posx+10,posy);
        g2.setColor(Color.GREEN);
        g2.drawOval(posx-9,posy-9,18,18);
        g1.setColor(Color.BLACK);
        g1.fillRect(1,340,515,140);
        long thickness = 4;
        BasicStroke basic= new BasicStroke(thickness);
        g2.setColor(Color.WHITE);
        g2.setStroke(basic);
        g2.drawRect(1, 340, 515, 140);

        g1.setColor(Color.WHITE);
        g1.setFont(font);
        g1.drawString("Levels gained : " + Wclevel, 20, 375);
        g1.drawString("Time passed : " + hours + " : " + minutes + " : " + seconds, 20, 400);
        g1.drawString("Experience gained : " +expGained,20,425);
        g1.drawString("Logs to next level: " + logsToNextLevel, 20, 450);
        g1.drawString("Logs chopped :  " + logsChopped, 20, 475);
        int money= (int) ((logsChopped*360)/runTime);
        g1.drawString("Money/Hour "+money,335,375);
        g1.drawString("Spearless", 335,400);

    }
    public void messaged(MessageEvent me) {
        String msg = me.text();

         if(msg.contains(" get some yew logs"))
        {
        logsChopped++;

        }
    }
}
