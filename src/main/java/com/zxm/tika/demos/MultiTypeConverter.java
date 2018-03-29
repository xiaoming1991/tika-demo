package com.zxm.tika.demos;

import com.zxm.tika.demos.exception.FileConvertException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.UUID;

public class MultiTypeConverter {

    public String convert2TextFile(File file, String targetPath) throws FileConvertException {

        //1、创建一个parser
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        ContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        context.set(Parser.class,parser);

        String newFileName = UUID.randomUUID().toString() + ".txt";
        InputStream is = null;
        FileWriter writer = null;
        try {
            is = new FileInputStream(file);
            //2、执行parser的parse()方法。
            parser.parse(is, handler, metadata, context);

            //3、写入新的文件

            writer = new FileWriter(new File(targetPath+"/"+newFileName));
            writer.write("================ File Metadata ================\n");
            for(String name : metadata.names()) {
                writer.write(name + ":" + metadata.get(name) + "\n");
            }
            writer.write("================ File Content ================\n");
            writer.write(handler.toString());
        } catch (SAXException e) {
            throw new FileConvertException("原文件数据提取异常：\n" + e.getMessage());
        } catch (TikaException e) {
            throw new FileConvertException("原文件数据提取异常：\n" + e.getMessage());
        } catch (IOException e) {
            throw new FileConvertException("数据写入异常：\n" + e.getMessage());
        } finally {
            try {
                if(is != null)
                    is.close();
                if(writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return newFileName;
    }

    public static void main(String[] args) throws FileConvertException {
        MultiTypeConverter converter = new MultiTypeConverter();
        String filePath = "/Users/zxm/Projects/Idea/Works/tika-demos/src/main/resources/doc.docx";
        String targetPath = "/Users/zxm/Projects/Idea/Works/tika-demos/src/main/resources";
        converter.convert2TextFile(new File(filePath), targetPath);
    }

}
