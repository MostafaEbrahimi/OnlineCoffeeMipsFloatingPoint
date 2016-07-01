/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.coffeemips.FPU_;

/**
 *
 * @author Msiavashi
 */
public class A1 {
    int bits = 23, bias = 127, Total = 32;
    ID_FLOAT idFloat = new ID_FLOAT();
    A1_A2 a1a2 = new A1_A2();

    public A1_A2 action(ID_FLOAT id_float){
        System.out.println("slm***************");
        this.idFloat=id_float;
        //if it was swc1 or lwc1
        if(this.idFloat.controlBits.equals("100010101000000") || this.idFloat.controlBits.equals("110011000100000")) {
            //this.idFloat.RT=this.idFloat.signExt;
            int ans = Integer.parseInt(this.idFloat.signExt, 2);//sing extends
            this.idFloat.RT_DATA = ans;
        }
        Structure Struct1=new Structure();
        Struct1=Util.IEEERepresentation(Struct1, idFloat.RS_DATA,bits ,bias);
        Structure Struct2=new Structure();
        Struct2=Util.IEEERepresentation(Struct2, idFloat.RT_DATA,bits ,bias);

        if(idFloat.controlBits.equals("100010101000000") || idFloat.controlBits.equals("110011000100000")){
            ////lw sw ke bayad rt=meghdare imm ....
            Struct2=Util.IEEERepresentation(Struct2, Integer.parseInt(idFloat.signExt,2),bits ,bias);
        }
        float f1;
        if (Struct1.ZeroF != 0 || Struct1.DenF != 0){
            f1 = Struct1.f;
        }else{
            f1 = Struct1.f + 1;
        }
        // Is B = 0 ?
        float f2;
        if (Struct2.ZeroF != 0 || Struct2.DenF != 0){
            f2 = Struct2.f;
        }else{
            f2 = Struct2.f + 1;
        }
        this.a1a2.f1=f1;
        this.a1a2.f2=f2;
        int e1 = Struct1.e;
        int e2 = Struct2.e;

        /***************alignment step***********/
        int bitsDiff = e1-e2;
        this.a1a2.PC=this.idFloat.PC;
        if(this.idFloat.controlBits.charAt(8)=='0'){
            this.a1a2.reg_des=this.idFloat.RT;
        }else{this.a1a2.reg_des=this.idFloat.RD;}
        if(this.idFloat.controlBits.equals("111000011000000")){
            this.a1a2.reg_des=-1;
        }
        this.a1a2.bitsDiff=bitsDiff;
        this.a1a2.controlBits=this.idFloat.controlBits;
        this.a1a2.rs=this.idFloat.RS_DATA;
        this.a1a2.rt=this.idFloat.RT_DATA;
        this.a1a2.signExt=this.idFloat.signExt;
        return a1a2;
//        if (e1 > e2) {
//            bitsDiff = (e1 - e2);
//            f2 = (float) (f2 / Math.pow(2,bitsDiff));
//            e2 = e1;
//        } else {
//            if (e1 < e2) {
//                bitsDiff = (e2 - e1);
//                f1 = (float) (f1 / Math.pow(2,bitsDiff));
//                e1 = e2;
//            } else {
//                bitsDiff=0;
//            }
//        }

    }
}
