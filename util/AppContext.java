package com.pinguela.rentexpres.desktop.util;

import com.pinguela.rentexpres.model.UserDTO;


public class AppContext {
        private static UserDTO currentUser;
       private static String rememberedUser;

        public static UserDTO getCurrentUser() {
                return currentUser;
        }

	public static void setCurrentUser(UserDTO user) {
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
