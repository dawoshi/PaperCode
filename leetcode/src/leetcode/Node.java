package leetcode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Node {
	public int value;
	public Node left;
	public Node right;
	public Node(int value){
		this.value = value;
	}
	public void preOrderRcur(Node head){
		if(head == null){
			return;
		}
		System.out.println(head.value+" ");
		preOrderRcur(head.left);
		preOrderRcur(head.right);
	}
	public void inOrderRecur(Node head){
		if(head == null){
			return;
		}
		inOrderRecur(head.left);
		System.out.println(head.value+" ");
		inOrderRecur(head.right);
	}

//	public void preOrderUnRecur(Node head){
//		if(head != null){
//			Stack<Node> stack = new Stack<Node>();
//			stack.add(head);
//			while(stack.isEmpty()){
//				head = stack.pop();
//				System.out.println(head.value);
//				if(head.right != null){
//					stack.push(head.right);
//				}
//				if(head.left != null){
//					stack.push(head.left);
//				}
//			}
//					
//		}
//		
//	}
	public void preOrderUnRecur(Node head){
		if(head != null){
			Stack<Node> stack = new Stack<Node>();
			stack.add(head);
			while(!stack.isEmpty()){
				head = stack.pop();
				System.out.println(head.value);
				if(head.right != null){
					stack.push(head.right);
				}
				if(head.left != null){
					stack.push(head.left);
				}
			}
		}
	}
	
	public void inOrderUnRecur2(Node head){
		if(head != null){
			Stack<Node> stack = new Stack<Node>();
			while(!stack.isEmpty()|| head != null){
				if(head != null){
					stack.add(head);
					head = head.left;
				}
				else{
					head = stack.pop();
					System.out.println(head.value+" ");
					head = head .right;
				}
			}
		}
	}
	
	public void inOrderUnRecur(Node head){
			if(head != null){
				Stack<Node> stack = new Stack<Node>();
				while(!stack.isEmpty() || head != null){
					if(head != null){
						stack.add(head);
						head = head.left;
					}else{
						head = stack.pop();
						System.out.println(head.value+" ");
						head.right = head;
					}
				}
			}	
	}
	
	
	public void posOrderUnRecur(Node head){
		if(head != null){
			Stack<Node> stack = new Stack<Node>();
			stack.add(head);
			Node top = null;
			while(!stack.isEmpty()){
				top = stack.peek();
				if(top.left != null && top.left != head && top.right != head){
					stack.add(top.left);
				}
				else if(top.right != null && top.right != null){
					stack.add(top.right);
				}else{
					System.out.println(stack.pop().value);
					head = top;
				}
			}
			
		}
	}
	
	
	public void posOrderRecur(Node head){
		if(head != null){
			Stack<Node> st1 = new Stack<Node>();
			Stack<Node> st2 = new Stack<Node>();
			st1.push(head);
			while(!st1.isEmpty()){
				head = st1.pop();
				st2.push(head);
				if(head.left != null){
					st1.add(head.left);
				}
				if(head.right != null){
					st1.add(head.right);
				}
			}
			while(!st2.isEmpty()){
				System.out.println(st2.pop().value + " ");
			}
		}
		
	}
	
	
	public int getHeight(Node node,int l){
		if(node == null){
			return l;
		}
		return Math.max(getHeight(node.left,l+1), getHeight(node.right,l+1));
		
	}
	
	
	public void posOrderRecur2(Node head){
		if(head != null){
			Stack<Node> stack = new Stack<Node>();
			stack.add(head);
			Node top = null;
			while(!stack.isEmpty()){
				 top = stack.peek();
				 if(top.left != null && top.left != head && top.right != head){
					 stack.add(top.left);
				 }
				 else if(top.right != null && top.right != head){
					 stack.add(top.right);
				 }
				 else{
					 System.out.println(stack.pop().value+" ");
					 head = top;
				 }
			}
			
		}
		
	}
	public String serialByPre(Node head){
		if(head == null){
			return "#!";
		}
		String res = head.value+"!";
		res += serialByPre(head.left);
		res += serialByPre(head.right);
		return res;
	}
	
	public Node recoByPreString(String preStr){
		String[] str = preStr.split("!");
		Queue<String> queue= new LinkedList<String>();
		for(int i = 0; i != str.length; i++){
			queue.offer(str[i]);
		}
		return reconPreOrder(queue);
		
	}
	
	public Node reconPreOrder2(Queue<String> queue){
		String value = queue.poll();
		if(value.equals("#!")){
			return null;
		}
		Node head = new Node(Integer.valueOf(value));
		head.left = reconPreOrder(queue);
		head.right = reconPreOrder(queue);
		return head;
	}

	public Node reconByPreString(String preStr){
		String[] str = preStr.split("!");
		Queue<String> queue = new LinkedList<String>();
		for(int i = 0; i != str.length; i++){
			queue.offer(str[i]);
		}
		return reconPreOrder(queue);
	}
	
	public Node reconPreOrder(Queue<String> queue){
		String value = queue.poll();
		if(value.equals("#!")){
			return null;
		}
		Node head = new Node(Integer.valueOf(value));
		head.left = reconPreOrder(queue);
		head.right = reconPreOrder(queue);
		return head;
	}
	
	public String seraialByLevel(Node head){
		if(head == null){
			return "#!";
		}
		Queue<Node> queue = new LinkedList<Node>();
		String res = head.value+"!";
		queue.offer(head);
		while(!queue.isEmpty()){
			head = queue.poll();
			if(head.left != null){
				queue.offer(head.left);
				res+=head.left.value+"!";
			}
			else{
				res+=head.left.value+"!";
			}
			if(head.right != null){
				queue.offer(head.right);
				res+=head.right+"!";
			}
			else{
				res+="#!";
			}
		}
		return res;	
	}
	
	public Node reconByLevelString(String levelStr){
		String[] str= levelStr.split("!");
		int index = 0;
		Node head = generateNodeByString(str[index++]);
		Queue<Node> queue = new LinkedList<Node>();
		if(head != null){
			queue.offer(head);
		}
		Node node = null;
		while(!queue.isEmpty()){
			node = queue.poll();
			node.left = generateNodeByString(str[index++]);
			node.right = generateNodeByString(str[index++]);
			if(node.left != null){
				queue.offer(node.left);
			}
			if(node.right != null){
				queue.offer(node.right);
			}
			
		}
		return head;
		
		
	}
	
	public void morrisIn2(Node head){
		if(head == null){
			return;
		}
		Node cur1 = head;
		Node cur2 = null;
		while(cur1 != null){
			cur2 = cur1.left;
			if(cur2 != null){
				while(cur2.right != null && cur2.right !=cur1){
					cur2 = cur2.right;
				}
				if(cur2.right == null){
					cur2.right = cur1;
					cur1 = cur1.left;
				}
				else{
					cur2.right = null;
				}
			}
		}
		System.out.println(cur1.value+" ");
		cur1 = cur1.right;
	}
	
	
	
	
	
	
	public Node generateNodeByString(String str){
		if(str.equals("#!")){
			return null;
		}
		else{
			return new Node(Integer.valueOf(str));
		}
		
	}
	public void morrisIn(Node head){
		if(head == null){
			return;
		}
		Node cur1 = head;
		Node cur2 = null;
		while(cur1 != null){
			cur2 = cur1.left;
			if(cur2 != null){
				while(cur2.right != null && cur2.right != cur1){
					cur2 = cur2.right;
				}
				if(cur2.right == null){
					cur2.right = cur1;
					cur1 = cur1.left;
					continue;
				}
				else{
					cur2.right = null;
				}
				
			}
			System.out.println(cur1.value+" ");
			cur1 = cur1.right;
		}
	}
	

}
