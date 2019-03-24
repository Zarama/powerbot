package baikai00.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt6.Bank;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GeItem;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Player;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class Utils extends ClientAccessor{
	private Walker walker = null;
	private GeItem geItem = null;
    public Utils(ClientContext ctx) {
        super(ctx);
        walker = new Walker(ctx);
        geItem = new GeItem(FIRE_RUNE_ID);
    }

    public int count(int id){
        return ctx.backpack.select().id(id).count();
    }
    
    public void walk(Tile... path){
        for (int i = 0; i < path.length; i++){
            running();

            final Tile currentTile = path[i];
            if (currentTile == null){
                break;
            }

            if (i != path.length-1){
                int distance1 = ctx.movement.distance(path[i]);
                int distance2 = ctx.movement.distance(path[i+1]);
                int end = ctx.movement.distance(path[path.length-1]);
                if (distance1 > end){
                	continue;
                }
                if (distance1 > distance2){
                    continue;
                }
            }

            ctx.movement.step(currentTile);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return distance(currentTile, Random.nextInt(2,4));
                }
            }, 500,5);
        }
    }

    public void running(){
        if (ctx.movement.energyLevel() > 35 && !ctx.movement.running()){
            ctx.movement.running(true);
        }
    }

    public int animation(){
        return ctx.players.local().animation();
    }
    
    public boolean distance(Tile t1, Tile t2, int offset){
        return t1.distanceTo(t2) <= offset;
    }
    
    public boolean distance(Tile t, int offset){
        return t.distanceTo(player()) <= offset;
    }

    public GameObject find(int id){
        return ctx.objects.select().id(id).nearest().poll();
    }
    
    public void closeBank(){
    	if (ctx.bank.opened()){
    		ctx.bank.close();
    	}
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean findDialog(){
        return false;
    }

    public void dropAllExcept(final int exceptId){
        Iterator<Item> items = ctx.backpack.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.id() != exceptId;
            }
        }).iterator();
//        int deleteCount = 0;
        while(items.hasNext()){
            final Item item = items.next();
            if (!item.interact(false,"Drop")){
                if (item.interact(false,"Destroy")){
                    if (findDialog()){
                        ctx.input.click(509,423,true);
                    }
                }
            }else{
                if (findDialog()){
                    ctx.input.click(509,423,true);
                }
            }
//            deleteCount++;
        }
    }
    
    public Locatable bank(){
    	return ctx.bank.nearest();
    }
    
    public Player player(){
    	return ctx.players.local();
    }

    public void storeAndTake(final int runeId){
    	if(ctx.bank.opened()){
        	if(count(ESS_ID) != 0){
        		ctx.bank.close();
        		return;
        	}
            if(ctx.bank.depositInventory()){
            	Condition.wait(new Callable<Boolean>(){
                    @Override
                    public Boolean call() throws Exception {
                        return count(runeId) == 0;
                    }
                }, 250, 20);
            }
            selectFireRuneCount();
            if (ctx.bank.withdraw(ESS_ID, 28)){
            	 Condition.wait(new Callable<Boolean>() {
 					@Override
 					public Boolean call() throws Exception {
 						return count(ESS_ID) > 0;
 					}
 				}, 250, 20);
            	recordTime();
            }
            ctx.bank.close();
        } else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            }
        }
    }
    
    public void setCamera(){
        ctx.camera.angle('n');
        ctx.camera.pitch(72);
    }
    
    private int fireRuneCount = 0;
    public int getFireRuneCount(){
    	return fireRuneCount;
    }
    
    public void setFireRuneCount(int count){
    	this.fireRuneCount = count;
    }
    
    private void selectFireRuneCount(){
    	int count = ctx.bank.select().id(FIRE_RUNE_ID).count(true);
    	if (count != 0){
    		fireRuneCount = count;
    	}
    }
    
    public int getPrice(){
    	return geItem == null ? 0 : geItem.price * fireRuneCount;
    }

    public static final long TIME_OFFSET = 8 * 60 * 60 * 1000;

    public long getThisTime(){
        return timeStart == 0 ? 0 - TIME_OFFSET : now() - timeStart - TIME_OFFSET;
    }

    private long now(){
        return System.currentTimeMillis();
    }

    private long timeStart = 0;
    private void recordTime(){
        if (timeStart == 0){
            timeStart = now();
        }else{
            long timeEnd = now();
            if (timeEnd-timeStart > 30000){
                lastTime = timeEnd-timeStart - TIME_OFFSET;
            }
            timeStart = timeEnd;
        }
    }
    public long lastTime = 0 - TIME_OFFSET;

    /******************fire begin******************/
    public static final int ESS_ID = 7936;
    public static final int FIRE_RUNE_ID = 554;
    
    public static final Tile[] PATH_FIRE_BANK = new Tile[]{
            new Tile(3323,3233,0),
            new Tile(3340,3233,0),
            new Tile(3345,3236,0)
    };
    public static final Tile[] PATH_FIRE_MAKE = new Tile[]{
            new Tile(3338,3232,0),
            new Tile(3321,3236,0),
            new Tile(3314,3253,0)
    };

    public static final Tile FIRE_MAKE_TILE = new Tile(2583,4839,0);
    public static final Tile FIRE_OUT_TILE = new Tile(2577,4846,0);
    public static final Tile FIRE_ENTER_TILE = new Tile(3314,3253,0);
    public static final Tile FIRE_BANK_TILE = new Tile(3347,3239,0);
    public static final Tile FIRE_PATH_HALF_TILE = new Tile(3325,3232,0);
    public static final Area AREA_ENTER_HALF = new Area(FIRE_ENTER_TILE, FIRE_PATH_HALF_TILE);
    public static final Area AREA_BANK_HALF = new Area(FIRE_BANK_TILE, FIRE_PATH_HALF_TILE);

    public static final int FIRE_BANK_ID = 83954;
    public static final int MAKE_FIRE_ID = 2482;
    public static final int ENTER_FIRE_ID = 2456;
    public static final int OUT_FIRE_ID = 2469;

    public String status = "init";

    public void fireBanking(){
        status = "BANKING";
        setCamera();
        final GameObject out = find(OUT_FIRE_ID);
        if (out.floor() == 0){
        	if (distance(out.tile(), 4)){
	       		 out.interact("Enter");
	           	 sleep(1000);
	     	 }else{
	     		 walk(out.tile());
	     	 }
        }else{
        	if (bank().tile().distanceTo(player()) < 10){
        		storeAndTake(FIRE_RUNE_ID);
        	}else {
        		walker.walkPath(PATH_FIRE_BANK);
        	}
        }
    }

    public void fireMaking(){
    	status = "MAKING";
    	setCamera();
    	closeBank();
    	final GameObject firCraft = find(MAKE_FIRE_ID);
    	if (firCraft.floor() == 0){
    		if (distance(firCraft.tile(), 4)){
    			firCraft.interact("Craft-rune");
                sleep(500);
    		}else{
    			walk(firCraft.tile());
    		}
    	}else{
    		if (distance(FIRE_ENTER_TILE, 7)){
    			find(ENTER_FIRE_ID).interact("Enter");
    		}else{
    			walker.walkPath(PATH_FIRE_MAKE);
        	}
    	}
    }
    /******************fire end******************/
    
    /******************air begin******************/
    public static final int AIR_CRAFT_ID = 2478;
    public static final int AIR_ENTER_ID = 2452;
    public static final int AIR_OUT_ID = 2465;
    public static final int AIR_RUNE_ID = 556;
    public static final Tile[] PATH_AIR_CRAFT = {
    		new Tile(3185, 3432),
    		new Tile(3174, 3429),
    		new Tile(3161, 3420),
    		new Tile(3144, 3413),
    		new Tile(3133, 3408)
    };
    public static final Tile[] PATH_AIR_BANK = {
    		new Tile(3133, 3408),
    		new Tile(3144, 3413),
    		new Tile(3161, 3420),
    		new Tile(3174, 3429),
    		new Tile(3185, 3432)
    };
    
    public void airCraft(){
    	status = "MAKING";
    	closeBank();
    	setCamera();
    	final GameObject airCraft = find(AIR_CRAFT_ID);
    	if (airCraft.floor() == 0){
    		if (distance(airCraft.tile(), 4)){
    			airCraft.interact("Craft-rune");
                sleep(500);
    		}else{
    			walk(airCraft.tile());
    		}
    	}else{
    		final GameObject enter = find(AIR_ENTER_ID);
    		if (distance(enter.tile(), 7)){
    			enter.interact("Enter");
    		}else{
    			walker.walkPath(PATH_AIR_CRAFT);
        	}
    	}
    }
    
    public void airBank(){
    	 status = "BANKING";
    	 setCamera();
         final GameObject out = find(AIR_OUT_ID);
         if (out.floor() == 0){
        	 if (distance(out.tile(), 4)){
        		 out.interact("Enter");
            	 sleep(1000);
	     	 }else{
	     		 walk(out.tile());
	     	 }
         }else{
         	if (bank().tile().distanceTo(player())<10){
         		storeAndTake(AIR_RUNE_ID);
         	}else {
         		walker.walkPath(PATH_AIR_BANK);
         	}
         }
    }
    /******************air end******************/
    
    /******************go to trade start******************/
    public static final Tile Tile_V = new Tile(3213, 3376);
    public static final Tile[] PATH_TO_TRADE = {
    		new Tile(3211, 3391),
    		new Tile(3211, 3404),
    		new Tile(3201, 3413),
    		new Tile(3198, 3426),
    		new Tile(3187, 3430),
    		new Tile(3187, 3444),
    		new Tile(3175, 3451),
    		new Tile(3167, 3460),
    		new Tile(3175, 3468),
    		new Tile(3181, 3473)
    };
    private int tState = 0;
    private long clickStart = 0;
    private int clickState = 0;
    public void dh(){
    	if (distance(Tile_V, 10)){
			tState = 1;
		}
    	
    	if (distance(PATH_TO_TRADE[PATH_TO_TRADE.length-1], 7)){
			tState = 2;
		}
    	
    	if (tState == 0 && clickState != 0){
    		if (clickState == 1){
    			if (now() - clickStart < 3000){
    				return;
    			}
    		}else if (clickState == 2){
    			if (now() - clickStart < 12000){
    				return;
    			}
    		}
    	}
    	
    	if (tState == 0){
    		status = "dh Home";
    		if(ctx.input.click(647, 173, true)){
    			clickState = 1;
    			clickStart = now();
				sleep(1000);
				if (ctx.input.click(486, 256, true)){
					clickState = 2;
					clickStart = now();
					sleep(1000);
					if (distance(Tile_V, 10)){
						tState = 1;
						clickState = 0;
					}
				}
			}
    	}else if (tState == 1){
    		status = "dh Walk";
    		if (distance(PATH_TO_TRADE[PATH_TO_TRADE.length-1], 7)){
    			tState = 2;
    		}else{
    			walker.walkPath(PATH_TO_TRADE);
    		}
    	}else if (tState == 2){
    		status = "dh Bank";
    		Locatable bank = bank();
    		if (distance(bank.tile(), 4)){
    			if(ctx.bank.opened()){
                    if(ctx.bank.depositInventory()){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.backpack.select().count() == 0;
                            }
                        }, 250, 20);
                    }
                    if (ctx.bank.withdrawMode(true)){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.bank.withdrawMode();
                            }
                        }, 250, 20);
                    }
                    if (ctx.bank.withdraw(ESS_ID, Bank.Amount.ALL)){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.bank.select().id(ESS_ID).count() == 0;
                            }
                        }, 250, 20);
                    }
                    if (ctx.bank.withdraw(FIRE_RUNE_ID, Bank.Amount.ALL)){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.bank.select().id(FIRE_RUNE_ID).count() == 0;
                            }
                        }, 250, 20);
                    }
                    if (ctx.bank.withdraw(AIR_RUNE_ID, Bank.Amount.ALL)){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.bank.select().id(AIR_RUNE_ID).count() == 0;
                            }
                        }, 250, 20);
                    }
                    ctx.bank.close();
                    ctx.controller.suspend();
                    status = "PAUSE";
                } else {
                    if(ctx.bank.inViewport()) {
                        if(ctx.bank.open()){
                            Condition.wait(new Callable<Boolean>(){
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.bank.opened();
                                }
                            }, 250, 20);
                        }
                    }
                }
    		}else{
    			walk(bank.tile());
    		}
    	}else{
    		ctx.controller.suspend();
    		status = "PAUSE";
    	}
    }
    
    /******************go to trade end******************/
}
