package org.uma.jmetal.utility;

import java.util.ArrayList;

public class Orthogonal{
	private static int Q; //水平数
	private static int F; //维度数
	private static int basecolumns; //基本列
	private static int columns;  //最终列数
	
	public static int [][] getOrthogoanlTable(){
		int J; //基本列
		for(J = 0;;J++){
			if(Math.pow(getQ(), J)/(getQ()-1)>=getF()){
				setBasecolumns(J);
				break;
			}
			
		}
		int [][] res = new int[(int)Math.pow(getQ(), getBasecolumns())+1][(int)Math.pow(getQ(), getBasecolumns())+1];
		
		if((int)Math.pow(getQ(),getBasecolumns()) == getF()){
			setColumns(getF());
		}else{
			setColumns((int)((Math.pow(getQ(), getBasecolumns())-1)/(getQ()-1)));
		}
		
		for(int k = 1;k<=getBasecolumns();k++){//基本列
			int j =(int) Math.pow(getQ(), (k-1))/(getQ()-1)+1;
			for(int i = 1;i <= Math.pow(getQ(),getBasecolumns());i++){ //基本行
				res[i][j] = (int)Math.abs((i-1)/(Math.pow(getQ(), getBasecolumns()-k)))%getQ();
				
			}
		}
		for(int k = 2;k<=getBasecolumns();k++){
			int j =(int)((Math.pow(getQ(), k-1)-1)/(getQ()-1)+1);
			for(int s= 1;s<=j-1;s++){
				for(int t = 1;t<=getQ()-1;t++){
					for(int i = 1;i<=Math.pow(getQ(),getBasecolumns());i++){
					     res[i][j+(s-1)*(getQ()-1)+t] = (res[i][s]*t+res[i][j])%getQ();
					}
				}
			}
		}
		for(int j =1;j<= getF();j++){
			for(int i = 1;i<=Math.pow(getQ(), getBasecolumns());i++){
				res[i][j]+=1;
			}
		}
		int [][] tmp = new int[(int)Math.pow(getQ(),getBasecolumns())][getF()];
		for(int i =1;i<=Math.pow(getQ(), getBasecolumns());i++){
			for(int j = 1;j<=getF();j++){
				tmp[i-1][j-1] = res[i][j];
			}
		}
		return tmp;	
	}
	public static void main(String args[]){
		Orthogonal tmp = new Orthogonal();
		tmp.setF(2);
		tmp.setQ(1);
		int [][] a= tmp.getOrthogoanlTable();
		for(int i =0;i<a.length;i++){
			for(int j = 0;j<a[0].length;j++){
				System.out.print(a[i][j]+",");
			}
			System.out.println();
		}
	}

	public static int getQ() {
		return Q;
	}

	public static void setQ(int q) {
		if(q < 2){
			Q = 2;
		}else{
		    Q = q;
		}
	}

	public static int getF() {
		return F;
	}

	public static void setF(int f) {
		F = f;
	}

	public static int getColumns() {
		return columns;
	}

	public static void setColumns(int columns) {
		Orthogonal.columns = columns;
	}

	public static int getBasecolumns() {
		return basecolumns;
	}

	public static void setBasecolumns(int basecolumns) {
		Orthogonal.basecolumns = basecolumns;
	}
	
	
	

}
