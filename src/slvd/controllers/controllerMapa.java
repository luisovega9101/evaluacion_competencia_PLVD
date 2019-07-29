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
import slvd.database.Asignatura;
import slvd.database.Indicador;
import slvd.database.IndicadorIndicador;
import slvd.database.MatrizAdyacencia;

public class controllerMapa extends AbstractController {

	IndicadorHome dao;
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
		List<Indicador> indicadores = dao.listarIndicxAsign(asig);
		List<IndicadorIndicador> listaindicindic = dao.listarIndicIndic();
		List<MatrizAdyacencia> matrizady = dao.matrizAdyacenc();
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("idexperto", idexperto);
		aux.put("asignatura", asig);
		aux.put("indicadores", indicadores);
		aux.put("listaindicindic", listaindicindic);
		aux.put("matrizady", matrizady);
		aux.put("path", path);
		return new ModelAndView("mcd", "aux", aux);
	}
	
	public calculoMatriz getC() {
		return c;
	}

	public void setC(calculoMatriz c) {
		this.c = c;
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
