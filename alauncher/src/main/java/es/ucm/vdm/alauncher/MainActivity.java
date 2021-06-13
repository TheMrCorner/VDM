package es.ucm.vdm.alauncher;

// Android
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// UCM
import es.ucm.vdm.androidengine.Engine;
import es.ucm.vdm.logic.Logic;

/**
 * This activity works as the launcher for the game. Creates the engine and the logic and sets them
 * to work together. Then just manages the different events from android related to the app life and
 * stopping events.
 */
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

        _eng = new Engine(this);
        _log = new Logic(_eng); // Must receive Engine

        _eng.setLogic(_log);

        _log.initLogic();

        setContentView(_eng.getView());
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
} // MainActivity
