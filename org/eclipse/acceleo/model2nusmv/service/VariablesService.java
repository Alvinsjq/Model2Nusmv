package org.eclipse.acceleo.model2nusmv.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.acceleo.model2nusmv.tricks.TypesTrick;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.BasicType;
import com.formaltech.smave.metamodel.smave.Model;
import com.formaltech.smave.metamodel.smave.NamedType;
import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.Variable;

public class VariablesService {
 
	/**
	 * 
	 * @param model 输入模型
	 * @return 整个模型的变量名集合
	 */
	public static List<Variable> Get_all_Variables(Model model){
		List<Variable> AllVarList = new ArrayList<>();
		for(EObject eObject : model.eContents()){
			if (eObject instanceof Variable){
				Variable variable = (Variable)eObject;
				System.err.println(variable.getName());
				AllVarList.add(variable);
			}
		}
		return AllVarList;	
	}
	
	/**
	 * 
	 * @param 输入一个operator
	 * @return operator中的所有变量
	 */
	public static List<Variable> Get_all_Variables(Operator operator){
		List<Variable> AllVarList = new ArrayList<>();
		EList<Variable> inputList = operator.getInput();
		for(Variable variable : inputList){
			if (variable.isIsInput()){
				AllVarList.add(variable);
			}
		}
		return AllVarList;	
	}
	
	/**
	 * 
	 * @param operator
	 * @return operator中的输入变量，并且该变量不是hidden变量
	 */
	public static List<Variable> Get_input_Variables(Operator operator){
		List<Variable> InputVarList = new ArrayList<>();
		EList<Variable> inputList = operator.getInput();
		for(Variable variable : inputList){
			//if (variable.isIsInput()&&!variable.isIsHidden()){
			if (variable.isIsInput()){
				InputVarList.add(variable);
			}
		}
		return InputVarList;	
	}
	
	
	/**
	 * 
	 * @param operator
	 * @return operator中的Hidden变量，该变量会放在DEFINE中声明
	 */
	public static List<Variable> Get_hidden_Variables(Operator operator){
		List<Variable> HiddenVarList = new ArrayList<>();
		EList<Variable> inputList = operator.getInput();
		for(Variable variable : inputList){
			if (variable.isIsInput()&& variable.isIsHidden()){
				HiddenVarList.add(variable);
			}
		}
		return HiddenVarList;	
	}
	
	
	/**
	 * 
	 * @param operator
	 * @return operator中的输出变量
	 */
	public static List<Variable> Get_output_Variables(Operator operator){
		List<Variable> OutputVarList = new ArrayList<>();
		EList<Variable> outputList = operator.getOutput();
		for(Variable variable : outputList){
			if (variable.isIsOutput()){
				OutputVarList.add(variable);
			}
		}
		return OutputVarList;	
	}
	
	
	/**
	 * 得到function中的input参数
	 * @param operator
	 * @return
	 */
	public String Get_Function_Input_params(Operator operator){
		StringBuffer paras = new StringBuffer();
		List<Variable> inputlist = Get_input_Variables(operator);
		int len = inputlist.size();
		if(len>0){
			paras.append("(");
			for (int i = 0; i < len; i++) {
				if(i<len-1)
					paras.append(inputlist.get(i).getName()).append(",");
				else
					paras.append(inputlist.get(i).getName());
			}
			paras.append(")");
		}
		return paras.toString();
	}
	
	/**
	 * 输入变量得到它的类型
	 * @param variable
	 * @return
	 */
	public String Get_variable_type(Model model,Variable variable){
		
		try {
			BasicType basicType = (BasicType)variable.getType();
			if(basicType.getBasicType().getName() == "BOOL"){
				return "boolean;";
			}
			else if(basicType.getBasicType().getName() == "INT8")
			{
				return "integer;";
			}
			else if(basicType.getBasicType().getName() == "INT16")
			{
				return "integer;";
			}
			else if(basicType.getBasicType().getName() == "INT32")
			{
				return "integer;";
			}
			else if(basicType.getBasicType().getName() == "INT64")
			{
				return "integer;";
			}
		} catch (ClassCastException e) {
			// TODO: handle exception
			System.out.println("variable type isn't basic type");
			NamedType namedType = (NamedType)variable.getType();			
			String typename = namedType.getType().getName().toString();
			StringBuffer typedetail = new StringBuffer();
			ArrayList<String> nametype = TypesTrick.GetElementsInType(model,typename);
			String declareOfType="{ ";
			for (int i = 0; i < nametype.size(); i++) {
				if(i!=nametype.size()-1)
					declareOfType +=nametype.get(i)+", ";
				else
					declareOfType += nametype.get(i)+" ";
			}
			declareOfType += "};";
			//return typename;
			return declareOfType;
		}
		
		return "other type";
	}

 
}
