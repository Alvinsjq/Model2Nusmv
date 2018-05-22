package org.eclipse.acceleo.model2nusmv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.acceleo.model2nusmv.tricks.MiddleState;

import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.Variable;

public class AssignService {
	/**
	 * 获得Node中的Assign里的init语句
	 * @param operator
	 * @return
	 */
	public ArrayList<String> Get_Node_Init(Operator operator){
		ArrayList<String> init = new ArrayList<String>();
		ArrayList<MiddleState> middlestates = new StateServiece().Get_MiddleStates(operator);
		List<Variable> outputlist = new VariablesService().Get_output_Variables(operator);
		for(int i=0;i<middlestates.size();i++){
			MiddleState state = middlestates.get(i);
			if(state.isInit){
				HashMap varvalue = state.getVarmap();
				for(Variable output : outputlist){
					String s ="init("+output.getName()+") := ";
					s += varvalue.get(output.getName()).toString();
					s += " ;";
					init.add(s);
				}
			}
		}
		return init;
	}
	
	
	/**
	 * 获取Node中的Assign里的next语句
	 * @param operator
	 * @return
	 */
	public ArrayList<String> Get_Node_Next(Operator operator){
		ArrayList<String> next = new ArrayList<String>();
		ArrayList<MiddleState> middlestates = new StateServiece().Get_MiddleStates(operator);
		List<Variable> outputlist = new VariablesService().Get_output_Variables(operator);
		for(int i=0;i<outputlist.size();i++){
			
			String nextsentence = "next("+outputlist.get(i).getName()+") := \n"
					+ "\t\t case\n";
			
			for(int j=0;j<middlestates.size();j++){
				if(!middlestates.get(i).isInit){
					
					
				
				
				
				}
			}
			
			nextsentence += "\t\t\t TRUE :"+outputlist.get(i).getName()+" ;\n";
			nextsentence += "\t\t esac;\n";
			next.add(nextsentence);
		}
		
		
		
		
		
		return next;
	}

}
