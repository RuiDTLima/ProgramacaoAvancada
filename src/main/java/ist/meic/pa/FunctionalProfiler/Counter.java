package ist.meic.pa.FunctionalProfiler;

public interface Counter {
    int value();
    Counter advance();
    /*default int abc() {
        return 1;
    }*/
}
