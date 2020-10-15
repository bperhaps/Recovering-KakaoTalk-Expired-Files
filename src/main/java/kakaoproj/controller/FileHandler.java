package kakaoproj.controller;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;

class FileHandler {
    static void exploreDir(String fromSrc, String toSrc) {
        try {
            File dir = new File(fromSrc);
            File[] fileList = dir.listFiles();

            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile()) {
                    FileHandler.fileTransform(file, toSrc);
                } else if (file.isDirectory()) {
                    exploreDir(file.getCanonicalPath().toString(), toSrc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void fileTransform(File file, String toSrc) throws Exception{
        String extension = "";
        FileInputStream inputStream = new FileInputStream(file.getCanonicalPath());
        if( (extension = getExtension(file)) == ""){
            return ;
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        File folder = new File(toSrc +
                "/" +
                sf.format(file.lastModified()));

        if(!folder.exists()) {
            folder.mkdirs();
        }

        FileOutputStream outputStream = new FileOutputStream(toSrc +
                "/" +
                sf.format(file.lastModified()) +
                "/" +
                file.getName() +
                "." +
                getExtension(file));


        FileChannel fcin = inputStream.getChannel();
        FileChannel fcout = outputStream.getChannel();

        long size = fcin.size();
        fcin.transferTo(0, size, fcout);

        fcin.close();
        fcout.close();

        outputStream.close();
        inputStream.close();
    }

    static private String getExtension(File filename) throws Exception {
        FileInputStream is = null;
        try {
            Metadata metadata = new Metadata();
            is = new FileInputStream(filename);
            ContentHandler contenthandler = new BodyContentHandler();
            Parser parser = new AutoDetectParser();
            ParseContext pc = new ParseContext();

            metadata.set(Metadata.RESOURCE_NAME_KEY, filename.getName());
            parser.parse(is, contenthandler, metadata, pc);

            if( is != null )
                is.close();

            return metadata.get(Metadata.CONTENT_TYPE).split("/")[1];
        } catch (Exception e){
            e.printStackTrace();

            if( is != null )
                is.close();

            return "";
        }
    }
}
