package slvd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.Controller;

import slvd.dao.IndicadorHome;
import slvd.database.Asignatura;

public class controllerAsignatura implements Controller {

	private IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		List<Asignatura> asignatura = dao.listarAsign();
		int cantAsig = asignatura.size();
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("asignatura", asignatura);
		aux.put("cantAsig", cantAsig);
		aux.put("path", path);
		return new ModelAndView("asignaturaAdmin", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}

}
