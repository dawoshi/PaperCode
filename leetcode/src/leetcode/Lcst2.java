package leetcode;

public class Lcst2 {
	
	public String lcst(String str1,String str2){

		if(str1 == null || str2 == null || str1.equals("") || str2.equals("")){
			return "";
		}
		char[] chs1 = str1.toCharArray();
		char[] chs2 = str2.toCharArray();
		int row = 0;
		int col = chs2.length-1;
		int max = 0;
		int end = 0;
		while(row<chs1.length){
			int i = row;
			int j = col;
			int len = 0;
			while(i<chs1.length && j<chs2.length){
				if(chs1[i]!=chs2[j]){
					len = 0;
				}else{
					len++;
				}
				if(len>max)
				{
					end = i;
					max = len;
				}
				i++;
				j++;
		     }
			if(col>0){
				col--;
			}else{
				row++;
			}	
	}
		return str1.substring(end-max+1,end+1);
  }
	public static void main(String args[]){
		Lcst2 lcst = new Lcst2();
		String str1 = "A1234B";
		String str2 = "CD1234";
		LongestSubstr test = new LongestSubstr();
		System.out.println(lcst.lcst(str1, str2));
	}
		

}
