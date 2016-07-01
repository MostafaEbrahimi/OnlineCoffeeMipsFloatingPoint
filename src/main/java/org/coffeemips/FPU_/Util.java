/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.coffeemips.FPU_;

/**
 *
 * @author roozbeh
 */
public class Util {
    
    static public Structure IEEERepresentation(Structure structure, float TheNum, int bits, int bias){
        //	if (TheNum==0) {
        structure.IEEE=0;
        structure.error=0;
        structure.maxerror=0;
        structure.Sign= false;
        structure.realSign=0;
        structure.f=0;
        structure.e=0;
        structure.ZeroF=1;
        structure.InfF=0;
        structure.DenF=0;
        structure.NanF= 0;
        structure.OveF= false;
        structure.UndF=false;
        structure.G=0;
        structure.R=0;
        structure.S=0;


        if (TheNum!=0) {
            structure.Sign = (TheNum < 0);
            structure.realSign = (TheNum < 0) ? -1 : 1;
            TheNum=Math.abs(TheNum);

            float Y= (float) (Math.log(TheNum)/Math.log(2));
            float N= (float) Math.floor(Y);
            float Z=Y-N;

            float ffull= (float) (TheNum*Math.pow(2,-N));

            if (ffull >= 2){
                ffull = ffull/2;
                N = N + 1;
            }
            ffull = ffull - 1;
            float fdecimal= (float) Math.floor(ffull*Math.pow(2,bits));

            structure.f= (float) (fdecimal*Math.pow(2,-bits));
            structure.e= (int) (bias+N);
            structure.IEEE= (float) (structure.realSign * (1+structure.f)*Math.pow(2,structure.e-bias));
            structure.UndF = false;
            structure.DenF = 0;


            if (TheNum < Math.pow(2,-bias+1)){
                N = -bias+1;
                ffull= (float) (TheNum/Math.pow(2,N));
                fdecimal= (float) Math.floor(ffull*Math.pow(2,bits));
                structure.f= (float) (fdecimal/Math.pow(2,bits));
                structure.e= 1;
                structure.DenF = 1;
                structure.IEEE= (float) (structure.realSign*structure.f*Math.pow(2,N));
            }


            if (TheNum < Math.pow(2,-bias+1-bits)){
                structure.DenF = 0;
                structure.UndF = false;
            }

            structure.error=TheNum-Math.abs(structure.IEEE);
            structure.maxerror= (float) Math.pow(2,structure.e-bias-bits);


            structure.ZeroF=0;
            structure.InfF = 0;
            structure.NanF = 0;
            if (structure.e==2*bias+1){
                if (structure.f==0){
                    structure.InfF = 1;
                } else {
                    structure.NanF = 1;
                }
            }
            structure.OveF = (structure.e > 2*bias+1);
        }
//        structure.e_str = unsignedToBinStr(Structure.e, Total-bits-1 );
//        structure.f_str = unsignedToBinStr(Structure.bits);
        return structure;
    }
    static public float  TruncateToBits(float Number, int bits){
        if (Number >= 0) {
            return (float) (Math.floor(Number * Math.pow(2,bits)) / Math.pow(2,bits));
        } else {
            return (float) (-Math.floor(-Number * Math.pow(2,bits)) / Math.pow(2,bits));
        }
    }
}
