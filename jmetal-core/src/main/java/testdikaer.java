import java.util.ArrayList;
import java.util.List;


public class testdikaer {
	public static void main(String args[]){
		List<List<Double>> res = new ArrayList<List<Double>>();
		List<Double> a1 = new ArrayList<>();
		a1.add(1.0);
		a1.add(2.0);
		List<Double> a2 = new ArrayList<>();
		a2.add(3.0);
		List<Double> a3 = new ArrayList<>();
		a3.add(4.0);
		res.add(a1);
		res.add(a2);
		res.add(a3);
		List<List<Double>> ant = new ArrayList<List<Double>>();
		List<Double> list = new ArrayList<Double>();
	    recursive (res,ant,0,list);
	    System.out.println("size"+ant.size());
	    for(int i=0;i<ant.size();i++){
	    	for(int j=0;j<ant.get(i).size();j++){
	    		System.out.print(ant.get(i).get(j)+", ");
	    	}
	    	System.out.println();
	    }
	}
    private static void recursive (List<List<Double>> dimValue, List<List<Double>> result, int layer, List<Double> curList) {  
        if (layer < dimValue.size() - 1) {  
            if (dimValue.get(layer).size() == 0) {  
                recursive(dimValue, result, layer + 1, curList);  
            } else {  
                for (int i = 0; i < dimValue.get(layer).size(); i++) {  
                    List<Double> list = new ArrayList<Double>(curList);  
                    list.add(dimValue.get(layer).get(i));  
                    recursive(dimValue, result, layer + 1, list);  
                }  
            }  
        } else if (layer == dimValue.size() - 1) {  
            if (dimValue.get(layer).size() == 0) {  
                result.add(curList);  
            } else {  
                for (int i = 0; i < dimValue.get(layer).size(); i++) {  
                    List<Double> list = new ArrayList<Double>(curList);  
                    list.add(dimValue.get(layer).get(i));  
                    result.add(list);  
                }  
            }  
        }  
    }  
}