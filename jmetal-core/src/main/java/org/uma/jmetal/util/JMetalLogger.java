package org.uma.jmetal.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class provides some facilities to manage loggers. One might use the
 * static logger of this class or use its own, custom logger. Also, we provide
 * the static method {@link #configureLoggers(File)} for configuring the loggers
 * easily. This method is automatically called before any use of the static
 * logger, but if you want it to apply on other loggers it is preferable to call
 * it explicitly at the beginning of your main() method.
 * 
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 */
@SuppressWarnings("serial")
public class JMetalLogger implements Serializable {

	public static final Logger logger = Logger.getLogger(JMetalLogger.class
			.getName());

	static {
		/*
		 * Configure the loggers with the default configuration. If the
		 * configuration method is called manually, this default configuration
		 * will be called before anyway, leading to 2 configuration calls,
		 * although only the last one is considered. This is a trade off to
		 * ensure that, if this method is not called manually, then it is still
		 * called automatically and as soon as we use jMetalLogger, in order to
		 * have at least the default configuration.
		 */
		try {
			configureLoggers(null);
		} catch (IOException e) {
			throw new RuntimeException(
					"Impossible to configure the loggers in a static way", e);
		}
	}

	/**
	 * This method provides a single-call method to configure the {@link Logger}
	 * instances. A default configuration is considered, enriched with a custom
	 * property file for more convenient logging. The custom file is considered
	 * after the default configuration, so it can override it if necessary. The
	 * custom file might be provided as an argument of this method, otherwise we
	 * look for a file named "jMetal.log.ini". If no custom file is provided,
	 * then only the default configuration is considered.
	 * 
	 * @param propertyFile
	 *            the property file to use for custom configuration,
	 *            <code>null</code> to use only the default configuration
	 * @throws IOException
	 */
	public static void configureLoggers(File propertyFile) throws IOException {
		// Prepare default configuration
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);
		printer.println(".level = INFO");
		printer.println("handlers = java.util.logging.FileHandler, java.util.logging.ConsoleHandler");
		printer.println("formatters = java.util.logging.SimpleFormatter");
		printer.println("java.util.logging.SimpleFormatter.format = %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$s: %5$s [%2$s]%6$s%n");

		printer.println("java.util.logging.FileHandler.pattern = jMetal.log");
		printer.println("java.util.logging.FileHandler.level = ALL");

		printer.println("java.util.logging.ConsoleHandler.level = ALL");

		// Retrieve custom configuration
		File defaultFile = new File("jMetal.log.ini");
		if (propertyFile != null) {
			printer.println(FileUtils.readFileToString(propertyFile));
		} else if (defaultFile.exists()) {
			printer.println(FileUtils.readFileToString(defaultFile));
		} else {
			// use only default configuration
		}
		printer.close();

		// Apply configuration
		LogManager manager = LogManager.getLogManager();
		manager.readConfiguration(IOUtils.toInputStream(new String(stream
				.toByteArray(), Charset.forName("UTF-8"))));
		logger.info("Loggers configured with " + propertyFile);
	}
	
	 /**
	   * 
	   * 获取每代的信息
	   * 
	   * @param pop 种群
	   * @param paretoFrontFile  真实pareto解集
	   * @throws Exception
	   */
	  public static void printLog(List<DoubleSolution> pop, String paretoFrontFile,String indicationPath,String number) throws Exception{
		  System.out.println("pop.get(0).getNumberOfObjectives():"+pop.get(0).getNumberOfObjectives());
		    Front referenceFront = new ArrayFront(paretoFrontFile);
		    //FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront) ;
		    //Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
		    //Front normalizedFront = frontNormalizer.normalize(new ArrayFront(pop)) ;
		    //List<PointSolution> normalizedPopulation = FrontUtils
		     //  .convertFrontToSolutionList(normalizedFront) ;
		    String outputString = "\n" ;
//		    double hypervolumen = new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "Hypervolume (N) : " +hypervolumen + "\n";
		    
		    double hypervolume = new PISAHypervolume<DoubleSolution>(referenceFront).evaluate(pop);
		    outputString += "Hypervolume     : " + hypervolume + "\n";
		    
//		    double epsilonn = new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "Epsilon (N)     : " + epsilonn + "\n" ;
		    
//		    double epsilon = new Epsilon<DoubleSolution>(referenceFront).evaluate(pop);
//		    outputString += "Epsilon         : " + epsilon  + "\n" ;
		    
//		    double gdn= new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "GD (N)          : " + gdn + "\n";
		    
		    double gd =  new GenerationalDistance<DoubleSolution>(referenceFront).evaluate(pop);
		    outputString += "GD              : " + gd  + "\n";
		    
//		    double igdn = new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "IGD (N)         : " + igdn  + "\n";
		    
		    double igd = new InvertedGenerationalDistance<DoubleSolution>(referenceFront).evaluate(pop);
		    outputString +="IGD             : " + igd + "\n";
		    
//		    double igdnplus = new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "IGD+ (N)        : " + igdnplus  + "\n";
		    
		    double igdplus = new InvertedGenerationalDistancePlus<DoubleSolution>(referenceFront).evaluate(pop);
		    outputString += "IGD+            : " + igdplus  + "\n";
		    
//		    double spread  =   new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
//		    outputString += "Spread (N)      : " + spread  + "\n";
		    
		    double spread = new Spread<DoubleSolution>(referenceFront).evaluate(pop);
		    outputString += "Spread          : " + spread  + "\n";
//		    outputString += "R2 (N)          : " +
//		        new R2<List<DoubleSolution>>(normalizedReferenceFront).runAlgorithm(normalizedPopulation) + "\n";
//		    outputString += "R2              : " +
//		        new R2<List<? extends Solution<?>>>(referenceFront).runAlgorithm(population) + "\n";
		    outputString += "Error ratio     : " +
		        new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(pop) + "\n";
		    
		    /**
		     * 输出到日志
		     */
		    JMetalLogger.logger.info(outputString);
		    
		    
		    /**
		     * 输出到文件
		     */
		    try {                                                                        
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件      
	             FileWriter wt = new FileWriter(number+indicationPath, true);                      
	             //wt.write(hypervolumen+","+hypervolume+","+epsilonn+","+epsilon+","+gdn+","+gd+","+igdn+","+igd+","+igdnplus+","+igdplus+","+spread+","+spreadre+"\n");
	             wt.write(hypervolume+","+gd+","+igd+","+spread+"\n");
	             wt.close();                                                  
	         } catch (IOException e) {                                                   
	             e.printStackTrace();                                                    
	         }    
	  }
}
