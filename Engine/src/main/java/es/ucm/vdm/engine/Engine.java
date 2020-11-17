package es.ucm.vdm.engine;

public interface Engine {
    
    Graphics getGraphics();
    
    Input getInput();
    
    void setLogic(Logic l);
    
    void HandleException(Exception e);
    
    int getWidth();
    
    int getHeight();
}
