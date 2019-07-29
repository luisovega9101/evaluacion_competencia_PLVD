package slvd.database;
// Generated 28-abr-2015 21:40:29 by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * IndicadorIndicador generated by hbm2java
 */
public class IndicadorIndicador  implements java.io.Serializable {


     private int idIndicadorIndicador;
     private Indicador indicadorByIdIndicadorEntrada;
     private Indicador indicadorByIdIndicadorSalida;
     private Set matrizAdyacenciaAsociadas = new HashSet(0);
     private Set matrizAdyacencias = new HashSet(0);

    public IndicadorIndicador() {
    }

	
    public IndicadorIndicador(int idIndicadorIndicador, Indicador indicadorByIdIndicadorEntrada, Indicador indicadorByIdIndicadorSalida) {
        this.idIndicadorIndicador = idIndicadorIndicador;
        this.indicadorByIdIndicadorEntrada = indicadorByIdIndicadorEntrada;
        this.indicadorByIdIndicadorSalida = indicadorByIdIndicadorSalida;
    }
    public IndicadorIndicador(int idIndicadorIndicador, Indicador indicadorByIdIndicadorEntrada, Indicador indicadorByIdIndicadorSalida, Set matrizAdyacenciaAsociadas, Set matrizAdyacencias) {
       this.idIndicadorIndicador = idIndicadorIndicador;
       this.indicadorByIdIndicadorEntrada = indicadorByIdIndicadorEntrada;
       this.indicadorByIdIndicadorSalida = indicadorByIdIndicadorSalida;
       this.matrizAdyacenciaAsociadas = matrizAdyacenciaAsociadas;
       this.matrizAdyacencias = matrizAdyacencias;
    }
   
    public int getIdIndicadorIndicador() {
        return this.idIndicadorIndicador;
    }
    
    public void setIdIndicadorIndicador(int idIndicadorIndicador) {
        this.idIndicadorIndicador = idIndicadorIndicador;
    }
    public Indicador getIndicadorByIdIndicadorEntrada() {
        return this.indicadorByIdIndicadorEntrada;
    }
    
    public void setIndicadorByIdIndicadorEntrada(Indicador indicadorByIdIndicadorEntrada) {
        this.indicadorByIdIndicadorEntrada = indicadorByIdIndicadorEntrada;
    }
    public Indicador getIndicadorByIdIndicadorSalida() {
        return this.indicadorByIdIndicadorSalida;
    }
    
    public void setIndicadorByIdIndicadorSalida(Indicador indicadorByIdIndicadorSalida) {
        this.indicadorByIdIndicadorSalida = indicadorByIdIndicadorSalida;
    }
    public Set getMatrizAdyacenciaAsociadas() {
        return this.matrizAdyacenciaAsociadas;
    }
    
    public void setMatrizAdyacenciaAsociadas(Set matrizAdyacenciaAsociadas) {
        this.matrizAdyacenciaAsociadas = matrizAdyacenciaAsociadas;
    }
    public Set getMatrizAdyacencias() {
        return this.matrizAdyacencias;
    }
    
    public void setMatrizAdyacencias(Set matrizAdyacencias) {
        this.matrizAdyacencias = matrizAdyacencias;
    }




}