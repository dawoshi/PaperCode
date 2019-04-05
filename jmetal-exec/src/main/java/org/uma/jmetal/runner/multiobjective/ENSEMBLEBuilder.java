package org.uma.jmetal.runner.multiobjective;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder.NSGAIIVariant;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class ENSEMBLEBuilder {

	private final Problem<DoubleSolution> problem;
	private int maxEvaluations;
	private int populationSize;
	private CrossoverOperator<DoubleSolution> crossoverOperator;
	private MutationOperator<DoubleSolution> mutationOperator;
	private SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator;
	private SolutionListEvaluator<DoubleSolution> evaluator;

	//private NSGAIIVariant variant;

	/**
	 * NSGAIIBuilder constructor
	 */
	public ENSEMBLEBuilder(Problem<DoubleSolution> problem,
			CrossoverOperator<DoubleSolution> crossoverOperator,
			MutationOperator<DoubleSolution> mutationOperator) {
		this.problem = problem;
		
		maxEvaluations = 25000; // 最大评估代数
		populationSize = 100;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = mutationOperator;
		selectionOperator = new BinaryTournamentSelection<DoubleSolution>(
				new RankingAndCrowdingDistanceComparator<DoubleSolution>());

		evaluator = new SequentialSolutionListEvaluator<DoubleSolution>(); // 评估函数
		//this.variant = NSGAIIVariant.NSGAII;
	}

	public ENSEMBLEBuilder setMaxEvaluations(int maxEvaluations) {
		if (maxEvaluations < 0) {
			throw new JMetalException("maxEvaluations is negative: "
					+ maxEvaluations);
		}
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public ENSEMBLEBuilder setPopulationSize(int populationSize) {
		if (populationSize < 0) {
			throw new JMetalException("Population size is negative: "
					+ populationSize);
		}

		this.populationSize = populationSize;

		return this;
	}

	public ENSEMBLEBuilder setSelectionOperator(
			SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator) {
		if (selectionOperator == null) {
			throw new JMetalException("selectionOperator is null");
		}
		this.selectionOperator = selectionOperator;

		return this;
	}

	public ENSEMBLEBuilder setSolutionListEvaluator(
			SolutionListEvaluator<DoubleSolution> evaluator) {
		if (evaluator == null) {
			throw new JMetalException("evaluator is null");
		}
		this.evaluator = evaluator;

		return this;
	}

//	public ENSEMBLEBuilder setVariant(NSGAIIVariant variant) {
//		this.variant = variant;
//
//		return this;
//	}

	public AbstractENSEMBLE build() {
		AbstractENSEMBLE algorithm = null;
		algorithm = new AbstractENSEMBLE(problem, maxEvaluations,
				populationSize, crossoverOperator, mutationOperator,
				selectionOperator, evaluator);
		return algorithm;
	}

	/* Getters */
	public Problem<DoubleSolution> getProblem() {
		return problem;
	}

	public int getMaxIterations() {
		return maxEvaluations;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public CrossoverOperator<DoubleSolution> getCrossoverOperator() {
		return crossoverOperator;
	}

	public MutationOperator<DoubleSolution> getMutationOperator() {
		return mutationOperator;
	}

	public SelectionOperator<List<DoubleSolution>, DoubleSolution> getSelectionOperator() {
		return selectionOperator;
	}

	public SolutionListEvaluator<DoubleSolution> getSolutionListEvaluator() {
		return evaluator;
	}
}
