/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class ParametroJuego {
    private int    idParametro;
    private String clave;
    private String valor;
    private String descripcion;

    public ParametroJuego() {}

    public ParametroJuego(int idParametro, String clave, String valor, String descripcion) {
        this.idParametro = idParametro;
        this.clave = clave;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    public int getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getValorInt (){
        try {
            return Integer.parseInt(valor); 
        } catch (NumberFormatException E) {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return clave + " = " + valor;
    }
}
