/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.processors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ecf.tools.serviceGenerator.utils.AsyncMethodType;
import org.eclipse.ecf.tools.serviceGenerator.utils.AsyncProperties;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.ecf.tools.serviceGenerator.visitors.MethodVisitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WildcardType;


public class AstProcessor {
	/*following imports should be removed frome the generated clazz*/
	private static final String ASYNC_ANOTATION= "org.eclipse.ecf.tools.serviceGenerator.annotations.Async";
	private static final String REMOTE_SERVICE_ANOTATION="org.eclipse.ecf.tools.serviceGenerator.annotations.RemoteService";
	private static final String ASYNC_TYPE= "org.eclipse.ecf.tools.serviceGenerator.annotations.AsyncType";
	private static final String AREMOTE_SERVICE_ANOTATION="org.eclipse.ecf.tools.serviceGenerator.annotations.AsyncRemoteService";

	private CompilationUnit origenalInterfaceUnit;
	private CompilationUnit newAsyncInterfaceUnit;
	private CompilationUnit newimplClazzUnit;
	private CompilationUnit serviceRegisterUnit;
	private MethodVisitor methodVisitor;
	private AST ast;
    private Logger log;
    
	/**
	 * basically generate new compilation units for Async interface and impl-clazzes
	 * 
	 * @param icompilationUnit-user selected file
	 */
	public AstProcessor(ICompilationUnit icompilationUnit,Logger log) {
		 ast = AST.newAST(AST.JLS4);
		 ASTParser parser = ASTParser.newParser(AST.JLS4);
		 parser.setSource(icompilationUnit);
		 origenalInterfaceUnit = (CompilationUnit) parser.createAST(null);
		 newAsyncInterfaceUnit = ast.newCompilationUnit();
		 newimplClazzUnit = ast.newCompilationUnit();
		 setServiceRegisterUnit(ast.newCompilationUnit());
		 methodVisitor = new MethodVisitor();
		 this.log = log;
		
	}
	
	public CompilationUnit getOrigenalInterfaceUnit() {
		return origenalInterfaceUnit;
	}

	public void setOrigenalInterfaceUnit(CompilationUnit origenalInterfaceUnit) {
		this.origenalInterfaceUnit = origenalInterfaceUnit;
	}

	public CompilationUnit getNewAsyncInterfaceUnit() {
		return newAsyncInterfaceUnit;
	}

	public void setNewAsyncInterfaceUnit(CompilationUnit newAsyncInterfaceUnit) {
		this.newAsyncInterfaceUnit = newAsyncInterfaceUnit;
	}

	public CompilationUnit getNewimplClazzUnit() {
		return newimplClazzUnit;
	}

	public void setNewimplClazzUnit(CompilationUnit newimplClazzUnit) {
		this.newimplClazzUnit = newimplClazzUnit;
	}

	public MethodVisitor getMethodVisitor() {
		return methodVisitor;
	}

	public void setMethodVisitor(MethodVisitor methodVisitor) {
		this.methodVisitor = methodVisitor;
	}

	public AST getAst() {
		return ast;
	}

	public void setAst(AST ast) {
		this.ast = ast;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	
	public CompilationUnit getNewunit() {
		return newAsyncInterfaceUnit;
	}

	public CompilationUnit getImpleunit() {
		return newimplClazzUnit;
	}

	public void setImpleunit(CompilationUnit impleunit) {
		this.newimplClazzUnit = impleunit;
	}

	public void setNewunit(CompilationUnit newunit) {
		this.newAsyncInterfaceUnit = newunit;
	}
	
	/**
	 * coping all existing imports from original interface
	 * also adding new imports which need for async-services
	 * @param extraImports-explicit imports 
	 * @param servicetype
	 */
	public void addImports(List<String> extraImports, int servicetype) {

		List<String> asynInterfaceImpoerts = new ArrayList<String>();

		asynInterfaceImpoerts
				.add(AsyncProperties.ASYNC_SERVICE_STR_IMPORTS_IFUTURE);
		asynInterfaceImpoerts
				.add(AsyncProperties.ASYNC_SERVICE_STR_IMPORTS_CALLBACkPROXY);
		asynInterfaceImpoerts
				.add(AsyncProperties.ASYNC_SERVICE_STR_IMPORTS_CALLBACK);
		if (servicetype == 1) {
			extraImports.add(AsyncProperties.ASYNC_SERVICE_STR_IMPORTS_IFUTURE);
			extraImports.add(AsyncProperties.ASYNC_SERVICE_STR_IMPORTS_CALLBACK);
		}
		createImports(newAsyncInterfaceUnit, asynInterfaceImpoerts);
		createImports(newimplClazzUnit, extraImports);
	}
	
	public void cretaePackage(String packName,String implePackName){
			   newAsyncInterfaceUnit.setPackage(createPackage(packName));
			   newimplClazzUnit.setPackage(createPackage(implePackName));
	}
	
	public PackageDeclaration cretaePackage2(String packageName){
		return createPackage(packageName);
	}
	
	
	

	/**
	 * New AST type declare for the interface and impl-clazzes  
	 * @param typename-name of the type
	 * @param isInterface-this value set to true when interface declaration
	 * @param supperclass- provide supper class, example-interface provide when impl-clazz declaration
	 * @return- return declared class or interface
	 */
	@SuppressWarnings("unchecked")
	public TypeDeclaration createType(String typename, boolean isInterface,String supperclass) {
		TypeDeclaration type=null;
			type = ast.newTypeDeclaration();
			type.setInterface(isInterface);
			type.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
			SimpleName newSimpleName = ast
					.newSimpleName(typename);
			type.setName(newSimpleName);
			Name name = ast.newName(supperclass);
			SimpleType superclassType = ast.newSimpleType(name);
			if (isInterface) {
				type.setSuperclassType(superclassType);
			} else {
				type.superInterfaceTypes().add(superclassType);
			}
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public TypeDeclaration createType2(String typename, boolean isInterface,String supperclass,ModifierKeyword modifierKeyword) {
		TypeDeclaration type=null;
			type = ast.newTypeDeclaration();
			type.setInterface(isInterface);
			type.modifiers().add(ast.newModifier(modifierKeyword));
			SimpleName newSimpleName = ast.newSimpleName(typename);
			type.setName(newSimpleName);
			if(supperclass!=null){
				Name name = ast.newName(supperclass);
				SimpleType superclassType = ast.newSimpleType(name);
				if (isInterface) {
					type.setSuperclassType(superclassType);
				} else {
					type.superInterfaceTypes().add(superclassType);
				}
			}
		return type;
	}
	
	
	
	/**
	 * method signatures will be added to the interface and full method added to the impl-clazz
	 * @param type - interface
	 * @param impletype - impl-clazz
	 * @param serviceType - 1 for Async-service  2 for sync-service
	 */
	@SuppressWarnings("unchecked")
	public void addMethods(TypeDeclaration type,TypeDeclaration impletype,int serviceType){

			    origenalInterfaceUnit.accept(methodVisitor);
			    List<MethodDeclaration> methods = methodVisitor.getMethods();
			    for (MethodDeclaration userMethodDec : methods) {
			    	List<Modifier> modifiers = userMethodDec.modifiers();
			    	if(serviceType==1){
			    	int asynctype = getAsyncType(modifiers);
			    	if(asynctype!=0){
			    		if(asynctype==AsyncMethodType.BOTH.getCode()){/*Both */
			    			/*creating Interface*/
			    			createMethod(type, userMethodDec, modifiers,true,false,false);
			    			createMethod(type, userMethodDec, modifiers,false,false,false);
			    			/*creating AsyncImpl*/
			    			createMethod(impletype, userMethodDec, modifiers,true,true,false);
			    			createMethod(impletype, userMethodDec, modifiers,false,true,false);
			    		}else if(asynctype==AsyncMethodType.CALLBACK.getCode()){/*call back only*/
			    			/*creating Interface*/
			    			createMethod(type, userMethodDec, modifiers,true,false,false); 
			    			/*creating AsyncImpl*/
			    			createMethod(impletype, userMethodDec, modifiers,true,true,false);
			    		}else if(asynctype==AsyncMethodType.FUTURE.getCode()){/*Future Only*/
			    			/*creating Interface*/
			    			createMethod(type, userMethodDec, modifiers,false,false,false); 
			    			/*creating AsyncImpl*/
			    			createMethod(impletype, userMethodDec, modifiers,false,true,false);
			    		}
			       }
			   } else {
				   /*creating syncImpl*/
				   createMethod(impletype, userMethodDec, modifiers,false,true,true);
			   }
			 }
		   newAsyncInterfaceUnit.types().add(type);
		  newimplClazzUnit.types().add(impletype);
	}
	
	private PackageDeclaration createPackage(String packName) {
		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		  Name newName = ast.newName(packName);
		  packageDeclaration.setName(newName);
		return packageDeclaration;
	}
	
	@SuppressWarnings("unchecked")
	public void createImports(CompilationUnit unit,List<String> extraImports) {
		List<ImportDeclaration> oldimports = origenalInterfaceUnit.imports();
		   /*adding already existing imports in the interface*/
          for (ImportDeclaration importDeclaration : oldimports) {
        	   Name name = importDeclaration.getName();
        	   String fqn = name.getFullyQualifiedName();
        	   /*Removing imports related to annotations*/
        	   if(!ASYNC_ANOTATION.equals(fqn)
        			   &&!REMOTE_SERVICE_ANOTATION.equals(fqn)&&!ASYNC_TYPE.equals(fqn)&&!AREMOTE_SERVICE_ANOTATION.equals(fqn)){
        		   unit.imports().add(getImportDec(fqn));
        	   }
		}
          /*adding new imports*/
          for (String strImport : extraImports) {
        	  unit.imports().add(getImportDec(strImport));
		}
	}
	
	/**
	 * constructing a method 
	 * @param type - this will be a interface or impl-clazz
	 * @param userMethodDec - method of the original interface
	 * @param modifiers - modifiers list of original method
	 * @param iscalback - check weather callback or Ifurure true for callback Async method
	 * @param isBody - true when method body required if false method body will be empty (method signature return)
	 * @param isSync - true for sync methods false for all Async methods
	 */
	@SuppressWarnings("unchecked")
	public void createMethod(TypeDeclaration type,
			MethodDeclaration userMethodDec, List<Modifier> modifiers,boolean iscalback,boolean isBody,boolean isSync) {
		 MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
 
		addModifers(modifiers, methodDeclaration);
  	
		addReturntype(userMethodDec, methodDeclaration,iscalback,isSync);
  	 
		addMethodName(userMethodDec, methodDeclaration); 
 
		addParameters(userMethodDec, methodDeclaration,iscalback);
		
		addExceptions(userMethodDec, methodDeclaration);
		
		if(isBody){
			addMethodBody(userMethodDec, methodDeclaration,iscalback,isSync);
		}
		type.bodyDeclarations().add(methodDeclaration);
	}
	
	public MethodDeclaration createMethod2() {
		 MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		return methodDeclaration;
	}
	

	/**
	 * Adding method Exceptions
	 * @param userMethodDec - original method
	 * @param methodDeclaration - constructing method
	 */
	@SuppressWarnings("unchecked")
	private void addExceptions(MethodDeclaration userMethodDec,
			MethodDeclaration methodDeclaration) {
		try{
			List<Object> thrownExceptions = userMethodDec.thrownExceptions();
		for (Object throwStatement : thrownExceptions) {
			String string = throwStatement.toString();
			methodDeclaration.thrownExceptions().add(ast.newSimpleName(string));
		}
		}catch(Throwable e){
			log.log(0, e.getMessage());
		}
	}

	/**
	 * Creating method body
	 * @param userMethodDec
	 * @param methodDeclaration
	 * @param iscalback
	 * @param isSync
	 */
	@SuppressWarnings("unchecked")
	private void addMethodBody(MethodDeclaration userMethodDec,
			MethodDeclaration methodDeclaration,boolean iscalback,boolean isSync) {
		Block newBlock = ast.newBlock();
		ReturnStatement newReturnStatement = ast.newReturnStatement();
		Type returnType2 = userMethodDec.getReturnType2();
		if(returnType2.isPrimitiveType()&& (iscalback||isSync)){
			 String string = returnType2.toString();
			 if(PrimitiveType.toCode(string)==PrimitiveType.INT){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0")));	
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.BOOLEAN){
				 newReturnStatement.setExpression(ast.newBooleanLiteral(false));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.BYTE){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0")));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.DOUBLE){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0.0")));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.FLOAT){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0.0")));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.LONG){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0")));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.SHORT){
				 newReturnStatement.setExpression(ast.newNumberLiteral(String.valueOf("0")));
			 }else if(PrimitiveType.toCode(string)==PrimitiveType.CHAR){
				 newReturnStatement.setExpression(ast.newCharacterLiteral());
			 }
		}else{
			newReturnStatement.setExpression(ast.newNullLiteral());	
		}
		newBlock.statements().add(newReturnStatement);
		methodDeclaration.setBody(newBlock);
	}

    /**
     * creating parameters
     * @param userMethodDec
     * @param methodDeclaration
     * @param iscalback
     */
	@SuppressWarnings("unchecked")
	private void addParameters(MethodDeclaration userMethodDec,
			MethodDeclaration methodDeclaration,boolean iscalback) {
		List<SingleVariableDeclaration> parameters = userMethodDec.parameters();
		for (SingleVariableDeclaration paramType : parameters) {
			Type ptype = paramType.getType();				    	
			SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
			if(ptype.isPrimitiveType()){
			     variableDeclaration.setType(cretaePrimetiveType(ptype));
			}else if(ptype.isArrayType()){
 				 variableDeclaration.setType(createArrayType(ptype));
			}else if(ptype.isParameterizedType()){
			    variableDeclaration.setType(createParameterziedType(ptype));
			}else if(ptype.isQualifiedType()){
			       //toDO
			}else if(ptype.isSimpleType()){
			    variableDeclaration.setType(cretaeSimpleType(ptype));
			}else if(ptype.isUnionType()){
				 //toDO
			}else if(ptype.isWildcardType()){
				 //toDO
			}
			variableDeclaration.setName(ast.newSimpleName(paramType.getName().toString()));
			methodDeclaration.parameters().add(variableDeclaration);
		}
		if(iscalback){
		  SingleVariableDeclaration variableDeclaration = cretaeCallbackVariable();
		  methodDeclaration.parameters().add(variableDeclaration);
		}
	}

	@SuppressWarnings("unchecked")
	private SingleVariableDeclaration cretaeCallbackVariable() {
		SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
		  SimpleType newSimpleType = ast.newSimpleType(ast.newSimpleName("IAsyncCallback"));
		  ParameterizedType newParameterizedType = ast.newParameterizedType(newSimpleType);
		  SimpleType strnewSimpleType = ast.newSimpleType(ast.newSimpleName("String"));
		  newParameterizedType.typeArguments().add(strnewSimpleType);
		  variableDeclaration.setType(newParameterizedType);
		  variableDeclaration.setName(ast.newSimpleName("args"));
		return variableDeclaration;
	}

	@SuppressWarnings("unchecked")
	private ParameterizedType createParameterziedType(Type ptype) {
		ParameterizedType parameterizedType = (ParameterizedType)ptype;
		String string =parameterizedType.getType().toString();
		Name newName = ast.newName(string);
		SimpleType newSimpleType = ast.newSimpleType(newName);
		ParameterizedType newParameterizedType = ast.newParameterizedType(newSimpleType);
		List<Type> typeArguments = parameterizedType.typeArguments();
		for (Type argtype : typeArguments) {
			 if(argtype.isSimpleType()){
				SimpleType argnewSimpleType = cretaeSimpleType(argtype);
			    newParameterizedType.typeArguments().add(argnewSimpleType);	
			}else if(argtype.isWildcardType()){
				 WildcardType newWildcardType = ast.newWildcardType();					   		     
		   		 newParameterizedType.typeArguments().add(newWildcardType);	
			}else if(argtype.isArrayType()){
				newParameterizedType.typeArguments().add(createArrayType(argtype));	
			}else if(argtype.isPrimitiveType()){
				newParameterizedType.typeArguments().add(cretaePrimetiveType(argtype));	
			}
		}
		return newParameterizedType;
	}

	private SimpleType cretaeSimpleType(Type ptype) {
		String string = ptype.toString();
		Name newName = ast.newName(string);
		SimpleType newSimpleType = ast.newSimpleType(newName);
		return newSimpleType;
	}

	private ArrayType createArrayType(Type ptype) {
		 ArrayType arrayType = (ArrayType)ptype;
		 ArrayType newArrayType=null;
		 Type componentType = arrayType.getComponentType();
		 if(componentType.isPrimitiveType()){
			 PrimitiveType newPrimitiveType = cretaePrimetiveType(componentType); 
			 newArrayType = ast.newArrayType(newPrimitiveType);
		 }else if(componentType.isSimpleType()){
			 SimpleType newSimpleType = cretaeSimpleType(componentType);
			 newArrayType = ast.newArrayType(newSimpleType);
		 }else if(componentType.isArrayType()){
			 
		 }else if(componentType.isParameterizedType()){
			 
		 }else if(componentType.isQualifiedType()){
			 
		 }else if(componentType.isUnionType()){
			 
		 }else if(componentType.isWildcardType()){
			 
		 }
		 return newArrayType;
	}

	private PrimitiveType cretaePrimetiveType(Type ptype) {
		 String string = ptype.toString();
		 PrimitiveType newPrimitiveType = ast.newPrimitiveType(PrimitiveType.toCode(string));
		return newPrimitiveType;
	}

	private void addMethodName(MethodDeclaration userMethodDec,
			MethodDeclaration methodDeclaration) {
		String strmethodName = userMethodDec.getName().toString();
		SimpleName newSimpleMethodName = ast.newSimpleName(strmethodName);
		methodDeclaration.setName(newSimpleMethodName);
	}

	private void addReturntype(MethodDeclaration userMethodDec,
			MethodDeclaration methodDeclaration, boolean iscalback,boolean isSync) {
		Type returnType2 = userMethodDec.getReturnType2();
		if (iscalback||isSync) {
			if (returnType2.isPrimitiveType()) {
				PrimitiveType newPrimitiveType = cretaePrimetiveType(returnType2);
				methodDeclaration.setReturnType2(newPrimitiveType);
			} else if (returnType2.isArrayType()) {
				ArrayType createArrayType = createArrayType(returnType2);
				methodDeclaration.setReturnType2(createArrayType);
			} else if (returnType2.isParameterizedType()) {
				ParameterizedType createParameterziedType = createParameterziedType(returnType2);
				methodDeclaration.setReturnType2(createParameterziedType);
			} else if (returnType2.isSimpleType()) {
				SimpleType cretaeSimpleType = cretaeSimpleType(returnType2);
				methodDeclaration.setReturnType2(cretaeSimpleType);
			}
		} else {
			Name newName = ast.newName("IFuture");
			SimpleType newSimpleType = ast.newSimpleType(newName);
			methodDeclaration.setReturnType2(newSimpleType);
		}
	}

	@SuppressWarnings("unchecked")
	private void addModifers(List<Modifier> modifiers, MethodDeclaration methodDeclaration) {
		for (Object modifierObj : modifiers) {
			if(modifierObj instanceof Modifier){
				Modifier modifier = (Modifier)modifierObj;
				Modifier newModifier = ast.newModifier(modifier.getKeyword());
				methodDeclaration.modifiers().add(newModifier);
			}
		 }
	}

	private int getAsyncType(List<Modifier> modifiers) {
		for (Object modifierObj : modifiers) {
		     if(modifierObj instanceof NormalAnnotation){
		    	 NormalAnnotation anno = (NormalAnnotation) modifierObj;
		    	 if("Async".equals(anno.getTypeName().toString())){
		    		 String value=null;
		    		 @SuppressWarnings("unchecked")
					List<ASTNode> values = anno.values();
			    	 for (ASTNode astNode : values) {
			    		 value = astNode.toString();
					   }
		    		   String[] split = value.split("=");
		    			if(AsyncMethodType.BOTH.getStrCode().contains(split[1].subSequence(1, 5))){
		    				return AsyncMethodType.BOTH.getCode();
		    			}else if(AsyncMethodType.CALLBACK.getStrCode().contains(split[1].subSequence(1, 5))){
		    				return AsyncMethodType.CALLBACK.getCode();
		    			}else if(AsyncMethodType.FUTURE.getStrCode().contains(split[1].subSequence(1, 5))){
		    				return AsyncMethodType.FUTURE.getCode();
		    		 }
		 		 }
		     }
		 }
		return 0;
	}
	
 private ImportDeclaration getImportDec(String qn) {
		 ImportDeclaration importDeclaration = ast.newImportDeclaration();
		 Name newName = ast.newName(qn);
         importDeclaration.setName(newName);
         importDeclaration.setOnDemand(false);
          
         return importDeclaration;
	}

public CompilationUnit getServiceRegisterUnit() {
	return serviceRegisterUnit;
}

public void setServiceRegisterUnit(CompilationUnit serviceRegisterUnit) {
	this.serviceRegisterUnit = serviceRegisterUnit;
}
}
