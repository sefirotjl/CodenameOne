package com.mycompany.myapp;


import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;


public class MyApplication {

    private Form current;

    public void init(Object context) {
        try{
            Resources theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
       }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        /*ConnectionRequest request = new ConnectionRequest();
        String url = "http://localhost:27665/Service1.asmx";
        request.setUrl(url);
        request.setPost(false);
        String headerName = "SOAPAction";
        String headerValue = "\"http://tempuri.org/HellorWorld\"";
        String contentType = "text/xml;charset=\"utf-8\"";
        request.setContentType(contentType);
        request.addRequestHeader(headerName, headerValue);
        //requestElement.addArgument(parameter, value);
        request.addResponseListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                 NetworkEvent e = (NetworkEvent)ev;
                 // … process the response
                 String mensaje1 = e.getMessage();
                 String mensaje2 = e.getError().toString();
                 String mensaje3 = e.getMessage();
                 String mensaje4 = e.getMessage();
                 Form hi = new Form("Hi World");
                hi.addComponent(new Label(mensaje1));
                hi.addComponent(new Label(mensaje2));
                hi.addComponent(new Label(mensaje3));
                hi.addComponent(new Label(mensaje4));
                hi.show();
            }
        });
        */

            final Form map = new Form("Map");
            
            ConnectionRequest request = new ConnectionRequest() {
                /*
                 int chr;
                 StringBuffer sb = new StringBuffer();                   
                String response="";        
                 protected void readResponse(InputStream input) throws IOException {
                      //reading the input stream    

                     while ((chr = input.read()) != -1){
                           sb.append((char) chr);
                       }
                     response = sb.toString();                                           
                     response = response.trim();
                     map.addComponent(new Label(response));
                     map.show();

                 }  */
                    protected void readResponse(InputStream input) throws IOException {
                    JSONParser p = new JSONParser();
                    Hashtable h = p.parse(new InputStreamReader(input));
                    map.addComponent(new Label(h.get("name").toString()));
                    map.addComponent(new Label(h.get("num").toString()));
                    map.addComponent(new Label(h.get("balance").toString()));
                    map.addComponent(new Label(h.get("is_vip").toString()));
                    //hayque protejer de nuls
                    //map.addComponent(new Label(h.get("nickname").toString()));



                    map.show();


                }
            };
            
             request.setUrl("http://localhost:8080/test/index.jsp");           
             request.setPost(false);
             request.addArgument("Id", "1");             
            
             NetworkManager.getInstance().addToQueue(request);

             

    } 

    

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

    

}
