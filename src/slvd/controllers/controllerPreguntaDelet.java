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

public class controllerPreguntaDelet extends AbstractController {

	private IndicadorHome dao;
	
	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {	
		
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {			
		int idpregunta = Integer.parseInt(request.getParameter("idpregunta"));
		Integer idAsign = Integer.parseInt(request.getParameter("id_asignatura"));
		Asignatura asig = dao.listaAsignxId(idAsign);		
		dao.deleteById(idpregunta);		
		Map<String, Object> aux = new HashMap<String, Object>();
		List<Pregunta> listarCuest = dao.listarPregAsign(asig);	
		int cantPreg = listarCuest.size();
		String path = request.getContextPath();
		aux.put("asignatura", asig);
		aux.put("pregunta", listarCuest);
		aux.put("cantPreg", cantPreg);
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
