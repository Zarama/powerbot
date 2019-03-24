package Trikkstr.scripts.utils;

import Trikkstr.scripts.goblin_killer.GoblinKiller;

import javax.swing.*;
import java.awt.*;


public class InitializeOptions extends Frame
{
    private int selection;

    public InitializeOptions()
    {
        selection = JOptionPane.showConfirmDialog(null, "Would you like to pickup and bury bones?",
                "Bury Bones?", JOptionPane.YES_NO_OPTION);
        
        if(selection == 0)
        {
            GoblinKiller.bones = true;
        }
        else
        {
           GoblinKiller.bones = false;
        }
    }
}
