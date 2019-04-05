package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class print {
	
	public void print(Node head){
		if(head == null){
			return;
		}
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(head);
		int level = 1;
		Node last = head;
		Node nlast = null;
		System.out.println("level:"+(level++)+":");
		while(!queue.isEmpty()){
			head = queue.poll();
			System.out.println(head.value);
			if(head.left != null){
				queue.offer(head.left);
				nlast = head.left;
			}
			if(head.right != null){
				queue.offer(head.right);
				nlast = head.right;
			}
			if(head == last && !queue.isEmpty()){
				System.out.println("\nlevel:"+(level++)+":");
				last = nlast;
			}
		}
		System.out.println();
	}

}
