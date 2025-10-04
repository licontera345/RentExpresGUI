package com.pinguela.rentexpres.desktop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinguela.rentexpres.model.CustomerDTO;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;

/**
 * Tabla paginada de customers con columnas enlazadas a City y Province
 * (por name).
 */
public class CustomerSearchTableModel extends AbstractSearchTableModel<CustomerDTO> {

	private static final long serialVersionUID = 1L;

	/* ───────────── Columnas ───────────── */
	private static final String[] DATA_COLUMNS = { "ID", "Name", "Apellido 1", "Apellido 2", "Teléfono", "Email",
			"Street", "Nº", "City", "Province", "Acciones" };

	private static final Class<?>[] DATA_CLASSES = { Integer.class, // ID
			String.class, // Name
			String.class, // LastName
			String.class, // SecondLastName
			String.class, // Teléfono
			String.class, // Email
			String.class, // Street
			String.class, // Número (lo tratamos como String en JTable)
			String.class, // City
			String.class, // Province
			Object.class // Acciones (botones)
	};

	/*
	 * Mapas name → name para permitir, por ejemplo, capitalizar o abreviar. En
	 * este ejemplo, solo devolvemos el mismo name.
	 */
	private final Map<String, String> cityMap;
	private final Map<String, String> provinceMap;

	public CustomerSearchTableModel(Map<String, String> locMap, Map<String, String> provMap,
			ProvinceService provinceService, CityService cityService) {
		super(DATA_COLUMNS, DATA_CLASSES);
		this.cityMap = locMap;
		this.provinceMap = provMap;
	}

	public CustomerSearchTableModel(List<CustomerDTO> data, Map<String, String> locMap, Map<String, String> provMap) {
		this(locMap, provMap, null, null);
		setCustomers(data);
	}

	@Override
	protected Integer getIdOf(CustomerDTO c) {
		return c.getId();
	}

	@Override
	protected Object getFieldAt(CustomerDTO c, int col) {
		Object v;
		switch (col) {
		case 0:
			v = c.getId();
			break;
		case 1:
			v = c.getName();
			break;
		case 2:
			v = c.getLastName();
			break;
		case 3:
			v = c.getSecondLastName();
			break;
		case 4:
			v = c.getPhone();
			break;
		case 5:
			v = c.getEmail();
			break;
		case 6:
			v = c.getStreet();
			break;
		case 7:
			v = c.getStreetNumber();
			break;
		case 8:
			v = cityMap.getOrDefault(c.getCityName(), c.getCityName());
			break;
		case 9:
			v = provinceMap.getOrDefault(c.getProvinceName(), c.getProvinceName());
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

	public void setCustomers(List<CustomerDTO> lista) {
		setData(lista);
	}

	public CustomerDTO getCustomerAt(int row) {
		return getItem(row);
	}

	// ------------- buildCityMap -------------
	public static Map<String, String> buildCityMap(List<CustomerDTO> customers) {
		Map<String, String> map = new HashMap<String, String>();
		if (customers == null)
			return map;

		for (CustomerDTO c : customers) {
			String name = c.getCityName();
			if (!map.containsKey(name)) {
				map.put(name, capitalizar(name));
			}
		}
		return map;
	}

	public static Map<String, String> buildProvinceMap(List<CustomerDTO> customers) {
		Map<String, String> map = new HashMap<String, String>();
		if (customers == null)
			return map;

		for (CustomerDTO c : customers) {
			String name = c.getProvinceName();
			if (!map.containsKey(name)) {
				map.put(name, capitalizar(name));
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
