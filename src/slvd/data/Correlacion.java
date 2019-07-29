package slvd.data;

import java.io.Serializable;

public class Correlacion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idusuario;
	private String idasignatura;
	private int idindicadorentrada;
	private String descripcentrada;
	private int idindicadorsalida;
	private String descripsalida;
	private int idindicadorindicador;
	private String variablelenguistica;
	
	public Correlacion() {
		// TODO Auto-generated constructor stub
	}
			
	public Correlacion(String idusuario, String idasignatura, int idindicadorentrada,
			String descripcentrada, int idindicadorsalida,
			String descripsalida, int idindicadorindicador,
			String variablelenguistica) {
		super();
		this.idusuario = idusuario;
		this.idasignatura = idasignatura;
		this.idindicadorentrada = idindicadorentrada;
		this.descripcentrada = descripcentrada;
		this.idindicadorsalida = idindicadorsalida;
		this.descripsalida = descripsalida;
		this.idindicadorindicador = idindicadorindicador;
		this.variablelenguistica = variablelenguistica;
	}

	public String getIdusuario() {
		return idusuario;
	}
	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}
	
	public String getIdasignatura() {
		return idasignatura;
	}

	public void setIdasignatura(String idasignatura) {
		this.idasignatura = idasignatura;
	}

	public int getIdindicadorentrada() {
		return idindicadorentrada;
	}
	public void setIdindicadorentrada(int idindicadorentrada) {
		this.idindicadorentrada = idindicadorentrada;
	}
	public String getDescripcentrada() {
		return descripcentrada;
	}
	public void setDescripcentrada(String descripcentrada) {
		this.descripcentrada = descripcentrada;
	}
	public int getIdindicadorsalida() {
		return idindicadorsalida;
	}
	public void setIdindicadorsalida(int idindicadorsalida) {
		this.idindicadorsalida = idindicadorsalida;
	}
	public String getDescripsalida() {
		return descripsalida;
	}
	public void setDescripsalida(String descripsalida) {
		this.descripsalida = descripsalida;
	}
	public int getIdindicadorindicador() {
		return idindicadorindicador;
	}
	public void setIdindicadorindicador(int idindicadorindicador) {
		this.idindicadorindicador = idindicadorindicador;
	}
	public String getVariablelenguistica() {
		return variablelenguistica;
	}
	public void setVariablelenguistica(String variablelenguistica) {
		this.variablelenguistica = variablelenguistica;
	} 	
}
