import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class test {
	
	
	public static void main(String args[]){
		List<List<Double>> dim = new ArrayList<List<Double>>();
		List<List<Double>> res = new ArrayList<List<Double>>();
		List<Double> a1 = new ArrayList<Double>();
		List<Double> a2 = new ArrayList<Double>();
		List<Double> a3 = new ArrayList<Double>();
		a1.add(1.0);
		a1.add(2.0);
		
		dim.add(a1);
//		dim.add(a2);
//		dim.add(a3);
		
		System.out.println("starting:");
		res = circulate(dim);
		
		for(int i =0;i<res.size();i++){
			for(int j =0;j<res.get(i).size();j++){
				System.out.print(res.get(i).get(j)+",");
			}
			System.out.println();
		}	
	}
	
	   private static List<List<Double>> circulate (List<List<Double>> dimValue) {  
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
	            for (Double string : list) {  
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
