package leetcode;

public class jumpTest {
	
	public int jump(int[] array){
		if(array == null || array.length == 0){
			return 0;
		}
		int cur = 0;
		int next = 0;
		int jump = 0;
		for(int i = 0; i<array.length;i++){
			if(cur<i){
				jump++;
				cur = next;
			}
			next = Math.max(next,i+array[i]);
		}
		return jump;
	}
	public static void main(String args[]){
		int[] arr ={3,2,3,1,1,4};
		jumpTest test = new jumpTest();
		System.out.println(test.jump(arr));		
	}

}
