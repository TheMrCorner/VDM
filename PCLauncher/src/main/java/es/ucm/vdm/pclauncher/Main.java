package es.ucm.vdm.pclauncher;

import es.ucm.vdm.logic.Logic;
import es.ucm.vdm.pcengine.Engine;

public class Main {

    /**
     * Main method called when application starts. Creates a new Engine and Logic and puts them
     * together to play the game.
     *
     * @param args (String[]) Arguments given to app
     */
    public static void main(String[] args){
        // Create Engine and Logic
        Engine _eng = new Engine();

        // Unify Engine and Logic
        Logic _log = new Logic(_eng);

        // Set logic in engine for update calls
        _eng.setLogic(_log);

        // Run game
        _eng.run();
    } // main
} // Main