/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Renders.RenderAutomotores;
import com.mycompany.Registro.ConexionSQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author jmazzetti
 */
public class IndexControlador extends Controlador {

    protected Window principal;
    protected Combobox cmbQueries, cmbParamUno, cmbParamDos;
    protected Button btnBuscar;
    protected Textbox txtBusquedaGenerica, txtBusquedaAvanzada;
    protected Listbox listaDatos;
    protected Statement sql;
    protected String consulta = "", paramUno = "", paramDos = "";
    protected ResultSet resultado, parcial;
    protected ListModelList modelo;

    /*
    Al inciar la aplicación, realiza una consulta a la base de datos para 
    cargar los posibles consultas que el usuario podrá realizar.
     */
    public void onCreate$principal() {

        try {
            this.sql = ConexionSQL.getConexion().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            consulta = "SELECT Q.descripcion FROM Queries AS Q";
            resultado = sql.executeQuery(consulta);
            while (resultado.next()) {
                cmbQueries.appendItem(resultado.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    
    Al crearse la grilla, lo hará de forma oculta ya que sin datos no tiene razón de ser.
     */
    public void onCreate$listaDatos() {
        listaDatos.setVisible(false);
    }

    /*
    
    Una vez el usuario seleccione el tipo de consulta que quiera realizar, se cargaran los
    datos que le servirán para ejecutar la consulta.
     */
    public void onSelect$cmbQueries() {
        cmbParamUno.getChildren().clear();
        cmbParamDos.getChildren().clear();
        consulta = "SELECT Q.query, Q.paramI, Q.paramII, Q.paramIII, Q.paramIV, Q.paramV FROM Queries AS Q WHERE Q.descripcion='"
                + cmbQueries.getText() + "'";
        try {
            resultado = sql.executeQuery(consulta);
            while (resultado!=null && resultado.next()) {
                consulta = resultado.getString(1);
                paramUno = resultado.getString(2);
                if (cmbQueries.getSelectedIndex() != 6) {
                    paramDos = resultado.getString(3);
                }

            }
            resultado = null;
            resultado = sql.executeQuery(paramUno);
            while (resultado!=null && resultado.next()) {
                cmbParamUno.appendItem(resultado.getString(1));
            }
            resultado = null;
            if (cmbQueries.getSelectedIndex() != 6) {
                resultado = sql.executeQuery(paramDos);
                while (resultado!=null && resultado.next()) {
                    cmbParamDos.appendItem(resultado.getString(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*    
    En caso de que el usuario haya elegido la opcion de consultar por marca, enseguida se
    debe realizar una consulta para obtener todos los modelos.
     */
    public void onSelect$cmbParamUno() {
        String consultaAux = "SELECT DISTINCT A.descripcion FROM Automoviles AS A, Marcas AS M WHERE M.marca='" + cmbParamUno.getText() + "' AND M.id_marca=A.id_marca";
        if (cmbQueries.getSelectedIndex() == 6) {
            cmbParamDos.getChildren().clear();
            try {
                resultado = sql.executeQuery(consultaAux);
                while (resultado!=null && resultado.next()) {
                    cmbParamDos.appendItem(resultado.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*
    
    Una vez haya seleccionado la consulta y los datos requeridos, al presionar el boton "buscar"
    se ejecutará la query, se cargarán los datos en la lista y se podrán visualizar en la pantalla.
     */
    public void onClick$btnBuscar() {
        if (cmbQueries.getSelectedIndex() >= 0
                && cmbParamUno.getSelectedIndex() >= 0
                && cmbParamDos.getSelectedIndex() >= 0) {
            consulta = consulta.replace("\"", "\'");
            consulta = consulta.replace("[PARAMETRO_1]", this.formatear(cmbParamUno.getText()));
            if (cmbQueries.getSelectedIndex() == 6) {
                consulta = consulta.replace("[PARAMETRO_2]", "\'"+cmbParamDos.getText()+"\'");
            } else {
                consulta = consulta.replace("[PARAMETRO_2]", this.formatear(cmbParamDos.getText()));
            }
            System.out.println("Consulta: " + consulta);

        }
        try {
            resultado = sql.executeQuery(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            List<ArrayList<String>> resultados = new ArrayList<ArrayList<String>>();
            while (resultado!=null && resultado.next()) {
                List<String> listaConDatosRecibidos = new ArrayList<String>();
                if (resultado.getString(1) != null) {
                    listaConDatosRecibidos.add(resultado.getString(1));
                } else {
                    listaConDatosRecibidos.add("sin datos");
                }
                if (resultado.getString(2) != null) {
                    listaConDatosRecibidos.add(resultado.getString(2));
                } else {
                    listaConDatosRecibidos.add("sin datos");
                }
                if (resultado.getString(3) != null) {
                    listaConDatosRecibidos.add(resultado.getString(3));
                } else {
                    listaConDatosRecibidos.add("sin datos");
                }
                if (resultado.getString(4) != null) {
                    listaConDatosRecibidos.add("$"+resultado.getString(4));
                } else {
                    listaConDatosRecibidos.add("sin datos");
                }
                if (resultado.getString(5) != null) {
                    listaConDatosRecibidos.add(resultado.getString(5));
                } else {
                    listaConDatosRecibidos.add("sin datos");
                }
                resultados.add((ArrayList<String>) listaConDatosRecibidos);
                modelo = new ListModelList(resultados);
                listaDatos.setModel(modelo);
                listaDatos.setItemRenderer(new RenderAutomotores());

            }
            listaDatos.setVisible(true);
            cmbParamUno.getChildren().clear();
            cmbParamDos.getChildren().clear();
            cmbQueries.setText("");
            cmbParamUno.setText("");
            cmbParamDos.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
    
    Este metodo se utiliza de forma privada y sirve para evitar problemas a la hora de ejecutar
    consultas, donde puede ocurri que espere un texto y reciba un numero o viceversa.
     */
    private String formatear(String cadena) {
        boolean esNumero = true;
        String alfabeto = "., /abcdefghijklmnñopqrstuvwxyz";
        cadena = cadena.toUpperCase();
        alfabeto = alfabeto.toUpperCase();
        for (int j = 0; j < alfabeto.length() && esNumero; j++) {
            for (int i = 0; i < cadena.length(); i++) {
                if (cadena.charAt(i) == alfabeto.charAt(j)) {
                    cadena = "'" + cadena + "'";
                    esNumero = false;
                }
                break;
            }
        }
        return cadena;
    }
}
