package slvd.data;

import java.io.Serializable;

public class Preguntaadd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int idPregunta;
	private String asignatura;
	private String pregunta;
	private String tipoPregunta;
	
	public Preguntaadd() {
		// TODO Auto-generated constructor stub
	}

	public Preguntaadd(int idPregunta, String asignatura, String pregunta,
			String tipoPregunta) {
		super();
		this.idPregunta = idPregunta;
		this.asignatura = asignatura;
		this.pregunta = pregunta;
		this.tipoPregunta = tipoPregunta;
	}

	public int getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(String asignatura) {
		this.asignatura = asignatura;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(String tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}
}
