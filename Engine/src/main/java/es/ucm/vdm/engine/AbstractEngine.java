package es.ucm.vdm.engine;

public abstract class AbstractEngine implements Engine {

    protected Graphics _g;
    protected Input _ip;
    protected Logic _l;
    protected Logic _tempLogic;


    protected long _lastFrameTime;
    protected long _currentTime, _nanoElapsedTime;
    protected double _elapsedTime;
    protected int _frames;
    protected long _info;

}
