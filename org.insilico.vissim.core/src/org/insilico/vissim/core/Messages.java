package org.insilico.vissim.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.insilico.vissim.core.messages"; //$NON-NLS-1$
	public static String fxml_file;
	public static String info_general;
	public static String init_error;
	public static String init_error_chart;
	public static String jfx_not_installed;
	public static String sbscl_not_found_error;
	public static String simulation_crashed_error;
	public static String table_not_found_error;
	public static String unknown_data_type_error;
	public static String unknown_simulation_type_error;
	public static String table_name_col;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
