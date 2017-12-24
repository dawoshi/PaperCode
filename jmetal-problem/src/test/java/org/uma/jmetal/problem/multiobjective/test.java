package org.uma.jmetal.problem.multiobjective;

import java.util.ArrayList;
import java.util.List;

public class test {
	public static void main(String args[]){
		List<List<Double>> list = new ArrayList<List<Double>>();
		List<Double> a1 = new ArrayList<Double>();
		List<Double> a2 = new ArrayList<Double>();
		List<Double> a3 = new ArrayList<Double>();
		a1.add(1.0);
		a1.add(0.0);
		a2.add(2.0);
		a3.add(0.0);
		a3.add(3.0);
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list = recursive(list);
		for(int i =0;i<list.size();i++){
			for(int j = 0;j<list.get(i).size();j++){
				System.out.print(list.get(i).get(j)+", ");
			}
			System.out.println();
		}
		
	}
	
	 private static List<List<Double>> recursive (List<List<Double>> dimValue) {  
	        int total = 1;  
	        for (List<Double> list : dimValue) {  
	            total *= list.size();  
	        }  
	        List<List<Double>> myResult = new ArrayList<List<Double>>();
	        int itemLoopNum = 1;  
	        int loopPerItem = 1;  
	        int now = 1;  
	        for (List<Double> list : dimValue) {  
	            now *= list.size();  
	  
	            int index = 0;  
	            int currentSize = list.size();  
	  
	            itemLoopNum = total / now;  
	            loopPerItem = total / (itemLoopNum * currentSize);  
	            int myIndex = 0;  
	            for (int t = 0;t<list.size();t++) {  
	                for (int i = 0; i < loopPerItem; i++) {
	                    if (myIndex == list.size()) {  
	                        myIndex = 0;  
	                    }  
	                    for (int j = 0; j < itemLoopNum; j++) {
	                    	if(index>myResult.size()-1){
	                    		List<Double> tmp = new ArrayList<Double>();
	                    		tmp.add(list.get(myIndex));
	                    		myResult.add(tmp);
	                    	}else{
	                    		myResult.get(index).add(list.get(myIndex));
	                    	}
	                        index++;  
	                    }  
	                    myIndex++;  
	                }  
	            }  
	        }  
	        return myResult;
	    }	

}
