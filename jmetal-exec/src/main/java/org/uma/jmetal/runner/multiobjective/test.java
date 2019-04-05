package org.uma.jmetal.runner.multiobjective;

import java.io.File;
import java.io.IOException;

public class test {
	public static void main(String args[]){
		   File directory = new File("");// 参数为空
		    String courseFile = null;
			try {
				courseFile = directory.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println("coursefile:"+courseFile);
	}

}
