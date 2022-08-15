/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Renders;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 *
 * @author jmazzetti
 */
public class RenderAutomotores implements ListitemRenderer{
//MARCA, DESCRIPCION, TIPO, VALOR Y FECHA
    @Override
    public void render(Listitem l, Object t, int i) throws Exception {
        List res= (ArrayList) t;
        Listcell marca = new Listcell(res.get(0).toString());
        l.appendChild(marca);
        Listcell descripcion = new Listcell(res.get(1).toString());
        l.appendChild(descripcion);
        Listcell tipo = new Listcell(res.get(2).toString());
        l.appendChild(tipo);
        Listcell valor = new Listcell(res.get(3).toString());
        l.appendChild(valor);
        Listcell fecha = new Listcell(res.get(4).toString());
        l.appendChild(fecha);
    }
    
}
