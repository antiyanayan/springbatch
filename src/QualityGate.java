import java.util.*;
import java.lang.*;

// This class provides information of the QualityQate status
public class QualityGate {
    
    private final boolean isPassed;
    
    public QualityGate(boolean isPassed) {
        this.isPassed = isPassed;
    }
    
    public boolean isPassed() {
        return this.isPassed;
    }

}
