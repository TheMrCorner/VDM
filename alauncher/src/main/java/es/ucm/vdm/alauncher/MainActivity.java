package es.ucm.vdm.alauncher;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.vdm.logic.Logic;
import es.ucm.vdm.androidengine.Engine;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Engine _eng;
    Logic _log;

    /**
     * Method called at the beginning of the Activity's life. Init everything needed
     * Creates an engine and a logic. Set them to play the game.
     *
     * @param savedInstanceState (Bundle) Instance saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _eng = new Engine();
        _log = new Logic(); // Must receive Engine

        // Set logic in Engine
        _eng.run();
    }

    /**
     * Method called when the app recovers focus.
     */
    @Override
    protected void onResume(){
        super.onResume();
        _eng.onResume(); // Resume engine
    } // onResume

    /**
     * Method called when app loses focus. Pause everything.
     */
    @Override
    protected void  onPause(){
        super.onPause();
        _eng.onPause(); // Pause engine
    } // onPause
}