package slvd.data;

import java.io.Serializable;

public class Indicadoradd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idIndicador;
	private String asignatura;
	private String asignatura_vieja;
	private String descripcionIndicador;
	
	public Indicadoradd() {
		// TODO Auto-generated constructor stub
	}
	
	public Indicadoradd(int idIndicador, String asignatura, String descripcionIndicador, String asignatura_vieja) {
		super();
		this.idIndicador = idIndicador;
		this.asignatura = asignatura;
		this.descripcionIndicador = descripcionIndicador;
		this.asignatura_vieja = asignatura_vieja;
	}
	
	public int getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(int idIndicador) {
		this.idIndicador = idIndicador;
	}

	public String getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(String asignatura) {
		this.asignatura = asignatura;
	}

	public String getDescripcionIndicador() {
		return descripcionIndicador;
	}

	public void setDescripcionIndicador(String descripcionIndicador) {
		this.descripcionIndicador = descripcionIndicador;
	}

	public String getAsignatura_vieja() {
		return asignatura_vieja;
	}

	public void setAsignatura_vieja(String asignatura_vieja) {
		this.asignatura_vieja = asignatura_vieja;
	}
	
}
