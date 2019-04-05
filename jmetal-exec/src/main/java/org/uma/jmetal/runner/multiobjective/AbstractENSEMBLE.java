package org.uma.jmetal.runner.multiobjective;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.impl.AbstractEvolutionaryAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SMOCrossover;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Configure;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.OrthogonalTable;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.naming.DescribedEntity;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;

public class AbstractENSEMBLE<S, Result> implements Algorithm<S>,Serializable{
	
	  protected List<DoubleSolution> population;
	  protected Problem<DoubleSolution> problem;
	  protected int maxPopulationSize ;
	  protected SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator ;
	  protected CrossoverOperator<DoubleSolution> crossoverOperator ;
	  protected MutationOperator<DoubleSolution> mutationOperator ;
	  public String indicationPath="ENSEMBLE.csv";
	  protected int evaluations;
	  protected SolutionListEvaluator<DoubleSolution> evaluator;
	  protected int maxEvaluations;
	  
	  public List<DoubleSolution> getPopulation() {
	    return population;
	  }
	  public void setPopulation(List<DoubleSolution> population) {
	    this.population = population;
	  }

	  public void setProblem(Problem<DoubleSolution> problem) {
	    this.problem = problem ;
	  }
	  public Problem<DoubleSolution> getProblem() {
	    return problem ;
	  }
	  
	  
	  public AbstractENSEMBLE(Problem<DoubleSolution> problem, int maxEvaluations, int populationSize,
		      CrossoverOperator<DoubleSolution> crossoverOperator, MutationOperator<DoubleSolution> mutationOperator,
		      SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator, SolutionListEvaluator<DoubleSolution> evaluator) {
		    this.maxEvaluations = maxEvaluations;
		    setMaxPopulationSize(populationSize); ;
		    this.crossoverOperator = crossoverOperator;
		    this.mutationOperator = mutationOperator;
		    this.selectionOperator = selectionOperator;
		    this.evaluator = evaluator;
		    this.problem = problem;
		  }
	  
	  public void run() {
		    List<DoubleSolution> offspringPopulation;
		    List<DoubleSolution> matingPopulation;
		    //System.out.println(problem.getName());
		    population = createInitialPopulation();
		    //System.out.println("population.size():"+population.size());
		    population = evaluatePopulation(population);
		    initProgress();
		    while (!isStoppingConditionReached()) {
		      matingPopulation = selection(population);
		      offspringPopulation = reproduction(matingPopulation);
		      offspringPopulation = evaluatePopulation(offspringPopulation);
		      population = replacement(population, offspringPopulation);
		      
			    try {   
		    	String rereferenceParetoFront=Configure.getReferenceParetoFrontPath();
				JMetalLogger.printLog((List<DoubleSolution>)population, rereferenceParetoFront, indicationPath,Configure.getproblem());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
		      updateProgress();
		    }
		  }

	   protected List<DoubleSolution> replacement(List<DoubleSolution> population2, List<DoubleSolution> offspringPopulation) {
		    List<DoubleSolution> jointPopulation = new ArrayList<>();
		    jointPopulation.addAll(population2);
		    jointPopulation.addAll(offspringPopulation);
		    RankingAndCrowdingSelection<DoubleSolution> rankingAndCrowdingSelection ;
		    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<DoubleSolution>(getMaxPopulationSize()) ;
		    List<DoubleSolution> pop =rankingAndCrowdingSelection.execute(jointPopulation);
		    return pop;
		  }
	protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
		    population = evaluator.evaluate(population, getProblem());
		    return population;
		  }
	protected boolean isStoppingConditionReached() {
		    return evaluations >= maxEvaluations;
		  }
	protected void initProgress() {
		    evaluations = getMaxPopulationSize();
		  }
	protected void updateProgress() {
		    evaluations += getMaxPopulationSize() ;
		  }
	/* Setters and getters */
	  public void setMaxPopulationSize(int maxPopulationSize) {
	    this.maxPopulationSize = maxPopulationSize ;
	  }
	  
	  public int getMaxPopulationSize() {
	    return maxPopulationSize ;
	  }
	  
	  public SelectionOperator<List<DoubleSolution>, DoubleSolution> getSelectionOperator() {
	    return selectionOperator;
	  }

	  public CrossoverOperator<DoubleSolution> getCrossoverOperator() {
	    return crossoverOperator;
	  }

	  public MutationOperator<DoubleSolution> getMutationOperator() {
	    return mutationOperator;
	  }

	  /**
	   * Constructor
	   * @param problem2 The problem to solve
	   */
	  public AbstractENSEMBLE(Problem<DoubleSolution> problem2) {
	    setProblem(problem2);
	  }

	  /**
	   * This method implements a default scheme create the initial population of genetic algorithm
	   * @return
	   */
	  protected List<DoubleSolution> createInitialPopulation() {
	    List<DoubleSolution> population = new ArrayList<>(getMaxPopulationSize());
	    for (int i = 0; i < getMaxPopulationSize(); i++) {
	    	DoubleSolution newIndividual = getProblem().createSolution();
	      population.add(newIndividual);
	    }
	    return population;
	  }

	  /**
	   * This method iteratively applies a {@link SelectionOperator} to the population to fill the mating pool population.
	   *
	   * @param population
	   * @return The mating pool population
	   */
	  protected List<DoubleSolution> selection(List<DoubleSolution> population) {
		List<DoubleSolution> matingPopulation = new ArrayList<>();
		selectionOperator= new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());
		for (int i = 0; i < getMaxPopulationSize(); i++) {
	    	DoubleSolution solution = selectionOperator.execute(population);
	      matingPopulation.add(solution);
	    }
		System.out.println("pop.size()"+population.size());
		List<DoubleSolution> tmp =new DifferentialEvolutionSelection().execute(population);
		for(DoubleSolution s:tmp){
			matingPopulation.add(s);
		}
		
		DoubleSolution ss = new BinaryTournamentSelection<DoubleSolution>().execute(population);
		matingPopulation.add(ss);
		RankingAndCrowdingSelection<DoubleSolution> rankingAndCrowdingSelection ;
		rankingAndCrowdingSelection = new RankingAndCrowdingSelection<DoubleSolution>(getMaxPopulationSize()) ;
		return rankingAndCrowdingSelection.execute(matingPopulation);
	  }

	  /**
	   * This methods iteratively applies a {@link CrossoverOperator} a  {@link MutationOperator} to the population to
	   * create the offspring population. The population size must be divisible by the number of parents required
	   * by the {@link CrossoverOperator}; this way, the needed parents are taken sequentially from the population.
	   *
	   * No limits are imposed to the number of solutions returned by the {@link CrossoverOperator}.
	   *
	   * @param matingPopulation
	   * @return The new created offspring population
	   */
	  protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPopulation) {
	    int numberOfParents = crossoverOperator.getNumberOfRequiredParents();

	    checkNumberOfParents(matingPopulation, numberOfParents);

	    List<DoubleSolution> offspringPopulation = new ArrayList<>();
	    List<DoubleSolution> cp = new ArrayList();
	    for (int i = 0; i < getMaxPopulationSize(); i += numberOfParents){
	      List<DoubleSolution> parents = new ArrayList<>(numberOfParents);
	      for (int j = 0; j < numberOfParents; j++) {
	        parents.add(matingPopulation.get(i+j));
	      }

	      List<DoubleSolution> offspring = crossoverOperator.execute(parents);
	      for(DoubleSolution s: offspring){
	        mutationOperator.execute(s);
	        offspringPopulation.add(s);
	      }
	      

	      /**
	       * 正交交叉操作
	       */  
	        double crossoverProbability = 0.1 ;
		    double crossoverDistributionIndex = 20.0 ;
		    List<DoubleSolution> ccc = new ArrayList();
		    OrthogonalTable.setQ(2); //水平数
		    OrthogonalTable.setThreshold(0.00001); //初始阈值s
		    List<DoubleSolution> cc = new SMOCrossover(crossoverProbability, crossoverDistributionIndex,problem).execute(parents);
		    RankingAndCrowdingSelection<DoubleSolution> rankingAndCrowdingSelection ;
		    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<DoubleSolution>(2) ;
		    List<DoubleSolution> pp =rankingAndCrowdingSelection.execute(cc);
		    for(DoubleSolution c:pp){
		    	cp.add(c);
		    }
		    
		    double cr = 1.0 ;
		    double f = 0.5 ;
//		    System.out.println("parents.size():"+parents.size());
//		    List<DoubleSolution> de = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin").execute(parents);
//		    
//		    for(DoubleSolution d:de){
//		    	cp.add(d);
//		    }
	    }
	    
	    List<DoubleSolution> rs = new ArrayList();
	    for(DoubleSolution r:cp){
	    	rs.add(r);
	    }
	    for(DoubleSolution r:offspringPopulation){
	    	rs.add(r);
	    }
	    
	    RankingAndCrowdingSelection<DoubleSolution> rankingAndCrowdingSelection ;
	    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<DoubleSolution>(getMaxPopulationSize()) ;
	    return rankingAndCrowdingSelection.execute(rs);
	  }

	  /**
	   * A crossover operator is applied to a number of parents, and it assumed that the population contains
	   * a valid number of solutions. This method checks that.
	   * @param matingPopulation
	   * @param numberOfParentsForCrossover
	   */
	  protected void checkNumberOfParents(List<DoubleSolution> matingPopulation, int numberOfParentsForCrossover) {
	    if ((matingPopulation.size() % numberOfParentsForCrossover) != 0) {
	      throw new JMetalException("Wrong number of parents: the remainder if the " +
	              "population size (" + matingPopulation.size() + ") is not divisible by " +
	              numberOfParentsForCrossover) ;
	    }
	  }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public S getResult() {
		// TODO Auto-generated method stub
		return (S) getPopulation();
	}
	}  