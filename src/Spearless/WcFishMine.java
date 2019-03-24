package Spearless;

import org.powerbot.script.Area;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import java.util.Random;
import java.awt.*;


/**
 * Created by Gaston on 23/05/2017.
 */

@Manifest(name = "WcFishMine", properties = "author=Spearless; topic=1333332; client=4;", description = "The objective of the script is for new accounts to level up Mining, Woodcutting. Start at Lumbridge with an axe, a pickaxe and a net, nothing else")
public class WcFishMine extends PollingScript<ClientContext>implements PaintListener {
   public Tile TILE_TO_ZONE[] = {new Tile(3223, 3216, 0), new Tile(3227, 3217, 0), new Tile(3231, 3218, 0), new Tile(3235, 3221, 0), new Tile(3236, 3225, 0), new Tile(3233, 3229, 0), new Tile(3230, 3232, 0), new Tile(3226, 3234, 0), new Tile(3223, 3237, 0), new Tile(3219, 3237, 0), new Tile(3215, 3237, 0), new Tile(3211, 3238, 0), new Tile(3208, 3241, 0), new Tile(3204, 3242, 0), new Tile(3200, 3242, 0), new Tile(3195, 3241, 0), new Tile(3192, 3244, 0), new Tile(3188, 3246, 0), new Tile(3184, 3246, 0), new Tile(3180, 3248, 0), new Tile(3177, 3252, 0), new Tile(3172, 3253, 0), new Tile(3168, 3254, 0), new Tile(3164, 3254, 0), new Tile(3160, 3254, 0), new Tile(3156, 3254, 0), new Tile(3151, 3254, 0), new Tile(3147, 3253, 0), new Tile(3143, 3251, 0), new Tile(3139, 3251, 0), new Tile(3137, 3247, 0), new Tile(3136, 3251, 0), new Tile(3136, 3255, 0), new Tile(3135, 3259, 0), new Tile(3131, 3261, 0), new Tile(3127, 3263, 0), new Tile(3123, 3263, 0), new Tile(3119, 3263, 0), new Tile(3115, 3263, 0), new Tile(3111, 3263, 0), new Tile(3107, 3263, 0),
            new Tile(3103, 3263, 0), new Tile(3103, 3259, 0), new Tile(3102, 3255, 0), new Tile(3098, 3253, 0), new Tile(3095, 3250, 0), new Tile(3091, 3250, 0), new Tile(3087, 3250, 0), new Tile(3084, 3253, 0), new Tile(3080, 3255, 0), new Tile(3076, 3257, 0), new Tile(3074, 3261, 0), new Tile(3074, 3265, 0), new Tile(3074, 3269, 0), new Tile(3074, 3273, 0), new Tile(3071, 3276, 0), new Tile(3067, 3276, 0), new Tile(3064, 3273, 0), new Tile(3061, 3270, 0), new Tile(3059, 3266, 0), new Tile(3055, 3264, 0), new Tile(3051, 3264, 0), new Tile(3047, 3264, 0), new Tile(3043, 3264, 0), new Tile(3039, 3262, 0), new Tile(3035, 3263, 0), new Tile(3031, 3263, 0), new Tile(3027, 3263, 0), new Tile(3023, 3263, 0), new Tile(3019, 3261, 0), new Tile(3015, 3262, 0), new Tile(3011, 3262, 0), new Tile(3007, 3262, 0), new Tile(3004, 3259, 0), new Tile(3000, 3258, 0), new Tile(2996, 3258, 0), new Tile(2996, 3258, 0), new Tile(2994, 3254, 0), new Tile(2994, 3250, 0), new Tile(2994, 3246, 0), new Tile(2994, 3242, 0), new Tile(2994, 3238, 0), new Tile(2991, 3235, 0), new Tile(2988, 3232, 0), new Tile(2987, 3236, 0), new Tile(2985, 3240, 0), new Tile(2983, 3244, 0)};

   public Tile TILE_TO_FISHINGZONE[] = {new Tile(3222, 3218, 0), new Tile(3226, 3218, 0), new Tile(3230, 3218, 0), new Tile(3232, 3214, 0), new Tile(3233, 3210, 0), new Tile(3234, 3206, 0), new Tile(3237, 3202, 0), new Tile(3239, 3198, 0), new Tile(3241, 3194, 0), new Tile(3244, 3191, 0), new Tile(3244, 3187, 0), new Tile(3244, 3183, 0), new Tile(3242, 3179, 0), new Tile(3241, 3175, 0), new Tile(3241, 3171, 0), new Tile(3241, 3167, 0), new Tile(3241, 3163, 0), new Tile(3241, 3159, 0), new Tile(3241, 3155, 0), new Tile(3241, 3151, 0), new Tile(3239, 3147, 0)};
    public Tile WALKING_BACK_FISHING[] = {new Tile(3239, 3146, 0), new Tile(3239, 3150, 0), new Tile(3239, 3154, 0), new Tile(3239, 3158, 0), new Tile(3239, 3162, 0), new Tile(3239, 3166, 0), new Tile(3239, 3170, 0), new Tile(3239, 3174, 0), new Tile(3239, 3178, 0), new Tile(3239, 3182, 0), new Tile(3240, 3186, 0), new Tile(3243, 3189, 0), new Tile(3244, 3193, 0), new Tile(3241, 3196, 0), new Tile(3240, 3200, 0), new Tile(3236, 3203, 0), new Tile(3236, 3207, 0), new Tile(3236, 3211, 0), new Tile(3235, 3215, 0), new Tile(3232, 3218, 0), new Tile(3228, 3218, 0), new Tile(3224, 3218, 0)};
    public Area TREE_AREA = new Area(new Tile(2976, 3264), new Tile(2994, 3253));
    public Area MINING_AREA = new Area(new Tile(2974, 3253), new Tile(2995, 3232));
    public Area LUMB_AREA = new Area(new Tile(2990, 3280, 0), new Tile(3234, 3217));
    public Area FISHING_AREA = new Area(new Tile(3230, 3154), new Tile(3247, 3145));
    public Area TREEPLAYER_AREA= new Area(new Tile(2977,3263), new Tile(2997,3251));
    public int clayID[] = {7453, 7484};
    public int copperINVID = 436;
    public int treeID[] = {1278, 1276};
    public int logsINVID = 1511;
    public int fishinSpotID = 1530;
    public int fishingInvID[]={317,319,321};
    public Color fishingColorBox= new Color(44, 200, 196);
    public Color woodColorBox = new Color(46, 200, 27);
    public Color mouseColor = new Color(209, 37, 16), boxColor = new Color(209, 30, 12), textColor = new Color(0, 0, 0);
    public Font font = new Font(("Arial"), Font.BOLD, 12);
    public Font titleFont= new Font(("Arial"), Font.ITALIC,16);
    public int hours, minutes, seconds, expGained, initialMinExp, initialWCExp, expGainedWC,initialFishExp,expGainedFish,initFishLevel,fishLevel,initWCLevel,initMinLevel,WCLevel,MinLevel;
    public long initialTime;
    private double runTime;

    private Npc fishingSpot = ctx.npcs.select().id(fishinSpotID).poll();

    public void moveToInv() {

        Random randomINVX = new Random();
        Random randomINVY = new Random();
        int invx = randomINVX.nextInt(30) + 627;
        int invy = randomINVY.nextInt(30) + 169;
        ctx.input.move(invx, invy);
        ctx.input.click(true);
    }

    public void checkMiningSkill() throws InterruptedException {

        Random randX = new Random();
        Random randY = new Random();
        int x = randX.nextInt(30) + 560;
        int y = randY.nextInt(31) + 170;
        ctx.input.move(x, y);
        ctx.input.click(true);

        Random randomXINSK = new Random();
        Random randomYINSK = new Random();
        int xsk = randomXINSK.nextInt(70) + 674;
        int ysk = randomYINSK.nextInt(30) + 206;
        ctx.input.move(xsk, ysk);

        Random randomsleep = new Random();
        int sleepe = randomsleep.nextInt(2000);
        Thread.sleep(2000 + sleepe);

        moveToInv();
    }

    public void antiBan() {
        Random randomXSK = new Random();
        Random randomYSK = new Random();
        int x = randomXSK.nextInt(900);
        int y = randomYSK.nextInt(600);
        ctx.input.move(x, y);
    }
    private void dropFish(){
        if(ctx.inventory.select().id(fishingInvID).count()>=1) {
            ctx.inventory.id(fishingInvID).each(new Filter<Item>() {
                @Override
                public boolean accept(Item item) {
                    return item.interact("Drop");
                }

            });
        }
    }
    private void dropLogsAtEnd(){
        if(ctx.inventory.select().id(logsINVID).count()>=1) {
            ctx.inventory.id(logsINVID).each(new Filter<Item>() {
                @Override
                public boolean accept(Item item) {
                    return item.interact("Drop");
                }

            });
        }
    }
    private void walkingFromFishing() {
        TilePath path = ctx.movement.newTilePath(WALKING_BACK_FISHING);
        path.randomize(2, 2);
        path.traverse();
    }

    private void dropOresAtEnd (){
        if(ctx.inventory.select().id(copperINVID).count()>=1){
            ctx.inventory.select().id(copperINVID).each(new Filter<Item>() {
                @Override
                public boolean accept(Item item) {
                    return item.interact("Drop");
                }
            });
        }
    }
    private void teleportToLumbridge() throws InterruptedException {

        Random randomX = new Random();
        Random randomY = new Random();
        Random randomYX = new Random();
        int YX = randomYX.nextInt(5);
        int x = randomX.nextInt(33) + 727;
        int y = randomY.nextInt(30) + 170;
        ctx.input.move(x, y);
        ctx.input.click(true);
        Thread.sleep(6000);
        ctx.input.move(573 + YX, 231 + YX);
        ctx.input.click(true);
        Thread.sleep(2000 + x);
        moveToInv();
      }



    private void fishing() {
        if (ctx.players.local().animation() == -1 && ctx.inventory.select().count()<28 ) {
            fishingSpot = ctx.npcs.select().id(fishinSpotID).nearest().poll();
            log.info("FISHING");
            fishingSpot.interact("Net");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        if(ctx.inventory.select().count()==28) {
            ctx.inventory.select().id(fishingInvID).each(new Filter<Item>() {
                @Override
                public boolean accept(Item item) {
                    log.info("Dropping");
                    return item.interact("Drop");

                }
            });
        }
    }

    private void walkingToMining() throws InterruptedException {
        TilePath pathToLumb = ctx.movement.newTilePath(TILE_TO_ZONE);
        pathToLumb.randomize(2, 2);
        pathToLumb.traverse();
    }

    private void mineRocks() {
        GameObject rock = ctx.objects.select().id(clayID).nearest().poll();
        if (ctx.players.local().animation() == -1) {
            rock.interact("Mine");
            if (rock.inViewport()) {

                log.info("Mining");

            } else {

                ctx.movement.step(rock);
                ctx.camera.turnTo(rock);
                log.info("camera");
            }

        }
    }

    private void chop() {

         GameObject tree = ctx.objects.select().id(treeID).nearest().poll();
        if (!TREEPLAYER_AREA.contains(ctx.players.local())) {
            Tile[] goTreeTile = {new Tile(2982, 3256)};
            TilePath path = ctx.movement.newTilePath(goTreeTile);
            path.randomize(3, 3);
            path.traverse();
        }
        if (ctx.players.local().animation() == -1 &&  ctx.inventory.select().id(logsINVID).count() < 25 && TREEPLAYER_AREA.contains(ctx.players.local())&& !ctx.players.local().inMotion()) {
            tree.interact("Chop");

            log.info("Chopping");

        }else{
            if(!tree.inViewport())
                ctx.objects.select().id(treeID).nearest().poll();
            ctx.camera.turnTo(tree);
        }

    }

    @Override
    public void start() {

        log.info("Hello, RSBot!");
        ctx.input.speed(100);
        initialTime = System.currentTimeMillis();
        initialMinExp = ctx.skills.experience(Constants.SKILLS_MINING);
        initialWCExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
        initialFishExp= ctx.skills.experience(Constants.SKILLS_FISHING);
        initFishLevel= ctx.skills.level(Constants.SKILLS_FISHING);
        initMinLevel= ctx.skills.level(Constants.SKILLS_MINING);
        initWCLevel= ctx.skills.level(Constants.SKILLS_WOODCUTTING);

    }

    public void poll() {

            switch (state()) {
                case WALKTOFISHING:

                    if (!LUMB_AREA.contains(ctx.players.local()) && minutes < 120 && minutes >= 119 && seconds < 50 && ctx.players.local().animation() == -1 || ctx.players.local().animation() == -1 && !LUMB_AREA.contains(ctx.players.local()) && minutes >= 299 && seconds < 13) {
                        try {
                            teleportToLumbridge();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    TilePath path = ctx.movement.newTilePath(TILE_TO_FISHINGZONE);
                    path.randomize(2, 2);
                    path.traverse();
                    log.info("Walking to fishing zone");
                    break;

                case WALKBACKLUMB:

                    if (minutes > 180) {
                        walkingFromFishing();
                    }
                    break;
                case FISH:
                    fishing();
                    break;
                case WALKLUMB:

                    if (LUMB_AREA.contains(ctx.players.local()))
                        try {
                            walkingToMining();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    log.info("Walking to mine");
                    break;
                case CHOP:

                    chop();
                    if (ctx.inventory.select().count() == 28) {
                        ctx.inventory.select().id(logsINVID).each(new Filter<Item>() {
                            @Override
                            public boolean accept(Item item) {
                                return item.interact("Drop");
                            }
                        });
                    }


                    break;

                case MINE:

                    Random random = new Random();
                    int x = random.nextInt(29);
                    switch (x) {
                        case 4:
                            antiBan();
                            break;
                        case 5:
                            antiBan();
                            break;
                        case 10:
                            try {
                                checkMiningSkill();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 20:
                            antiBan();
                            break;
                        case 21:
                            antiBan();
                            break;
                        default:
                            mineRocks();

                    }
                    if (ctx.inventory.select().count() == 28) {
                        ctx.inventory.select().id(copperINVID).each(new Filter<Item>() {
                            @Override
                            public boolean accept(Item item) {
                                return item.interact("Drop");

                            }
                        });
                        log.info("Drop");
                    }
                    break;
                case NOTHING:

                    try {
                        walkingToMining();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }


    private State state() {
        fishingSpot = ctx.npcs.id(fishinSpotID).select().poll();
        GameObject rock = ctx.objects.select().id(clayID).nearest().poll();
        if (LUMB_AREA.contains(ctx.players.local()) && minutes < 5 && !MINING_AREA.contains(ctx.players.local()) || LUMB_AREA.contains(ctx.players.local()) && minutes>181 && minutes<189&& !rock.inViewport()) {
            log.info("walking to mine");
            return State.WALKLUMB;

        } else if (rock.inViewport() && ctx.inventory.select().id(logsINVID).count() == 0 && minutes < 60&& MINING_AREA.contains(ctx.players.local()) || minutes >180 && minutes<= 240&& MINING_AREA.contains(ctx.players.local()) ) {
log.info("MINE");
            dropFish();
            return State.MINE;
        } else if (minutes >= 60 && minutes < 119|| minutes>240&& minutes<300) {
          dropOresAtEnd();
            log.info("Woodcuting");
            return State.CHOP;

        } else {
            if ((minutes >= 119&& minutes<122) && (!FISHING_AREA.contains(ctx.players.local()))|| minutes >=300&& minutes<303 && !FISHING_AREA.contains(ctx.players.local())) {
                log.info("Walking to fishing zone");
               dropLogsAtEnd();
                return State.WALKTOFISHING;

            } else if ((minutes >= 120 && minutes < 180) && FISHING_AREA.contains(ctx.players.local()) ||minutes>=300&& FISHING_AREA.contains(ctx.players.local()) ) {
                log.info("Fishing");
                return State.FISH;
            } else if ((minutes >= 180 && minutes <= 181) ) {
              dropFish();
                return State.WALKBACKLUMB;
            } else {

                log.info("NOTHING");
                return State.NOTHING;
            }
        }
    }


    private enum State{
        FISH,MINE,CHOP,NOTHING,WALKLUMB,WALKBACKLUMB,WALKTOFISHING

    }
    public void repaint(Graphics g1){
        Graphics2D g= (Graphics2D)g1;

        int x= (int) ctx.input.getLocation().getX();
        int y= (int)ctx.input.getLocation().getY();
        g.setColor(mouseColor);
        g.drawLine(x,y-10,x,y+10);
        g.drawLine(x-10,y,x+10,y);

        hours=(int)((System.currentTimeMillis()-initialTime)/3600000);
        minutes=(int)((System.currentTimeMillis()-initialTime)/60000);
        seconds=(int)((System.currentTimeMillis()-initialTime)/1000)%60;
        runTime= (double)(System.currentTimeMillis()-initialTime)/3600000;

        int xperhourMin = (int) (expGained/runTime);
        int xperHourFish= (int)(expGainedFish/runTime);
        int xperHourWc= (int)(expGainedWC/runTime);
        g.setColor(boxColor);
        g.fillRoundRect(8,340,300,140,50,5);
        g.setColor((textColor));
        g.setFont(font);

        expGained= ctx.skills.experience(Constants.SKILLS_MINING)-initialMinExp;
         expGainedWC= ctx.skills.experience(Constants.SKILLS_WOODCUTTING)-initialWCExp;
        expGainedFish= ctx.skills.experience(Constants.SKILLS_FISHING)-initialFishExp;
        fishLevel= ctx.skills.level(Constants.SKILLS_FISHING)-initFishLevel;
        WCLevel= ctx.skills.level(Constants.SKILLS_WOODCUTTING)-initWCLevel;
        MinLevel= ctx.skills.level(Constants.SKILLS_MINING)-initMinLevel;
        g.setFont(titleFont);
        g.drawString("Mining",22,364);
        g.drawString("1.00 v",250,364);
        g.setFont(font);
        g.drawString("Experienced gained: "+expGained,22,394 );
        g.drawString("Time running: "+minutes+ " :"+seconds,22,416 );
        g.drawString("Levels gained : " +MinLevel,22,438);
        g.drawString("XP/Hour = " +xperhourMin,22,460);

        g.setColor(woodColorBox);
        g.fillRoundRect(300,340,220,140,5,5);
        g.setColor(textColor);
        g.setFont(titleFont);
        g.drawString("Woodcutting",310,364);
        g.drawString("1.00 v",450,364);
        g.setFont(font);
        g.drawString("Levels gained :" +WCLevel,310,470);
        g.drawString("Experience gained: "+expGainedWC,310,440);
        g.drawString("Time Running: " + minutes+ ": " + seconds,310,410);
        g.drawString("XP/Hour = " + xperHourWc,310,380);

        g.setColor(fishingColorBox);
        g.fillRoundRect(300,210,220,126,5,5);
        g.setColor(textColor);
        g.setFont(titleFont);
        g.drawString("Fishing",380,230);
        g.setFont(font);
        g.drawString("Fishing levels gained : " +fishLevel,320,270);
        g.drawString("Experience gained : " + expGainedFish,320,300);
        g.drawString("XP/ Hour = " + xperHourFish,320,330);
        long thickness = 4;
        BasicStroke basic= new BasicStroke(thickness);
        g.setColor(Color.black);
        g.setStroke(basic);
        g.drawRect(300,210,220,126);
        g.drawRect(300,340,220,140);
        g.drawRect(8,340,290,140);


    }




}

