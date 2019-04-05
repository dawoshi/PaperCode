package leetcode;
import java.util.HashMap;

public class MaxLength {
	public int maxlength(int arr[], int k){
		if(arr.length == 0 || arr == null){
			return 0;
		}
		int maxlen = 0;
		int sum = 0;
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		map.put(0, -1);
		for(int i = 0;i<arr.length; i++){
			sum += arr[i];
			if(map.containsKey(sum-k)){
				maxlen = Math.max(maxlen, i-map.get(sum-k));
			}else{
				map.put(sum-k, i);
			}
		}
		return maxlen;
	}
	
	public int preOrder(Node head, int sum, int presum, int level, int maxlen, HashMap<Integer,Integer> map){
		if(head == null){
			return maxlen;
		}
		
		int cursum = presum + head.value;
		if(!map.containsKey(cursum)){
			map.put(cursum,level);
		}
		if(map.containsKey(cursum)){
			maxlen = Math.max(maxlen,level-map.get(cursum-sum));
		}
		maxlen = preOrder(head.left,sum,cursum,level+1,maxlen,map);
		maxlen = preOrder(head.right,sum,cursum,level+1,maxlen,map);
		if(level == map.get(cursum)){
			map.remove(cursum);
		}
		return maxlen;
	
	}

	public Node posOrder(Node head, int[] record){
		if(head == null){
			record[0] = 0;
			record[1] = Integer.MAX_VALUE;
			record[2] = Integer.MIN_VALUE;
		}
		int value = head.value;
		Node left = head.left;
		Node right = head.right;
		Node lBST = posOrder(left,record);
		int lSize = record[0];
		int lMax = record[1];
		int lMin = record[2];
		Node rBST = posOrder(right,record);
		int rSize = record[0];
		int rMax =  record[1];
		int rMin = record[2];
		record[1] = Math.max(value,rMax);
		record[2] = Math.min(value,lMin);
		if(left == lBST && right == rBST && value>lMax && value<rMin){
			record[0] = rSize+lSize+1;
			return head;
		}
		record[0] = Math.max(rSize,lSize);
		return lSize>rSize?lBST:rBST;
	}
	public int bstTopoSize(Node head){
 		if(head == null){
			return 0;
		}
		int max = maxTop(head,head);
		max = Math.max(max, bstTopoSize(head.left));
		max = Math.max(max, bstTopoSize(head.right));
		return max;
	}
	public int maxTop(Node h,Node n){
		if(h !=null && n != null && isBSTNode(h,n,n.value)){
			return maxTop(h,n.left)+maxTop(h,n.right)+1;
		}
		return 0;
		
	}
	public boolean isBSTNode(Node h,Node n,int value){
		if(h == null){
			return false;
		}
		if(h==n){
			return true;
		}
		return isBSTNode(h.value>value? h.left:h.right,n,value);
		
		
	}
	
	
	
}
