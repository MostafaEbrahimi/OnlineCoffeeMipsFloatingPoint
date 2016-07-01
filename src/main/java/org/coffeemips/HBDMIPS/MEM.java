
package org.coffeemips.HBDMIPS;

import java.util.ArrayList;
import java.util.List;
import org.coffeemips.memory.AddressAllocator;
/**
 * Represents Memory Stage.
 * @author HBD
 */
public class MEM {

        /**
         * Stored as a List of String Represents Memory.
         */
	List<String> data_mem;
        /**
         * EXE/MEM Pipeline Register of MEM stage.
         */
	EXE_MEM exemem;
        /**
         * MEM/WB Pipeline Register of MEM stage.
         */
	MEM_WB memwb;
        /**
         * ???????????????????????????????????????????????????????????????????
         */
	IF stage_if;

	public MEM(EXE_MEM exemem, MEM_WB memwb, IF stage_If) {
		this.exemem = exemem;
		this.memwb = memwb;
		this.stage_if = stage_If;
		data_mem = new ArrayList<>(org.coffeemips.FileHandler.FileIO.FiletoStringArray("dataCache.txt"));
	}
    public void isFloat(EXE_MEM exe){
        this.exemem=exe;
    }
        /**
         * Do the job of Memory.
         * This includes:
         * 1- Get controlBits from EXE/MEM Pipeline Register.
         * 2- If MEM_Write is Set then we should write Data which 
         *    is stored in RT_DATA in EXE/MEM, to the address of 
         *    memory that ALU_Result points to.
         * 3- If MEM_READ is Set then we should read Data which
         *    ALU_Result points to its address in memory.
         * 4- Store ALU_Result, WriteRegister, controlBits in 
         *    in MEM/WB Pipeline Register.
         */
	public MEM_WB action(boolean modebit,org.coffeemips.memory.AddressAllocator aa) {
		boolean MEM_READ = (exemem.getControlBits().charAt(5)) == '0' ? false
				: true;
		boolean MEM_WRITE = (exemem.getControlBits().charAt(6)) == '0' ? false
				: true;
        if(exemem.getWrite_Register()==-1){
            memwb.setALU_result(exemem.getALU_result());
            memwb.setWrite_Register(exemem.getWrite_Register());
            memwb.setControlBits(exemem.getControlBits());
            return this.memwb;
        }
		if (MEM_WRITE) {
                        if(this.exemem.controlBits.charAt(0)=='1'){
			data_mem.set((int)exemem.getALU_result(), Float.toString(exemem.getRT_DATA()));
			System.out.println("datamem with this address : "+ exemem.getALU_result() + "\t"+ data_mem.get((int)exemem.getALU_result()));
                        }else{
                            int res=(int)exemem.getRT_DATA();
                            data_mem.set((int)exemem.getALU_result(), Integer.toString(res));
                        }
		}
		// MEM_READ
		if (MEM_READ) {
                    String data;
                    if(this.exemem.controlBits.charAt(0)=='1'){
                        int tmp = (int)exemem.getALU_result();
                        data=data_mem.get(tmp);
                        memwb.setREAD_DATA(Float.parseFloat(data));
                        memwb.setALU_result(exemem.getALU_result());
                        memwb.setWrite_Register(exemem.getWrite_Register());
                        memwb.setControlBits(exemem.getControlBits());
                    }
                    else{
                        if(modebit){
                            int i =0 ;
                            data = aa.getMemory().get(aa.parse8DigitHex(i));
                        }else{
                        
                            data = data_mem.get((int)exemem.getALU_result());
                        }
                        
                        memwb.setREAD_DATA(Integer.parseInt(data));
                        memwb.setALU_result((int)exemem.getALU_result());
                        memwb.setWrite_Register(exemem.getWrite_Register());
                        memwb.setControlBits(exemem.getControlBits());
                    }
                }

        memwb.setALU_result(exemem.getALU_result());
        memwb.setWrite_Register(exemem.getWrite_Register());
        memwb.setControlBits(exemem.getControlBits());
        return this.memwb;
	}
        
        
        /**
         * View current Memory.
         * @return Print - memory in a String each cell in a line formatted. 
         */
        public boolean isint(String str){
            try{
                Integer.parseInt(str);
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        public String print() {
            String print="";
            for (int i = 0; i < data_mem.size(); i++) {
                if(isint(data_mem.get(i))) {
                    print += "Cell  [" + i + "] : " + Integer.parseInt(data_mem.get(i)) + "\n";
                }else{
                    print += "Cell  [" + i + "] : " + Float.parseFloat(data_mem.get(i)) + "\n";
                }
            }
            return print;
        }
}