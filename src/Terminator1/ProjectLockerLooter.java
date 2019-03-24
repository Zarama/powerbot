package Terminator1;

import Terminator1.node.ProjectLockerLooter.*;
import Terminator1.node.ProjectLockerLooter.Bank;
import org.powerbot.script.*;
import org.powerbot.script.GeItem;
import org.powerbot.script.rt4.*;

import Terminator1.api.Node;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Genoss on 12/25/2016:8:22 AM
 */
@Script.Manifest(description = "Crack's safe at the rogues den.Please check the forums for requirements and script status.", name = "Safe Cracker", properties = "author=Terminator1; topic=1325593; client=4;")
public class ProjectLockerLooter extends PollingScript<ClientContext> implements PaintListener,MessageListener,MouseListener{

    private final Color color1 = new Color(102, 0, 102);
    private final Color color2 = new Color(153, 0, 153);
    private final Color color4 = new Color(255, 255, 255);
    private final Color color5 = new Color(204, 0, 204);
    private final Color color6 = new Color(140, 0, 105);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 1, 10);
    private final Font font2 = new Font("Arial", 1, 13);
    private final Font font3 = new Font("Arial", 1, 20);

    private boolean optimize = false,click = false,efood = true,hide = false,hidden = true;
    private char hid = '-';//—
    private final int steth = 5560,banker = 3194,sap = 1623,eme = 1621,rub = 1619, dia = 1617,coi = 995;
    private final Rectangle r = new Rectangle(720, 168, 40, 28),r0 = new Rectangle(540, 210, 40, 21), r1 = new Rectangle(580, 210, 60, 21),r2 = new Rectangle(640,210,40,21),r3 = new Rectangle(680, 210, 60, 21),
            sr = new Rectangle(540,260,200,13), sr0 = new Rectangle(540,299,200,13),sr1 = new Rectangle(540,338,200,13),sr2 = new Rectangle(540,377,200,13),sr3 = new Rectangle(540,455,200,13),sr4 = new Rectangle(540,416,200,13);
    private long encryptono = 0,startTime = 0,cracked = 0,time = 0,cx = 0,xpgain = 0, coins = 0,
            sapphire = 0,ruby = 0,emerald = 0,diamond = 0,lsapphire = 0,lruby = 0,lemerald = 0,ldiamond = 0,lcoins = 0,tmploot = 0,tmploothr = 0,totalloot = 0,totalloothr = 0;
    private int uid = 0,food = 0,amount = 0,healthLimit = 0,spotPos = 0,tc[] = new int[4],choice,tab = 0,psapphire = 0,pruby = 0,pemerald = 0,pdiamond = 0,shred = 0,shred0 = 0,shred1 = 0,shred2 = 0,lvlg = 0,priority = 0;
    private List<Node> eL = new ArrayList<Node>();
    private String input,lp,pp,status = "Current status",username = "Null";
    private Tile spot[] = {new Tile(3055,4977,1),new Tile(3057,4977,1),new Tile(3055,4970,1),new Tile(3057,4970,1)};

    private String rePos(int pos) {
        String tmp;
        switch (pos) {
            case 0:
                tmp = "North West";
                break;
            case 1:
                tmp = "North East";
                break;
            case 2:
                tmp = "South West";
                break;
            default:
                tmp = "South East";
                break;
        }
        return tmp;
    }

    private int addString(String s,long t) {
        float tmp = 1;
        for(int i=0;i<s.length();i++) {
            tmp+=(t - s.charAt(i))/(t-s.charAt(Math.abs(i-1)));
            tmp*=i;
        }
        return (int)(tmp/s.length());
    }

    private void encryptoNumero() {
        String user = ctx.players.local().name();
        long tmp = uid,tmp1 = Math.abs(addString(ctx.players.local().name(),tmp)-tmp);
        for(int i=0;i<user.length();i++) {
            tmp+=((i+tmp1)*user.charAt(i));
        }
        tmp-=tmp1;
        encryptono = tmp;
    }

    private void LSet() throws IOException {
        Properties prop = new Properties();
        FileInputStream stream = new FileInputStream(getStorageDirectory()+"/."+encryptono);
        prop.load(stream);
        priority = spotPos = Integer.parseInt(prop.getProperty("spotPos"));
        amount = Integer.parseInt(prop.getProperty("amount"));
        healthLimit = Integer.parseInt(prop.getProperty("healthLimit"));
        optimize = Boolean.parseBoolean(prop.getProperty("optimize"));
        food = Integer.parseInt(prop.getProperty("food"));
        efood = Boolean.parseBoolean(prop.getProperty("efood"));
        stream.close();
    }

    private void SSet() throws IOException {
        Properties prop = new Properties();
        FileOutputStream stream = new FileOutputStream(getStorageDirectory()+"/."+encryptono);
        prop.setProperty("spotPos",""+spotPos);
        prop.setProperty("amount",""+amount);
        prop.setProperty("healthLimit",""+healthLimit);
        prop.setProperty("optimize",""+optimize);
        prop.setProperty("food",""+food);
        prop.setProperty("efood",""+efood);
        prop.store(stream,"Careful what you touch here.");
        stream.close();
    }

    private void GUILaunch() {
        hidden = false;
        GUI gui = new GUI();
        JFrame jw = new JFrame();
        jw.add(gui);
        jw.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        jw.setSize(450,225);
        jw.setVisible(true);
        while(!hidden) {
            Condition.sleep(250);
        }
        jw.setVisible(false);
        jw.dispose();
    }

    private void initPrice() {
        GeItem ge = new org.powerbot.script.rt4.GeItem(sap);
        psapphire = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(eme);
        pemerald = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(rub);
        pruby = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(dia);
        pdiamond = ge.price;
    }

    @Override
    public void start() {
        username = ctx.properties.getProperty("user.name");
        uid = Integer.parseInt(ctx.properties.getProperty("user.id"));
        encryptoNumero();
        initPrice();
        try {
            LSet();
        } catch (IOException ignored) {
        }
        if(food == 0)
            GUILaunch();
        lp = pp = rePos(priority);
        while(amount > 28 || amount < 1){
            input = JOptionPane.showInputDialog(null, "Quantity of food to withdraw:", ""+amount);
            amount = Integer.parseInt(input);
        }
        while(healthLimit >= ctx.skills.realLevel(Constants.SKILLS_HITPOINTS)){
            input = JOptionPane.showInputDialog(null, "At what hp should I eat:", ""+healthLimit);
            healthLimit = Integer.parseInt(input);
        }
        eL.addAll(Arrays.asList(new WalkToLocker(ctx,this,"WalkToLocker"),new Loot(ctx,this,"Looter"),new Eat(ctx,this,"Eater"),new WalkToBank(ctx,this,"WalkToBank"),new Bank(ctx,this,"Banker")));
        startTime = getRuntime();
    }

    public void stop() {
        try {
            SSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void checkSpots() {
        int tmp = 0,tmp0 = 0,tmp1 = 0,tmp2 = 0;//Used to count temporary player locations
        for(Player nearme:ctx.players.select()) {
            if(!nearme.name().equals(ctx.players.local().name())) {
                if(nearme.tile().distanceTo(spot[0]) == 0) {
                    tmp++;
                } else if(nearme.tile().distanceTo(spot[1]) == 0) {
                    tmp0++;
                } else if(nearme.tile().distanceTo(spot[2]) == 0) {
                    tmp1++;
                } else if(nearme.tile().distanceTo(spot[3]) == 0) {
                    tmp2++;
                }
            }

        }
        tc[0] = tmp;
        tc[1] = tmp0;
        tc[2] = tmp1;
        tc[3] = tmp2;
        choice = Math.min(tc[0],Math.min(tc[1],Math.min(tc[2],tc[3])));
    }

    private void autoOptimize() {
        if(choice!=tc[priority]) {
            Condition.sleep(Random.nextInt(250,750));
            if(tc[2] == choice) {
                spotPos = 2;
            } else if(tc[3] == choice) {
                spotPos = 3;
            } else if(tc[0] == choice) {
                spotPos = 0;
            } else {
                spotPos = 1;
            }
        } else {
            spotPos = priority;
        }
        lp = rePos(spotPos);
    }

    @Override
    public void resume() {
        click = false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClick(boolean l) {
        click = l;
    }

    public void setFood(int l) {
        food = l;
    }

    public void resetLoc() {
        lcoins = 0;
        lsapphire = 0;
        lemerald = 0;
        lruby = 0;
        ldiamond = 0;
    }

    public int getFoodID() {
        return food;
    }

    public int getFoodAmount() {
        return amount;
    }

    public int getStethID() {
        return steth;
    }

    public int getBankerID() {
        return banker;
    }

    public int getHealthLimt() {
        return healthLimit;
    }

    public int getSpotPos() {
        return spotPos;
    }

    public boolean getClick() {
        return click;
    }

    public boolean getEatFood() {
        return efood;
    }

    public long getRunTime() {
        return getRuntime();
    }

    public Tile getSpot() {
        return spot[spotPos];
    }

    private void shred(long milliseconds) {
        shred = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        shred0 = (int) (milliseconds / 1000) % 60;
        shred1 = (int) ((milliseconds / (1000 * 60)) % 60);
        shred2 = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    @Override
    public void poll() {
        if(optimize && ((tc[priority] != 0)||(ctx.players.local().tile().distanceTo(spot[priority])>1))) {
            autoOptimize();
        }
        for(Node node:eL) {
            if (node.isReady())
                node.executeBlock();
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        hid = 'O';
        if(!hide) {
            hid = '—';
            g.setColor(color1);
            g.fillRect(520, 168, 240, 336);
            g.setStroke(stroke1);
            g.drawRect(520, 168, 240, 336);
            g.setColor(color2);
            g.fillRect(540, 231, 200, 252);
            g.drawRect(540, 231, 200, 252);
            g.setColor(color1);
            g.fillRect(540, 210, 40, 21);
            g.drawRect(540, 210, 40, 21);
            g.fillRect(580, 210, 60, 21);
            g.drawRect(580, 210, 60, 21);
            g.fillRect(640, 210, 40, 21);
            g.drawRect(640, 210, 40, 21);
            g.fillRect(680, 210, 60, 21);
            g.drawRect(680, 210, 60, 21);
            g.setColor(color4);
            g.setFont(font1);
            g.drawString("MAIN", 548, 224);
            g.drawString("THIEVING", 588, 224);
            g.drawString("LOOT", 648, 224);
            g.drawString("SETTINGS", 684, 224);
            g.setColor(color5);
            g.fillRect(540, 260, 200, 13);
            g.drawRect(540, 260, 200, 13);
            g.fillRect(540, 299, 200, 13);
            g.drawRect(540, 299, 200, 13);
            g.fillRect(540, 338, 200, 13);
            g.drawRect(540, 338, 200, 13);
            g.fillRect(540, 416, 200, 13);
            g.drawRect(540, 416, 200, 13);
            g.fillRect(540, 377, 200, 13);
            g.drawRect(540, 377, 200, 13);
            time = getRuntime() - startTime;
            if(tab == 0) {
                g.setColor(color2);
                g.fillRect(540, 210, 40, 21);
                g.drawRect(540, 210, 40, 21);
                g.setColor(color4);
                g.setFont(font1);
                g.drawString("MAIN", 548, 224);
                g.setColor(color5);
                g.fillRect(540, 455, 200, 13);
                g.drawRect(540, 455, 200, 13);
                g.setColor(color4);
                g.setFont(font2);
                shred(time);
                g.drawString("Account:"+username, 552, 273);
                g.drawString("Status:"+status, 552, 312);
                g.drawString("RunTime:"+shred+"D::"+shred2+"H::"+shred1+"M::"+shred0+"S", 552, 351);
                g.drawString("SafesCracked:"+cracked+"("+(long)((cracked*3600000D)/time)+")", 552, 390);
                g.drawString("Locker:"+lp, 552, 429);
                g.drawString("Priority:"+pp, 551, 468);
            } else if(tab == 1) {
                g.setColor(color2);
                g.fillRect(580, 210, 60, 21);
                g.drawRect(580, 210, 60, 21);
                g.setColor(color4);
                g.setFont(font1);
                g.drawString("THIEVING", 588, 224);
                g.setColor(color4);
                g.setFont(font2);
                shred(time);
                cx = ctx.skills.experience(Constants.SKILLS_THIEVING);
                int cl = ctx.skills.level(Constants.SKILLS_THIEVING);
                long xpDiff = ctx.skills.experienceAt(cl +1)-cx;
                double xpPerHour = (long)(xpgain * 3600000D) /time;
                long xpBwLevel = ctx.skills.experienceAt(cl +1)-ctx.skills.experienceAt(cl);
                double xpCovered = xpBwLevel - xpDiff;
                double xpPrePercentage = xpCovered/xpBwLevel;
                int xpPercentage = (int)(xpPrePercentage*Math.pow(10,2));
                g.setColor(color6);
                g.fillRect(540, 432, 200, 32);
                g.drawRect(540, 432, 200, 32);
                g.setColor(color5);
                g.fillRect(540, 432, 2*xpPercentage, 32);
                g.drawRect(540, 432, 2*xpPercentage, 32);
                g.setFont(font3);
                g.setColor(color4);
                g.drawString(""+xpPercentage+"%", 620, 456);
                g.setFont(font2);
                g.drawString("Level:"+ctx.skills.level(Constants.SKILLS_THIEVING)+"("+lvlg+")", 552, 273);
                g.drawString("XP Gained:"+xpgain, 552, 312);
                g.drawString("XP/HR:"+(long)xpPerHour, 552, 351);
                g.drawString("XTL:"+xpDiff, 552, 429);
                xpPerHour/=3600000;
                long timeToLevel = (long)(xpDiff/xpPerHour);
                shred(timeToLevel);
                g.drawString("TTL:"+shred+"D::"+shred2+"H::"+shred1+"M::"+shred0+"S", 552, 390);
            } else if(tab == 2) {
                g.setColor(color2);
                g.fillRect(640, 210, 40, 21);
                g.drawRect(640, 210, 40, 21);
                g.setColor(color5);
                g.fillRect(540, 455, 200, 13);
                g.drawRect(540, 455, 200, 13);
                g.setColor(color4);
                g.setFont(font1);
                g.drawString("LOOT", 648, 224);
                g.setColor(color4);
                g.setFont(font2);
                totalloot = tmploot = coins;
                tmploothr = (long)(tmploot * 3600000D) / time;
                g.drawString("Coins:"+tmploot+"("+tmploothr+")", 552, 273);
                tmploot = sapphire;
                tmploothr = (long)(tmploot * 3600000D) / time;
                totalloot+=(tmploot*psapphire);
                g.drawString("Sapphire:"+tmploot+"("+tmploothr+")", 552, 312);
                tmploot = emerald;
                tmploothr = (long)(tmploot * 3600000D) / time;
                totalloot+=(tmploot*pemerald);
                g.drawString("Emerald:"+tmploot+"("+tmploothr+")", 552, 351);
                tmploot = ruby;
                tmploothr = (long)(tmploot * 3600000D) / time;
                totalloot+=(tmploot*pruby);
                g.drawString("Ruby:"+tmploot+"("+tmploothr+")", 552, 390);
                tmploot = diamond;
                tmploothr = (long)(tmploot * 3600000D) / time;
                totalloot+=(tmploot*pdiamond);
                g.drawString("Diamond:"+tmploot+"("+tmploothr+")", 552, 429);
                totalloothr = (long)(totalloot * 3600000D) / time;
                g.drawString("Profit:"+totalloot+"("+totalloothr+")", 551, 468);

            } else {
                g.setColor(color2);
                g.fillRect(680, 210, 60, 21);
                g.drawRect(680, 210, 60, 21);
                g.setColor(color4);
                g.setFont(font1);
                g.drawString("SETTINGS", 684, 224);
                g.setColor(color5);
                g.fillRect(540, 455, 200, 13);
                g.drawRect(540, 455, 200, 13);
                g.setColor(color4);
                g.setFont(font2);
                shred(time);
                g.drawString("Locker:"+lp, 552, 273);
                g.drawString("Withdraw quantity:"+amount, 552, 312);
                g.drawString("HP to eat at:"+healthLimit, 552, 351);
                g.drawString("AutoOptimized:"+optimize, 552, 390);
                g.drawString("Eat on full inventory:"+efood, 552, 429);
                g.drawString("All settings",551,469);
            }
        }
        g.setColor(color5);
        g.fillRect(720, 168, 40, 28);
        g.drawRect(720, 168, 40, 28);
        g.setColor(color4);
        checkSpots();
        g.drawString(""+tc[0],spot[0].matrix(ctx).mapPoint().x,spot[0].matrix(ctx).mapPoint().y);
        g.drawString(""+tc[1],spot[1].matrix(ctx).mapPoint().x,spot[1].matrix(ctx).mapPoint().y);
        g.drawString(""+tc[2],spot[2].matrix(ctx).mapPoint().x,spot[2].matrix(ctx).mapPoint().y);
        g.drawString(""+tc[3],spot[3].matrix(ctx).mapPoint().x,spot[3].matrix(ctx).mapPoint().y);
        g.drawString(""+hid, 735, 187);
    }



    private void crackCheck() {
        Item clusterc = ctx.inventory.select().id(coi).poll();
        if(clusterc.stackSize() > lcoins) {
            coins+=(clusterc.stackSize()-lcoins);
            lcoins = clusterc.stackSize();
        } else if(ctx.inventory.select().id(sap).count() > lsapphire) {
            lsapphire = ctx.inventory.select().id(sap).count();
            sapphire++;
        } else if(ctx.inventory.select().id(eme).count() > lemerald) {
            lemerald = ctx.inventory.select().id(eme).count();
            emerald++;
        } else if(ctx.inventory.select().id(rub).count() > lruby) {
            lruby = ctx.inventory.select().id(rub).count();
            ruby++;
        } else {
            ldiamond = ctx.inventory.select().id(dia).count();
            diamond++;
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        String txt = messageEvent.text().toLowerCase();
        if(txt.contains("you start"))
            click=true;
        else if(txt.contains("you slip") || txt.contains("you get") || txt.contains("you eat")) {
            click = false;
            if(txt.contains("you get")) {
                xpgain += 70;
                cracked++;
                crackCheck();
            }
        } else if(txt.contains("congratulations, you"))
            lvlg++;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(r.contains(e.getPoint())) {
            hide = !hide;
        } else if(!hide) {
            if(r0.contains(e.getPoint())) {
                tab = 0;
            } else if(r1.contains(e.getPoint())) {
                tab = 1;
            } else if(r2.contains(e.getPoint())) {
                tab = 2;
            } else if(r3.contains(e.getPoint())) {
                tab = 3;
            } else if(tab == 3) {
                ctx.controller.suspend();
                if(sr.contains(e.getPoint())) {
                    input = ((String) JOptionPane.showInputDialog(null,"Where do you want to crack your safe?:","The choice of sage",JOptionPane.QUESTION_MESSAGE,null,new String[]{"North East","North West","South East","South West"},0)).toLowerCase();
                    if(input.equals("north west")) {
                        priority = spotPos = 0;
                    } else if(input.equals("north east")) {
                        priority = spotPos = 1;
                    } else if(input.equals("south west")) {
                        priority = spotPos = 2;
                    } else if(input.equals("south east")) {
                        priority = spotPos = 3;
                    }
                    lp = pp = rePos(priority);
                } else if(sr0.contains(e.getPoint())) {
                    do {
                        input = JOptionPane.showInputDialog(null, "How much would you like to withdraw:", ""+amount);
                        amount = Integer.parseInt(input);
                    }while(amount > 28 || amount < 1);
                    //Locker quantity
                } else if(sr1.contains(e.getPoint())) {
                    do {
                        input = JOptionPane.showInputDialog(null, "At what hp should I eat:", ""+healthLimit);
                        healthLimit = Integer.parseInt(input);
                    }while(healthLimit >= ctx.skills.realLevel(Constants.SKILLS_HITPOINTS));
                } else if(sr2.contains(e.getPoint())) {
                    optimize = JOptionPane.showConfirmDialog(null, "Do you want to auto optimize the bot?(Uses more resources)", "AutoOptimize spot", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                } else if(sr4.contains(e.getPoint())) {
                   efood = JOptionPane.showConfirmDialog(null, "Do you want to eat on full inventory?", "Eat food", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                } else if(sr3.contains(e.getPoint())) {
                    GUILaunch();
                }
                ctx.controller.resume();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class GUI extends JPanel {
        JTextField tf,tfText1;
        JLabel lbLabel0,lbLabel1,lbLabel6,lbLabel7,lbLabel9;
        JComboBox cmbCombo0,cmbCombo1,cmbCombo;
        JCheckBox cbBox1;
        JButton button;

        /**
         *Constructor for the Settings object
         */
        GUI()
        {
            super();
            setBorder( BorderFactory.createTitledBorder( "Settings" ) );

            String foodl[] = new String[28];

            int i = 0 ;
            for(Item item:ctx.inventory.items()) {
                foodl[i] = item.name();
                i++;
            }

            GridBagLayout gbSettings = new GridBagLayout();
            GridBagConstraints gbcSettings = new GridBagConstraints();
            setLayout( gbSettings );

            lbLabel0 = new JLabel( "Quantity of food to be withdrawn:"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 0;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel0, gbcSettings );
            add( lbLabel0 );

            tf = new JTextField(""+amount);
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 0;
            gbcSettings.gridwidth = 11;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( tf, gbcSettings );
            add( tf );

            lbLabel0 = new JLabel( "At what hp do I eat:"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 1;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel0, gbcSettings );
            add( lbLabel0 );

            tfText1 = new JTextField( ""+healthLimit );
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 1;
            gbcSettings.gridwidth = 11;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( tfText1, gbcSettings );
            add( tfText1 );

            lbLabel6 = new JLabel( "Where do you want me to crack your safe:"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 3;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel6, gbcSettings );
            add( lbLabel6 );

            String []dataCombo0 = { "NorthWest", "NorthEast", "SouthWest","SouthEast" };
            cmbCombo0 = new JComboBox( dataCombo0 );
            cmbCombo0.setSelectedIndex(priority);
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 3;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( cmbCombo0, gbcSettings );
            add( cmbCombo0 );

            lbLabel7 = new JLabel( "Food:"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 4;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel7, gbcSettings );
            add( lbLabel7 );

            lbLabel9 = new JLabel( "Eat on full inventory:"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 5;
            gbcSettings.gridwidth = 11;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel9, gbcSettings );
            add( lbLabel9 );

            cmbCombo = new JComboBox( foodl );
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 4;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( cmbCombo, gbcSettings );
            add( cmbCombo );

            String []dataList1 = { "Yes", "No"};
            cmbCombo1 = new JComboBox( dataList1 );
            if(efood)
                cmbCombo1.setSelectedIndex(0);
            else
                cmbCombo1.setSelectedIndex(1);
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 5;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( cmbCombo1, gbcSettings );
            add( cmbCombo1 );

            lbLabel1 = new JLabel( "Optimize the location(Uses more resources):"  );
            gbcSettings.gridx = 0;
            gbcSettings.gridy = 2;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 1;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( lbLabel1, gbcSettings );
            add( lbLabel1 );

            cbBox1 = new JCheckBox( "");
            cbBox1.setSelected(optimize);
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 2;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( cbBox1, gbcSettings );
            add( cbBox1 );

            button = new JButton( "Start"  );
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    amount = Integer.parseInt(tf.getText());
                    healthLimit = Integer.parseInt(tfText1.getText());
                    optimize = cbBox1.isSelected();
                    priority = spotPos = cmbCombo0.getSelectedIndex();
                    pp = lp = rePos(priority);
                    efood = cmbCombo1.getSelectedIndex() == 0;
                    food = ctx.inventory.itemAt(cmbCombo.getSelectedIndex()).id();
                    hidden = true;
                }
            });
            gbcSettings.gridx = 1;
            gbcSettings.gridy = 6;
            gbcSettings.gridwidth = 1;
            gbcSettings.gridheight = 1;
            gbcSettings.fill = GridBagConstraints.BOTH;
            gbcSettings.weightx = 1;
            gbcSettings.weighty = 0;
            gbcSettings.anchor = GridBagConstraints.NORTH;
            gbSettings.setConstraints( button, gbcSettings );
            add(button );
        }
    }

}
