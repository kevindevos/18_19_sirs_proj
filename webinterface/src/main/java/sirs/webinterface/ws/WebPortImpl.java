package sirs.webinterface.ws;

import common.sirs.ws.NoteView;
import sirs.web.ws.WebPortType;
import sirs.webinterface.domain.NotesManager;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.List;

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


}
