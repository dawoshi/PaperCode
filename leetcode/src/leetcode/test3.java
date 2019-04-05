package leetcode;

public class test3 {
	
	public int getWinScore(int[] arr){
		if(arr == null || arr.length == 0 ){
			return 0;
		}
		return Math.max(f(arr,0,arr.length-1), s(arr,0,arr.length-1));
		
	}
	public int f(int[] arr, int i, int j){
		if(i==j){
			return arr[i];
		}
		return Math.max(arr[i]+s(arr,i+1,j),arr[j]+s(arr,i,j-1));
	}
	public int s(int[] arr, int i, int j){
		if(i == j){
			return 0;
		}
		return Math.min(f(arr,i+1,j),f(arr,i,j-1));
	}
	public static void main(String args[]){
		int[] arr={1,2,100,4};
		test3 test = new test3();
		System.out.println(test.getWinScore(arr));
		
	}

}
