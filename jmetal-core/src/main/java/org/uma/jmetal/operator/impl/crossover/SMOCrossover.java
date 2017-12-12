package org.uma.jmetal.operator.impl.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;
import org.uma.jmetal.util.Orthogonal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	  private Problem<DoubleSolution> problem;
	  private RandomGenerator<Double> randomGenerator ;

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex,Problem<DoubleSolution> problem) {
	    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds(),problem) ;
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RandomGenerator<Double> randomGenerator,Problem<DoubleSolution> problem) {
	    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds(),problem) ;
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair,Problem<DoubleSolution> problem) {
		  this(crossoverProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble(),problem);
	  }

	  /** Constructor */
	  public SMOCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator,Problem<DoubleSolution> problem) {
	    if (crossoverProbability < 0) {
	      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
	    } else if (distributionIndex < 0) {
	      throw new JMetalException("Distribution index is negative: " + distributionIndex);
	    }

	    this.crossoverProbability = crossoverProbability ;
	    this.distributionIndex = distributionIndex ;
	    this.solutionRepair = solutionRepair ;
	    this.randomGenerator = randomGenerator ;
	    this.problem = problem;
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
	        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
	        
	        for(int i=0;i<parent1.getNumberOfVariables();i++){
	        	if(Math.abs(up[i]-low[i])> Orthogonal.getThreshold()){
	        		count++;
	        		status[i] = true;
	        		recordTrue.add(i);
	        		map.put(i,count-1);
	        	}else{
	        		status[i] = false;
	        		recordFalse.add(i);
	        	}
	        }
	        
	        if(count>0){
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
	       if(count>0 && count<parent1.getNumberOfVariables()){
	        for(int i=0;i<Orthogonal.getRows();i++){
	        	for(int j = 0;j<parent1.getNumberOfVariables();j++){
	        		if(status[j]==false){
	        			int t1 = j;
	        			while(t1>=0 && status[t1]!=true){
	        				t1--;
	        			}
	        			if(t1<0 &&(j+1<parent1.getNumberOfVariables())){
	        				int t2 = j + 1;
	    					while (t2<parent1.getNumberOfVariables() && status[t2] != true)
	    					{
	    						t2++;
	    					}
	    					if (t2<parent1.getNumberOfVariables()&&status[t2] == true)
	    					{
	    						int temp21 = map.get(t2);//record_statue[t2];//取出status列 对应的正交表的列
	    						int temp22 = orthogonaltable[i][temp21];//pop[temp21].col_length[i];//取出正交表中对应的离散化矩阵的位置
	    						maptable[i][j] = disArray[temp22-1][j];//out_array[j].col_length[i] = value_arry[j].col_length[temp22];
	    					}
	    				}
	    				else//左边找到
	    				{
	    					int temp31 = map.get(t1);//record_statue[t1];
	    					int temp32 = orthogonaltable[i][temp31];//pop[temp31].col_length[i];//取出正交表中对应的离散化矩阵的位置
	    					maptable[i][j] = disArray[temp32-1][j];// out_array[j].col_length[i] = value_arry[j].col_length[temp32];
	    				}
	        		}
	        	}
	        }
	        }
	       
	       
	       
	  System.out.println("Q:"+Orthogonal.getQ()+"  "+"F:"+Orthogonal.getF()+" "+"count:"+count);  
	  System.out.println("row:"+maptable.length+"  "+"col:"+maptable[0].length);
	        for(int i = 0;i<maptable.length;i++){
	        	for(int j = 0;j<maptable[0].length;j++){
	        		System.out.print(maptable[i][j]+", ");
	        	}
	        	System.out.println();
	        }
	   }else{
		   //走原来的交叉
	   }
//	    while(true){
//	    }
	    		       
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

	public Problem<DoubleSolution> getProblem() {
		return problem;
	}

	public void setProblem(Problem<DoubleSolution> problem) {
		this.problem = problem;
	}
	}
