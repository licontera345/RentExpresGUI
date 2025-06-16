package com.pinguela.rentexpres.desktop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.ClienteDTO;
import com.pinguela.rentexpres.service.LocalidadService;
import com.pinguela.rentexpres.service.ProvinciaService;

/**
 * Tabla paginada de clientes con columnas enlazadas a Localidad y Provincia
 * (por nombre).
 */
public class ClienteSearchTableModel extends AbstractSearchTableModel<ClienteDTO> {

	private static final long serialVersionUID = 1L;

	/* ───────────── Columnas ───────────── */
	private static final String[] DATA_COLUMNS = { "ID", "Nombre", "Apellido 1", "Apellido 2", "Teléfono", "Email",
			"Calle", "Nº", "Localidad", "Provincia", "Acciones" };

	private static final Class<?>[] DATA_CLASSES = { Integer.class, // ID
			String.class, // Nombre
			String.class, // Apellido1
			String.class, // Apellido2
			String.class, // Teléfono
			String.class, // Email
			String.class, // Calle
			String.class, // Número (lo tratamos como String en JTable)
			String.class, // Localidad
			String.class, // Provincia
			Object.class // Acciones (botones)
	};

	/*
	 * Mapas nombre → nombre para permitir, por ejemplo, capitalizar o abreviar. En
	 * este ejemplo, solo devolvemos el mismo nombre.
	 */
	private final Map<String, String> localidadMap;
	private final Map<String, String> provinciaMap;

	public ClienteSearchTableModel(Map<String, String> locMap, Map<String, String> provMap,
			ProvinciaService provinciaService, LocalidadService localidadService) {
		super(DATA_COLUMNS, DATA_CLASSES);
		this.localidadMap = locMap;
		this.provinciaMap = provMap;
	}

	public ClienteSearchTableModel(List<ClienteDTO> data, Map<String, String> locMap, Map<String, String> provMap) {
		this(locMap, provMap, null, null);
		setClientes(data);
	}

	@Override
	protected Integer getIdOf(ClienteDTO c) {
		return c.getId();
	}

	@Override
	protected Object getFieldAt(ClienteDTO c, int col) {
		Object v;
		switch (col) {
		case 0:
			v = c.getId();
			break;
		case 1:
			v = c.getNombre();
			break;
		case 2:
			v = c.getApellido1();
			break;
		case 3:
			v = c.getApellido2();
			break;
		case 4:
			v = c.getTelefono();
			break;
		case 5:
			v = c.getEmail();
			break;
		case 6:
			v = c.getCalle();
			break;
		case 7:
			v = c.getNumero();
			break;
		case 8:
			v = localidadMap.getOrDefault(c.getNombreLocalidad(), c.getNombreLocalidad());
			break;
		case 9:
			v = provinciaMap.getOrDefault(c.getNombreProvincia(), c.getNombreProvincia());
			break;
		case 10:
			v = null;
			/* botones */ break;
		default:
			v = null;
			break;
		}
		return v;
	}

	public void setClientes(List<ClienteDTO> lista) {
		setData(lista);
	}

	public ClienteDTO getClienteAt(int row) {
		return getItem(row);
	}

	// ------------- buildLocalidadMap -------------
	public static Map<String, String> buildLocalidadMap(List<ClienteDTO> clientes) {
		Map<String, String> map = new HashMap<String, String>();
		if (clientes == null)
			return map;

		for (ClienteDTO c : clientes) {
			String nombre = c.getNombreLocalidad();
			if (!map.containsKey(nombre)) {
				map.put(nombre, capitalizar(nombre));
			}
		}
		return map;
	}

	public static Map<String, String> buildProvinciaMap(List<ClienteDTO> clientes) {
		Map<String, String> map = new HashMap<String, String>();
		if (clientes == null)
			return map;

		for (ClienteDTO c : clientes) {
			String nombre = c.getNombreProvincia();
			if (!map.containsKey(nombre)) {
				map.put(nombre, capitalizar(nombre));
			}
		}
		return map;
	}

	private static String capitalizar(String txt) {
		if (txt == null || txt.isEmpty())
			return "";
		String lower = txt.toLowerCase();
		return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
	}
}
