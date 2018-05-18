package org.eclipse.acceleo.model2nusmv.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.BinaryOp;
import com.formaltech.smave.metamodel.smave.Equation;
import com.formaltech.smave.metamodel.smave.Expression;
import com.formaltech.smave.metamodel.smave.IdExpression;
import com.formaltech.smave.metamodel.smave.IfThenElseOp;
import com.formaltech.smave.metamodel.smave.ListExpression;
import com.formaltech.smave.metamodel.smave.NAryOp;
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
				Expression rightExp = equation.getRight();
				if(left.size()==1){
					String localvariable = left.get(0).getName();
					if(!IsOutputVariables(localvariable,operator)){
						String rightexp = "";
						//得到右边的式子
						rightexp = Get_Right_Expression(rightExp);
						AllEquations.add(localvariable+" := "+rightexp+";");
						
					}
					else{
						//处理ASSIGN中需要的输出变量
						
						// ...
					}
				}
			}
		}
		return AllEquations;
	}

	private String Get_Right_Expression(Expression rightExp) {
		// TODO Auto-generated method stub
		
		if(rightExp instanceof IdExpression){
			IdExpression idExpression = (IdExpression)rightExp;
			return idExpression.getPath().getName();
			
		}else if(rightExp instanceof BinaryOp){
			BinaryOp binaryOp = (BinaryOp)rightExp;
			EList<Expression> operands = binaryOp.getOperand();
			String left = "";
			String op = GetBinaryOp(binaryOp.getOperator());
			String rig ="";
			try {
				//在二元操作无嵌套的情况下
				IdExpression leftop = (IdExpression)operands.get(0);
				IdExpression rigop = (IdExpression)operands.get(1);
				left = leftop.getPath().getName().toString();
				rig = rigop.getPath().getName().toString();
				
				// 在二元操作嵌套的情况下
				// ....
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("出现不仅是idexpression的情况");
			}
			return left+op+rig;
			
		}else if(rightExp instanceof NAryOp){
			NAryOp binaryOp = (NAryOp)rightExp;
			EList<Expression> operands = binaryOp.getOperand();
			String left = "";
			String op = GetBinaryOp(binaryOp.getOperator());
			String rig ="";
			try {
				//在二元操作无嵌套的情况下
				IdExpression leftop = (IdExpression)operands.get(0);
				IdExpression rigop = (IdExpression)operands.get(1);
				left = leftop.getPath().getName().toString();
				rig = rigop.getPath().getName().toString();
				
				// 在二元操作嵌套的情况下
				// ....
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("出现不仅是idexpression的情况");
			}
			return left+op+rig;
			
		}else if(rightExp instanceof IfThenElseOp){
			StringBuffer ifsentence = new StringBuffer();
			ifsentence.append("if ");
			IfThenElseOp ifThenElseOp = (IfThenElseOp)rightExp;
			Expression ifexp = ifThenElseOp.getIf();
			ListExpression thenexp = (ListExpression) ifThenElseOp.getThen();
			ListExpression elseexp = (ListExpression) ifThenElseOp.getElse();
		
	
			if(ifexp instanceof IdExpression){
				//先考虑if中的条件是一个idexpression
				IdExpression ifid = (IdExpression)ifexp;
				ifsentence.append(ifid.getPath().getName().toString()+" ");
				
			}else if(ifexp instanceof BinaryOp){
				//再考虑if的条件是一个相对复杂的二元操作....
			}else{
				//更复杂情况...
			}

			ifsentence.append("then (");
			EList<Expression> thenlist = thenexp.getItem();
			EList<Expression> elselist = elseexp.getItem();
			
			for(Expression e : thenlist){
				if(e instanceof IdExpression){
					//先考虑if中的条件是一个idexpression
					IdExpression ide = (IdExpression)e;
					ifsentence.append(ide.getPath().getName().toString()+" ");
				}else{
					//复杂情况...
				}
			}
				
		    ifsentence.append(") else (");
			for(Expression e : elselist){
				if(e instanceof IdExpression){
					//先考虑if中的条件是一个idexpression
					IdExpression ide = (IdExpression)e;
					if(ide.getPath()!=null)
						ifsentence.append(ide.getPath().getName().toString()+" ");
				}else{
					//复杂情况...
				}
			}
		    ifsentence.append(")");
		    
		    return ifsentence.toString();
			
		}else {
			//补充更多的expression
			//....
		
		}
			return "";
	}

	private String GetBinaryOp(String operator) {
		// TODO Auto-generated method stub
		switch (operator) {
		case "div":
			return " / ";
		case "&gt;":
			return " > ";
		case "&gt;=":
			return " >= ";
		case "&lt;":
			return " < ";
		case "&lt;=":
			return " <= ";
		case "mod":
			return " % ";
		case "&lt;&gt;":
			return " != ";
		case "-":
			return " - ";
		case "*":
			return " * ";
		case "/":
			return " / ";		
		case "=":
			return " = ";
		}
		return "";
	}

	/**
	 * 返回应该出现在Assign中的等式
	 * 也就是左边是输出变量，右边是一些表达式
	 * @param operator
	 * @return
	 */
	public ArrayList<String> Get_Assign_InFunction(Operator operator){
		ArrayList<String> AllEquations = new ArrayList<String>();
		for(EObject eobject : operator.eContents()){
			if(eobject instanceof Equation){
				//System.err.println("Equations");
				Equation equation = (Equation)eobject;
				EList<Variable> left = equation.getLeft();
				if(left.size()==1){
					String outputvariable = left.get(0).getName();
					if(IsOutputVariables(outputvariable,operator))
						AllEquations.add(outputvariable);
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
		List<Variable> output_variables = new VariablesService().Get_output_Variables(operator);
		for (Variable variable : output_variables) {
			if(variable.getName().equals(variableName))
				return true;
		}
		return false;
	}

}
