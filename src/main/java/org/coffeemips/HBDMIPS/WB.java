package org.coffeemips.HBDMIPS;
/**
 * Represents WriteBack stage.
 * @author HBD
 */
public class WB {
    
        /**
         * Uses instance of InstructionDecode initialized
         * earlier; Because <b>RegisterFile</b> is contained in it! :|
         */
	ID id;
        
        /**
         * MEM/WB Pipeline Register of MEM stage. 
         */
	MEM_WB memwb;//

	public WB(ID id, MEM_WB memwb) {
		this.id = id;
		this.memwb = memwb;
	}
	public void isFloat(MEM_WB wb){
		this.memwb=wb;
	}
        
        /**
         * Do the job of WriteBack.
         * This includes:
         * 1- Get MEM2REG, REG_WRITE controlBits from MEM/WB Pipeline Register.
         * 2- If something should be written to RegisterFile;
         *      Then decide according to the MEM2REG which one of
         *      ALU_Result [R-Type] or READ_Data [Load] should be used.
         */
	public void action(boolean modebit) {
		boolean MEM2REG = (memwb.getControlBits().charAt(9)) == '0' ? false
				: true;
		boolean REG_WRITE = (memwb.getControlBits().charAt(1)) == '0' ? false
				: true;
		if(this.memwb.getWrite_Register()==-1){
			if(this.memwb.getALU_result()==0f){
				id.reg_float.flag_code=1;
			}else{id.reg_float.flag_code=0;}
			return ;
		}
		if (REG_WRITE) {
			if (!MEM2REG){
                                if(this.memwb.controlBits.charAt(0)=='1'){
                                    id.reg_float.setReg(this.memwb.Write_Register, this.memwb.getALU_result());
                                }else{
				id.regfile.setReg(memwb.Write_Register,(int)memwb.getALU_result());
                                }
                        }
                        else{
                            if(this.memwb.controlBits.charAt(0)=='1'){
                                id.reg_float.setReg(this.memwb.Write_Register, this.memwb.getREAD_DATA());
                            }else{
				id.regfile.setReg(memwb.Write_Register,(int)memwb.getREAD_DATA());
                        
                            }
                        }
                }
	}
}