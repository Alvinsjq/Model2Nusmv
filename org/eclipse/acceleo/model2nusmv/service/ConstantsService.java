package org.eclipse.acceleo.model2nusmv.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.BasicType;
import com.formaltech.smave.metamodel.smave.ConstValue;
import com.formaltech.smave.metamodel.smave.Constant;
import com.formaltech.smave.metamodel.smave.Expression;
import com.formaltech.smave.metamodel.smave.IdExpression;
import com.formaltech.smave.metamodel.smave.Model;
import com.formaltech.smave.metamodel.smave.Value;


public class ConstantsService {


	/**
	 * 得到元模型中的所有常量
	 * @param model
	 * @return
	 */
	public static List<Constant> Get_Constant_Variables(Model model){
		List<Constant> constantList = new ArrayList<>();
		for(EObject eObject : model.eContents()){
			if (eObject instanceof Constant){
				Constant constant = (Constant)eObject;
				constantList.add(constant);
			}
		}
		return constantList;	
	}
	
	/**
	 * 获取常量的类型
	 * @param constant
	 * @return
	 */
	public static String Get_Constant_type(Constant constant){
		BasicType basicType = (BasicType) constant.getType();
		if(basicType.getBasicType().getName() == "BOOL"){
			return "boolean";
		}
		else if(basicType.getBasicType().getName() == "INT8")
		{
			return "integer";
		}
		else if(basicType.getBasicType().getName() == "INT16")
		{
			return "integer";
		}
		else if(basicType.getBasicType().getName() == "INT32")
		{
			return "integer";
		}
		else if(basicType.getBasicType().getName() == "INT64")
		{
			return "integer";
		}
		
		return "other type";
	}
	
	/**
	 * 得到常量的值
	 * @param constant
	 * @return
	 */
	public String Get_Constat_Value(Constant constant){
		String returnString = "sm" ;
		Expression expression = constant.getValue();
	    if(expression instanceof ConstValue){
	    	ConstValue constValue = (ConstValue) expression;
	    	//System.err.println(constValue.getValue());
	    	returnString = constValue.getValue();
	    }	 
	    return returnString;
	}


	

	public boolean IsInteger(String type){
		if(type.equals("integer"))
			return true;
		else return false;
		
	}
	
}
