package leetcode;

public class minCost{
	
	public int mincost(String str1,String str2,int ic, int dc,int rc){
		if(str1 == null || str2 == null){
			return 0;
		}
		int m = str1.length()+1, n = str2.length()+1;
		int[][] dp=new int[m][n];
		for(int i=1; i<m; i++){
			dp[i][0] = dc*i; 
		}
		for(int j = 1; j<n; j++){
			dp[0][j] = ic*j;
		}
		for(int i = 1; i<m; i++){
			for(int j= 1; j<n; j++){
				if(str1.charAt(i-1) == str2.charAt(j-1)){
					dp[i][j] = dp[i-1][j-1];
				}
				else{
					dp[i][j] = dp[i-1][j-1]+rc;
				}
				dp[i][j]=Math.min(dp[i][j], dp[i-1][j]+dc);
				dp[i][j] = Math.min(dp[i][j],dp[i][j-1]+ic);
				
			}
		}
		return dp[m-1][n-1];
	}

}
