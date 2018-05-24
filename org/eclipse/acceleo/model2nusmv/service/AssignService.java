package org.eclipse.acceleo.model2nusmv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.acceleo.model2nusmv.tricks.MapsortByKey;
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
		StateServiece stateservice = new StateServiece();
		ArrayList<MiddleState> _middlestates = stateservice.Get_MiddleStates(operator);
		List<Variable> outputlist = new VariablesService().Get_output_Variables(operator);
		ArrayList<MiddleState> middlestates = stateservice.Set_Transitions_Of_State(operator, _middlestates);
		
		
		for(int i=0;i<outputlist.size();i++){
			String nextsentence = "next("+outputlist.get(i).getName()+") := \n"
					+ "\t\t\t case\n";
			//按照状态来遍历条件和目标状态
			
			for(int j=0;j<middlestates.size();j++){
				MiddleState mstate = middlestates.get(j);
//				if(mstate.isInit){
					//拿到设置好的状态转移list，并选取对应输出变量序号的hashmap
					ArrayList<HashMap> transitions = mstate.getTransitions();
					HashMap vartrans = transitions.get(i);
					Map resultMap = new MapsortByKey().sortMapByKey(vartrans);
					
					//遍历hashmap
					Iterator iter = resultMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						Object key = entry.getKey();
						Object val = entry.getValue();
						
						nextsentence += "\t\t\t\t "+key.toString()+" : "+val.toString()+" ;\n";
					}
//				}
			}
			nextsentence += "\t\t\t\t TRUE :"+outputlist.get(i).getName()+" ;\n";
			nextsentence += "\t\t\t esac;\n";
			next.add(nextsentence);
		}
		return next;
	}

}
