import java.util.*;
import java.lang.*;

public class QualityGate {
    
    private final boolean isPassed;
    
    public QualityGate(boolean isPassed) {
        this.isPassed = isPassed;
    }
    
    public boolean isPassed() {
        return this.isPassed;
    }

}
