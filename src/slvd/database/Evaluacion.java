package slvd.database;
// Generated 28-abr-2015 21:40:29 by Hibernate Tools 3.6.0

import java.util.HashSet;
import java.util.Set;

/**
 * Evaluacion generated by hbm2java
 */
public class Evaluacion  implements java.io.Serializable {


    private int idEvaluacion;
    private Usuario usuario;
    private Float promedioValor;
    private String evaluacion;

   public Evaluacion() {
   }

	
   public Evaluacion(int idEvaluacion, Usuario usuario) {
       this.idEvaluacion = idEvaluacion;
       this.usuario = usuario;
   }
   public Evaluacion(int idEvaluacion, Usuario usuario, Float promedioValor, String evaluacion) {
      this.idEvaluacion = idEvaluacion;
      this.usuario = usuario;
      this.promedioValor = promedioValor;
      this.evaluacion = evaluacion;
   }
  
   public int getIdEvaluacion() {
       return this.idEvaluacion;
   }
   
   public void setIdEvaluacion(int idEvaluacion) {
       this.idEvaluacion = idEvaluacion;
   }
   public Usuario getUsuario() {
       return this.usuario;
   }
   
   public void setUsuario(Usuario usuario) {
       this.usuario = usuario;
   }
   public Float getPromedioValor() {
       return this.promedioValor;
   }
   
   public void setPromedioValor(Float promedioValor) {
       this.promedioValor = promedioValor;
   }
   public String getEvaluacion() {
       return this.evaluacion;
   }
   
   public void setEvaluacion(String evaluacion) {
       this.evaluacion = evaluacion;
   }
}