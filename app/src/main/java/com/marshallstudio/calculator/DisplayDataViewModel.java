package com.marshallstudio.calculator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Objects;


public class DisplayDataViewModel extends ViewModel {
    private MutableLiveData<String> mExpression;
    private MutableLiveData<String> mResult;
    private char[] mOperators ={'+','-','×','÷'};
    int mBraces=0;


    public MutableLiveData<String> getExpression(){
        if (mExpression==null){
            mExpression=new MutableLiveData<>();
            mExpression.setValue("");
        }
        return mExpression;
    }

    public void appendToExpression(String character){
        if (mExpression==null){
            mExpression=new MutableLiveData<>();
            mExpression.setValue("");
        }
        //Return if user gave multiply, divide, closing brace or dot as first input. Reject it.
        if (Objects.requireNonNull(mExpression.getValue()).length()==0 && (character.charAt(0)=='×' || character.charAt(0)=='÷' || character.charAt(0)==')'|| character.charAt(0)=='.' || character.charAt(0)=='+'|| character.charAt(0)=='%')){
            return;
        }
        //Return if user tried entering multiple dots or if the previous character before dot wasn't a number
        else if (character.charAt(0)=='.' && !(mExpression.getValue().charAt(mExpression.getValue().length()-1)>='0' && (mExpression.getValue().charAt(mExpression.getValue().length()-1)<='9'))){
            return;
        }




        else if (character.charAt(0)=='.'){
            int length=mExpression.getValue().length();
            for (int i=length-1;i>=0;i--){
                if (mExpression.getValue().charAt(i)=='.'){
                    return;
                }
                else if (mExpression.getValue().charAt(i)=='+' || mExpression.getValue().charAt(i)=='-'|| mExpression.getValue().charAt(i)=='×' ||mExpression.getValue().charAt(i)=='÷'||mExpression.getValue().charAt(i)=='('||mExpression.getValue().charAt(i)==')'){
                    break;
                }
            }
        }

        else if ((character.charAt(0)=='+'||character.charAt(0)=='×'||character.charAt(0)=='÷'||character.charAt(0)=='-'||character.charAt(0)=='(' ||character.charAt(0)==')' )&&mExpression.getValue().length()>0&&  mExpression.getValue().charAt(mExpression.getValue().length()-1)=='.'){
            return;
        }

        else if (character.charAt(0)=='('){
            mBraces++;
        }

        else if (character.charAt(0)==')'){
            if (mBraces>0 && ((mExpression.getValue().length()>0 && mExpression.getValue().charAt(mExpression.getValue().length()-1)>='0' && mExpression.getValue().charAt(mExpression.getValue().length()-1)<='9') || mExpression.getValue().charAt(mExpression.getValue().length()-1)==')')){
                mBraces--;
            }
            else {
                return;
            }

        }


        else if (character.charAt(0)=='+'||character.charAt(0)=='×'||character.charAt(0)=='÷' ){
            if (mExpression.getValue().length()>0 && mExpression.getValue().charAt(mExpression.getValue().length()-1)=='('){
                return;
            }
        }



        boolean isPreviousOperator=false;
        for (char op : mOperators){
            if (mExpression.getValue().length()>0 && (mExpression.getValue().charAt(mExpression.getValue().length()-1)==op)){
                isPreviousOperator=true;
            }
        }

        boolean isDuplicateOperator=false;
        if (isPreviousOperator){
            for (char op : mOperators){
                if (character.charAt(0)=='-' && mExpression.getValue().length()>0){
                    char previousCharacter=mExpression.getValue().charAt(mExpression.getValue().length()-1);
                    if (previousCharacter=='-' ||previousCharacter=='+' ){
                        isDuplicateOperator=true;
                        break;
                    }
                }
                else if (character.charAt(0)==op) {
                    isDuplicateOperator=true;
                    break;
                }
            }
        }

        if (!isDuplicateOperator){
            mExpression.setValue(mExpression.getValue()+character);
        }


    }

    public void removeCharacter(){
        if (!Objects.requireNonNull(mExpression.getValue()).isEmpty()){
            if (mExpression.getValue().charAt(mExpression.getValue().length()-1)=='('){
                mBraces--;
            }
            else if (mExpression.getValue().charAt(mExpression.getValue().length()-1)==')'){
                mBraces++;
            }
            mExpression.setValue(Objects.requireNonNull(mExpression.getValue()).substring(0,mExpression.getValue().length()-1));

        }

    }

    public void clearExpression(){
        mExpression.setValue("");
        mResult.setValue("");
        mBraces=0;
    }


    

    public void solveExpression(){
        if (mResult==null){
            mResult=new MutableLiveData<>();
        }
        if (Objects.requireNonNull(mExpression.getValue()).length()==0){
            return;
        }
        if (mExpression.getValue().length()>0 && (mExpression.getValue().charAt(mExpression.getValue().length()-1)>='0' && mExpression.getValue().charAt(mExpression.getValue().length()-1)<='9' || mExpression.getValue().charAt(mExpression.getValue().length()-1)==')'|| mExpression.getValue().charAt(mExpression.getValue().length()-1)=='%')){
            ExpressionSolver expressionSolver=new ExpressionSolver();
            mResult.setValue(expressionSolver.solveExpression(Objects.requireNonNull(mExpression.getValue())));
        }

    }


    public MutableLiveData<String> getResult(){
        if (mResult==null){
            mResult=new MutableLiveData<>();
            mResult.setValue("");
        }
        return mResult;
    }
}
