/**
 * 
 */
package com.afb.dsi.numarch.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * @author rodrigue_toukam
 *
 */
public class ExtractImagesUseCase extends PDFStreamEngine{
	
	private final String filePath;
    private final String outputDir;
    
    private List<String> listBase64;
    private int nbre;

    // Constructor
    public ExtractImagesUseCase(String filePath,
                                String outputDir){
        this.filePath = filePath;
        this.outputDir = outputDir;
        listBase64 = new ArrayList<String>();
    }

    // Execute
    public List<String> execute(){
    	nbre = 0;
        try{
            File file = new File(filePath);
            String partSeparator = ",";
			if (filePath.contains(partSeparator)) {
			  String encodedImg = filePath.split(partSeparator)[1];
			  byte[] _data = Base64.getDecoder().decode(encodedImg);
		      
			  PDDocument document = PDDocument.load(_data);

              for(PDPage page : document.getPages()){
                processPage(page);
              }
			  
			}
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
        
        return listBase64;
    }

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException{
        String operation = operator.getName();
        
        

        if("Do".equals(operation)){
            COSName objectName = (COSName) operands.get(0);
            PDXObject pdxObject = getResources().getXObject(objectName);

            if(pdxObject instanceof PDImageXObject){
                // Image
                PDImageXObject image = (PDImageXObject) pdxObject;
                BufferedImage bImage = image.getImage();

                // File
                //String randomName = UUID.randomUUID().toString();
                String randomName = "image-"+(nbre++);
                File outputFile = new File(outputDir,randomName + ".jpg");
                
               
                // Write image to file
                ImageIO.write(bImage, "JPG", outputFile);
                
                byte[] fileContent = Files.readAllBytes(outputFile.toPath());
                String code =  Base64.getEncoder().encodeToString(fileContent);
                listBase64.add(code);
              

            }else if(pdxObject instanceof PDFormXObject){
                PDFormXObject form = (PDFormXObject) pdxObject;
                showForm(form);
            }
        }

        else super.processOperator(operator, operands);
    }
    
}
