package org.eclipse.acceleo.model2nusmv.service;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.OperatorKind;

public class ModuleService {
 
	/**
	 * 判断是不是node
	 * @param operator
	 * @return
	 */
	public boolean OperatorIs_Node(Operator operator){
		OperatorKind operatorKind = operator.getKind();
		if(operatorKind.getValue() == 1)
			return true;
		else 
			return false;
	}
	
	/**
	 * 判断是不是function
	 * @param operator
	 * @return 该operator的类型是否是Function
	 */
	public boolean OperatorIs_Function(Operator operator){
		OperatorKind operatorKind = operator.getKind();
		if(operatorKind.getValue() == 2)
			return true;
		else 
			return false;
	}

 
	
	
	
}
