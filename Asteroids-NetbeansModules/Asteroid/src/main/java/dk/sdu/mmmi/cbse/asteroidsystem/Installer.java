package dk.sdu.mmmi.cbse.asteroidsystem;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static AsteroidControlSystem acs;

    @Override
    public void restored() {
        acs = new AsteroidControlSystem();
    }

}
