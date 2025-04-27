package iuh.fit.gui.menu;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface MenuEvent {

	public void menuSelected(int index, int subIndex, MenuAction action) throws MalformedURLException, NotBoundException, RemoteException;
}
