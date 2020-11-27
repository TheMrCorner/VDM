package es.ucm.vdm.engine;

import java.io.InputStream;

public interface Engine {
    
    Graphics getGraphics();
    
    Input getInput();

    InputStream openInputStream(String filename);
    
    void setLogic(Logic l);
    
    void HandleException(Exception e);
    
    int getWidth();
    
    int getHeight();
}
