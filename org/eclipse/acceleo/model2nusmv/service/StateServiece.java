package org.eclipse.acceleo.model2nusmv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.acceleo.model2nusmv.tricks.MiddleState;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.Expression;
import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.State;
import com.formaltech.smave.metamodel.smave.Transition;
import com.formaltech.smave.metamodel.smave.Variable;

public class StateServiece {
	/**
	 * 一个node，获得它的各个状态
	 * 并转换为MiddleState类元素,返回一个list
	 * @param operator
	 */
	public ArrayList<MiddleState> Get_MiddleStates(Operator operator){
		 
		ArrayList<MiddleState> MiddleStates = new ArrayList<>();
		
		Iterator<EObject> it = operator.eAllContents();
		while(it.hasNext()){
			EObject eObject = it.next();
			if (eObject instanceof State){
				State state = (State)eObject;
				MiddleState mState = new MiddleState();
				
				mState.setStateName(state.getName());
				mState.setInit(state.isInitial());
				
				
				/**
				 * 这里得到当前状态的实例。状态是由输出变量决定的，根据模型得到输出变量的取值
				 */
				HashMap varmap = new HashMap<>();//得到处在哪个状态，状态由变量的取值决定
				ArrayList<String[]> allEquationsInThisState = new EquationService().Get_All_Equations_In_States(operator, state);
				ArrayList<String[]> varvalue = new EquationService().Delete_local_variables(operator, allEquationsInThisState);
						
				
				for(int i=0;i<varvalue.size();i++){
					String[] equation = varvalue.get(i);
					varmap.put(equation[0],equation[1]);
				}
				mState.setVarmap(varmap);
				
				
				MiddleStates.add(mState);
			}	
		}
		return MiddleStates;
	}
	
	/**
	 * 得到完整的中间状态
	 * @param operator
	 * @param middlestates
	 * @return
	 */
	public ArrayList<MiddleState> Set_Transitions_Of_State(Operator operator,ArrayList<MiddleState> middlestates){
		ArrayList<MiddleState> MiddleStates = new ArrayList<>();
		List<Variable> outputlist = new VariablesService().Get_output_Variables(operator);
		Iterator<EObject> it = operator.eAllContents();
		while(it.hasNext()){
			EObject eObject = it.next();
			if (eObject instanceof State){
				State state = (State)eObject;
			/**
			 * 这里得到条件及目标状态,这里的目标状态
			 */
				
				MiddleState mstate = Find_MiddleState_with_StateName(state.getName(), middlestates);
				ArrayList<HashMap> transitions = new ArrayList<>();
				
				HashMap vartrans = null;
				
				for(Variable v : outputlist){
					vartrans = new HashMap<>();
					//确定condition,由此使的输出变量取值与转移条件确定
					
					String condition_outputVar = "("+v.getName()+" = "+mstate.getVarmap().get(v.getName())+")";
					String condition_inputVar = "";
					List<Transition> trans = state.getTransition();
					for(int i=0;i<trans.size();i++){
						Transition t = trans.get(i);
						State targetstate = t.getTarget();
						Expression condition = t.getCondition();	
						String exp = new EquationService().Get_Expression(condition);
						MiddleState targetmiddlestate = Find_MiddleState_with_StateName(targetstate.getName(), middlestates);
						String vartarget = targetmiddlestate.getVarmap().get(v.getName()).toString();
						
						condition_inputVar += "("+exp+")";
						String cond = condition_outputVar +" & "+ condition_inputVar;
						vartrans.put(cond, vartarget);
					}
				}
			
			}
		
		}
		
		
		return MiddleStates;
	}
	
	
	/**
	 * 根据状态名来寻找状态
	 * @param StateName
	 * @param middlestates
	 * @return
	 */
	public MiddleState Find_MiddleState_with_StateName(String StateName, ArrayList<MiddleState> middlestates){
		
		for(MiddleState mstate : middlestates){
			if(mstate.getStateName().equals(StateName))
				return mstate;
		}
		
		return null;
		
		
	}
	
}
