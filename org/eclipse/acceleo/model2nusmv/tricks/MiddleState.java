package org.eclipse.acceleo.model2nusmv.tricks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *	主要用来装载解析的state
 * @author Alvin Is
 *
 */
public class MiddleState {
	public String StateName;
	public boolean isInit;
	public HashMap varmap;
//	public HashMap targetState;//<condition,var1>
//	public HashMap targetAction;//<condition,var2>
	public ArrayList<HashMap> transitions;  //这个list是按输出变量来排的，第一个元素是第一个输出变量中所包含的转移条件和目标状态
	
	public MiddleState(){
		varmap = new HashMap<>();
		ArrayList<HashMap> transitions = new ArrayList<>();
	}


	public String getStateName() {
		return StateName;
	}


	public void setStateName(String stateName) {
		StateName = stateName;
	}


	public boolean isInit() {
		return isInit;
	}


	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}


	public HashMap getVarmap() {
		return varmap;
	}


	public void setVarmap(HashMap varmap) {
		this.varmap = varmap;
	}


	public ArrayList<HashMap> getTransitions() {
		return transitions;
	}


	public void setTransitions(ArrayList<HashMap> transitions) {
		this.transitions = transitions;
	}



	
	
		
}
	