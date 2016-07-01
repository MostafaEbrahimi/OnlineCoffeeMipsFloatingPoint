/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.coffeemips.FPU_;
//res_des + pc new +exe_mem
import org.coffeemips.HBDMIPS.EXE_MEM;

/**
 *
 * @author Msiavashi
 */
public class A4 {//computer va ID ...va entekhabe reg_des
    int bits = 23, bias = 127, Total = 32;
    A3_A4 a3a4 = new A3_A4();
    public EXE_MEM exemem = new EXE_MEM();
    public EXE_MEM action(A3_A4 aa ){
        this.a3a4=aa;
        float f=this.a3a4.f;
        float f3=this.a3a4.f;
        float e3=this.a3a4.e;
        float e=this.a3a4.e;
        float R1=this.a3a4.R1;
        float R2=this.a3a4.R2;
        float result;
        int f3realSign=this.a3a4.f3realSign;
        result = (float) (f3realSign*f*Math.pow(2,e-bias));
        Structure Struct3=new  Structure();
        Struct3 = Util.IEEERepresentation(Struct3, result, bits, bias);


        // Round to nearest Even
        float tmp = 0;
        if ( R1 != 0 && R2 != 0) {
            tmp = R1;
        }
        else if (R1 == 0 || R2 == 0) {
            tmp = 0;
        }
        /*TODO: replaced with another calue check it out*/
        f = (float) (f3 + (tmp)*Math.pow(2,-bits));
        float temp = (float) ((f3*Math.pow(2,bits-1)) - Math.floor(f3*Math.pow(2,bits-1)));
        int lastBit = 0;
        if (temp >= 0.5  ) { lastBit = 1;}

        float feven = f;
//	fodd = f;
        if ((R1 == 1) && (R2 == 0)) { feven = (float) (feven + lastBit*Math.pow(2,-bits));
        }
//	if ((R1 == 1) && (R2 == 0)) { fodd = fodd + (lastBit^1)*pow(2,-bits)}
        e = e3;
        result = (float) (f3realSign*feven*Math.pow(2,e-bias));
        Struct3 = Util.IEEERepresentation(Struct3, result, bits, bias);

        //use this.a3a4.signExt
        float MyResult = Struct3.IEEE;
        System.out.println(MyResult);
        this.exemem.setALU_result(MyResult);
        this.exemem.setControlBits(this.a3a4.controlBits);
        // this.exemem.setNew_PC();///set karaden pc jadid
        this.exemem.setRT_DATA(this.a3a4.rt);//rt data

        if(MyResult==0){
            this.exemem.setZERO(true);
        }
        else{
            this.exemem.setZERO(false);
        }
        if(this.a3a4.controlBits.charAt(13)=='1'){///
            this.exemem.setWrite_Register(this.a3a4.reg_des);
        }
        else{
            this.exemem.setWrite_Register(this.a3a4.reg_des);
        }
        return this.exemem;


    }

    public int new_pc(float res,char signal,int pc,String imm){
        int ans=pc;
        if(res==0 && signal=='1'){
            ans = Integer.parseInt(imm, 2);///////////////IMPORTANT should never execute ......

        }else{
            ans=pc;
        }
        return ans;
    }
}
