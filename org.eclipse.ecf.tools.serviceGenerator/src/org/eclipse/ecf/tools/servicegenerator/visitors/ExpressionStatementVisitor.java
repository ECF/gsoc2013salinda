/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.servicegenerator.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;

public class ExpressionStatementVisitor extends ASTVisitor {
        List<ExpressionStatement> expressionStatements = new ArrayList<ExpressionStatement>();
       
        private String operator;
        @Override
        public boolean visit(ExpressionStatement node) {
                expressionStatements.add(node);
                return super.visit(node);
        }
        public boolean visit(InfixExpression node){
                operator=node.getOperator().toString();
                return super.visit(node);
        }
        public List<ExpressionStatement> getExpressionStatements() {
                return expressionStatements;
        }
       
        public String  getOprator(){
                return operator;
        }
}

