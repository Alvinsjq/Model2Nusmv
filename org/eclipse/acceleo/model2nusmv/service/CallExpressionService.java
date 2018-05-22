package org.eclipse.acceleo.model2nusmv.service;


import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.CallExpression;
import com.formaltech.smave.metamodel.smave.Constant;
import com.formaltech.smave.metamodel.smave.Expression;
import com.formaltech.smave.metamodel.smave.IdExpression;
import com.formaltech.smave.metamodel.smave.OpCall;
import com.formaltech.smave.metamodel.smave.Operator;

public class CallExpressionService {
	
	/**
	 * 得到在node中被状态机调用的functions和参数
	 * 并为其做变量声明
	 * @param operator
	 */
	public ArrayList<String> Get_Call_Functions(Operator operator){
		ArrayList<String> call_functions = new ArrayList<String>();
		Iterator<EObject> it = operator.eAllContents();
		
		while(it.hasNext()){
			EObject eObject = it.next();
			String call_fun_name="";
			if (eObject instanceof CallExpression){
				CallExpression callExpression = (CallExpression)eObject;			
				OpCall opcall = (OpCall)callExpression.getOperator();
				if(opcall!=null){
					Operator ope = opcall.getOperator();
//					System.err.println(ope.getName());
					call_fun_name = ope.getName();
					EList<Expression> callparams = callExpression.getCallParameter();
					String params =call_fun_name+"(";
					for(int i=0;i<callparams.size();i++){
						if(callparams.get(i) instanceof IdExpression){
							IdExpression pd = (IdExpression)callparams.get(i);
							if(i<callparams.size()-1)
								params += pd.getPath().getName()+", ";
							else
								params += pd.getPath().getName()+")";
						}
					}
					String res = "_"+call_fun_name+" ： "+params+";";
					call_functions.add(res);
				}
			}
		}
		
		return call_functions;
	}
	

	
	
}
