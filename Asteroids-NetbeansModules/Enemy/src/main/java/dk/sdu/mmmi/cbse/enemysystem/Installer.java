package dk.sdu.mmmi.cbse.enemysystem;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static EnemyControlSystem ecs;

    @Override
    public void restored() {
        ecs = new EnemyControlSystem();
    }

}
