package org.uma.jmetal.runner.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.casmocde.AbstractCASMOCDE;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.Configure;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class CASMOCDERunner extends AbstractAlgorithmRunner {
	public static void main(String[] args) throws JMetalException,
			FileNotFoundException {
		Problem<DoubleSolution> problem;
		AbstractCASMOCDE algorithm;
		CrossoverOperator<DoubleSolution> crossover;
		MutationOperator<DoubleSolution> mutation;
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
		String problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
		String referenceParetoFront = "D:/codes/guoxinian/jMetal/jmetal-problem/src/test/resources/pareto_fronts/ZDT2.pf";
		Configure.setReferenceParetoFrontPath(referenceParetoFront);
		Configure.setIndicationPath("ERSSMOCDE.csv");
		problem = (DoubleProblem)ProblemUtils.<DoubleSolution>loadProblem(problemName);
		Configure.setproblem(problem.getName());
		System.out.println(problem.getName());
		int maxEvaluations = 25001; // 最大评估代数
		int populationSize = 100;

		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 20.0;
		crossover = new SBXCrossover(crossoverProbability,
				crossoverDistributionIndex);
		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new PolynomialMutation(mutationProbability,
				mutationDistributionIndex);
		SolutionListEvaluator<DoubleSolution> evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
		selection = new BinaryTournamentSelection<DoubleSolution>(
				new RankingAndCrowdingDistanceComparator<DoubleSolution>());
		algorithm = new AbstractCASMOCDE(problem, maxEvaluations,populationSize, crossover, mutation,selection, evaluator);
		algorithm.run();
		
		List<DoubleSolution> population = (List<DoubleSolution>) algorithm.getResult() ;
		printFinalSolutionSet(population);
		if (!referenceParetoFront.equals("")) {
		      printQualityIndicators(population, referenceParetoFront) ;
		    }
	}

}
