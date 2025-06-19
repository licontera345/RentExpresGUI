package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UsuarioDTO;


public class AppContext {
        private static UsuarioDTO currentUser;
       private static String rememberedUser;

        public static UsuarioDTO getCurrentUser() {
                return currentUser;
        }

	public static void setCurrentUser(UsuarioDTO user) {
		currentUser = user;
	}

        public static void clearCurrentUser() {
                currentUser = null;
        }

       public static String getRememberedUser() {
               return rememberedUser;
       }

       public static void setRememberedUser(String user) {
               rememberedUser = user;
       }
}
