package org.eclipse.acceleo.model2nusmv.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.BinaryOp;
import com.formaltech.smave.metamodel.smave.CallExpression;
import com.formaltech.smave.metamodel.smave.ConstValue;
import com.formaltech.smave.metamodel.smave.Equation;
import com.formaltech.smave.metamodel.smave.Expression;
import com.formaltech.smave.metamodel.smave.IdExpression;
import com.formaltech.smave.metamodel.smave.IfThenElseOp;
import com.formaltech.smave.metamodel.smave.Last;
import com.formaltech.smave.metamodel.smave.ListExpression;
import com.formaltech.smave.metamodel.smave.NAryOp;
import com.formaltech.smave.metamodel.smave.OpCall;
import com.formaltech.smave.metamodel.smave.Operator;
import com.formaltech.smave.metamodel.smave.State;
import com.formaltech.smave.metamodel.smave.UnaryOp;
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
			
		}else if(rightExp instanceof Last){
			Last last = (Last)rightExp;
			return last.getVariable().getName();
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
			String op = NAryOp(binaryOp.getOperator());
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
			ifsentence.append(" (");
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

			ifsentence.append(") ? (");
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
				
		    ifsentence.append(") : (");
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
			
		}else if(rightExp instanceof CallExpression){
			
			String call_fun_name="";
			String params = "";
			CallExpression callExpression = (CallExpression)rightExp;			
			OpCall opcall = (OpCall)callExpression.getOperator();
			if(opcall!=null){
				Operator ope = opcall.getOperator();
//				System.err.println(ope.getName());
				call_fun_name = ope.getName();
				EList<Expression> callparams = callExpression.getCallParameter();
				params =call_fun_name+"(";
				for(int i=0;i<callparams.size();i++){
					if(callparams.get(i) instanceof IdExpression){
						IdExpression pd = (IdExpression)callparams.get(i);
						if(i<callparams.size()-1)
							params += pd.getPath().getName()+", ";
						else
							params += pd.getPath().getName()+")";
					}
				}
			}
			return params;
		}else {
			//补充更多的expression
			//....
		
		}
			return "";
	}

	private String NAryOp(String operator) {
		// TODO Auto-generated method stub
		switch (operator) {
		case "+":
			return " + ";
		case "*":
			return " * ";
		case "@":
			// 从类型中取出数组大小比较方便，这里暂时写了两个参数的情况
			break;
		case "and":
			return " & ";
		case "or":
			return " | ";
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
	
	
	private String GetUnaryOp(String operator){
		switch (operator) {
		case "not":
			return "!";
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
				Expression rightExp = equation.getRight();
				if(left.size()==1){
					String outputvariable = left.get(0).getName();
					if(IsOutputVariables(outputvariable,operator)){
						//处理ASSIGN中需要的输出变量
						// ...
//						AllEquations.add(outputvariable);
						String rightassign = "";
						//得到右边的式子
						rightassign = Get_Right_Expression(rightExp);
						AllEquations.add(outputvariable+" := "+rightassign+";");
						
					}else{
						//非输出变量不处理
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

	
	/**
	 * 得到State中的所有的等式
	 * @param operator
	 * @return
	 */
	public ArrayList<String[]> Get_All_Equations_In_States(Operator operator, State state){
		ArrayList<String[]> AllEquations = new ArrayList<String[]>();
		for(EObject eobject : state.eContents()){
			if(eobject instanceof Equation){
				//System.err.println("Equations");
				Equation equation = (Equation)eobject;
				EList<Variable> left = equation.getLeft();
				Expression rightExp = equation.getRight();
				if(left.size()==1){
					String localvariable = left.get(0).getName();			
						String rightexp = "";
						//得到右边的式子
						rightexp = Get_Right_Expression(rightExp);
						String[] ele = new String[2];
						ele[0] = localvariable;
						ele[1] = rightexp;
						AllEquations.add(ele);
				}
			}
		}
		return AllEquations;
	}
	
	/**
	 * 输入一个状态的所有等式
	 * 将中间的local 变量去除
	 * 返回一串只有变量的等式List
	 * 注意* 这里还没有考虑到等式中有调用的情况，也就是说现在仅能处理只有简单赋值的状态，有调用的状态需要重新设计算法
	 * @param equations
	 * @return
	 */
	public ArrayList<String[]> Delete_local_variables( Operator operator, ArrayList<String[]> equations){
		ArrayList<String[]> EquationsNoLocal = new ArrayList<>();
		List<Variable> ouputvars = new VariablesService().Get_output_Variables(operator);
		for(int i=0;i<ouputvars.size();i++){
			String[] s = new String[2];
			s[0] = ouputvars.get(i).getName();
			for(int j=0;j<equations.size();j++){
				
				String[] ss = equations.get(j);
				if(ss[0].equals(s[0])){
					//发现了该等式左边为输出变量，则寻找右边的表达式
					for(int k=0;k<equations.size();k++){
						if(ss[1].equals(equations.get(k)[0])){
							//如果等式的左边为输出表达式等式的右边，那么该等式的右边就是输出变量的取值
							s[1] = equations.get(k)[1];
							EquationsNoLocal.add(s);
							break;
						}
					}
				}
				
			}
		}
		
		//...
		
		return EquationsNoLocal;
		
	}
	
	
	public String Get_Expression(Expression expression){
		
		if(expression instanceof IdExpression){
			IdExpression idExpression = (IdExpression)expression;
			return idExpression.getPath().getName();
				
		}else if(expression instanceof BinaryOp){
			BinaryOp binaryOp = (BinaryOp)expression;
			
			if(binaryOp.getOperator().equals("times")){
				// times不处理
				EList<Expression> opreands = binaryOp.getOperand();
				for(int i=0;i<opreands.size();i++){
					Expression opex = opreands.get(i);
					if(!(opex instanceof NAryOp)){
						continue;
					}else{
						//这里处理NAryOp的部分
						NAryOp nAryOp = (NAryOp)opex;
						String op = NAryOp(nAryOp.getOperator());
						String expression_in_naryop="";
						EList<Expression> el = nAryOp.getOperand();
						
						for(int _i=0;_i<el.size();_i++){
							if(el.get(_i)instanceof ConstValue){
								ConstValue cv = (ConstValue)el.get(_i);
								if(_i == 0)
									expression_in_naryop += ReturnTF(cv.getValue());
								else 
									expression_in_naryop += " = " + ReturnTF(cv.getValue());
							}else if(el.get(_i)instanceof IdExpression){
								IdExpression cv = (IdExpression)el.get(_i);
								if(_i == 0)
									expression_in_naryop += cv.getPath().getName();
								else 
									expression_in_naryop += " = " + cv.getPath().getName();
							}else{
								
							}
						}
						return expression_in_naryop;
					}
				}
			}
			
			return "";
		}else if(expression instanceof UnaryOp){
			UnaryOp unaryOp = (UnaryOp)expression;
			EList<Expression> opreands = unaryOp.getOperand();
			String opre = "";
			for(int i=0;i<opreands.size();i++){
				Expression opex = opreands.get(i);
				if(opex instanceof IdExpression){
					IdExpression idp = (IdExpression)opex;
					opre += idp.getPath().getName();
				}else{
					//其他expression
				}
			}
			return GetUnaryOp(unaryOp.getOperator())+"("+opre+")";
		}else if(expression instanceof NAryOp){
			
		}else{
			
			
		}
		return "";
	}
	
	
	/**
	 * 帮助
	 * @param s
	 * @return
	 */
	private String ReturnTF(String s){
		if(s.equals("true"))
			return "TRUE";
		else if(s.equals("false"))
				return "FALSE";
		else 
			return s;
	}
	
	
	
	
}
