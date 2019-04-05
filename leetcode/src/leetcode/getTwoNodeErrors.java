package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class getTwoNodeErrors {

	public Node[] getNodeErrors(Node head){
		Node[] errors =  new Node[2];
		if(head == null){
			return null;
		}
		Queue<Node> queue = new LinkedList<Node>();
		Node pre = null;
		while(!queue.isEmpty()&&head != null){
			if(head !=null){
				queue.offer(head);
				head = head.left;
			}
			else{
				head = queue.poll();
				if(pre!=null && pre.value>head.value){
					errors[0] = errors[0] == null? pre:errors[0];
					errors[1] = head;
				}
				pre = head;
				head = head.right;
			}
		}
		return errors;
	}
}
