package org.server.biz;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by Mostafa on 6/28/2016.
 */
public class FileServer {

    /**
     * root directory of file-server
     */
    private String rootPath="C:\\D\\java\\CoffeeMips\\CoffeeMIPS\\FileServerRoot";


    /**
     *
     * get name of a file and return
     * the this file from root directory
     * of file server
     * @param name
     * @return FileReader
     * @throws FileNotFoundException
     */
    public FileReader getFile(String name) throws FileNotFoundException {
        String filePath=rootPath+name;
        File file =new File(filePath);
        FileReader fileReader = null;
        System.out.println("file requested"+file.getName());
        if(file.exists() && file.isFile()){
            fileReader=new FileReader(filePath);
        }
        else {
            System.out.println("File not Exist:"+file.getName());
        }
        return fileReader;
    }

    /**
     * save a file to the root
     * directory of the file-server's
     * root path
     * @param is
     * @param filepath
     * @throws IOException
     */
    public static void saveFile(InputStream is, String filepath) throws IOException {
        File file =new File(filepath);
        if(file.exists()){
            file.delete();
        }
        PrintWriter writer = new PrintWriter(file,"UTF-8");
        StringWriter writer1 = new StringWriter();
        IOUtils.copy(is, writer1, "UTF-8");
        String theString = writer1.toString();
        writer.print(theString);
        writer.close();
    }

    /**
     * Read a file and
     * return a FileReader that you can
     * use it for get file content
     * @param fileName
     * @return
     */
    public FileReader readFromFile(String fileName){
        try{
            FileReader inputFile = new FileReader(fileName);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            return inputFile;

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }
        return null;
    }

    public static InputStream convertStringToInputStream(String string){
        InputStream inputStream=new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }

    /**
     * get root path of the
     * file-server directory
     * @return
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * set root path
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }


}
