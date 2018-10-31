package sirs.app.ws;


import pt.ulisboa.tecnico.sdis.kerby.SecurityHelper;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@HandlerChain(file = "/app-ws_handler-chain.xml")
@WebService(
        endpointInterface = "sirs.app.ws.AppPortType",
        wsdlLocation = "app.wsdl",
        name ="AppWebService",
        portName = "AppPort",
        targetNamespace="http://ws.app.sirs/",
        serviceName = "AppService"
)
public class AppPortImpl implements AppPortType {
    private static final String VALID_SERVER_PASSWORD = "nhdchdps";
    public static Key kcsSessionKey;
    public static Key serverKey;

    public AppPortImpl(){
        serverKey = generateServerKeyFromPassword();
    }

    // end point manager
    private AppEndpointManager endpointManager;

    public AppPortImpl(AppEndpointManager endpointManager) {
        serverKey = generateServerKeyFromPassword();
        this.endpointManager = endpointManager;
    }

    private Key generateServerKeyFromPassword(){
        try{
            return SecurityHelper.generateKeyFromPassword(VALID_SERVER_PASSWORD);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String testPing(String inputMessage) {
        return null;
    }

    @Override
    public void testClear() {

    }


    // Auxiliary operations --------------------------------------------------




    // View helpers ----------------------------------------------------------



    // Exception helpers -----------------------------------------------------


}
