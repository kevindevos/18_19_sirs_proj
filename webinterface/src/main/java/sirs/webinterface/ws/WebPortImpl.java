package sirs.webinterface.ws;

import common.sirs.ws.NoteView;
import sirs.Security;
import sirs.web.ws.WebPortType;
import sirs.web.ws.WebpageDigestView;
import sirs.webinterface.domain.NotesManager;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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

        try {
            URL staticFolderURL = WebPortType.class.getClassLoader().getResource("/static");
            URL templatesFolderURL = WebPortType.class.getClassLoader().getResource("/templates");

            if(staticFolderURL != null && templatesFolderURL != null){
                staticFolder = new File(staticFolderURL.getPath());
                templatesFolder = new File(templatesFolderURL.getPath());

                File[] staticFiles = staticFolder.listFiles();
                File[] templatesFiles = templatesFolder.listFiles();
                File[] htmlFiles = Stream.concat(Arrays.stream(staticFiles), Arrays.stream(templatesFiles)).toArray(File[]::new);

                WebpageDigestView webpageDigestView;
                byte[] bytes;
                for(File file : htmlFiles){
                    // load contents of file
                    bytes = Files.readAllBytes(file.toPath());

                    webpageDigestView = new WebpageDigestView();
                    webpageDigestView.setPageName(file.getPath());
                    webpageDigestView.setHtml(bytes);
                    webpageDigestView.setDigest(Security.buildDigestFrom(bytes));

                    webpageDigestViews.add(webpageDigestView);
                }

            }
        } catch (IOException e) {
            System.out.printf("Failed to load configuration: %s%n", e);
        }

        return webpageDigestViews;
    }

    @Override
    public void recoverWebPages(List<WebpageDigestView> backup){
        // TODO
    }
}
