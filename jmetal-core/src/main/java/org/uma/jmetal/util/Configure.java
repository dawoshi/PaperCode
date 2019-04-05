package org.uma.jmetal.util;

public class Configure {
	 public static String referenceParetoFrontPath;
	 public static String problem;
	 public static String indicationPath;
	 public static String getIndicationPath() {
		return indicationPath;
	}
	public static void setIndicationPath(String indicationPath) {
		Configure.indicationPath = indicationPath;
	}
	public static String getReferenceParetoFrontPath(){
		 return referenceParetoFrontPath;
	 }
	 public static void setReferenceParetoFrontPath(String referenceParetoFrontPath){
		Configure.referenceParetoFrontPath=referenceParetoFrontPath;
	 }
	 
	 public static String getproblem(){
		 return problem;
	 }
	 public static void setproblem(String problem){
		Configure.problem=problem;
	 }
	 
}
