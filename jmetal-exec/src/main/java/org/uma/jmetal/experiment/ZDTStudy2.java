package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SMOCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.Fonseca;
import org.uma.jmetal.problem.multiobjective.Kursawe;
import org.uma.jmetal.problem.multiobjective.Schaffer;
import org.uma.jmetal.problem.multiobjective.UF.UF1;
import org.uma.jmetal.problem.multiobjective.UF.UF10;
import org.uma.jmetal.problem.multiobjective.UF.UF2;
import org.uma.jmetal.problem.multiobjective.UF.UF3;
import org.uma.jmetal.problem.multiobjective.UF.UF4;
import org.uma.jmetal.problem.multiobjective.UF.UF5;
import org.uma.jmetal.problem.multiobjective.UF.UF6;
import org.uma.jmetal.problem.multiobjective.UF.UF7;
import org.uma.jmetal.problem.multiobjective.UF.UF8;
import org.uma.jmetal.problem.multiobjective.UF.UF9;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ5;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ6;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ7;
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
import org.uma.jmetal.util.ProblemUtils;
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
public class ZDTStudy2 {

  private static final int INDEPENDENT_RUNS = 30 ;

  public static void main(String[] args) throws IOException {
//    if (args.length != 1) {
//      throw new JMetalException("Needed arguments: experimentBaseDirectory") ;
//    }
    //String experimentBaseDirectory = "D:/codes/guoxinian/" ;
    String experimentBaseDirectory="C:/Users/William/Desktop/data/";

    List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
    problemList.add(new ExperimentProblem<>(new ZDT1()));
    problemList.add(new ExperimentProblem<>(new ZDT2()));
    problemList.add(new ExperimentProblem<>(new ZDT3()));
    problemList.add(new ExperimentProblem<>(new ZDT4()));
    problemList.add(new ExperimentProblem<>(new ZDT6()));
    
    problemList.add(new ExperimentProblem<>(new DTLZ1()));
    problemList.add(new ExperimentProblem<>(new DTLZ2()));
    problemList.add(new ExperimentProblem<>(new DTLZ3()));
    problemList.add(new ExperimentProblem<>(new DTLZ4()));
    problemList.add(new ExperimentProblem<>(new DTLZ5()));
    problemList.add(new ExperimentProblem<>(new DTLZ6()));
    problemList.add(new ExperimentProblem<>(new DTLZ7()));
    
    problemList.add(new ExperimentProblem<>(new UF1()));
    problemList.add(new ExperimentProblem<>(new UF2()));
    problemList.add(new ExperimentProblem<>(new UF3()));
    problemList.add(new ExperimentProblem<>(new UF4()));
    problemList.add(new ExperimentProblem<>(new UF5()));
    problemList.add(new ExperimentProblem<>(new UF6()));
    problemList.add(new ExperimentProblem<>(new UF7()));
    problemList.add(new ExperimentProblem<>(new UF8()));
    problemList.add(new ExperimentProblem<>(new UF9()));
    problemList.add(new ExperimentProblem<>(new UF10()));
    
    problemList.add(new ExperimentProblem<>(new Schaffer()));
    problemList.add(new ExperimentProblem<>(new Fonseca()));
    problemList.add(new ExperimentProblem<>(new Kursawe()));
    
    

    

    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
            configureAlgorithmList(problemList);

    ExperimentBuilder<DoubleSolution, List<DoubleSolution>> zdt2Study = new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("ZDTStudy2");
    zdt2Study.setAlgorithmList(algorithmList);
    zdt2Study.setProblemList(problemList);
    zdt2Study.setExperimentBaseDirectory(experimentBaseDirectory);
    zdt2Study.setOutputParetoFrontFileName("FUN");
    zdt2Study.setOutputParetoSetFileName("VAR");
    zdt2Study.setReferenceFrontDirectory(experimentBaseDirectory + "/ZDTStudy2/referenceFronts");
    zdt2Study.setIndicatorList(Arrays.asList(
            new Epsilon<DoubleSolution>(), new Spread<DoubleSolution>(), new GenerationalDistance<DoubleSolution>(),
            new PISAHypervolume<DoubleSolution>(),
            new InvertedGenerationalDistance<DoubleSolution>(), new InvertedGenerationalDistancePlus<DoubleSolution>()));
    zdt2Study.setIndependentRuns(INDEPENDENT_RUNS);
    zdt2Study.setNumberOfCores(8);
    Experiment<DoubleSolution, List<DoubleSolution>> experiment = zdt2Study.build();

    new ExecuteAlgorithms<>(experiment).run();
    new GenerateReferenceParetoSetAndFrontFromDoubleSolutions(experiment).run();
    new ComputeQualityIndicators<>(experiment).run() ;
    new GenerateLatexTablesWithStatistics(experiment).run() ;
    new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
    new GenerateFriedmanTestTables<>(experiment).run();
    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run() ;
  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
   * {@link TaggedAlgorithm}, which is a decorator for class {@link Algorithm}.
   *
   * @param problemList
   * @return
   */
  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
   * {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
   *
   * @param problemList
   * @return
   */
  static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
          List<ExperimentProblem<DoubleSolution>> problemList) {
    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();

    for (int i = 0; i < problemList.size(); i++) {
      Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<DoubleSolution>(
              problemList.get(i).getProblem(),
              new SBXCrossover(1.0, 20.0),
              new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
              .build();
      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
    }
    
//    double crossoverProbability = 0.1 ;
//	double crossoverDistributionIndex = 20.0 ;
//	OrthogonalTable.setQ(2); //水平数
//	OrthogonalTable.setThreshold(0.8); //初始阈值s 
//	
//  for (int i = 0; i < problemList.size(); i++) {
//  Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<DoubleSolution>(
//          problemList.get(i).getProblem(),
//  	    new SMOCrossover(crossoverProbability, crossoverDistributionIndex,problemList.get(i).getProblem()),
//          //new SBXCrossover(1.0, 20.0),
//          new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
//          .build();
//  algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
//}
    
    for (int i = 0; i < problemList.size(); i++) {
	      double cr = 1.0 ;
	      double f = 0.5 ;
	      Problem<DoubleSolution> problem = problemList.get(i).getProblem();
	      MutationOperator<DoubleSolution> mutation;
	      DifferentialEvolutionCrossover crossover;
	      
	      crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");

	      double mutationProbability = 1.0 / problem.getNumberOfVariables();
	      double mutationDistributionIndex = 20.0;
	      mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

	      Algorithm<List<DoubleSolution>> algorithm = new MOEADBuilder(problem, MOEADBuilder.Variant.MOEAD)
	              .setCrossover(crossover)
	              .setMutation(mutation)
	              .setMaxEvaluations(25000)
	              .setPopulationSize(100)
	              .setResultPopulationSize(100)
	              .setNeighborhoodSelectionProbability(0.9)
	              .setMaximumNumberOfReplacedSolutions(2)
	              .setNeighborSize(20)
	              .setFunctionType(AbstractMOEAD.FunctionType.TCHE)
	              .setDataDirectory("MOEAD_Weights")
	              .build();
	      algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
	    }

    return algorithms ;
  }
}
