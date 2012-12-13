package com.comercio.appagente;


import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.MapComponent;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class MainAgente {

    private Form current;
    private String user, pass;
    


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
        Form log = new Form("Login");
        log.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Label lbl1 = new Label("Introduce tu usuario");
        log.addComponent(lbl1);
        final TextArea txtA1 = new TextArea();
        log.addComponent(txtA1);
        Label lbl2 = new Label("Introduce tu contraseña");
        log.addComponent(lbl2);
        final TextArea txtA2 = new TextArea();
        log.addComponent(txtA2);
        Button btn1 = new Button("Ingresar");
        log.addComponent(btn1);
        log.show();
        btn1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                user = txtA1.getText().toString();
                pass = txtA2.getText().toString();
                ConnectionRequest request = new ConnectionRequest() {
                    protected void readResponse(InputStream input) throws IOException {
                    JSONParser p = new JSONParser();
                    Hashtable h = p.parse(new InputStreamReader(input));
                    String response = (String)h.get("resp");
                    String pass = (String)h.get("pass");
                    
                    if (response.equals("ok")){
                        if (h.get("pass").toString().equals(pass)){
                            Form main = new Form("Inicio");
                            main.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                            
                            //hacemos el contenedor de los tickets
                            Tabs tab = new Tabs();
                            Container cont1 = new Container();
                            cont1.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                            Container cont2 = new Container();
                            ComboBox cmb1 = new ComboBox();
                            Label lbl3 = new Label("Selecciona el ticket");
                            cont1.addComponent(lbl3);
                            cont1.addComponent(cmb1);
                            Label lbl4 = new Label("Datos del ticket");
                            cont1.addComponent(lbl4);
                            ComponentGroup cmpG = new ComponentGroup();
                            TextArea txtA3 = new TextArea();
                            TextField txtF1 = new TextField();
                            TextField txtF2 = new TextField();
                            cmpG.addComponent(txtA3);
                            cmpG.addComponent(txtF1);
                            cmpG.addComponent(txtF2);
                            cont1.addComponent(cmpG);
                            Button btn2 = new Button("Marcar como Resuelto");
                            cont1.addComponent(btn2);
                            Button btn3 = new Button("Bajar Nuevos Tickets");
                            cont1.addComponent(btn3);
                            tab.addTab("Tickets",cont1);
                            
                            //hacemos el contenedor del mapa
                            cont2.setLayout(new BorderLayout());
                            cont2.setScrollable(false);
                            final MapComponent mc = new MapComponent();
                            putMeOnMap(mc);
                            mc.zoomToLayers();

                            cont2.addComponent(BorderLayout.CENTER, mc);
                                                        
                            tab.addTab("Mapa",cont2);
                            
                            //metemos todo al main
                            main.addComponent(tab);
                            main.show();
                        }
                        else{
                            Dialog.show("Error", "El usuario o contraseña es incorrecto"
                                    , "Ok", null);
                        }
                    }
                    else{
                        Dialog.show("Error", "El usuariono existe"
                                    , "Ok", null);
                    }
                }
            };
             
             request.setUrl("http://localhost:8080/WSAgentes/login.jsp");           
             request.setPost(false);
             request.addArgument("user", user);
             request.setContentType("text/html; charset=UTF-8");
             NetworkManager.getInstance().addToQueue(request);
             
            }
        });

    }
    
    private void putMeOnMap(MapComponent map) {
        try {
            Location loc = LocationManager.getLocationManager().getCurrentLocation();
            Coord lastLocation = new Coord(loc.getLatitude(), loc.getLongtitude());
            Image i = Image.createImage("/blue_pin.png");
            PointsLayer pl = new PointsLayer();
            pl.setPointIcon(i);
            PointLayer p = new PointLayer(lastLocation, "You Are Here", i);
            p.setDisplayName(true);
            pl.addPoint(p);
            map.addLayer(pl);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
