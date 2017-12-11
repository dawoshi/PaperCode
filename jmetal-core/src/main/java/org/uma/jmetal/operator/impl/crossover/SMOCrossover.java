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
 * @author William.guo <guoxinian@aliyun.com>
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
	        	for(int j = 0;j<Orthogonal.getQ();j++){
	        		if(j ==0){
	        			disArray[j][i] = low[i]; 
	        		}else if(j>0&&j<(Orthogonal.getQ()-1)){
	        			disArray[j][i] = low[i]+j*((up[i]-low[i])/(Orthogonal.getQ()-1));
	        		}else{
	        			disArray[j][i] = up[i];
	        		}
	        	}
	        }
 
	        //获取大于阈值的维度数
	        int count = 0;
	        boolean[] status = new boolean[parent1.getNumberOfVariables()];
	        List<Integer> recordTrue = new ArrayList<Integer>();
	        List<Integer> recordFalse = new ArrayList<Integer>();
	        
	        for(int i=0;i<parent1.getNumberOfVariables();i++){
	        	if(Math.abs(up[i]-low[i])> Orthogonal.getThreshold()){
	        		count++;
	        		status[i] = true;
	        		recordTrue.add(i);
	        	}else{
	        		status[i] = false;
	        		recordFalse.add(i);
	        	}
	        }
	        
	        
	        
	        //离散化矩阵和正交表映射
	        Orthogonal.setF(count);
	        int [][] orthogonaltable = Orthogonal.getOrthogoanlTable();
	        	        
	        double[][] maptable = new double[Orthogonal.getRows()][parent1.getNumberOfVariables()];
	       
	        //大于阈值的列进行填充
	        for(int j=0;j<count;j++){
	        	int col = recordTrue.get(j);
	        	for(int i = 0;i<Orthogonal.getRows();i++){
	        		maptable[i][col] = disArray[orthogonaltable[i][j]-1][col];
	        	}
	        }
	        
	        //小于阈值的列进行填充
	        
	        
	        
	        
	        
	    	for (int i = 1; i <= my_pow(q, Selec_j(q, count)); i++)//映射正交没有选中的几列
	    	{
	    		for (int j = 1; j <= f; j++)//count是根据阈值新生成的数组的大小
	    		{
	    			if (status[j] == false)
	    			{
	    				int t1 = j - 1;
	    				while (t1 >= 1 && status[t1] != true)
	    				{
	    					t1--;
	    				}
	    				if (t1 < 1)//左边找不到往右找
	    				{
	    					int t2 = j + 1;
	    					while (status[t2] != true)
	    					{
	    						t2++;
	    					}
	    					if (status[t2] == true)
	    					{
	    						int temp21 = record_statue[t2];//取出status列 对应的正交表的列
	    						int temp22 = pop[temp21].col_length[i];//取出正交表中对应的离散化矩阵的位置
	    						out_array[j].col_length[i] = value_arry[j].col_length[temp22];
	    					}
	    				}
	    				else//左边找到
	    				{
	    					int temp31 = record_statue[t1];
	    					int temp32 = pop[temp31].col_length[i];//取出正交表中对应的离散化矩阵的位置
	    					out_array[j].col_length[i] = value_arry[j].col_length[temp32];
	    				}
	    			}//
	    		}
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
