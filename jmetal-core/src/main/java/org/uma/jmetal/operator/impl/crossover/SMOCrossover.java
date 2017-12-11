package org.uma.jmetal.operator.impl.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;
import org.uma.jmetal.util.Orthogonal;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author William
 *
 */

@SuppressWarnings("serial")
public class SMOCrossover implements CrossoverOperator<DoubleSolution>{
	 private static final double EPS = 1.0e-14;

	  private double distributionIndex ;
	  private double crossoverProbability  ;  //交叉率
	  private RepairDoubleSolution solutionRepair ;

	  private RandomGenerator<Double> randomGenerator ;

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex) {
	    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds()) ;
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RandomGenerator<Double> randomGenerator) {
	    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds(), randomGenerator) ;
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair) {
		  this(crossoverProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
	    if (crossoverProbability < 0) {
	      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
	    } else if (distributionIndex < 0) {
	      throw new JMetalException("Distribution index is negative: " + distributionIndex);
	    }

	    this.crossoverProbability = crossoverProbability ;
	    this.distributionIndex = distributionIndex ;
	    this.solutionRepair = solutionRepair ;

	    this.randomGenerator = randomGenerator ;
	  }

	  /* Getters */
	  public double getCrossoverProbability() {
	    return crossoverProbability;
	  }

	  public double getDistributionIndex() {
	    return distributionIndex;
	  }

	  /* Setters */
	  public void setCrossoverProbability(double probability) {
	    this.crossoverProbability = probability ;
	  }

	  public void setDistributionIndex(double distributionIndex) {
	    this.distributionIndex = distributionIndex ;
	  }

	  /** Execute() method */
	  @Override
	  public List<DoubleSolution> execute(List<DoubleSolution> solutions) {
	    if (null == solutions) {
	      throw new JMetalException("Null parameter") ;
	    } else if (solutions.size() != 2) {
	      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
	    }

	    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
	  }

	  /** doCrossover method */
	  public List<DoubleSolution> doCrossover(
	      double probability, DoubleSolution parent1, DoubleSolution parent2) {
	    List<DoubleSolution> offspring = new ArrayList<DoubleSolution>(2);

	    offspring.add((DoubleSolution) parent1.copy()) ;
	    offspring.add((DoubleSolution) parent2.copy()) ;

	    //int i;
	    double rand;
	    double y1, y2, lowerBound, upperBound;
	    double c1, c2;
	    double alpha, beta, betaq;
	    double valueX1, valueX2;

	    if (randomGenerator.getRandomValue() <= probability) {
	    	double[] low = new double[parent1.getNumberOfVariables()];
	    	double[] up = new double[parent1.getNumberOfVariables()];
	      for (int i = 0; i < parent1.getNumberOfVariables(); i++) {
	    	  
	        valueX1 = parent1.getVariableValue(i);
	        valueX2 = parent2.getVariableValue(i);
	        
	        //生成【parent1，paarent2】之间的 up-low数组
	        
	        if(valueX1>valueX2){
	        	up[i] = valueX1;
	        	low[i] = valueX2;
	        }else{
	        	up[i] = valueX2;
	        	low[i] = valueX1;
	        }
	      }
	        
	        //up-low数组离散化
	        double[][] disArray = new double[Orthogonal.getQ()][parent1.getNumberOfVariables()];
	        for (int i = 0; i < parent1.getNumberOfVariables(); i++) {
	        	for(int j = 1;j<=Orthogonal.getQ();j++){
	        		if(j ==1){
	        			disArray[i][j] = low[i]; 
	        		}else if(j>1&&j<Orthogonal.getQ()){
	        			disArray[i][j] = low[i]+(j-1)*((up[i]-low[i])/(Orthogonal.getQ()-1));
	        		}else{
	        			disArray[i][j] = up[i];
	        		}
	        	}
	        }
	        
	        //离散化矩阵和正交表映射
	        double[][] maptable = new double[Orthogonal.getColumns()][];
	        for(int i= 0;i<parent1.getNumberOfVariables();i++){
	        	int j =1;
	        }
	        
	        
	        
	        
	        
	        
	        
	        
//	        
//	        if (randomGenerator.getRandomValue() <= 0.5) {
//	          if (Math.abs(valueX1 - valueX2) > EPS) {
//
//	            if (valueX1 < valueX2) {
//	              y1 = valueX1;
//	              y2 = valueX2;
//	            } else {
//	              y1 = valueX2;
//	              y2 = valueX1;
//	            }
//
//	            lowerBound = parent1.getLowerBound(i);
//	            upperBound = parent1.getUpperBound(i);
//
//	            rand = randomGenerator.getRandomValue();
//	            beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
//	            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));
//
//	            if (rand <= (1.0 / alpha)) {
//	              betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
//	            } else {
//	              betaq = Math
//	                  .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
//	            }
//	            c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));
//
//	            beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
//	            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));
//
//	            if (rand <= (1.0 / alpha)) {
//	              betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
//	            } else {
//	              betaq = Math
//	                  .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
//	            }
//	            c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));
//
//	            c1 = solutionRepair.repairSolutionVariableValue(c1, lowerBound, upperBound) ;
//	            c2 = solutionRepair.repairSolutionVariableValue(c2, lowerBound, upperBound) ;
//
//	            if (randomGenerator.getRandomValue() <= 0.5) {
//	              offspring.get(0).setVariableValue(i, c2);
//	              offspring.get(1).setVariableValue(i, c1);
//	            } else {
//	              offspring.get(0).setVariableValue(i, c1);
//	              offspring.get(1).setVariableValue(i, c2);
//	            }
//	          } else {
//	            offspring.get(0).setVariableValue(i, valueX1);
//	            offspring.get(1).setVariableValue(i, valueX2);
//	          }
//	        } else {
//	          offspring.get(0).setVariableValue(i, valueX1);
//	          offspring.get(1).setVariableValue(i, valueX2);
//	        }
	  
	    }

	    return offspring;
	  }

	  @Override
	  public int getNumberOfRequiredParents() {
	    return 2 ;
	  }

	  @Override
	  public int getNumberOfGeneratedChildren() {
	    return 2;
	  }
	}
