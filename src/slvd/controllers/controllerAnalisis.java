package slvd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import slvd.dao.IndicadorHome;
import slvd.data.calculoMatriz;
import slvd.database.AnalisisEstatico;
import slvd.database.Asignatura;
import slvd.database.Indicador;
import slvd.database.MatrizAdyacencia;

public class controllerAnalisis extends AbstractController {

	private IndicadorHome dao;
	calculoMatriz c;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {	
		String idexperto = request.getParameter("idexperto");	
		int idasign = Integer.parseInt(request.getParameter("id_asignatura"));
		Asignatura asig = dao.listaAsignxId(idasign);		
		c.calculoMatrizAdy(asig);		
		calculoEstatico(asig);	
		List<Indicador> indicadores = dao.listarIndicxAsign(asig);		
		List<AnalisisEstatico> analisis = dao.listaAnalisis(indicadores);	
		String path = request.getContextPath();
		Map<String, Object> aux = new HashMap<String, Object>();
		aux.put("idexperto", idexperto);
		aux.put("asignatura", asig);
		aux.put("indicadores", indicadores);
		aux.put("analisis", analisis);
		aux.put("path", path);
		return new ModelAndView("analisisEstatico", "aux", aux);
	}
	
	public void calculoEstatico(Asignatura asig) {
		Map<Integer, Float> auxEntrada = calculoGradoEntrada(asig);
		Map<Integer, Float> auxSalida = calculoGradoSalida(asig);
		Map<Integer, Float> auxEntradaNormalizado = calculoGradoEntradaNormalizado(asig);
		Map<Integer, Float> auxSalidaNormalizado = calculoGradoSalidaNormalizado(asig);
		Map<Integer, Float> auxCentralidad = calculoCentralidad(asig);
		Map<Integer, Float> auxCentralidadNormalizada = calculoCentralidadNormalizada(asig);
		List<Indicador> indic = dao.listarIndicxAsign(asig);
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();			
			if (auxEntrada.containsKey(idInd) && auxSalida.containsKey(idInd) &&
					auxEntradaNormalizado.containsKey(idInd) && auxSalidaNormalizado.containsKey(idInd) &&
					auxCentralidad.containsKey(idInd) && auxCentralidadNormalizada.containsKey(idInd)) {				
				float id = auxEntrada.get(idInd);
				String ids = String.format("%.4g%n", id);
				id = Float.parseFloat(ids.replace(",", "."));
				
				float od = auxSalida.get(idInd);
				String ods = String.format("%.4g%n", od);
				od = Float.parseFloat(ods.replace(",", "."));
				
				float idN = auxEntradaNormalizado.get(idInd);
				String idNs = String.format("%.4g%n", idN);
				idN = Float.parseFloat(idNs.replace(",", "."));
				
				float odN = auxSalidaNormalizado.get(idInd);
				String odNs = String.format("%.4g%n", odN);
				odN = Float.parseFloat(odNs.replace(",", "."));
				
				float c = auxCentralidad.get(idInd);
				String cs = String.format("%.4g%n", c);
				c = Float.parseFloat(cs.replace(",", "."));
				
				float cN = auxCentralidadNormalizada.get(idInd);
				String cNs = String.format("%.4g%n", cN);
				cN = Float.parseFloat(cNs.replace(",", "."));
				
				dao.insertaAnalisisEstatico(indicador, id, od, idN, odN, c, cN);			
			}
		}
	}

	public Map<Integer, Float> calculoCentralidad(Asignatura asig) {
		float c = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		Map<Integer, Float> auxEntrada = calculoGradoEntrada(asig);
		Map<Integer, Float> auxSalida = calculoGradoSalida(asig);
		List<Indicador> indic = dao.listarIndicxAsign(asig);

		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();			
			if (auxEntrada.containsKey(idInd) && auxSalida.containsKey(idInd)) {				
				float id = auxEntrada.get(idInd);
				float od = auxSalida.get(idInd);
				c = (float) id + od;
				String cs = String.format("%.4g%n", c);
				c = Float.parseFloat(cs.replace(",", "."));
				aux.put(indicador.getIdIndicador(), c);
				c = 0;
			}
		}
		return aux;
	}
	
	public Map<Integer, Float> calculoCentralidadNormalizada(Asignatura asig) {
		float cN = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		Map<Integer, Float> auxCentralidad = calculoCentralidad(asig);
		List<Indicador> indic = dao.listarIndicxAsign(asig);
		float sumaT=0;
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();		
			sumaT += auxCentralidad.get(idInd);				
		}
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();			
			if (auxCentralidad.containsKey(idInd)) {				
				float id = auxCentralidad.get(idInd);
				if(sumaT!=0){
					cN = (float) id/sumaT;
				}
				String cNs = String.format("%.4g%n", cN);
				cN = Float.parseFloat(cNs.replace(",", "."));
				aux.put(indicador.getIdIndicador(), cN);
				cN = 0;
			}
		}
		return aux;
	}

	public Map<Integer, Float> calculoGradoEntrada(Asignatura asig) {
		float id = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		List<MatrizAdyacencia> matriz = dao.matrizAdyacenc();
		List<Indicador> indic = dao.listarIndicxAsign(asig);

		for (Indicador indicador : indic) {			
			for (MatrizAdyacencia matrizadyacencia : matriz) {
				int idIndInd = matrizadyacencia.getIndicadorIndicador()
						.getIdIndicadorIndicador();
				int indicEntr = dao.listaIndicIndicXId(idIndInd)
						.getIndicadorByIdIndicadorEntrada().getIdIndicador();
				if (indicEntr == indicador.getIdIndicador()) {
					id += (float) matrizadyacencia.getValorAbsoluto();
				}
			}
			String ids = String.format("%.4g%n", id);
			id = Float.parseFloat(ids.replace(",", "."));
			aux.put(indicador.getIdIndicador(), id);
			id = 0;
		}
		return aux;
	}
	
	public Map<Integer, Float> calculoGradoEntradaNormalizado(Asignatura asig) {
		float idN = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		Map<Integer, Float> auxEntrada = calculoGradoEntrada(asig);
		List<Indicador> indic = dao.listarIndicxAsign(asig);
		float sumaT=0;
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();		
			sumaT += auxEntrada.get(idInd);				
		}
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();			
			if (auxEntrada.containsKey(idInd)) {				
				float id = auxEntrada.get(idInd);
				if(sumaT!=0){
					idN = (float) id/sumaT;
				}
				String idNs = String.format("%.4g%n", idN);
				idN = Float.parseFloat(idNs.replace(",", "."));
				aux.put(indicador.getIdIndicador(), idN);
				idN = 0;
			}
		}
		return aux;
	}

	public Map<Integer, Float> calculoGradoSalida(Asignatura asig) {
		float od = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		List<MatrizAdyacencia> matriz = dao.matrizAdyacenc();
		List<Indicador> indic = dao.listarIndicxAsign(asig);

		for (Indicador indicador : indic) {
			for (MatrizAdyacencia matrizadyacencia : matriz) {
				int idIndInd = matrizadyacencia.getIndicadorIndicador()
						.getIdIndicadorIndicador();
				int indicSalid = dao.listaIndicIndicXId(idIndInd)
						.getIndicadorByIdIndicadorSalida().getIdIndicador();
				if (indicSalid == indicador.getIdIndicador()) {
					od += (float) matrizadyacencia.getValorAbsoluto();
				}
			}
			String ods = String.format("%.4g%n", od);
			od = Float.parseFloat(ods.replace(",", "."));
			aux.put(indicador.getIdIndicador(), od);
			od = 0;
		}
		return aux;
	}
	
	public Map<Integer, Float> calculoGradoSalidaNormalizado(Asignatura asig) {
		float odN = 0;
		Map<Integer, Float> aux = new HashMap<Integer, Float>();
		Map<Integer, Float> auxSalida = calculoGradoSalida(asig);
		List<Indicador> indic = dao.listarIndicxAsign(asig);
		float sumaT=0;
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();		
			sumaT += auxSalida.get(idInd);				
		}
		for (Indicador indicador : indic) {
			int idInd = indicador.getIdIndicador();			
			if (auxSalida.containsKey(idInd)) {				
				float id = auxSalida.get(idInd);
				if(sumaT!=0){
					odN = (float) id/sumaT;
				}				
				String odNs = String.format("%.4g%n", odN);
				odN = Float.parseFloat(odNs.replace(",", "."));
				aux.put(indicador.getIdIndicador(), odN);
				odN = 0;
			}
		}
		return aux;
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
	
	public calculoMatriz getC() {
		return c;
	}

	public void setC(calculoMatriz c) {
		this.c = c;
	}
}