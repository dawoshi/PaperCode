import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;


public class bug {
		public static int populationSize = 100000;
		public static String problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
		public static Problem<DoubleSolution> problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);
	
	  public static List<DoubleSolution> createInitialPopulation() {
		    List<DoubleSolution> population = new ArrayList<>(populationSize);
		    for (int i = 0; i < populationSize; i++) {
		      DoubleSolution newIndividual = problem.createSolution();
		      population.add(newIndividual);
		    }
		    
		    SequentialSolutionListEvaluator<DoubleSolution> evaluator= new SequentialSolutionListEvaluator<DoubleSolution>();
		    population = evaluator.evaluate(population, problem);
		    RankingAndCrowdingSelection<DoubleSolution> rankingAndCrowdingSelection ;
		    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<DoubleSolution>(1000);
		    List<DoubleSolution> pop =rankingAndCrowdingSelection.execute(population) ;
		    return pop;
		  }
	
	public static void main(String args){
		createInitialPopulation();
		
		
	}

}
