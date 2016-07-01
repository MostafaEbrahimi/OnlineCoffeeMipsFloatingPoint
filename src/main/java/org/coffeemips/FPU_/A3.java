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
public class A3 {
    A2_A3 a2a3 = new A2_A3();
    A3_A4 a3a4 = new A3_A4();
    int bits = 23, bias = 127, Total = 32;
    public A3_A4 action(A2_A3 aa){
        this.a2a3=aa;
        float mna1=this.a2a3.mna1;
        float mna2=this.a2a3.mna2;
        float f3 = mna1 + mna2;
        int e1=this.a2a3.e1;
        int e2=this.a2a3.e2;
        Structure Struct1=new Structure();
        Struct1=Util.IEEERepresentation(Struct1, this.a2a3.rs,bits ,bias);
        Structure Struct2=new Structure();
        Struct2=Util.IEEERepresentation(Struct2, this.a2a3.rt,bits ,bias);
        int f1sign=this.a2a3.f1sign;
        int f2sign=this.a2a3.f2sign;
        float TheNum= (float) (f3*Math.pow(2,e1-bias));
        float f1=this.a2a3.f1;
        float f2=this.a2a3.f2;
        Structure Struct3=new Structure() ;
        Struct3 = Util.IEEERepresentation(Struct3, TheNum, bits, bias);

        boolean f3sign = (f3 < 0);
        int f3realSign = (f3 < 0) ? -1 : 1;
        f3 = Math.abs(f3);

        // Find G, R ans S for f3
        float Fullf3 = f3;
        f3 = Util.TruncateToBits(f3,bits);
        float Extra3 = (float) ((Fullf3 - f3)*Math.pow(2,bits));

        //f3orig = Struct1.realSign*f1 + Math.pow(-1,Struct2.Sign^Subtract)*f2;
        int asign = 0;
        int bsign = 0;
        // post calculation modification
//	    if(Subtract)
        //          GRSf3=Struct1.realSign*(Struct1.G*4 + Struct1.R*2 + Struct1.S)- Struct2.realSign*(Struct2.G*4+Struct2.R*2+Struct2.S);
        //    else

        if(!(Struct1.G != 0|| Struct1.R != 0|| Struct1.S != 0) && !(Struct2.G != 0|| Struct2.R != 0|| Struct2.S !=0))
        {
        }
        else
        {

            if(((f1sign != f2sign ) && (f1 > f2) && !(Struct1.G != 0|| Struct1.R != 0|| Struct1.S !=0)))
            {
                asign = 1;
            }
            else if(((f2sign != f1sign)&& (f2 > f1) && !(Struct2.G != 0|| Struct2.R != 0|| Struct2.S != 0)))
            {
                bsign = 1;
            }

        }

        int GRSf3= (int) (f1sign*(asign*8 + Struct1.G*4 + Struct1.R*2 + Struct1.S)+ f2sign*(bsign*8 + Struct2.G*4+Struct2.R*2+Struct2.S));

        if (GRSf3<0)
        {
            GRSf3=Math.abs(GRSf3) & 7;
        }
        int Gf3=0;
        int Rf3=0;
        int Sf3=0;
        if (GRSf3 >= 4)
        {
            Gf3=1;
            GRSf3-=4;
        }
        if (GRSf3 >=2)
        {
            Rf3=1;
            GRSf3-=2;
        }
        if (GRSf3 == 1)
        {
            Sf3=1;
        }
        f3 = (float) (f3 + Gf3*Math.pow(2,-bits-1) +  Rf3*Math.pow(2,-bits-2)
                        + Sf3*Math.pow(2,-bits-3));
        float result = (float) (f3realSign*f3*Math.pow(2,(e2 - bias)));
        Struct3 = Util.IEEERepresentation(Struct3, result, bits+3, bias);

        Fullf3 = Struct3.f;
        float e3 = Struct3.e;
        float R1, R2;
        if(Math.abs(f3)<1 && e1!=e2)
        {
            f3+=Gf3*Math.pow(2,-bias);
            R1=Rf3;
            R2=Sf3;
        }else{
            R1=Gf3;
            R2=(Sf3 != 0 || Rf3 != 0)? 1:0;
        }
        Struct3 = Util.IEEERepresentation(Struct3, result, bits, bias);
        f3 = Struct3.f;

        if (Struct3.DenF != 0.0f || Struct3.ZeroF != 0){
            f3 = f3;
        } else {
            f3 = f3 + 1;
        }
        this.a3a4.PC=this.a2a3.PC;
        this.a3a4.controlBits=this.a2a3.controlBits;
        this.a3a4.reg_des=this.a2a3.reg_des;
        this.a3a4.R1=R1;
        this.a3a4.R2=R2;
        this.a3a4.e=e3;
        this.a3a4.f=f3;
        this.a3a4.rt=this.a2a3.rt;
        this.a3a4.result=result;
        this.a3a4.f3realSign=f3realSign;
        this.a3a4.signExt=this.a2a3.signExt;
        return a3a4;
    }
}
