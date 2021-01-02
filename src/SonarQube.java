import java.util.*;
import java.lang.*;

public class SonaarQube {
  private final QualityGate gate;
  
  public SonarQube(QualityGate gate) {
    this.gate = gate;
  }
  
  public QualityGate getQualityGate() {
    return this.gate;
  }

  public void showResults() {
    this.gate.showResults();
  } 
}
