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
import slvd.database.Indicador;
import slvd.database.IndicadorIndicador;
import slvd.database.MatrizAdyacenciaAsociada;
import slvd.database.VariableLenguistica;

public class controllerCorrelacion extends AbstractController {

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

		List<Indicador> indicadores = dao.listarIndicxAsign(asig);
		int s = indicadores.size();
		if (s < 2) {
			List<Asignatura> asignatura = dao.listarAsign();
			String txt = "No existen indicadores suficientes para establecer la evaluación de competencia";
			aux.put("txt", txt);
			aux.put("idexperto", idexperto);
			aux.put("asignatura", asignatura);
			String sms = "Seleccione una asignatura para la evaluación de competencias";
			String accion= "correlacion";			
			aux.put("sms", sms);
			aux.put("accion", accion);
			return new ModelAndView("seleccionAsign", "aux", aux);
		}
		correlacion(idexperto, indicadores);
		List<IndicadorIndicador> listaindicindic = dao.listarIndicIndic();
		List<MatrizAdyacenciaAsociada> matrizady = dao
				.obtenerMatrizAdyacUsuario(idexperto);
		List<VariableLenguistica> varleng = dao.listarVarLeng();

		String path = request.getContextPath();
		aux.put("idexperto", idexperto);
		aux.put("asignatura", asig);
		aux.put("indicadores", indicadores);
		aux.put("listaindicindic", listaindicindic);
		aux.put("matrizady", matrizady);
		aux.put("varleng", varleng);
		aux.put("path", path);
		return new ModelAndView("correlacion", "aux", aux);
	}

	public void correlacion(String iduser, List<Indicador> indicadores) {
		for (Indicador indicadorE : indicadores) {
			for (Indicador indicadorS : indicadores) {
				int indicE = indicadorE.getIdIndicador();
				int indicS = indicadorS.getIdIndicador();
				if (indicE != indicS) {
					dao.insertIndicIndic(indicadorE, indicadorS);
					IndicadorIndicador idIndInd = dao.obtenerIndInd(indicadorE,
							indicadorS);
					int idii = idIndInd.getIdIndicadorIndicador();
					List<MatrizAdyacenciaAsociada> m = dao.obtenerMatrizAdyacUserIdd(
							iduser, idIndInd);
					if (m.size()== 0) {
						dao.crearMatrizAsoc(iduser, idii, 5);
					}
				}
			}
		}
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
