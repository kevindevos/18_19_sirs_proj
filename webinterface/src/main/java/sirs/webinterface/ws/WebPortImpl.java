package sirs.webinterface.ws;

import common.sirs.ws.NoteView;
import sirs.Security;
import sirs.web.ws.WebPortType;
import sirs.web.ws.WebpageDigestView;
import sirs.webinterface.domain.NotesManager;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@HandlerChain(file = "/web-ws_handler-chain.xml")
@WebService(endpointInterface = "sirs.web.ws.WebPortType",
    wsdlLocation = "web.wsdl",
    name ="WebService",
    portName = "WebPort",
    targetNamespace="http://ws.web.sirs/",
    serviceName = "WebService"
)
public class WebPortImpl implements WebPortType {

    public static String privatePassword;

    public WebPortImpl(){}

	// end point manager
	private WebEndpointManager endpointManager;

	public WebPortImpl(WebEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}


    @Override
    public String testPing(String inputMessage){
        return "hello web client";
    }

    @Override
    public void testClear(){
    }

    @Override
    public List<NoteView> takeRecentlyChangedNotes(){
        return NotesManager.getInstance().takeRecentlyChangedNotes();
    }

    @Override
    public List<WebpageDigestView> getWebpageDigests(){
	    File staticFolder, templatesFolder;
	    List<WebpageDigestView> webpageDigestViews = new ArrayList<>();

        URL staticFolderURL = WebPortType.class.getResource("/static");
        URL templatesFolderURL = WebPortType.class.getResource("/templates");


        if(staticFolderURL != null && templatesFolderURL != null){
            staticFolder = new File(staticFolderURL.getPath());
            templatesFolder = new File(templatesFolderURL.getPath());

            File[] staticFiles = staticFolder.listFiles();
            File[] templatesFiles = templatesFolder.listFiles();
            File[] htmlFiles = Stream.concat(Arrays.stream(staticFiles), Arrays.stream(templatesFiles)).toArray(File[]::new);

            System.err.println("htmlfiles " + htmlFiles);

            WebpageDigestView webpageDigestView;
            for(File file : htmlFiles){
                // load contents of file
                String fileContents = readFile(file);
                System.out.println("read file contents of: " + file.getName() + " : " + fileContents);

                webpageDigestView = new WebpageDigestView();
                webpageDigestView.setPageName(file.getPath());
                webpageDigestView.setHtml(fileContents.getBytes());
                webpageDigestView.setDigest(Security.buildDigestFrom(fileContents));

                webpageDigestViews.add(webpageDigestView);
            }

        }

        return webpageDigestViews;
    }

    private String readFile(File file){
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String contents = "";
            String line = null;

            while(true){
                line = bufferedReader.readLine();
                if(line != null){
                    contents+=line;
                }else{
                    break;
                }
            }
            return contents;
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void recoverWebPages(List<WebpageDigestView> backup){
        for(WebpageDigestView webpageDigestView : backup){


        }
    }
}
