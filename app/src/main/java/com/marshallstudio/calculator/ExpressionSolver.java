package com.marshallstudio.calculator;

import android.util.Log;

import java.util.Stack;

public class ExpressionSolver {
    private Stack<Double> mNumbers;
    private Stack<Character> mOperators;

    public ExpressionSolver(){
        mNumbers=new Stack<>();
        mOperators=new Stack<>();
    }

    public String solveExpression(String expression){
        expression=equalizeBraces(expression);
        expression=insertMultiplyBeforeAndAfterApplicableBraces(expression);
        boolean isNextMinus=false;
        for (int i=0;i<expression.length();i++){
            char token=expression.charAt(i);
            /*------------------------------ CHECK FOR NUMBERS ------------------------------*/
            if (token>='0' && token<='9'){
                StringBuilder buffer = new StringBuilder();
                buffer.append(token);
                int j=i+1;
                while (j<expression.length() && (expression.charAt(j)>='0' && expression.charAt(j)<='9' || expression.charAt(j)=='.')) {
                    token = expression.charAt(j);
                    buffer.append(token);
                    j++;
                }


                double number=Double.parseDouble(buffer.toString());
                if (isNextMinus){
                    number*=-1;
                    isNextMinus=false;
                }
                mNumbers.push(number);


                i=j-1;
            }

            /*------------------------------ CHECK FOR OPENING BRACE -----------------------------*/
            else if (token=='('){
                mOperators.push('(');
                if (i+1<expression.length()-1){
                    if (expression.charAt(i+1)=='-'){
                        isNextMinus=true;
                        i++;
                    }

                }
            }

            /*------------------------------ CHECK FOR CLOSING BRACE -----------------------------*/
            else if (token==')'){
                while (mOperators.peek()!='('){
                    char operator=mOperators.pop();
                    double numberTwo=mNumbers.pop();
                    double numberOne=mNumbers.pop();
                    if (!mOperators.isEmpty() && mOperators.peek()=='-'){
                        numberOne*=-1;
                        mOperators.pop();
                        mOperators.push('+');
                    }
                    mNumbers.push(solve(operator,numberOne,numberTwo));
                }
                mOperators.pop();
            }

            /*------------------------------ CHECK FOR OPERATORS -----------------------------*/
            else if (token=='+'||token=='-'||token=='×'||token=='÷'){
                if (token=='×'||token=='÷'){
                    if (i+1<expression.length()-1){
                        if (expression.charAt(i+1)=='-'){
                            isNextMinus=true;
                            i++;
                        }
                    }
                }
                while (!mOperators.isEmpty() && hasMorePrecedence(token,mOperators.peek())){

                    char operator=mOperators.pop();
                    double numberTwo=mNumbers.pop();
                    double numberOne=mNumbers.pop();
                    if (!mOperators.isEmpty() && mOperators.peek()=='-'){
                        numberOne*=-1;
                        mOperators.pop();
                        mOperators.push('+');
                    }
                    mNumbers.push(solve(operator,numberOne,numberTwo));
                }

                mOperators.push(token);
            }

            else if (token=='%'){
                mNumbers.push(mNumbers.pop()/100d);
            }
        }

        while (!mOperators.isEmpty() && mNumbers.size()>1){
            char operator=mOperators.pop();
            double numberTwo=mNumbers.pop();
            double numberOne=mNumbers.pop();
            if (!mOperators.isEmpty() && mOperators.peek()=='-'){
                numberOne*=-1;
                mOperators.pop();
                mOperators.push('+');
            }
            mNumbers.push(solve(operator,numberOne,numberTwo));
        }
        double answer=mNumbers.pop();
        if (mOperators.size()>0){
            if (mOperators.pop()=='-'){
                answer*=-1;
            }
        }
        return String.valueOf(answer);
    }


    private double solve(char operator, double numberOne, double numberTwo){
        switch (operator){
            case '+':
                Log.e("Solving", numberOne +"+"+ numberTwo);
                return numberOne+numberTwo;
            case '-':
                Log.e("Solving", numberOne +"-"+ numberTwo);
                return numberOne-numberTwo;
            case '×':
                Log.e("Solving", numberOne +"×"+ numberTwo);
                return numberOne*numberTwo;
            case '÷':
                Log.e("Solving", numberOne +"÷"+ numberTwo);
                return numberOne/numberTwo;
        }
        return 0;
    }

    private boolean hasMorePrecedence(char newOperator,char oldOperator){
        return ((newOperator == '+' || newOperator == '-') && (oldOperator == '×' || oldOperator == '÷')) || ((newOperator == '×' || newOperator == '÷') && (oldOperator == '×' || oldOperator == '÷'));
    }

    private String equalizeBraces(String expression){
        int bracesLeftUnclosed=0;
        for (int i=0;i<expression.length();i++){
            char token=expression.charAt(i);
            if (token=='('){
                bracesLeftUnclosed++;
            }
            else if (token==')'){
                bracesLeftUnclosed--;
            }
        }

        StringBuilder expressionBuilder = new StringBuilder(expression);
        for (int i = 0; i<bracesLeftUnclosed; i++){
            expressionBuilder.append(")");
        }
        expression = expressionBuilder.toString();
        return expression;
    }

    private String insertMultiplyBeforeAndAfterApplicableBraces(String expression){
        for (int i=0;i<expression.length();i++){
            char token=expression.charAt(i);
            if (token=='(' && i-1>=0){
                if (expression.charAt(i-1)>='0' && expression.charAt(i-1)<='9'){
                    expression=expression.substring(0,i)+"×"+expression.substring(i);
                }
                else if (expression.charAt(i-1)==')'){
                    expression=expression.substring(0,i+1)+"×"+expression.substring(i+1);
                }
            }
            else if (token==')' && i+1<expression.length()){
                if (expression.charAt(i+1)>='0' && expression.charAt(i+1)<='9'){
                    expression=expression.substring(0,i+1)+"×"+expression.substring(i+1);
                }
            }
        }
        return expression;
    }

}
