package org.uma.jmetal.algorithm.multiobjective.nsgaii;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class NSGAII<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {
  protected final int maxEvaluations;

  protected final SolutionListEvaluator<S> evaluator;

  protected int evaluations;
  protected String referenceParetoFront ="D:/codes/guoxinian/PaperCode/jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.3D.pf";
  //D:/codes/guoxinian/PaperCode/jmetal-problem/src/test/resources/pareto_fronts
  protected String indicatePath ="NSGAII.csv";


  /**
   * Constructor
   */
  public NSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
      CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
    super(problem);
    this.maxEvaluations = maxEvaluations;
    setMaxPopulationSize(populationSize); ;

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.selectionOperator = selectionOperator;

    this.evaluator = evaluator;
  }

  @Override protected void initProgress() {
    evaluations = getMaxPopulationSize();
  }

  @Override protected void updateProgress() {
    evaluations += getMaxPopulationSize() ;
  }

  @Override protected boolean isStoppingConditionReached() {
    return evaluations >= maxEvaluations;
  }

  @Override protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, getProblem());

    return population;
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population);
    jointPopulation.addAll(offspringPopulation);

    RankingAndCrowdingSelection<S> rankingAndCrowdingSelection ;
    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(getMaxPopulationSize()) ;
    
    
    
    List<S> pop =rankingAndCrowdingSelection.execute(jointPopulation) ;
    try {
		printLog((List<DoubleSolution>)pop,referenceParetoFront);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return pop;
  }
  
  /**
   * 
   * 获取每代的信息
   * 
   * @param pop 种群
   * @param paretoFrontFile  真实pareto解集
   * @throws Exception
   */
  protected void printLog(List<DoubleSolution> pop, String paretoFrontFile) throws Exception{
	    Front referenceFront = new ArrayFront(paretoFrontFile);
	    FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront) ;
	    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
	    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(pop)) ;
	    List<PointSolution> normalizedPopulation = FrontUtils
	        .convertFrontToSolutionList(normalizedFront) ;
	    String outputString = "\n" ;
	    double hypervolumen = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "Hypervolume (N) : " +hypervolumen + "\n";
	    
	    double hypervolume = new PISAHypervolume<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString += "Hypervolume     : " + hypervolume + "\n";
	    
	    double epsilonn = new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "Epsilon (N)     : " + epsilonn + "\n" ;
	    
	    double epsilon = new Epsilon<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString += "Epsilon         : " + epsilon  + "\n" ;
	    
	    double gdn= new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "GD (N)          : " + gdn + "\n";
	    
	    double gd =  new GenerationalDistance<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString += "GD              : " + gd  + "\n";
	    
	    double igdn = new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "IGD (N)         : " + igdn  + "\n";
	    
	    double igd = new InvertedGenerationalDistance<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString +="IGD             : " + igd + "\n";
	    
	    double igdnplus = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "IGD+ (N)        : " + igdnplus  + "\n";
	    
	    double igdplus = new InvertedGenerationalDistancePlus<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString += "IGD+            : " + igdplus  + "\n";
	    
	    double spread  =   new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    outputString += "Spread (N)      : " + spread  + "\n";
	    
	    double spreadre = new Spread<DoubleSolution>(referenceFront).evaluate(pop);
	    outputString += "Spread          : " + spreadre  + "\n";
//	    outputString += "R2 (N)          : " +
//	        new R2<List<DoubleSolution>>(normalizedReferenceFront).runAlgorithm(normalizedPopulation) + "\n";
//	    outputString += "R2              : " +
//	        new R2<List<? extends Solution<?>>>(referenceFront).runAlgorithm(population) + "\n";
	    outputString += "Error ratio     : " +
	        new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(pop) + "\n";
	    
	    /**
	     * 输出到日志
	     */
	    JMetalLogger.logger.info(outputString);
	    
	    
	    /**
	     * 输出到文件
	     */
//	    DefaultFileOutputContext fileout = new DefaultFileOutputContext(indicatePath);
//	    BufferedWriter bw = fileout.getFileWriter();
	    try {                                                                        
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件      
             FileWriter wt = new FileWriter(indicatePath, true);                      
             wt.write(hypervolumen+","+hypervolume+","+epsilonn+","+epsilon+","+gdn+","+gd+","+igdn+","+igd+","+igdnplus+","+igdplus+","+spread+","+spreadre+"\n");  
             wt.flush();
             wt.close();                                                  
         } catch (IOException e) {                                                   
             e.printStackTrace();                                                    
         }
  }
  
  

  @Override public List<S> getResult() {
    return getNonDominatedSolutions(getPopulation());
  }

  protected List<S> getNonDominatedSolutions(List<S> solutionList) {
    return SolutionListUtils.getNondominatedSolutions(solutionList);
  }

  @Override public String getName() {
    return "NSGAII" ;
  }

  @Override public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version II" ;
  }
}
