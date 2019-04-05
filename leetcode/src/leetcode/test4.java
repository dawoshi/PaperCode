package leetcode;

public class test4 {
	public int getWinScore(int[] arr){
		if(arr == null || arr.length == 0){
			return 0;
		}
		int[][] f= new int[arr.length][arr.length];
		int[][] s= new int[arr.length][arr.length];
		for(int j = 0; j<arr.length; j++){
			f[j][j] = arr[j];
			for(int i = j-1; i>=0; i--){
				f[i][j] = Math.max(arr[i]+s[i+1][j], arr[j]+s[i][j-1]);
				s[i][j] = Math.min(f[i+1][j],f[i][j-1]);
			}
		}
		return Math.max(f[0][arr.length-1], s[0][arr.length-1]);
	}
	public static void main(String args[]){
		test4 test = new test4();
		int[] arr={1,2,100,4};
		System.out.println(test.getWinScore(arr));
	}

}
