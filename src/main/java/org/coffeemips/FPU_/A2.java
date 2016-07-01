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
public class A2 {
    A1_A2 a1a2 = new A1_A2();
    A2_A3 a2a3 = new A2_A3();
    int bits = 23, bias = 127, Total = 32;
    public A2_A3 action(A1_A2 temp){
        this.a1a2=temp;
        Structure Struct1=new Structure();
        Struct1=Util.IEEERepresentation(Struct1, this.a1a2.rs,bits ,bias);
        Structure Struct2=new Structure();
        Struct2=Util.IEEERepresentation(Struct2, this.a1a2.rt,bits ,bias);
       
        float f1=this.a1a2.f1;
        float f2=this.a1a2.f2;
        int e1 = Struct1.e;
        int e2 = Struct2.e;
        
        if(this.a1a2.bitsDiff<0){//e1<e2
            f2 = (float) (f2 / Math.pow(2,this.a1a2.bitsDiff));
            int bitsDiff=this.a1a2.bitsDiff;
            bitsDiff=Math.abs(bitsDiff);
            e2=e1;
            
        }
        else if(this.a1a2.bitsDiff<0){
            e1=e2;
            f1= (float) (f1 / Math.pow(2,this.a1a2.bitsDiff));
        }
         float Fullf1 = f1;
        float Fullf2 = f2;
        this.a2a3.e1=e1;
        f1 = Util.TruncateToBits(f1,bits);
        f2 = Util.TruncateToBits(f2,bits);
        float Extra1 = (float) ((Fullf1 - f1)*Math.pow(2,bits));
        float Extra2 = (float) ((Fullf2 - f2)*Math.pow(2,bits));

        Struct1.G = 0;
        if (Extra1 >=0.5 ){
            Struct1.G = 1;
            Extra1 = (float) (Extra1 - 0.5);
        }

        Struct2.G = 0;
        if (Extra2 >=0.5 ){
            Struct2.G = 1;
            Extra2 = (float) (Extra2 - 0.5);
        }

        Struct1.R = 0;
        if (Extra1 >= 0.25 ){
            Struct1.R = 1;
            Extra1 = (float) (Extra1 - 0.25);
        }

        Struct2.R = 0;
        if (Extra2 >= 0.25){
            Struct2.R = 1;
            Extra2 = (float) (Extra2 - 0.25);
        }

        Struct1.S = 0;
        if (Extra1 > 0 ){
            Struct1.S = 1;
        }

        Struct2.S = 0;
        if (Extra2 > 0 ){
            Struct2.S = 1;
        }

        int carryin = 0;

        int f1sign = Struct1.realSign;
        int f2sign = Struct2.realSign;

        float mna1= f1sign*f1;

        float mna2= f2sign*f2;

        /*TODO: check the != 0 condition*/
        if(!(Struct1.G != 0|| Struct1.R != 0 || Struct1.S != 0) && !(Struct2.G != 0|| Struct2.R !=0|| Struct2.S != 0))
        {
        }
        else
        {
            if(((f1sign != f2sign) && (f1 > f2) && !(Struct1.G != 0|| Struct1.R != 0|| Struct1.S != 0)))
            {
                carryin = (int) Math.pow(2,-23);

                if(f1 < f2)
                {
                    if(f2sign == -1)
                        mna2 = f2sign*f2 + carryin;
                    else
                        mna2 = f2sign*f2 - carryin;

                }
                else
                {
                    if(f1sign == -1)
                        mna1 = f1sign*f1 + carryin;
                    else
                        mna1 = f1sign*f1 - carryin;
                }
            }
            else if(((f2sign != f1sign)&& (f2 > f1) && !(Struct2.G != 0|| Struct2.R != 0|| Struct2.S != 0)))
            {
                carryin = (int) Math.pow(2,-23);
                if(f1 < f2)
                {
                    if(f2sign == -1)
                        mna2 = f2sign*f2 + carryin;
                    else
                        mna2 = f2sign*f2 - carryin;
                }
                else
                {
                    if(f1sign == -1)
                        mna1 = f1sign*f1 + carryin;
                    else
                        mna1 = f1sign*f1 - carryin;
                }

            }
        }

        this.a2a3.PC=this.a1a2.PC;
        this.a2a3.controlBits=this.a1a2.controlBits;
        this.a2a3.reg_des=this.a1a2.reg_des;
        this.a2a3.rs=this.a1a2.rs;
        this.a2a3.rt=this.a1a2.rt;
        this.a2a3.mna1=mna1;
        this.a2a3.mna2=mna2;
        this.a2a3.f1sign=f1sign;
        this.a2a3.f2sign=f2sign;
        this.a2a3.f1=f1;
        this.a2a3.f2=f2;
        this.a2a3.e2=e2;
        this.a2a3.rt=this.a1a2.rt;
        this.a2a3.signExt=this.a1a2.signExt;
        return a2a3;
    }
}
