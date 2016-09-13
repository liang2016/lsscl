/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.compound;

/**
 *  
 */
public class OrOperator extends BinaryOperator {
    public OrOperator(LogicalOperator operand1, LogicalOperator operand2) {
        super(operand1, operand2);
    }

    @Override
    public boolean evaluate() {
        return operand1.evaluate() || operand2.evaluate();
    }

    @Override
    public String toString() {
        return "OR(" + operand1 + "," + operand2 + ")";
    }
}
