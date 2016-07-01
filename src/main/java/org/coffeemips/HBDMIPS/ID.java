package org.coffeemips.HBDMIPS;

// note ourselves about write back

import org.coffeemips.FPU.Controller;
import org.coffeemips.FPU.Registers;
import org.coffeemips.FPU_.ID_FLOAT;

/**
 * This class represents <b>Instruction Decode</b> stage.
 * Instructions index start from left to right.   0 1 2 .. 31  :|
 * @author HBD
 */
public class ID{
	public Register_file regfile = new Register_file("FILE");// 32 of 32bit
    public Registers reg_float=new Registers();                                               //MIPS architecture
    public ID_FLOAT idFLoat;                                         //Registers.
	private CU cu = new CU(); //Control Unit
	private IF_ID ifid;// IF/ID for ID stage.
	private ID_EXE idexe;// ID/EXE for ID stage.
	private IF stage_if;// 
    private Controller regFile=new Controller();
    public String cu_result="";

	public ID(IF_ID ifid, ID_EXE idexe,IF stage_if) {
        this.idFLoat=new ID_FLOAT();/////need to fix
        this.ifid = ifid;
        this.idexe = idexe;
        this.stage_if = stage_if;
	}
        

    /**
     * ِِDo the job of InstructionDecode stage.
     * This includes:
     * 1- Fetch instruction from IF/ID.
     * 2- Set opcode of Control Unit.
     * 3- Check J-Type or I-Type so Set PC.
     * 4- Set Register File in ID stage.
     *      [Considering exception of Write Data]
     * 5- Save SignExtended Address of InstructionCode
     *    to ID/EXE Pipeline Register.
     * 6- Save 13bit ControlBits come from CU
     *    to ID/EXE Pipeline Register.
     * 7- Save RS, RT Addresses & Data to ID/EXE Pipeline Register.
     * 8- Save current PC  to ID/EXE Pipeline Register.
     */
    public boolean isBranchFloat(){
        if(stage_if.getInstruction().substring(0,6).equals("010001") && stage_if.getInstruction().substring(6,11).equals("01000")){
            return true;
        }
        return false;
    }

	public Object action(boolean mode) {

		String instruction = ifid.getIns();
		Object ans;
                cu.setOpcode(instruction.substring(0, 6));
                this.cu_result = cu.action(instruction.substring(0, 6),instruction);
                //System.out.println(cu_result + " %%%%%%%%%%");
                if(cu_result.charAt(0)=='0'){
                    if(cu_result.equals("")){/// for bc1t + singal hahye controll + alu faghat zarb dar 2 kune ....
                        idexe.setSignExt(signExt(instruction.substring(16, 32)));
                        idexe.setControlBits(cu_result);
                        idexe.setRS_DATA(ifid.getPC());
                        idexe.setRT_DATA(0);
                        //idexe.setRT(RT);
                        //idexe.setRD(RD);
                        idexe.setPC(ifid.getPC());
                    }
                
    //		if (Integer.parseInt(instruction.substring(0, 6),2) == 2){
    //                        //it means I-Type or J-Type instruction,
    //                        //so PC should change. 
    //			stage_if.setPC(Integer.parseInt(instruction.substring(6, 32),2)); 
    //		}
                    // R-Type instruction format: 6bit opcode - 5bit RS - 5bit RT
                    //                            5bit RD - 5bit shamt - 6bit func.
                    int RS = Integer.parseInt(instruction.substring(6, 11), 2);
                    int RT = Integer.parseInt(instruction.substring(11, 16), 2);
                    int RD = Integer.parseInt(instruction.substring(16, 21), 2);

                    int RS_DATA = regfile.getRegfile(RS);
                    int RT_DATA = regfile.getRegfile(RT);

                    //Save all SignExtend, ControlBits[Which come from CU],
                    //Register Source, Rgister Temp and Register Destination,
                    //RegisterFile Datas stored in RS & RT addresses,
                    //ID [Or current] stage's Program counter.
                    //All in ID/EXE Pipeline Register.
                    idexe.setSignExt(signExt(instruction.substring(16, 32)));

                    if(cu_result.charAt(11)=='1'){// means if instruction is jump
                        idexe.setSignExt(("0000".concat(instruction.substring(6, 32))).concat("00"));
                    }
                    idexe.setControlBits(cu_result);
                    idexe.setRS_DATA(RS_DATA);
                    idexe.setRT_DATA(RT_DATA);
                    idexe.setRT(RT);
                    idexe.setRD(RD);
                    idexe.setPC(ifid.getPC());

                }
                
                else{/////need to commit
                    if(this.ifid.ins.substring(0,6).equals("010001") && this.ifid.ins.substring(6,11).equals("10000")){
                        int FS=Integer.parseInt(instruction.substring(16,21), 2);
                        int FT=Integer.parseInt(instruction.substring(11, 16), 2);
                        float FS_DATA = this.reg_float.getReg(FS);//regfile.getRegfile(RS);
                        float FT_DATA = this.reg_float.getReg(FT);
                        FT_DATA*=-1;
                        this.idFLoat.PC=this.idexe.PC;
                        this.idFLoat.RS_DATA=FS_DATA;
                        this.idFLoat.RT_DATA=FT_DATA;
                        this.idFLoat.controlBits=cu_result;
                        this.idFLoat.RT=FT;
                        this.idFLoat.signExt=this.signExt(instruction.substring(16, 32));
                        ans=this.idFLoat;
                        return ans;
                    }
                    if(this.ifid.ins.substring(0,6).equals("110001") || this.ifid.ins.substring(0,6).equals("111001") ){
                        int RS=Integer.parseInt(instruction.substring(6,11), 2);
                        int FT=Integer.parseInt(instruction.substring(11, 16), 2);
                        //String signextends=this.ifid.ins.substring(16, 32);
                        int RS_DATA = regfile.getRegfile(RS);
                        float FT_DATA = this.reg_float.getReg(FT);
                        this.idFLoat.PC=this.idexe.PC;
                        this.idFLoat.RS_DATA=RS_DATA;
                        this.idFLoat.RT_DATA=FT_DATA;
                        this.idFLoat.controlBits=cu_result;
                        this.idFLoat.RT=FT;
                        this.idFLoat.signExt=this.signExt(instruction.substring(16, 32));
                        ans=this.idFLoat;
                        return ans;
                    }
                    else if(this.ifid.ins.substring(0,6).equals("010001")){
                    int RD = Integer.parseInt(instruction.substring(16, 21), 2);
                    int RS = Integer.parseInt(instruction.substring(11, 16), 2);
                    int RT = Integer.parseInt(instruction.substring(21,26), 2);
                    float RS_DATA = this.regFile.getFloatRegisters().getReg(RS);
                    float RT_DATA = this.regFile.getFloatRegisters().getReg(RT);///
                    System.out.println(RD +" bbv " + RS+ " " + RD);
                    this.idFLoat.PC=this.idexe.PC;
                    this.idFLoat.RD=RD;
                    this.idFLoat.RS_DATA=RS_DATA;
                    this.idFLoat.RT_DATA=RT_DATA;
                    this.idFLoat.controlBits=this.signExt(cu_result);
                    this.idFLoat.RT=RT;
                    this.idFLoat.signExt=instruction.substring(16,32);
                    ans=this.idFLoat;
                    return ans;
                    }
                }
                return null;
		
	}
//        public String signexetnd(String number){
//            String ans=number;
//            if(number.charAt(15)=='0'){
//                for(int counter=0;counter<15;counter++){
//                    ans='0'+ans;
//                }
//            }else{
//                for(int counter=0;counter<15;counter++){
//                    ans='1'+ans;
//                }
//            }
//            return ans;
//        }
        
    /**
     *
     * @return regfile - all 32 of 32bit registers as a
     */
	public Register_file getRegfile() {
		return regfile;
	}
        

    /**
     *
     * @param regfile - set all 32 of 32bit registers via
     * an instance of Register_file class.
     */
	public void setRegfile(Register_file regfile) {
		this.regfile = regfile;
	}

        
    /**
     *
     * @return CU - instance of CU class initialized in
     * ID [this] stage.
     */
	public CU getCu() {
		return cu;
	}

    /**
     * Set Control Unit.
     * afterward returned CU instance can take Opcode
     * via its action method.
     * @param cu - an instance of CU [Control Unit] Class.
     */
    public void setCu(CU cu) {
    this.cu = cu;
}


    /**
     *
     * @return ifid - instance of IF/ID currently existing in
     * ID stage.
     */
	public IF_ID getIfid() {
		return ifid;
	}

        
    /**
     * Set the side of IF/ID Pipeline Register
     * existing in ID stage.
     * @param ifid - an instance of IF/ID class.
     */
	public void setIfid(IF_ID ifid) {
		this.ifid = ifid;
	}

        
    /**
     *
     * @return idexe -an instance of ID/EXE class currently existing
     * in ID stage.
     */
	public ID_EXE getIdexe() {
		return idexe;
	}

        
    /**
     * Set the side of ID/EXE Pipeline Register
     * existing in ID stage.
     * @param idexe - an instance of ID/EXE class.
     */
	public void setIdexe(ID_EXE idexe) {
		this.idexe = idexe;
	}

        
    /**
     * SignExtend I-Type or J-Type Instructions.
     * @param inp - 16bit address existing in the right side of
     * the instructionCode. In convention of HBD bits 16 to 31,
     * from left to Right :!
     * @return out - 32bit extended address.
     */
	private String signExt(String inp) {
		String out = null;
		if (inp.charAt(0) == '1') {
			out = "1111111111111111" + inp;
		} else if (inp.charAt(0) == '0') {
			out = "0000000000000000" + inp;
		}
		return out;
	}

    public Registers getReg_float() {
        return reg_float;
    }
}
