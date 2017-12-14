package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SMOCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT2;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.OrthogonalTable;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.io.FileNotFoundException;
import java.util.List;

public class My_DE_Runner extends AbstractAlgorithmRunner{/**
	   * @param args Command line arguments.
	   * @throws JMetalException
	   * @throws FileNotFoundException
	   * Invoking command:
	    java org.uma.jmetal.runner.multiobjective.NSGAIIRunner problemName [referenceFront]
	   */
	  public static void main(String[] args) throws JMetalException, FileNotFoundException {
	    Problem<DoubleSolution> problem;
	    Algorithm<List<DoubleSolution>> algorithm;
	    CrossoverOperator<DoubleSolution> crossover;
	    MutationOperator<DoubleSolution> mutation;
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
	    String referenceParetoFront = "" ;

	    String problemName ;
	    if (args.length == 1) {
	      problemName = args[0];
	    } else if (args.length == 2) {
	      problemName = args[0] ;
	      referenceParetoFront = args[1] ;
	    } else {
	      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
	      referenceParetoFront ="D:/codes/guoxinian/jMetal/jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf";
	    }

	    problem = new ZDT1(); //ProblemUtils.<DoubleSolution> loadProblem(problemName);

	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 20.0 ;
	    OrthogonalTable.setQ(2); //水平数
	    OrthogonalTable.setThreshold(0.2); //初始阈值
	    crossover = new SMOCrossover(crossoverProbability, crossoverDistributionIndex,problem);
	    double mutationProbability = 1.0 / problem.getNumberOfVariables();
	    double mutationDistributionIndex = 20.0 ;
	    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

	    selection = new BinaryTournamentSelection<DoubleSolution>(
	        new RankingAndCrowdingDistanceComparator<DoubleSolution>());

	      algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation)
	        .setSelectionOperator(selection)
	        .setMaxEvaluations(25000) //最大评估代数
	        .setPopulationSize(300) //种群个体数量
	        .build() ;

	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
	        .execute() ;

	    List<DoubleSolution> population = algorithm.getResult() ;
	    long computingTime = algorithmRunner.getComputingTime() ;

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

	    printFinalSolutionSet(population);
	    System.out.println(referenceParetoFront);
	    if (!referenceParetoFront.equals("")) {
	      printQualityIndicators(population, referenceParetoFront) ;
	    }
	  }
	}