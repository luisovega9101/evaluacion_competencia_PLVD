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
import slvd.database.Asignatura;
import slvd.database.Pregunta;

public class controllerCuestionario extends AbstractController {

	private IndicadorHome dao;
	
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
		
		Map<String, Object> aux = new HashMap<String, Object>();
		List<Pregunta> lista = dao.listarPregAsign(asig);
		int cantPreg = lista.size();
		
		String path = request.getContextPath();
		aux.put("asignatura", asig);
		aux.put("pregunta", lista);
		aux.put("cantPreg", cantPreg);
		aux.put("idexperto", idexperto);
		aux.put("path", path);
		return new ModelAndView("cuestionarioGestion", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
