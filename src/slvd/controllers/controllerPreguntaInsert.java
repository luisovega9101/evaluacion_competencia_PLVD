package slvd.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.springframework.web.portlet.util.PortletUtils;

import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;

import slvd.dao.IndicadorHome;
import slvd.data.Preguntaadd;
import slvd.database.Asignatura;
import slvd.database.Pregunta;
import slvd.database.Respuesta;

public class controllerPreguntaInsert extends SimpleFormController {

	private IndicadorHome dao;

	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Preguntaadd pp = new Preguntaadd();
		String idrecibidoAsg = ParamUtil.get(request, "id_asignatura",
				"default");
		pp.setAsignatura(idrecibidoAsg);
		pp.setTipoPregunta("Simple");
		String idP = ParamUtil.get(request, "idpregunta", "default");
		if (!idP.equalsIgnoreCase("default")) {
			int idPreg = Integer.parseInt(idP);
			Pregunta p = dao.pregunta(idPreg);
			pp.setIdPregunta(idPreg);
			pp.setPregunta(p.getPregunta());
			pp.setTipoPregunta(p.getTipoPregunta());
		}
		return pp;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println("On submit action fromcontroller ");
		String btnContinuar = ParamUtil.get(request, "button_continuar",
				"default");
		String btnCancelar = ParamUtil.get(request, "button_cancelar",
				"default");
		String idA = request.getParameter("asignatura");
		String nameFile = "vacio";
		if (request instanceof MultipartRequest) {
			System.out.println("ok tiene file################################");
			MultipartFile my_file = ((MultipartRequest) request)
					.getFile("imagen_file");

			/**/
			System.out.println("Debug de valores--------------------");

			String nombreCompleto = my_file.getOriginalFilename();
			System.out.println("Nombre completo " + nombreCompleto);

			String nombre = my_file.getName();
			System.out.println("Nombre corto " + nombre);

			System.out.println("tamanno" + my_file.getSize());

			System.out.println("Debug de valores FIN-------------------");

			/**/

			PortletConfig config = (PortletConfig) request
					.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			String path_portlet = config.getPortletContext().getRealPath("");
			System.out.println("Writing file on " + path_portlet);

			if (my_file.getSize() != 0) {
				nameFile = new Date().getTime() + "";
				String urlFile = path_portlet + File.separator + "images"
						+ File.separator + "archivos" + File.separator;
				File image_file = new File(urlFile + nameFile + ".jpg");
				// File image_file = new File(path_portlet + File.separator +
				// nameFile + ".jpg");
				FileOutputStream fos = new FileOutputStream(image_file);
				fos.write(my_file.getBytes());
				fos.flush();
				fos.close();
			}

		} else {
			System.out.println("no multipart instance");
		}
		if (!btnContinuar.equalsIgnoreCase("default")) {
			String preg = request.getParameter("pregunta");
			String tipopreg = request.getParameter("tipoPregunta");
			request.setAttribute("idAsig", idA);
			request.setAttribute("pregunt", preg);
			request.setAttribute("tipoPreg", tipopreg);
			request.setAttribute("nameFile", nameFile);
			String dec = "continuar";
			String idPregunta = request.getParameter("idPregunta");
			if (!idPregunta.equalsIgnoreCase("0")) {
				request.setAttribute("idP", idPregunta);
				dec = "editar";
			}
			request.setAttribute("decision", dec);
		} else if (!btnCancelar.equalsIgnoreCase("default")) {
			request.setAttribute("idAsig", idA);
			String dec = "cancelar";
			request.setAttribute("decision", dec);
		} else {
			System.out.println("redirecting ...");
			PortletUtils.clearAllRenderParameters(response);
			response.setRenderParameter("action", "insertarpregunta");
		}
	}

	@Override
	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("path", path);
		String decis = (String) request.getAttribute("decision");
		String idAs = (String) request.getAttribute("idAsig");
		if (decis.equalsIgnoreCase("cancelar")) {
			int idasign = Integer.parseInt(idAs);
			Asignatura asig = dao.listaAsignxId(idasign);
			List<Pregunta> lista = dao.listarPregAsign(asig);
			int cantPreg = lista.size();
			aux.put("asignatura", asig);
			aux.put("pregunta", lista);
			aux.put("cantPreg", cantPreg);
			return new ModelAndView("cuestionarioGestion", "aux", aux);
		}
		String preg = (String) request.getAttribute("pregunt");
		String tipopreg = (String) request.getAttribute("tipoPreg");
		
		
		String nameFile = (String) request.getAttribute("nameFile");
		if(nameFile!="vacio"){
			nameFile = nameFile+".jpg";
		}
		aux.put("pregunt", preg);
		aux.put("tipopreg", tipopreg);
		aux.put("nameFile", nameFile);
		aux.put("idAs", idAs);
		String idPr = "0";
		if (decis.equalsIgnoreCase("editar")) {
			idPr = (String) request.getAttribute("idP");
			Pregunta p = dao.pregunta(Integer.parseInt(idPr));
			List<Respuesta> lista = dao.listaRpta(p);
			aux.put("idPr", idPr);
			aux.put("rpta", lista);
		}
		aux.put("idPr", idPr);
		return new ModelAndView(getSuccessView(), "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}