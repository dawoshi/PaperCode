package org.uma.jmetal.algorithm.multiobjective.smocde;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIMeasures;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.SteadyStateNSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder.NSGAIIVariant;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.util.List;


//public class SMOCDEBuilder 

public class SMOCDEBuilder<S extends Solution<?>> implements AlgorithmBuilder<SMOCDE<S>> {
	  

	  /**
	   * SMOCDEBuilder class
	   */
	  private Problem<S> problem;
	  private int maxEvaluations;
	  private int populationSize;
	  private CrossoverOperator<S>  crossoverOperator;
	  private MutationOperator<S> mutationOperator;
	  private SelectionOperator<List<S>, S> selectionOperator;
	  private SolutionListEvaluator<S> evaluator;

	  private NSGAIIVariant variant;

	  /**
	   * NSGAIIBuilder constructor
	   */
	  public SMOCDEBuilder(Problem<S> problem, CrossoverOperator<S> crossoverOperator,
	      MutationOperator<S> mutationOperator) {
	    this.problem = problem;
	    maxEvaluations = 25000; //最大评估代数
	    populationSize = 100;
	    this.crossoverOperator = crossoverOperator ;
	    this.mutationOperator = mutationOperator ;
	    selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>()) ;
	    evaluator = new SequentialSolutionListEvaluator<S>(); //评估函数
	  }

	  public SMOCDEBuilder<S> setMaxEvaluations(int maxEvaluations) {
	    if (maxEvaluations < 0) {
	      throw new JMetalException("maxEvaluations is negative: " + maxEvaluations);
	    }
	    this.maxEvaluations = maxEvaluations;

	    return this;
	  }

	  public SMOCDEBuilder<S> setPopulationSize(int populationSize) {
	    if (populationSize < 0) {
	      throw new JMetalException("Population size is negative: " + populationSize);
	    }

	    this.populationSize = populationSize;

	    return this;
	  }

	  public SMOCDEBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
	    if (selectionOperator == null) {
	      throw new JMetalException("selectionOperator is null");
	    }
	    this.selectionOperator = selectionOperator;

	    return this;
	  }

	  public SMOCDEBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
	    if (evaluator == null) {
	      throw new JMetalException("evaluator is null");
	    }
	    this.evaluator = evaluator;

	    return this;
	  }


	  public SMOCDEBuilder<S> setVariant(NSGAIIVariant variant) {
	    this.variant = variant;

	    return this;
	  }

	  public SMOCDE<S> build() {	    
	      return new SMOCDE<S>(problem, maxEvaluations, populationSize, crossoverOperator,
	          mutationOperator, selectionOperator, evaluator);
	  }

	  /* Getters */
	  public Problem<S> getProblem() {
	    return problem;
	  }

	  public int getMaxIterations() {
	    return maxEvaluations;
	  }

	  public int getPopulationSize() {
	    return populationSize;
	  }

	  public CrossoverOperator<S> getCrossoverOperator() {
	    return crossoverOperator;
	  }

	  public MutationOperator<S> getMutationOperator() {
	    return mutationOperator;
	  }

	  public SelectionOperator<List<S>, S> getSelectionOperator() {
	    return selectionOperator;
	  }

	  public SolutionListEvaluator<S> getSolutionListEvaluator() {
	    return evaluator;
	  }
	}
	
	


