package slvd.data;

import java.io.Serializable;

public class Respuestaadd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int idRespuesta;
	private int idPregunta;
	private String respuesta;
	private float peso;
	
	public Respuestaadd() {
		// TODO Auto-generated constructor stub
	}

	public Respuestaadd(int idRespuesta, int idPregunta, String respuesta,
			float peso) {
		super();
		this.idRespuesta = idRespuesta;
		this.idPregunta = idPregunta;
		this.respuesta = respuesta;
		this.peso = peso;
	}

	public int getIdRespuesta() {
		return idRespuesta;
	}

	public void setIdRespuesta(int idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public int getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}
}
