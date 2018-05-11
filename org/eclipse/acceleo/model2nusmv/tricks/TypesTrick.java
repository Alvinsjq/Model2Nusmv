package org.eclipse.acceleo.model2nusmv.tricks;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.formaltech.smave.metamodel.smave.Enum;
import com.formaltech.smave.metamodel.smave.Model;
import com.formaltech.smave.metamodel.smave.Type;
import com.formaltech.smave.metamodel.smave.Value;

public class TypesTrick {

	public static ArrayList<String> GetElementsInType(Model model,String typename) {
		// TODO Auto-generated method stub
		ArrayList<String> ElementsIntype = new ArrayList<String>();
		for(EObject eObject : model.eContents()){
			if (eObject instanceof Type){
				Type type = (Type)eObject;
				//判断Type是否为Enum类型
				//System.out.println(type.getDefinition());//com.formaltech.smave.metamodel.smave.impl.EnumImpl@c4cdb9
				//System.out.println(type.getName());//t_LightColor
				if(type.getName().equals(typename)){
					if(type.getDefinition() instanceof Enum){
						//System.err.println("Enum");
						Enum defenum = (Enum)type.getDefinition();
						EList<Value> eList =defenum.getValue();
						for(Value v : eList)
							ElementsIntype.add(v.getName());
					}
				}
			}
		}
		return ElementsIntype;	
	}
	
	
}
