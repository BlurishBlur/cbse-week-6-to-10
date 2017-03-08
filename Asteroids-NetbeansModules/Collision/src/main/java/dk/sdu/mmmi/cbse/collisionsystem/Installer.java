package dk.sdu.mmmi.cbse.collisionsystem;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static CollisionDetectionSystem cds;

    @Override
    public void restored() {
        cds = new CollisionDetectionSystem();
    }

}
