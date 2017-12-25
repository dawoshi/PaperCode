package org.uma.jmetal.experiment;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SMOCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT2;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT3;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT4;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT6;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.OrthogonalTable;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateFriedmanTestTables;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateReferenceParetoSetAndFrontFromDoubleSolutions;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

	import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

	/**
	 * Example of experimental study based on solving the ZDT problems with algorithms NSGAII,
	 * SPEA2, and SMPSO
	 *
	 * This experiment assumes that the reference Pareto front are not known, so the names of files containing
	 * them and the directory where they are located must be specified.
	 *
	 * Six quality indicators are used for performance assessment.
	 *
	 * The steps to carry out the experiment are:
	 * 1. Configure the experiment
	 * 2. Execute the algorithms
	 * 3. Generate the reference Pareto fronts
	 * 4. Compute que quality indicators
	 * 5. Generate Latex tables reporting means and medians
	 * 6. Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
	 * 7. Generate R scripts to obtain boxplots
	 *
	 * @author Antonio J. Nebro <antonio@lcc.uma.es>
	 */
	public class MOEADstudy2 {
		 private static final int INDEPENDENT_RUNS = 30;

		  public static void main(String[] args) throws IOException {
//		    if (args.length != 1) {
//		      throw new JMetalException("Missing argument: experimentBaseDirectory");
//		    }
		    String experimentBaseDirectory = "C:/Users/William/Desktop/data/test/";

		    List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
		    problemList.add(new ExperimentProblem<>(new ZDT1()));
		    problemList.add(new ExperimentProblem<>(new ZDT2()));
		    problemList.add(new ExperimentProblem<>(new ZDT3()));
		    problemList.add(new ExperimentProblem<>(new ZDT4()));
		    problemList.add(new ExperimentProblem<>(new ZDT6()));

		    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
		            configureAlgorithmList(problemList);

		    List<String> referenceFrontFileNames = Arrays.asList("ZDT1.pf", "ZDT2.pf", "ZDT3.pf", "ZDT4.pf", "ZDT6.pf");

		    Experiment<DoubleSolution, List<DoubleSolution>> experiment =
		            new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("ZDTStudy")
		                    .setAlgorithmList(algorithmList)
		                    .setProblemList(problemList)
		                    .setReferenceFrontDirectory("D:/codes/guoxinian/PaperCode/jmetal-problem/src/test/resources/pareto_fronts")
		                    .setReferenceFrontFileNames(referenceFrontFileNames)
		                    .setExperimentBaseDirectory(experimentBaseDirectory)
		                    .setOutputParetoFrontFileName("FUN")
		                    .setOutputParetoSetFileName("VAR")
		                    .setIndicatorList(Arrays.asList(
		                            new Epsilon<DoubleSolution>(),
		                            new Spread<DoubleSolution>(),
		                            new GenerationalDistance<DoubleSolution>(),
		                            new PISAHypervolume<DoubleSolution>(),
		                            new InvertedGenerationalDistance<DoubleSolution>(),
		                            new InvertedGenerationalDistancePlus<DoubleSolution>()))
		                    .setIndependentRuns(INDEPENDENT_RUNS)
		                    .setNumberOfCores(8)
		                    .build();

		    new ExecuteAlgorithms<>(experiment).run();
		    new ComputeQualityIndicators<>(experiment).run();
		    new GenerateLatexTablesWithStatistics(experiment).run();
		    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
		    new GenerateFriedmanTestTables<>(experiment).run();
		    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run();
		  }

		  /**
		   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
		   * a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
		   */
		  static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
		          List<ExperimentProblem<DoubleSolution>> problemList) {
		    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();

//		    for (int i = 0; i < problemList.size(); i++) {
//		      double mutationProbability = 1.0 / problemList.get(i).getProblem().getNumberOfVariables();
//		      double mutationDistributionIndex = 20.0;
//		      Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder((DoubleProblem) problemList.get(i).getProblem(),
//		              new CrowdingDistanceArchive<DoubleSolution>(100))
//		              .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
//		              .setMaxIterations(250)
//		              .setSwarmSize(100)
//		              .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
//		              .build();
//		      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
//		    }

		    for (int i = 0; i < problemList.size(); i++) {
		      Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<DoubleSolution>(
		              problemList.get(i).getProblem(),
		              new SMOCrossover(0.9, 20,problemList.get(i).getProblem()),
		              //new SBXCrossover(1.0, 20.0),
		              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0)).build();
		      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
		    }

//		    for (int i = 0; i < problemList.size(); i++) {
//		      Algorithm<List<DoubleSolution>> algorithm = new SPEA2Builder<DoubleSolution>(
//		              problemList.get(i).getProblem(),
//		              new SBXCrossover(1.0, 10.0),
//		              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
//		              .build();
//		      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
//		    }
		    
//		    for (int i = 0; i < problemList.size(); i++) {
//			      double cr = 1.0 ;
//			      double f = 0.5 ;
//			      Problem<DoubleSolution> problem = problemList.get(i).getProblem();
//			      MutationOperator<DoubleSolution> mutation;
//			      DifferentialEvolutionCrossover crossover;
//			      
//			      crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");
//
//			      double mutationProbability = 1.0 / problem.getNumberOfVariables();
//			      double mutationDistributionIndex = 20.0;
//			      mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
//
//			      Algorithm<List<DoubleSolution>> algorithm = new MOEADBuilder(problem, MOEADBuilder.Variant.MOEAD)
//			              .setCrossover(crossover)
//			              .setMutation(mutation)
//			              .setMaxEvaluations(25000)
//			              .setPopulationSize(100)
//			              .setResultPopulationSize(100)
//			              .setNeighborhoodSelectionProbability(0.9)
//			              .setMaximumNumberOfReplacedSolutions(2)
//			              .setNeighborSize(20)
//			              .setFunctionType(AbstractMOEAD.FunctionType.TCHE)
//			              .setDataDirectory("MOEAD_Weights")
//			              .build();
//			      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
//			    }

		    return algorithms;
		  }
		}
	

