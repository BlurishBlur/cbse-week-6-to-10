package dk.sdu.mmmi.cbse.bulletsystem;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static BulletControlSystem bcs;

    @Override
    public void restored() {
        bcs = new BulletControlSystem();
    }

}
