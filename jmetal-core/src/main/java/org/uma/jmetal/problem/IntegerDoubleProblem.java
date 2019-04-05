package org.uma.jmetal.problem;

/**
 * Interface representing problems having integer and double variables
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface IntegerDoubleProblem<S> extends Problem<S> {
  public int getLowerBound(int index) ;
  public int getUpperBound(int index) ;
  public int getNumberOfIntegerVariables() ;
  public int getNumberOfDoubleVariables() ;
}
