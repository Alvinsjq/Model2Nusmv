package org.eclipse.acceleo.model2nusmv.service;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.Equation;
import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.Variable;

public class EquationService {
	
	/**
	 * 返回一个list，其中都是等式
	 * @param operator
	 * @return
	 */
	public ArrayList<String> Get_All_Equations(Operator operator){
		ArrayList<String> AllEquations = new ArrayList<String>();
		for(EObject eobject : operator.eContents()){
			if(eobject instanceof Equation){
				//System.err.println("Equations");
				Equation equation = (Equation)eobject;
				EList<Variable> left = equation.getLeft();
				if(left.size()==1){
					String localvariable = left.get(0).getName();
					if(!IsOutputVariables(localvariable,operator))
						AllEquations.add(localvariable);
					else{
						//处理ASSIGN中需要的输出变量
						
						// ...
					}
				}
			}
		}
		return AllEquations;
	}

	
	/**
	 * 确定该变量名是否是输出变量，因为输出变量在VAR中已声明，
	 * 需要在ASSIGN中才能进行赋值
	 * @param variableName
	 * @param operator
	 * @return
	 */
	public boolean IsOutputVariables(String variableName,Operator operator){
		
		return false;
	}
}
