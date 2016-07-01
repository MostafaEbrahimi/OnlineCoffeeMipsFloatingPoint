package org.server.service;



import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.server.biz.FileServer;
import org.server.biz.Main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;

//TODO need to handle session for prevent conflict file codes

/**
 * Created by Mostafa on 6/27/2016.
 */
@Path("/execute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Execute {
    private Main main;
    private boolean debugMode=false;


    /**
     * test your server by sending request to this
     * path http://127.0.0.1/execute/testme
     * this will return you success message
     * to client
     * @param request
     * @return String
     */
    @GET
    @Path("/testme")
    public String testME(@Context HttpServletRequest request){
        System.out.println("request from a client");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("OnlineCoffeeMips","Have a good coffee with coffee mips");
        return jsonObject.toString();
    }


    /**
     * upload file using form <b>using form html</b>
     * and assemble your code
     * @param inputStream
     * @param contentDispositionHeader
     * @param response
     * @return String
     * @throws IOException
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String fileUpload(@FormDataParam("file") InputStream inputStream,
                             @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                             @Context HttpServletResponse response) throws IOException {
        main=new Main();
        return main.runAssemble(inputStream,contentDispositionHeader).toString();
    }


    /**
     * <b>warn:</b> don't use this method
     * instead of this use getLastStateOfRegisters()
     * return register file of last file uploaded
     * probably if session added to this project need
     * for change this method
     * @param request
     * @return String
     */
    @GET
    @Path("/getresgisters")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRegistersOfLastFileUploaded(@Context HttpServletRequest request){
        if(!debugMode){
            if(main==null){
                return new JSONObject().put("error","cannot load your file").toString();
            }
//            return main.runRun(main.getFilepath()).toString();
            return "{}";
        }
        return new JSONObject().put("debugeMode","debug mode is enable please disable it").toString();
    }


    /**
     * get you the last state of registers that
     * code was upload and runned
     * @param request
     * @return registerState
     */
    @GET
    @Path("/getregisterfile")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLastStateOfRegisters(@Context HttpServletRequest request){
        if(this.main.getLastRegistersState()!=null){
            return main.getLastRegistersState().toString();
        }
        if(main.getLastRegistersState()!=null){
            return new JSONObject().put("error","code not been run").toString();
        }
        return new JSONObject().put("registerLastState","[]").toString();
    }


    /**
     * like registers but return object
     * of memory state
     * @param request
     * @return memoryStateJSONObject
     */
    @GET
    @Path("/getmemory")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMemoryLastState(@Context HttpServletRequest request){
        if(main==null){
            return new JSONObject().put("error","nothing executed").toString();
        }
        if(main.getLastMemoryState()==null){
            return new JSONObject().put("error","code not been run").toString();
        }
        return main.getLastMemoryState().toString();
    }


    /**
     * if you are in debug mode
     * it will run next instruction
     * @param request
     * @return
     */
    @GET
    @Path("/nextins")
    @Produces(MediaType.APPLICATION_JSON)
    public String runNextInstruction(@Context HttpServletRequest request){
        if(this.debugMode==true && this.main!=null){
            return this.main.runNextInstruction().toString();
        }
        return new JSONObject().put("error","debug mode is disable").toString();
    }


    /**
     * switch between debug and run mode
     * send your mode in url like:
     * 127.0.0.1/execute/debug/enable
     * or for disable debug mode
     * 127.0.0.1/execute/debug/disable
     * @param mode
     * @param request
     * @return SuccessORError
     */
    @GET
    @Path("/debug/{mode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String enableDebug(@PathParam("mode") String mode,@Context  HttpServletRequest request){

        if(mode.equals("enable")){
            if(this.debugMode==true){
                return new JSONObject().put("error","debug mode was enable").toString();
            }
            this.debugMode=true;
            return new JSONObject().put("success","debug mode is enable now").toString();
        }
        if (mode.equals("disable")){
            if(this.debugMode==false){
                return new JSONObject().put("error","debug mode was disable").toString();
            }
            this.debugMode=false;
            return new JSONObject().put("success","debug mode is disable now").toString();
        }
        return new JSONObject().put("error","wrong path /debug/{enable or disable}").toString();
    }


    /**
     * reset registers and memory
     * need to more implementation
     * @param request
     * @return successORError
     */
    @GET
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public String reset(@Context HttpServletRequest request){
        this.main=null;
        return  new JSONObject().put("success","system was reset reset").toString();
    }

    /**
     * receive code from client
     * in json object and run it
     * @param sample
     * @param request
     * @return assembledInstructions
     */
    @POST
    @Path("/getcode")
    public String getCode(String sample,@Context HttpServletRequest request){
        System.out.println(sample);
        JSONObject jsonObject=new JSONObject(sample);
        String code= (String) jsonObject.get("code");
        if(main==null){
            main=new Main();
        }
        InputStream is=FileServer.convertStringToInputStream(code);
        return this.main.runAssemble(is,null).toString();
    }

    @GET
    @Path("/run")
    public String run(@Context HttpServletRequest request){
        if(main!=null){
            main.runAllOfAction(main.getFilepath());
            return new JSONObject().put("success","run this method").toString();
        }
        return new JSONObject().put("error","no code for run").toString();
    }


}
