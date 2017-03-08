package dk.sdu.mmmi.cbse.player;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static PlayerControlSystem pcs;

    @Override
    public void restored() {
        pcs = new PlayerControlSystem();
    }

}
